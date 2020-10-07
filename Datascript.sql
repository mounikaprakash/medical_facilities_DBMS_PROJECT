DROP TABLE MEDICALFACILITY_T CASCADE CONSTRAINTS;
DROP TABLE CITY_STATE_T CASCADE CONSTRAINTS;
DROP TABLE STATE_COUNTRY_T CASCADE CONSTRAINTS;
DROP TABLE CERTIFICATION_T CASCADE CONSTRAINTS;
DROP TABLE FACILITY_CERTIFICATION_T CASCADE CONSTRAINTS;
DROP TABLE SERVICEDEPARTMENT_T CASCADE CONSTRAINTS;
DROP TABLE SERVICES_T CASCADE CONSTRAINTS;
DROP TABLE SERVICEDEPT_SERVICES_T CASCADE CONSTRAINTS;
DROP TABLE EQUIPMENT_T CASCADE CONSTRAINTS;
DROP TABLE EQUIPMENT_SERVICES_T CASCADE CONSTRAINTS;
DROP TABLE STAFF_T CASCADE CONSTRAINTS;
DROP TABLE STAFF_SECONDARY_DEPT_T CASCADE CONSTRAINTS;
DROP TABLE RECORD_VITALS_T CASCADE CONSTRAINTS;
DROP TABLE CHECKIN_T CASCADE CONSTRAINTS;
DROP TABLE PATIENT_T CASCADE CONSTRAINTS;
DROP TABLE REFERRAL_REASON_T CASCADE CONSTRAINTS;
DROP TABLE OUTCOME_REPORT_T CASCADE CONSTRAINTS;
DROP TABLE SYMPTOMS_T CASCADE CONSTRAINTS;
DROP TABLE SEVERITY_SCALE_T CASCADE CONSTRAINTS;
DROP TABLE CHECKIN_SYMPTOMS_METADATA_T CASCADE CONSTRAINTS;
DROP TABLE BODYPARTS_T CASCADE CONSTRAINTS;
DROP TABLE CHECKIN_SYMPTOM_T CASCADE CONSTRAINTS;
DROP TABLE BODYPART_SERVICE_DEPT_T CASCADE CONSTRAINTS;
DROP TABLE NEGATIVE_EXPERIENCE_T CASCADE CONSTRAINTS;
DROP TABLE REFERRAL_T CASCADE CONSTRAINTS;
DROP TABLE SYMPTOM_BODYPART_T CASCADE CONSTRAINTS;
DROP TABLE CHECKIN_BODYPART_T CASCADE CONSTRAINTS;

DROP SEQUENCE MEDICALFACILITY_S;

DROP SEQUENCE CERTIFICATION_S;

DROP SEQUENCE SERVICEDEPARTMENT_S;

DROP SEQUENCE SERVICES_S;

DROP SEQUENCE STAFF_S;

DROP SEQUENCE PATIENT_S;

DROP SEQUENCE CHECKIN_S;

DROP SEQUENCE SYMPTOMS_S;

DROP SEQUENCE BODYPARTS_S;
-------------------------------------------------------------------------------------------------------
CREATE table STATE_COUNTRY_T(
STATENAME VARCHAR2(50) primary key,
COUNTRY VARCHAR2(50)NOT NULL
);


CREATE TABLE CITY_STATE_T(
    CITYNAME VARCHAR2(50),
    STATENAME VARCHAR2(50) REFERENCES STATE_COUNTRY_T(STATENAME),
    PRIMARY KEY(CITYNAME)
 );
 


CREATE table MEDICALFACILITY_T(
    FacilityId INTEGER,
    DoorNumber Integer,
    StreetName Varchar2(200),
    CityName varchar2(50) references CITY_STATE_T(CITYNAME),
    FacilityName varchar2(50),
    ClassificationId Char(2),
    CapacityVal Integer,
    check (ClassificationId in ('01', '02', '03')),
    primary key(FacilityId)
);



CREATE table CERTIFICATION_T(
CERTIFICATIONID VARCHAR2(50),
NAME VARCHAR2(150),
PRIMARY KEY(CERTIFICATIONID)
);

CREATE table FACILITY_CERTIFICATION_T(
FACILITYID INTEGER REFERENCES MedicalFacility_T(FACILITYID),
CERTIFICATIONID VARCHAR2(50) REFERENCES CERTIFICATION_T(CERTIFICATIONID),
CERTIFIEDDATE DATE,
EXPIRATIONDATE DATE,
PRIMARY KEY(FACILITYID,CERTIFICATIONID)
);

