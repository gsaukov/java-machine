----------------------------------------------------------------------
------ SELECT WITH DISCRIMINATOR LIMIT -------------------------------
----------------------------------------------------------------------

SELECT * FROM (SELECT b.ORDER_ID FROM ORDER b
    inner join EVENTS e on e.EVENT_ID = b.GROUP_ID
    inner join ACCOUNTS ac on ac.ACCOUNT_ID = e.ACCOUNT_ID
    inner join CUSTOMER cu on cu.CUSTOMER_ID = ac.CUSTOMER_ID
    inner join DOMAIN dm on dm.DOMAIN_ID = CASE WHEN ac.DISCRIMINATOR = 'StandardAccount'
        THEN ac.DOMAIN_ID ELSE cu.DOMAIN_ID END WHERE dm.NAME in ('PLUTO')
                                                and b.ORDER_ID NOT IN (SELECT ORDER_ID FROM EXPORTED_ORDER)
and b.ORDER_ID NOT IN (SELECT ORDER_ID FROM CANCELLED_ORDER) ORDER BY b.ORDER_ID ASC) WHERE ROWNUM <= 20

