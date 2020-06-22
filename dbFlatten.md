sudo docker pull store/oracle/database-enterprise:12.2.0.1

#sudo docker run -d -it --name efraud -P store/oracle/database-enterprise:12.2.0.1
sudo docker run -d -it --name efraud -p 49161:1521 store/oracle/database-enterprise:12.2.0.1

sudo docker exec -it efraud bash #-c "source /home/oracle/.bashrc; sqlplus /nolog"
sqlplus sys/Oradoc_db1@ORCLCDB as sysdba

alter session set "_ORACLE_SCRIPT"=true;
CREATE USER fraud_admin IDENTIFIED BY password;
GRANT CONNECT TO fraud_admin;
GRANT CONNECT, RESOURCE, DBA TO fraud_admin;
#GRANT CREATE SESSION GRANT ANY PRIVILEGE TO fraud_admin;
GRANT UNLIMITED TABLESPACE TO fraud_admin;

sqlplus fraud_admin/password@ORCLCDB

drop table transaction;
drop table decision;
drop table featureobject;
drop table step;
drop table validation;
drop table state;
drop table rule;
drop table feature;


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

create table decision (
    id NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    transferid varchar2 (255),
    idrule NUMBER,
    result varchar2 (255),
    starttime timestamp,
    endtime timestamp,
    duration float,
    creationdate timestamp DEFAULT CURRENT_TIMESTAMP,
    modificationdate timestamp
);
CREATE OR REPLACE TRIGGER update_timestamp_decision
BEFORE UPDATE ON decision
FOR EACH ROW
BEGIN
    :NEW.modificationdate := systimestamp;
END;
/

create table featureobject (
    id NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    transferid varchar2 (255),
    idfeature NUMBER,
    name varchar2 (255),
    description varchar2 (500),
    classname varchar2 (255),
    transfertype varchar2 (255),
    enable varchar(1),
    fixed varchar(1),
    value float,
    computationtype varchar2 (255),
    featuretype varchar2 (255),
    starttime timestamp,
    endtime timestamp,
    duration float,
    message varchar2 (255),
    creationdate timestamp DEFAULT CURRENT_TIMESTAMP,
    modificationdate timestamp    
);
CREATE OR REPLACE TRIGGER update_timestamp_featureobject
BEFORE UPDATE ON featureobject
FOR EACH ROW
BEGIN
    :NEW.modificationdate := systimestamp;
END;
/

create table step (
    id NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    transferid varchar2 (255),
    ordre NUMBER,
    label varchar2 (255),
    starttime timestamp,
    endtime timestamp,
    localduration float,
    endtimeoflastoperator timestamp,
    durationfromlastoperator float,
    creationdate timestamp DEFAULT CURRENT_TIMESTAMP,
    modificationdate timestamp
);
CREATE OR REPLACE TRIGGER update_timestamp_step
BEFORE UPDATE ON step
FOR EACH ROW
BEGIN
    :NEW.modificationdate := systimestamp;
END;
/

create table validation (
    id NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    transferid varchar2 (255),
    field varchar2 (255),
    status varchar(1),
    message varchar2 (255),
    creationdate timestamp DEFAULT CURRENT_TIMESTAMP,
    modificationdate timestamp    
);
CREATE OR REPLACE TRIGGER update_timestamp_validation
BEFORE UPDATE ON validation
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
    content         varchar2 (4000),
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

