sudo docker pull store/oracle/database-enterprise:12.2.0.1

#sudo docker run -d -it --name efraud -P store/oracle/database-enterprise:12.2.0.1
sudo docker run -d -it --name efraud -p 49161:1521 -v OracleDBData:/ORCL store/oracle/database-enterprise:12.2.0.1
#docker run -d -it --name <Oracle-DB> -v /home/sam/Documents/oracle/data:/ORCL store/oracle/database-enterprise:12.2.0.1

sudo docker exec -it efraud bash 
#-c "source /home/oracle/.bashrc; sqlplus /nolog"
sqlplus sys/Oradoc_db1@ORCLCDB as sysdba

alter session set "_ORACLE_SCRIPT"=true;
CREATE USER fraud_admin IDENTIFIED BY password;
GRANT CONNECT TO fraud_admin;
GRANT CONNECT, RESOURCE, DBA TO fraud_admin;
GRANT UNLIMITED TABLESPACE TO fraud_admin;

#GRANT CREATE SESSION GRANT ANY PRIVILEGE TO fraud_admin;

sqlplus fraud_admin/password@ORCLCDB

drop table transaction;
drop table decision;
drop table featureobject;
drop table step;
drop table validation;
drop table state;
drop table rule;
drop table feature;


CREATE TABLE searchprofiles (
    id NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    name            varchar2 (255),
    description     varchar2 (255),
    content         varchar2 (255),
    creationdate timestamp DEFAULT CURRENT_TIMESTAMP,
    modificationdate timestamp
);
CREATE OR REPLACE TRIGGER update_timestamp_searchprofiles
BEFORE UPDATE ON searchprofiles
FOR EACH ROW
BEGIN
    :NEW.modificationdate := systimestamp;
END;
/

CREATE TABLE transaction (
    transferid varchar2 (255) PRIMARY KEY,
    transferreference varchar2 (255),
    transfertype varchar2 (255),
    messagetype varchar2 (255),
    userdata varchar2 (255),
    rawmessage varchar2 (255),
    originsystem varchar2 (255),
    settlementdate TIMESTAMP,
    debtoraccount varchar2 (255),
    debtorbic varchar2 (255),
    debtorcountry varchar2 (255),
    debtorname varchar2 (255),
    creditoraccount varchar2 (255),
    creditorbic varchar2 (255),
    creditorcountry varchar2 (255),
    creditorname varchar2 (255),
    amount FLOAT,
    currency varchar2 (10),
    decision varchar2 (255),
    duration FLOAT,
    errorcompute varchar(1),
    validated  varchar(1),
    decisions blob,
    features blob,
    validationBeans blob,
    steps blob,
    scorings blob,
    creationdate timestamp DEFAULT CURRENT_TIMESTAMP,
    modificationdate timestamp
);
CREATE OR REPLACE TRIGGER update_timestamp_transaction
BEFORE UPDATE ON transaction
FOR EACH ROW
BEGIN
    :NEW.modificationdate := systimestamp;
END;
/

CREATE TABLE state (
    id NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    featurename varchar2 (255),
    key varchar2 (255),
    value varchar2 (255),
    PRIMARY KEY (featurename, key),
    creationdate timestamp DEFAULT CURRENT_TIMESTAMP,
    modificationdate timestamp      
);
CREATE OR REPLACE TRIGGER update_timestamp_state
BEFORE UPDATE ON state
FOR EACH ROW
BEGIN
    :NEW.modificationdate := systimestamp;
END;
/

CREATE TABLE rule (
    id NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    name            varchar2 (255),
    description     varchar2 (255),
    type            varchar2 (50),
    enable          varchar2 (1),
    content         blob,
    tree            blob,
    creationdate timestamp DEFAULT CURRENT_TIMESTAMP,
    modificationdate timestamp
);
CREATE OR REPLACE TRIGGER update_timestamp_rule
BEFORE UPDATE ON rule
FOR EACH ROW
BEGIN
    :NEW.modificationdate := systimestamp;
END;
/

CREATE TABLE scoring (
    id NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    name            varchar2 (255),
    description     varchar2 (255),
    enable          varchar2 (1),
    content         blob,
    creationdate timestamp DEFAULT CURRENT_TIMESTAMP,
    modificationdate timestamp
);
CREATE OR REPLACE TRIGGER update_timestamp_scoring
BEFORE UPDATE ON scoring
FOR EACH ROW
BEGIN
    :NEW.modificationdate := systimestamp;
END;
/

ALTER TABLE scoring ADD(created_at varchar2 (255));

CREATE TABLE feature (
    id NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    name            varchar2 (255),
    description     varchar2 (255),
    classname       varchar2 (255),
    transfertype    varchar2 (255),
    enable          varchar2 (1),
    fixed           varchar2 (1),
    value           FLOAT,
    computationtype varchar2 (255),
    featuretype     varchar2 (255),
    rule         varchar2 (4000),
    creationdate timestamp DEFAULT CURRENT_TIMESTAMP,
    modificationdate timestamp
);
CREATE OR REPLACE TRIGGER update_timestamp_feature
BEFORE UPDATE ON feature
FOR EACH ROW
BEGIN
    :NEW.modificationdate := systimestamp;
END;
/








create table toto (
    id NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    name varchar2 (255),
    age float
);
insert into toto (name,age) values('toto',25);

insert into toto (name,age) select 'toto',25 from dual where not exists(select name,age from toto where name='toto' and age=25);

insert into toto (name,age) select 'toto1',25 from dual where not exists(select name,age from toto where name='toto1' and age=25);

insert into toto (name,age) select 'toto2',35 from dual where not exists(select name from toto where name='toto2');



MERGE INTO toto a
  USING (SELECT 35 id, 'name inserted' name FROM DUAL) incoming
  ON (a.id = incoming.id )
  WHEN MATCHED THEN UPDATE SET name='name updated', age=100
  WHEN NOT MATCHED THEN 
  INSERT (name, age) VALUES ('name inserted', 63);

delete from transaction;
delete from featureobject;
delete from step;
delete from decision;
commit;

select transferid from transaction where settlementdate >= to_date('2020-01-16T12:05:34', 'YYYY-MM-DD"T"HH24:MI:SS')
select transferid from transaction where                   settlementdate >= TO_TIMESTAMP('2018-01-16 12:05:34', 'yyyy-MM-dd HH24:mi:ss');
select transferid from transaction where validated='Y' and settlementdate >= TO_TIMESTAMP('2019-12-01 12:33:10, 'yyyy-MM-dd HH24:mi:ss') offset 0 ROWS FETCH NEXT 10 ROWS ONLY
insert into transaction