CREATE table SERVICEDEPARTMENT_T(
FACILITYID INTEGER REFERENCES MedicalFacility_T(FACILITYID) NOT NULL,
DEPARTMENTID CHAR(5),
DIRECTOR VARCHAR2(50),
DEPTNAME VARCHAR2(50),
DEPTTYPE VARCHAR2(50) CHECK (DEPTTYPE IN ('MEDICAL','NON-MEDICAL')),
PRIMARY KEY(DEPARTMENTID)
);

CREATE TABLE SERVICES_T(
CODE VARCHAR2(50),
NAME VARCHAR2(150),
PRIMARY KEY(CODE)
);


CREATE TABLE SERVICEDEPT_SERVICES_T(
CODE VARCHAR2(50)REFERENCES SERVICES_T(CODE),
DEPARTMENTID CHAR(5) REFERENCES SERVICEDEPARTMENT_T(DEPARTMENTID),
PRIMARY KEY(DEPARTMENTID,CODE)
);

CREATE TABLE EQUIPMENT_T(
NAME VARCHAR2(150),
PRIMARY KEY(NAME)
);

CREATE TABLE EQUIPMENT_SERVICES_T(
CODE VARCHAR2(50) REFERENCES SERVICES_T(CODE),
NAME VARCHAR2(150)REFERENCES EQUIPMENT_T(NAME),
PRIMARY KEY(CODE,NAME)
);

CREATE table STAFF_T(
EMPLOYEEID VARCHAR2(50),
PRIMARY_DEPARTMENTID CHAR(5) REFERENCES SERVICEDEPARTMENT_T(DEPARTMENTID) NOT NULL,
HIREDATE DATE,
DESIGNATION VARCHAR2(50) CHECK (DESIGNATION IN ('MEDICAL','NON-MEDICAL')),
STAFFNAME VARCHAR2(50),
PRIMARY KEY(EMPLOYEEID)
);

alter table staff_t
add cityname VARCHAR2(50);

alter table staff_t
add DOB DATE;

create table STAFF_SECONDARY_DEPT_T(
EMPLOYEEID VARCHAR2(50) REFERENCES STAFF_T(EMPLOYEEID),
SECONDARY_DEPARTMENTID CHAR(5) REFERENCES SERVICEDEPARTMENT_T(DEPARTMENTID),
PRIMARY KEY(EMPLOYEEID,SECONDARY_DEPARTMENTID)
);

CREATE table PATIENT_T(
FIRSTNAME VARCHAR2(50),
LASTNAME VARCHAR2(50),
PATIENTID VARCHAR2(50),
PHONE VARCHAR2(50),
DOB DATE,
DOORNO INTEGER,
STREETNAME VARCHAR2(50),
CITYNAME VARCHAR2(50) REFERENCES CITY_STATE_T(CITYNAME) NOT NULL,
PRIMARY KEY(PATIENTID))
;

CREATE table CHECKIN_T(
CHECKID VARCHAR2(50),
FACILITYID INTEGER REFERENCES MedicalFacility_T(FACILITYID) NOT NULL,
PATIENTID VARCHAR2(50) REFERENCES PATIENT_T(PATIENTID) NOT NULL,
LISTSTATUS VARCHAR2(50) CHECK (LISTSTATUS IN ('HIGH','NORMAL','QUARANTINE')),
CHECKIN_STARTTIME TIMESTAMP,
CHECKIN_ENDTIME TIMESTAMP,
TREATMENT_STARTTIME TIMESTAMP,
TREATMENT_ENDTIME TIMESTAMP,
PRIMARY KEY(CHECKID)
);

CREATE table RECORD_VITALS_T(
EMPLOYEEID VARCHAR2(50) REFERENCES STAFF_T(EMPLOYEEID) NOT NULL,
CHECKID VARCHAR2(50) REFERENCES CHECKIN_T(CHECKID),
TEMPERATURE DECIMAL(5,2) NOT NULL,
BPSYSTOLIC INTEGER NOT NULL,
BPDIASTOLIC INTEGER NOT NULL,
PRIMARY KEY(CHECKID)
);


CREATE table REFERRAL_REASON_T(
CHECKID VARCHAR2(50) REFERENCES CHECKIN_T(CHECKID) NOT NULL,
REASONCODE INTEGER CHECK (REASONCODE IN (0,1,2,3)),
NAME_OF_SERVICE VARCHAR2(50) NOT NULL,
DESCRIPION VARCHAR2(255) NOT NULL,
PRIMARY KEY(CHECKID,REASONCODE)
);

