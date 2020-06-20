
----------------------------------------------------------------------
------ CURSOR BULK UPDATE --------------------------------------------
----------------------------------------------------------------------

declare
    cursor cu is select cu.CUSTOMER_ID, mn.NUMBER_ID from USERS u
                    inner join CUSTOMER cu on u.CUSTOMER_ID = cu.CUSTOMER_ID
                    inner join MOBILE_NUMBER mn on u.USER_ID = mn.USER_ID
                    inner join CARD_C cc on cc.CUSTOMER_ID = cu.CUSTOMER_ID
                 where cc.CARD_NAME in ('cardname');

    type t is table of cu%ROWTYPE;
    v t;

begin
    open cu;
    loop
        fetch cu bulk collect into v limit 1000;
            FORALL i IN v.FIRST..v.LAST
                UPDATE CUSTOMER c
                SET STATUS = 'VERIFIED',
                    ADDRESS_STATUS = 'VERIFIED',
                    IDENTIFICATION = 'IDENTIFIED',
                    UPDATED_BY = 'BULK',
                    LAST_UPDATE_TS = CURRENT_TIMESTAMP
                WHERE c.customer_id = v(i).CUSTOMER_ID;
            commit;

            FORALL i IN v.FIRST..v.LAST
                update MOBILE_NUMBER mn
                SET NUMBER_STATE = 'OK',
                    UPDATED_BY = 'BULK',
                    LAST_UPDATE_TS = CURRENT_TIMESTAMP
                WHERE mn.NUMBER_ID = v(i).NUMBER_ID;
            commit;
        exit when cu%NOTFOUND;
        v.delete;
    end loop;
    close cu;
end;
/

----------------------------------------------------------------------
------ INSERT ALL, MIGRATION -----------------------------------------
----------------------------------------------------------------------

declare

  cursor cu is SELECT CUSTOMER_ID, IBAN, INSERTED_BY, SYS_GUID() as TEMP_ID from PHYSICAL_ACCOUNT
  WHERE CUSTOMER_ID NOT IN (SELECT CUSTOMER_ID FROM CUSTOMER_ACCOUNT);

  type t is table of cu%ROWTYPE;
  v t;

begin
  open cu;
  loop
    fetch cu bulk collect into v limit 10000;
    FORALL i IN v.FIRST..v.LAST

    insert all
    into BANK_ACCOUNT (BANK_ACCOUNT_ID, BANK_ACCOUNT_TYPE, IBAN, SOURCE, VERSION,
                                     INSERTED_BY, UPDATED_BY)
    values (v(i).TEMP_ID, 'IBAN', v(i).IBAN, 'BankAccount', 0, 'MIGRATION', 'MIGRATION')

    into CUSTOMER_ACCOUNT (CUSTOMER_ACCOUNT_ID, CUSTOMER_ID, BANK_ACCOUNT, TYPE, VERSION,
                                       INSERTED_BY, UPDATED_BY)
    values (SYS_GUID(), v(i).CUSTOMER_ID, v(i).TEMP_ID, 'CUSTOMER', 0, v(i).INSERTED_BY, 'MIGRATION')
    SELECT * FROM dual;
    commit;

    exit when cu%NOTFOUND;
  end loop;
  close cu;
end;
/

----------------------------------------------------------------------
------ INSERT CASE ---------------------------------------------------
----------------------------------------------------------------------

declare

  cursor cu is
    SELECT INSTRUMENT_ID, IBAN, ACCOUNT_NUMBER, CODE, COUNTRY_ID from BANK_ACCOUNT
    WHERE BANK_ACCOUNT_NUMBER_TYPE is null;

  type t is table of cu%ROWTYPE;
  v t;

begin
  open cu;
  loop
    fetch cu bulk collect into v limit 10000;
    FORALL i IN v.FIRST..v.LAST

    update BANK_ACCOUNT ba set ba.BANK_ACCOUNT_TYPE =
      case
      when (v(i).IBAN is not null)
        THEN 'IBAN'
      when (v(i).COUNTRY = 'US' and v(i).ACCOUNT_NUMBER is not null and v(i).CODE is not null)
        THEN 'US'
      ELSE
        'RUSSIA'
      end,

      ba.UPDATED_BY = 'MIGRATION',
      ba.LAST_UPDATE_TS = systimestamp
    where ba.INSTRUMENT_ID = v(i).INSTRUMENT_ID and ba.BANK_ACCOUNT_TYPE is null;

    commit;
    exit when cu%NOTFOUND;
  end loop;
  close cu;
end;
/

----------------------------------------------------------------------
------ REPLACE FIELD BETWEEN SCHEMAS ---------------------------------
----------------------------------------------------------------------

declare
    v_cust_id varchar2 := ''; -- <- place particular customer_id here
begin
    for cr in (select badata.receiver_holder, hdata.u_rowid
            from ( select baba.CUSTOMER_ID, batsd.RECEIVER_HOLDER, batsd.AMOUNT, batsd.inserted_at
                    from bank_agent_data.TRANSACTION_SEPA_DD batsd
                        inner join bank_agent_data.BANK_ACCOUNT baba
                            on batsd.BANK_ACCOUNT_ID = baba.ID ) badata
                inner join ( select hte.CUSTOMER_ID, hte.AMOUNT, hte.TRANSACTION_DATE, hte.insert_ts, hte.rowid u_rowid
                                from horus_data.TRANSACTION_EVENT hte
                            where hte.REFERENCE_TYPE= 'ORDER'
                                and hte.DETAILS not like '%"transactionDetails":{"receiver":"%'
                                and hte.TRANSACTION_DATE > add_months(trunc(sysdate),-1)) hdata
            on badata.customer_id = hdata.customer_id
            and badata.amount      = hdata.amount
            and badata.inserted_at between hdata.insert_ts - 1 and hdata.insert_ts + 1
  and ( v_cust_id is null or hdata.customer_id = v_cust_id )
    ) loop
update horus_data.TRANSACTION_EVENT te
set te.DETAILS  = REPLACE(te.DETAILS, '"Details":{', '"Details":{"receiver":"' || badata.receiver_holder || '",');
where te.rowid = cr.u_rowid;
    end loop;
    end;
    /
