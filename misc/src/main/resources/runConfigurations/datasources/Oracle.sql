alter session set "_ORACLE_SCRIPT"=true;
create user depositary identified by password;
grant all privileges to depositary;

drop user depositary cascade ;

ALTER SESSION SET CURRENT_SCHEMA = depositary;

select count(e.ORDER_UUID) from execution e
delete EXECUTION

select count(*) from EXECUTION;
select * from DEPOSIT;
update EXECUTION set ROUTE = 'BUY' where ROUTE = '0';
update EXECUTION set ROUTE = 'SELL' where ROUTE = '1';
update EXECUTION set ROUTE = 'SHORT' where ROUTE = '2';

update DEPOSIT set ROUTE = 'BUY' where ROUTE = '0';
update DEPOSIT set ROUTE = 'SELL' where ROUTE = '1';
update DEPOSIT set ROUTE = 'SHORT' where ROUTE = '2';


select DEPOSIT_UUID, max(ACCOUNT_ID) as acc, count(UUID) as count from execution
where ACCOUNT_ID = 'TEST_ACCOUNT_ID'
group by DEPOSIT_UUID

order by count desc
;
select count(*) from EXECUTION
where ACCOUNT_ID = 'TEST_ACCOUNT_ID';


select * from EXECUTION where UUID = '8d5faa26-e594-467d-88d9-6396ed22909c';