CREATE TABLE REFERRAL_T(
CHECKID VARCHAR2(50) REFERENCES CHECKIN_T(CHECKID) PRIMARY KEY,
REFERRERID VARCHAR2(50) REFERENCES STAFF_T(EMPLOYEEID) NOT NULL,
FACILITYID INTEGER REFERENCES MEDICALFACILITY_T(FACILITYID) NOT NULL)
;

CREATE table OUTCOME_REPORT_T(
CHECKID VARCHAR2(50) REFERENCES CHECKIN_T(CHECKID) NOT NULL,
EMPLOYEEID VARCHAR2(50) REFERENCES STAFF_T(EMPLOYEEID) NOT NULL,
TREATMENT_GIVEN VARCHAR2(255) NOT NULL,
DISCHARGE_STATUS VARCHAR2(50)CHECK (DISCHARGE_STATUS IN ('TREATED SUCCESSFULLY','DECEASED','REFERRED')) NOT NULL,
PRIMARY KEY(CHECKID)
);


CREATE TABLE SYMPTOMS_T(
SYMPTOM_CODE VARCHAR2(50),
SYMPTOM_NAME VARCHAR2(150),
SCALE_TYPE VARCHAR2(50),
PRIMARY KEY(SYMPTOM_CODE)
);

CREATE TABLE SEVERITY_SCALE_T(
SCALE_VALUE VARCHAR2(50) NOT NULL,
SCALE_TYPE VARCHAR2(50) NOT NULL
);



CREATE table CHECKIN_SYMPTOMS_METADATA_T(
SYMPTOM_CODE VARCHAR2(50) REFERENCES SYMPTOMS_T(SYMPTOM_CODE) NOT NULL,
CHECKID VARCHAR2(50) REFERENCES CHECKIN_T(CHECKID),
DURATION DECIMAL(5,2) NOT NULL,
ISFIRSTOCCURANCE CHAR(1) NOT NULL,
INCIDENT VARCHAR2(50) NOT NULL,
SCALE_VALUE VARCHAR2(50),
PRIMARY KEY(CHECKID,SYMPTOM_CODE)
);

CREATE TABLE BODYPARTS_T(
BODYPARTNAME VARCHAR2(50) NOT NULL,
CODE VARCHAR2(50),
PRIMARY KEY(CODE)
);


CREATE TABLE BODYPART_SERVICE_DEPT_T(
DEPARTMENTID CHAR(5) REFERENCES SERVICEDEPARTMENT_T(DEPARTMENTID),
CODE VARCHAR2(50)DEFAULT 'ALL' REFERENCES BODYPARTS_T(CODE),
PRIMARY KEY(DEPARTMENTID,CODE)
);

CREATE TABLE NEGATIVE_EXPERIENCE_T(
CODE INTEGER CHECK (CODE IN (1,2)),
TEXTDESCRIPTION VARCHAR2(255),
CHECKID VARCHAR2(50) REFERENCES CHECKIN_T(CHECKID),
PRIMARY KEY(CHECKID,CODE)
);

CREATE TABLE SYMPTOM_BODYPART_T(
    SYMPTOM_CODE VARCHAR2(50) REFERENCES SYMPTOMS_T(SYMPTOM_CODE),
    CODE VARCHAR2(50) REFERENCES BODYPARTS_T(CODE),
    PRIMARY KEY(SYMPTOM_CODE, CODE)
 );
 
 CREATE TABLE CHECKIN_BODYPART_T(
    CHECKID VARCHAR2(50) REFERENCES CHECKIN_T(CHECKID),
    CODE VARCHAR2(50) REFERENCES BODYPARTS_T(CODE),
    PRIMARY KEY(CHECKID, CODE)
 );
------------------------------------------------------------------------------
CREATE TABLE RULE_PRIORITY_T(
ASSESSMENT_ID INT PRIMARY KEY,
PRIORITY VARCHAR2(50) CHECK(PRIORITY IN ('HIGH','NORMAL','QUARANTINE'))
);




