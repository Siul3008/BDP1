
-----------------------------------------------------------------------------------------------
--Students:
--Meylin Carvajal Baltodano
--Luis D. Rivas Castro
--Ma. Fernanda Vega Acosta

-----------------------------------------------------------------------------------------------


--Creation of the table for parameters 


--------------------
--parameter

CREATE TABLE sysParameter(
    id  NUMBER(6),
    name VARCHAR(20) CONSTRAINT parameter_name_nn NOT NULL, CONSTRAINT parameter_name_uk UNIQUE(name),
    description VARCHAR(50),
    value VARCHAR(255)


);

ALTER TABLE sysParameter
ADD 
CONSTRAINT pk_sysParameter PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);






