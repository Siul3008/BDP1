
-----------------------------------------------------------------------------------------------
--Students:
--Meylin Carvajal Baltodano
--Luis D. Rivas Castro
--Ma. Fernanda Vega Acosta

-----------------------------------------------------------------------------------------------


--Creation of additional tables needed to specify other cobditions/characteristics
--Also tables related to veterinarians and health statuses



-------------------------------------------------------
--currency
CREATE TABLE currency(
    id     NUMBER(3) ,
    name    VARCHAR(25) CONSTRAINT curency_name_nn NOT NULL,
    acronym VARCHAR(3)CONSTRAINT currency_acronym_nn NOT NULL
);


ALTER TABLE currency
ADD 
CONSTRAINT pk_currency PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);



--------------------------------------------------------------------

--------------------------------------------------------------
--For addresses

--Province


CREATE TABLE province(
    id     NUMBER(6) ,
    name    VARCHAR(25) CONSTRAINT province_name_nn NOT NULL
);


ALTER TABLE province
ADD 
CONSTRAINT pk_province PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);



--Canton


CREATE TABLE canton(
    id     NUMBER(6) ,
    idProvince NUMBER (6),
    name    VARCHAR(25) CONSTRAINT canton_name_nn NOT NULL
);


ALTER TABLE canton
ADD 
CONSTRAINT pk_canton PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE canton
ADD CONSTRAINT fk_idProvince FOREIGN KEY(idProvince) REFERENCES province (id);


--District


CREATE TABLE district(
    id     NUMBER(6) ,
    idCanton NUMBER(6),
    name    VARCHAR(25) CONSTRAINT district_name_nn NOT NULL
);


ALTER TABLE district
ADD 
CONSTRAINT pk_district PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);



ALTER TABLE district
ADD CONSTRAINT fk_idCanton FOREIGN KEY(idCanton) REFERENCES canton (id);



----------------------------------------------------------------------
-----------------
--Veterinarian

CREATE TABLE veterinarian(
    id     NUMBER(6) ,
    name    VARCHAR(25) CONSTRAINT veterinarian_name_nn NOT NULL
);


ALTER TABLE veterinarian
ADD 
CONSTRAINT pk_veterinarian PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);





----------------------------------------------------------------------------------
----------------------------------------------------------------------------------------

--Health status and its related tables


----------------------------------------------

--healthStatus


CREATE TABLE healthStatus(
    id     NUMBER(6) ,
    illnessState    VARCHAR(25) CONSTRAINT healthSt_illSt_nn NOT NULL,
    description  VARCHAR(50) 
);


ALTER TABLE healthStatus
ADD 
CONSTRAINT pk_healthStatus PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);




------------------------------------------------------
--medicine

CREATE TABLE medicine(
    id     NUMBER(6) ,
    name    VARCHAR(25) CONSTRAINT medicine_name_nn NOT NULL
);


ALTER TABLE medicine
ADD 
CONSTRAINT pk_medicine PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);



--Health status x medicine
CREATE TABLE healthStxMedicine( 
    idHealthStatus NUMBER(6),
    idMedicine NUMBER(6),
    dose VARCHAR(20) CONSTRAINT HSxMEd_dose_nn NOT NULL
    
);

ALTER TABLE healthStxMedicine
ADD 
CONSTRAINT pk_healthStxMedicine PRIMARY KEY (idHealthStatus,idMedicine)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE healthStxMedicine
ADD CONSTRAINT fk_healthStxMedicine_med FOREIGN KEY(idMedicine) REFERENCES medicine (id);

ALTER TABLE healthStxMedicine
ADD CONSTRAINT fk_healthStxMedicine_hs FOREIGN KEY(idHealthStatus) REFERENCES healthStatus (id);




----------------------------------------

--Disease



CREATE TABLE disease(
    id     NUMBER(6) ,
    name    VARCHAR(25)CONSTRAINT disease_name_nn NOT NULL
);


ALTER TABLE disease
ADD 
CONSTRAINT pk_disease PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);



--Health status x disease
CREATE TABLE healthStxDisease( 
    idHealthStatus NUMBER(6),
    idDisease NUMBER(6),
    description VARCHAR (50)
    
);

ALTER TABLE healthStxDisease
ADD 
CONSTRAINT pk_healthStxDisease PRIMARY KEY (idHealthStatus,idDisease)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE healthStxDisease
ADD CONSTRAINT fk_healthStxDisease_dis FOREIGN KEY(idDisease) REFERENCES disease (id);

ALTER TABLE healthStxDisease
ADD CONSTRAINT fk_healthStxDisease_hs FOREIGN KEY(idHealthStatus) REFERENCES healthStatus (id);


--------------------------------------------------------
--Treatment


CREATE TABLE treatment(
    id     NUMBER(6) ,
    name    VARCHAR(25) CONSTRAINT treatment_name_nn NOT NULL
);


ALTER TABLE treatment
ADD 
CONSTRAINT pk_treatment PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);



--Health status x treatment
CREATE TABLE healthStxtreatment( 
    idHealthStatus NUMBER(6),
    idtreatment NUMBER(6)
    
);

ALTER TABLE healthStxTreatment
ADD 
CONSTRAINT pk_healthStxTreatment PRIMARY KEY (idHealthStatus,idTreatment)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE healthStxTreatment
ADD CONSTRAINT fk_healthStxTreatment_t FOREIGN KEY(idTreatment) REFERENCES treatment (id);

ALTER TABLE healthStxTreatment
ADD CONSTRAINT fk_healthStxTreatment_hs FOREIGN KEY(idHealthStatus) REFERENCES healthStatus (id);



----------------------------------------