CREATE TABLE ASSESSMENT_RULES_T(
ASSESSMENT_ID INT REFERENCES RULE_PRIORITY_T(ASSESSMENT_ID),
SYMPTOM_CODE VARCHAR2(50) REFERENCES SYMPTOMS_T(SYMPTOM_CODE),
CODE VARCHAR2(50) REFERENCES BODYPARTS_T(CODE),
COMPARISION CHAR(2) CHECK (COMPARISION IN ('>', '=', '<', '>=','<=','!=')),
SEVERITY_VALUE VARCHAR2(50),
PRIMARY KEY(ASSESSMENT_ID, SYMPTOM_CODE, CODE, SEVERITY_VALUE));

--------------------------------------------------------------------------------------------------

INSERT INTO STATE_COUNTRY_T (STATENAME, COUNTRY)
VALUES ('North Carolina', 'USA');

INSERT INTO STATE_COUNTRY_T (STATENAME, COUNTRY)
VALUES ('New York', 'USA');

INSERT INTO STATE_COUNTRY_T (STATENAME, COUNTRY)
VALUES ('California', 'USA');


INSERT INTO CITY_STATE_T (CITYNAME,STATENAME)
VALUES ('Raleigh','North Carolina');

INSERT INTO CITY_STATE_T (CITYNAME,STATENAME)
VALUES ('New York','New York');

INSERT INTO CITY_STATE_T (CITYNAME,STATENAME)
VALUES ('Mountain View','California');

INSERT INTO CITY_STATE_T (CITYNAME,STATENAME)
VALUES ('Santa Cruz','California');


INSERT INTO MEDICALFACILITY_T (FacilityId,DoorNumber,StreetName,CityName,FacilityName,ClassificationId,CapacityVal)
VALUES (MEDICALFACILITY_S.NEXTVAL,2650,'Wolf Village Way Box 7220','Raleigh','Wolf Hospital','03',300);

INSERT INTO MEDICALFACILITY_T (FacilityId,DoorNumber,StreetName,CityName,FacilityName,ClassificationId,CapacityVal)
VALUES (MEDICALFACILITY_S.NEXTVAL,2500,'Sacramento','Santa Cruz','California Health Care','02',150);

INSERT INTO MEDICALFACILITY_T (FacilityId,DoorNumber,StreetName,CityName,FacilityName,ClassificationId,CapacityVal)
VALUES (MEDICALFACILITY_S.NEXTVAL,489,'First Avenue', 'New York','Suny Medical Center','01',10);



INSERT INTO CERTIFICATION_T (CERTIFICATIONID,NAME)
VALUES ('CER001','Comprehensive Stroke Certification');

INSERT INTO CERTIFICATION_T (CERTIFICATIONID,NAME)
VALUES ('CER002','ISO Certification');

INSERT INTO CERTIFICATION_T (CERTIFICATIONID,NAME)
VALUES ('CER003','Primary Stroke Certification');

INSERT INTO FACILITY_CERTIFICATION_T (FACILITYID,CERTIFICATIONID,CERTIFIEDDATE,EXPIRATIONDATE)
VALUES (1000,'CER001','12-DEC-1990','11-NOV-2025');


INSERT INTO FACILITY_CERTIFICATION_T (FACILITYID,CERTIFICATIONID,CERTIFIEDDATE,EXPIRATIONDATE)
VALUES (1001,'CER002','09-MAY-2011','08-FEB-2024');


INSERT INTO FACILITY_CERTIFICATION_T (FACILITYID,CERTIFICATIONID,CERTIFIEDDATE,EXPIRATIONDATE)
VALUES (1002,'CER002','01-JAN-2018','31-DEC-2028');

INSERT INTO FACILITY_CERTIFICATION_T (FACILITYID,CERTIFICATIONID,CERTIFIEDDATE,EXPIRATIONDATE)
VALUES (1002,'CER003','01-JAN-2018','31-DEC-2028');



/DEPEARTMENT STARTS WITH 10 AS WE NEED 5 DIGIT CODES FOR DEPARTMENTS/

INSERT INTO SERVICEDEPARTMENT_T (FACILITYID,DEPARTMENTID,DIRECTOR,DEPTNAME,DEPTTYPE)
VALUES (1001,'ER000','Musical Robert','Emergency room','MEDICAL');

INSERT INTO SERVICEDEPARTMENT_T (FACILITYID,DEPARTMENTID,DIRECTOR,DEPTNAME,DEPTTYPE)
VALUES (1000,'GP000','Muscular Rob','General practice department','MEDICAL');

INSERT INTO SERVICEDEPARTMENT_T (FACILITYID,DEPARTMENTID,DIRECTOR,DEPTNAME,DEPTTYPE)
VALUES (1001, 'GP001','Millennium Roberten','General practice department','MEDICAL');

INSERT INTO SERVICEDEPARTMENT_T (FACILITYID,DEPARTMENTID,DIRECTOR,DEPTNAME,DEPTTYPE)
VALUES (1000,'OP000','Medical Robot','Optometry','MEDICAL');

INSERT INTO SERVICEDEPARTMENT_T (FACILITYID,DEPARTMENTID,DIRECTOR,DEPTNAME,DEPTTYPE)
VALUES (1000,'SE000','Miscellaneous Robotor','Security','NON-MEDICAL');

INSERT INTO SERVICEDEPARTMENT_T (FACILITYID,DEPARTMENTID,DIRECTOR,DEPTNAME,DEPTTYPE)
VALUES (1002,'ER001','Massaging Robin','Emergency Room','MEDICAL');


INSERT INTO SERVICES_T(CODE,NAME)
VALUES ('SER01','Emergency');

INSERT INTO SERVICES_T(CODE,NAME)
VALUES ('SGP01','General practice');

INSERT INTO SERVICES_T(CODE,NAME)
VALUES ('VIS01','Vision');


INSERT INTO SERVICEDEPT_SERVICES_T(CODE,DEPARTMENTID)
VALUES ('SER01','ER000');

INSERT INTO SERVICEDEPT_SERVICES_T(CODE,DEPARTMENTID)
VALUES ('SGP01','GP000');

INSERT INTO SERVICEDEPT_SERVICES_T(CODE,DEPARTMENTID)
VALUES ('SGP01','GP001');

INSERT INTO SERVICEDEPT_SERVICES_T(CODE,DEPARTMENTID)
VALUES ('VIS01','OP000');


INSERT INTO EQUIPMENT_T(NAME)
VALUES ('ER combo rack');

INSERT INTO EQUIPMENT_T(NAME)
VALUES ('Blood pressure monitor');

INSERT INTO EQUIPMENT_T(NAME)
VALUES ('thermometer');

INSERT INTO EQUIPMENT_T(NAME)
VALUES ('Vision Screener');



INSERT INTO EQUIPMENT_SERVICES_T(CODE,NAME)
VALUES ('SER01','ER combo rack');

INSERT INTO EQUIPMENT_SERVICES_T(CODE,NAME)
VALUES ('SGP01','Blood pressure monitor');

INSERT INTO EQUIPMENT_SERVICES_T(CODE,NAME)
VALUES ('VIS01','Vision Screener');

INSERT INTO EQUIPMENT_SERVICES_T(CODE,NAME)
VALUES ('SGP01','thermometer');


INSERT INTO STAFF_T(EMPLOYEEID,PRIMARY_DEPARTMENTID,HIREDATE,DESIGNATION,STAFFNAME,DOB)
VALUES ('89001','OP000','21-JUN-2019','MEDICAL','Medical Robot', '19-APR-1989');

INSERT INTO STAFF_T(EMPLOYEEID,PRIMARY_DEPARTMENTID,HIREDATE,DESIGNATION,STAFFNAME,DOB)
VALUES ('93001','ER000','29-AUG-2018','MEDICAL','Musical Robert', '29-JAN-1993');

INSERT INTO STAFF_T(EMPLOYEEID,PRIMARY_DEPARTMENTID,HIREDATE,DESIGNATION,STAFFNAME,DOB)
VALUES ('67001','GP000','12-OCT-1983','MEDICAL','Muscular Rob', '09-DEC-1967');

INSERT INTO STAFF_T(EMPLOYEEID,PRIMARY_DEPARTMENTID,HIREDATE,DESIGNATION,STAFFNAME,DOB)
VALUES ('88001','GP000','21-JUN-2019','MEDICAL','Mechanical Roboto', '18-MAY-1988');

INSERT INTO STAFF_T(EMPLOYEEID,PRIMARY_DEPARTMENTID,HIREDATE,DESIGNATION,STAFFNAME,DOB)
VALUES ('91001','GP001','20-SEP-2018','MEDICAL','Millennium Roberten', '28-JUN-1991');

INSERT INTO STAFF_T(EMPLOYEEID,PRIMARY_DEPARTMENTID,HIREDATE,DESIGNATION,STAFFNAME,DOB)
VALUES ('66001','ER000','01-OCT-1993','MEDICAL','Missionary Robinson', '08-JUL-1966');

INSERT INTO STAFF_T(EMPLOYEEID,PRIMARY_DEPARTMENTID,HIREDATE,DESIGNATION,STAFFNAME,DOB)
VALUES ('89002','SE000','19-AUG-2014','NON-MEDICAL','Miscellaneous Robotor', '19-APR-1989');

INSERT INTO STAFF_T(EMPLOYEEID,PRIMARY_DEPARTMENTID,HIREDATE,DESIGNATION,STAFFNAME,DOB)
VALUES ('93002','SE000','18-OCT-2017','NON-MEDICAL','Musician Root', '29-JAN-1993');

INSERT INTO STAFF_T(EMPLOYEEID,PRIMARY_DEPARTMENTID,HIREDATE,DESIGNATION,STAFFNAME,DOB)
VALUES ('67002','ER001','20-DEC-1990','MEDICAL','Massaging Robin', '09-DEC-1967');







INSERT INTO STAFF_SECONDARY_DEPT_T(EMPLOYEEID,SECONDARY_DEPARTMENTID)
VALUES ('88001','OP000');





INSERT INTO PATIENT_T(FIRSTNAME,LASTNAME,PATIENTID,PHONE,DOB,DOORNO,STREETNAME,CITYNAME)
VALUES ('John','Smith','1','9007004567','01-JAN-1990',100,'Avent Ferry Road','Raleigh');

INSERT INTO PATIENT_T(FIRSTNAME,LASTNAME,PATIENTID,PHONE,DOB,DOORNO,STREETNAME,CITYNAME)
VALUES ('Jane','Doe','2','9192453245','29-FEB-2000',1016,'Lexington Road','New York');

INSERT INTO PATIENT_T(FIRSTNAME,LASTNAME,PATIENTID,PHONE,DOB,DOORNO,STREETNAME,CITYNAME)
VALUES ('Rock','Star','3','5403127893','31-AUG-1970',1022,'Amphitheatre Parkway','Mountain View');

INSERT INTO PATIENT_T(FIRSTNAME,LASTNAME,PATIENTID,PHONE,DOB,DOORNO,STREETNAME,CITYNAME)
VALUES ('Sheldon','Cooper','4','6184628437','26-MAY-1984',1210,'2500 Sacramento','Santa Cruz');



INSERT INTO CHECKIN_T(CHECKID,FACILITYID,PATIENTID,LISTSTATUS,CHECKIN_STARTTIME,CHECKIN_ENDTIME,TREATMENT_STARTTIME,TREATMENT_ENDTIME)
VALUES (TO_CHAR(CHECKIN_S.NEXTVAL),1000,'1',null,null,null,null,null);

INSERT INTO CHECKIN_T(CHECKID,FACILITYID,PATIENTID,LISTSTATUS,CHECKIN_STARTTIME,CHECKIN_ENDTIME,TREATMENT_STARTTIME,TREATMENT_ENDTIME)
VALUES (TO_CHAR(CHECKIN_S.NEXTVAL),1000,'2',null,null,null,null,null);

INSERT INTO CHECKIN_T(CHECKID,FACILITYID,PATIENTID,LISTSTATUS,CHECKIN_STARTTIME,CHECKIN_ENDTIME,TREATMENT_STARTTIME,TREATMENT_ENDTIME)
VALUES (TO_CHAR(CHECKIN_S.NEXTVAL),1001,'3',null,NULL,null,null,null);

INSERT INTO CHECKIN_T(CHECKID,FACILITYID,PATIENTID,LISTSTATUS,CHECKIN_STARTTIME,CHECKIN_ENDTIME,TREATMENT_STARTTIME,TREATMENT_ENDTIME)
VALUES (TO_CHAR(CHECKIN_S.NEXTVAL),1002,'4',null,NULL,null,null,null);


INSERT INTO SYMPTOMS_T(SYMPTOM_CODE,SYMPTOM_NAME,SCALE_TYPE)
VALUES ('SYM001','Pain','1-10');

INSERT INTO SYMPTOMS_T(SYMPTOM_CODE,SYMPTOM_NAME,SCALE_TYPE)
VALUES ('SYM002','Diarrhea','Normal/Severe');

INSERT INTO SYMPTOMS_T(SYMPTOM_CODE,SYMPTOM_NAME,SCALE_TYPE)
VALUES ('SYM003','Fever','Low/High');

INSERT INTO SYMPTOMS_T(SYMPTOM_CODE,SYMPTOM_NAME,SCALE_TYPE)
VALUES ('SYM004','Physical Exam','Normal/Premium');

INSERT INTO SYMPTOMS_T(SYMPTOM_CODE,SYMPTOM_NAME,SCALE_TYPE)
VALUES ('SYM005','Lightheadedness','Normal/Severe');

INSERT INTO SYMPTOMS_T(SYMPTOM_CODE,SYMPTOM_NAME,SCALE_TYPE)
VALUES ('SYM006','Blurred vision','Normal/Severe');

INSERT INTO SYMPTOMS_T(SYMPTOM_CODE,SYMPTOM_NAME,SCALE_TYPE)
VALUES ('SYM007','Bleeding','Moderate/Heavy');



INSERT INTO SEVERITY_SCALE_T(SCALE_VALUE,SCALE_TYPE)
VALUES ('1','1-10');

INSERT INTO SEVERITY_SCALE_T(SCALE_VALUE,SCALE_TYPE)
VALUES ('2','1-10');

INSERT INTO SEVERITY_SCALE_T(SCALE_VALUE,SCALE_TYPE)
VALUES ('3','1-10');

INSERT INTO SEVERITY_SCALE_T(SCALE_VALUE,SCALE_TYPE)
VALUES ('4','1-10');

INSERT INTO SEVERITY_SCALE_T(SCALE_VALUE,SCALE_TYPE)
VALUES ('5','1-10');

INSERT INTO SEVERITY_SCALE_T(SCALE_VALUE,SCALE_TYPE)
VALUES ('6','1-10');

INSERT INTO SEVERITY_SCALE_T(SCALE_VALUE,SCALE_TYPE)
VALUES ('7','1-10');

INSERT INTO SEVERITY_SCALE_T(SCALE_VALUE,SCALE_TYPE)
VALUES ('8','1-10');

INSERT INTO SEVERITY_SCALE_T(SCALE_VALUE,SCALE_TYPE)
VALUES ('9','1-10');

INSERT INTO SEVERITY_SCALE_T(SCALE_VALUE,SCALE_TYPE)
VALUES ('10','1-10');

INSERT INTO SEVERITY_SCALE_T(SCALE_VALUE,SCALE_TYPE)
VALUES ('Normal','Normal/Severe');

INSERT INTO SEVERITY_SCALE_T(SCALE_VALUE,SCALE_TYPE)
VALUES ('Severe','Normal/Severe');

INSERT INTO SEVERITY_SCALE_T(SCALE_VALUE,SCALE_TYPE)
VALUES ('High','Low/High');

INSERT INTO SEVERITY_SCALE_T(SCALE_VALUE,SCALE_TYPE)
VALUES ('Low','Low/High');

INSERT INTO SEVERITY_SCALE_T(SCALE_VALUE,SCALE_TYPE)
VALUES ('Normal','Normal/Premium');

INSERT INTO SEVERITY_SCALE_T(SCALE_VALUE,SCALE_TYPE)
VALUES ('Premium','Normal/Premium');

INSERT INTO SEVERITY_SCALE_T(SCALE_VALUE,SCALE_TYPE)
VALUES ('Moderate','Moderate/Heavy');

INSERT INTO SEVERITY_SCALE_T(SCALE_VALUE,SCALE_TYPE)
VALUES ('Heavy','Moderate/Heavy');


INSERT INTO CHECKIN_SYMPTOMS_METADATA_T(SYMPTOM_CODE,CHECKID,DURATION,ISFIRSTOCCURANCE,INCIDENT,SCALE_VALUE)
VALUES ('SYM003','1000',1,'N','Unknown','High');

INSERT INTO CHECKIN_SYMPTOMS_METADATA_T(SYMPTOM_CODE,CHECKID,DURATION,ISFIRSTOCCURANCE,INCIDENT,SCALE_VALUE)
VALUES ('SYM001','1002',3,'Y','Fell off bike','5');

INSERT INTO CHECKIN_SYMPTOMS_METADATA_T(SYMPTOM_CODE,CHECKID,DURATION,ISFIRSTOCCURANCE,INCIDENT,SCALE_VALUE)
VALUES ('SYM002','1003',1,'N','Pepper challenge','Severe');

INSERT INTO CHECKIN_SYMPTOMS_METADATA_T(SYMPTOM_CODE,CHECKID,DURATION,ISFIRSTOCCURANCE,INCIDENT,SCALE_VALUE)
VALUES ('SYM006','1004',1,'N','Unknown','Normal');


INSERT INTO BODYPARTS_T(BODYPARTNAME,CODE)
VALUES ('Left Arm','ARM000');

INSERT INTO BODYPARTS_T(BODYPARTNAME,CODE)
VALUES ('Right Arm','ARM001');

INSERT INTO BODYPARTS_T(BODYPARTNAME,CODE)
VALUES ('Abdominal','ABD000');

INSERT INTO BODYPARTS_T(BODYPARTNAME,CODE)
VALUES ('Eye','EYE000');

INSERT INTO BODYPARTS_T(BODYPARTNAME,CODE)
VALUES ('Heart','HRT000');

INSERT INTO BODYPARTS_T(BODYPARTNAME,CODE)
VALUES ('Chest','CST000');

INSERT INTO BODYPARTS_T(BODYPARTNAME,CODE)
VALUES ('Head','HED000');



INSERT INTO BODYPARTS_T(BODYPARTNAME,CODE)
VALUES ('dummy','ALL');


INSERT INTO BODYPART_SERVICE_DEPT_T(DEPARTMENTID,CODE)
VALUES ('OP000','EYE000');


INSERT INTO BODYPART_SERVICE_DEPT_T(DEPARTMENTID,CODE)
VALUES ('ER000', 'ALL');


INSERT INTO BODYPART_SERVICE_DEPT_T(DEPARTMENTID,CODE)
VALUES ('GP000', 'ALL');

INSERT INTO BODYPART_SERVICE_DEPT_T(DEPARTMENTID,CODE)
VALUES ('GP001', 'ALL');

INSERT INTO BODYPART_SERVICE_DEPT_T(DEPARTMENTID,CODE)
VALUES ('SE000', 'ALL');

INSERT INTO BODYPART_SERVICE_DEPT_T(DEPARTMENTID,CODE)
VALUES ('ER001', 'ALL');


INSERT INTO SYMPTOM_BODYPART_T(SYMPTOM_CODE, CODE)
VALUES ('SYM002', 'ABD000');

INSERT INTO SYMPTOM_BODYPART_T(SYMPTOM_CODE, CODE)
VALUES ('SYM005', 'HED000');

INSERT INTO SYMPTOM_BODYPART_T(SYMPTOM_CODE, CODE)
VALUES ('SYM006', 'EYE000');

INSERT INTO CHECKIN_BODYPART_T(CHECKID, CODE)
VALUES ('1000', 'EYE000');

INSERT INTO CHECKIN_BODYPART_T(CHECKID, CODE)
VALUES ('1000', 'ARM000');

INSERT INTO RULE_PRIORITY_T(ASSESSMENT_ID, PRIORITY)
VALUES (1, 'HIGH');

INSERT INTO RULE_PRIORITY_T(ASSESSMENT_ID, PRIORITY)
VALUES (2, 'HIGH');

INSERT INTO RULE_PRIORITY_T(ASSESSMENT_ID, PRIORITY)
VALUES (3, 'NORMAL');


INSERT INTO ASSESSMENT_RULES_T(ASSESSMENT_ID, SYMPTOM_CODE, CODE, COMPARISION, SEVERITY_VALUE)
VALUES (1,'SYM001','CST000','>','7');

INSERT INTO ASSESSMENT_RULES_T(ASSESSMENT_ID, SYMPTOM_CODE, CODE, COMPARISION, SEVERITY_VALUE)
VALUES (1,'SYM003','OTH000','=','High');

INSERT INTO ASSESSMENT_RULES_T(ASSESSMENT_ID, SYMPTOM_CODE, CODE, COMPARISION, SEVERITY_VALUE)
VALUES (2,'SYM001','HED000','>','7');

INSERT INTO ASSESSMENT_RULES_T(ASSESSMENT_ID, SYMPTOM_CODE, CODE, COMPARISION, SEVERITY_VALUE)
VALUES (2,'SYM006','EYE000','=','Normal');

INSERT INTO ASSESSMENT_RULES_T(ASSESSMENT_ID, SYMPTOM_CODE, CODE, COMPARISION, SEVERITY_VALUE)
VALUES (2,'SYM005','HED000','=','Normal');

INSERT INTO ASSESSMENT_RULES_T(ASSESSMENT_ID, SYMPTOM_CODE, CODE, COMPARISION, SEVERITY_VALUE)
VALUES (3,'SYM001','HED000','<=','7');

INSERT INTO ASSESSMENT_RULES_T(ASSESSMENT_ID, SYMPTOM_CODE, CODE, COMPARISION, SEVERITY_VALUE)
VALUES (3,'SYM006','HED000','=','Normal');
