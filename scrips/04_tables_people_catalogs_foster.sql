-----------------------------------------------------------------------------------------------
--Students:
--Meylin Carvajal Baltodano
--Luis D. Rivas Castro
--Ma. Fernanda Vega Acosta

-----------------------------------------------------------------------------------------------


--Creation of the tables related to person, and some characteristics of the pets
--that are going to be needed for the foster homes in their conditions


-------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------

--person

CREATE TABLE person(
    id     NUMBER(6) ,
    firstName      VARCHAR(20) CONSTRAINT person_fname_nn NOT NULL,
    secondName      VARCHAR(20),
    firstLastName       VARCHAR(25)CONSTRAINT person_flname_nn NOT NULL,
    secondLastName       VARCHAR(25)                                    
);


ALTER TABLE person
ADD 
CONSTRAINT pk_person PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);



------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------


-- Pet color
CREATE TABLE Color(
    id     NUMBER(6) ,
    name    VARCHAR(25) CONSTRAINT color_name_nn NOT NULL
);


ALTER TABLE Color
ADD 
CONSTRAINT pk_color PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);

-------------------------------------------------------------------------
--Pet Size

CREATE TABLE petSize(
    id     NUMBER(6) ,
    name    VARCHAR(25) CONSTRAINT petSize_name_nn NOT NULL
);


ALTER TABLE petSize
ADD 
CONSTRAINT pk_petSize PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);

--------------------------------------------------------------------------------


---------------------------------------------------

--Pet type

CREATE TABLE petType(
    id     NUMBER(6) ,
    name    VARCHAR(25) CONSTRAINT petType_name_nn NOT NULL
);


ALTER TABLE petType
ADD 
CONSTRAINT pk_petType PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);




------------------------------------------------------------------------------
------------------------------------------------------------------------------
--Rescuer

CREATE TABLE rescuer(
    idPerson   NUMBER(6)
);


ALTER TABLE rescuer
ADD 
CONSTRAINT pk_rescuer PRIMARY KEY (idPerson)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);

ALTER TABLE rescuer
ADD  CONSTRAINT fk_rescuer_person FOREIGN KEY(idPerson) REFERENCES person (id); 



----------------------------------------------------------------------------
-----------------------------------------------------------------------------

--Adopter and its related tables

----------------------------------
--Star Rating 


CREATE TABLE starRating(
    id     NUMBER(6) ,
    name      VARCHAR(20) CONSTRAINT sRating_name_nn NOT NULL,
    star      VARCHAR(10) CONSTRAINT sRating_star_nn NOT NULL,
    ratingDate       DATE NOT NULL                                   
);


ALTER TABLE starRating
ADD 
CONSTRAINT pk_starRating PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


---------------------------------
--Adopter

CREATE TABLE adopter(
    idPerson   NUMBER(6),
    idStarRating NUMBER(6),
    note VARCHAR(50) 
);

ALTER TABLE adopter
ADD 
CONSTRAINT pk_adopter PRIMARY KEY (idPerson)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);

ALTER TABLE adopter
ADD  CONSTRAINT fk_adopter_person FOREIGN KEY(idPerson) REFERENCES person (id); 


ALTER TABLE adopter
ADD  CONSTRAINT fk_idStarRating FOREIGN KEY(idStarRating) REFERENCES starRating (id); 




---------------------------------------------------------------------
---------------------------------------------------------------------

--foster home and its related tables

-----------------------------------------
--foster home

CREATE TABLE fosterHome(
    idPerson   NUMBER(6)
);

ALTER TABLE fosterHome
ADD 
CONSTRAINT pk_fosterHome PRIMARY KEY (idPerson)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);



-----------------------------------------
--foster condition and its related tables

--requiresFoodDonation

CREATE TABLE requiresFoodDonation(
    id   NUMBER(6),
    name VARCHAR(20) CONSTRAINT fDonation_name_nn NOT NULL
);

ALTER TABLE requiresFoodDonation
ADD 
CONSTRAINT pk_requiresFoodDonation PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);




--fosterCondition

CREATE TABLE fosterCondition(
    id   NUMBER(6),
    idFoodDonation NUMBER(6),
    notes VARCHAR(20)
);

ALTER TABLE fosterCondition
ADD 
CONSTRAINT pk_fosterCondition PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE fosterCondition
ADD  CONSTRAINT fk_idFoodDonation FOREIGN KEY(idFoodDonation) REFERENCES requiresFoodDonation (id);







--fosterHomexfosterCondition


CREATE TABLE fosterHomexfosterCondition(
    idFosterHome NUMBER(6), 
    idFosterCondition NUMBER(6)
    
);



ALTER TABLE fosterHomexfosterCondition
ADD 
CONSTRAINT pk_fHomexfCond PRIMARY KEY (idFosterHome, idFosterCondition)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE fosterHomexfosterCondition
ADD CONSTRAINT fk_fHomexfCond_home FOREIGN KEY(idFosterHome) REFERENCES fosterHome (idPerson);

ALTER TABLE fosterHomexfosterCondition
ADD CONSTRAINT fk_fHomexfCond_condition FOREIGN KEY(idFosterCondition) REFERENCES fosterCondition (id);




--fosterConditionxAccSize


CREATE TABLE fosterConditionxAccSize( 
    idFosterCondition NUMBER(6),
    idPetSize NUMBER(6)
    
);



ALTER TABLE fosterConditionxAccSize
ADD 
CONSTRAINT pk_fCondxAccSize PRIMARY KEY (idFosterCondition,idPetSize)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE fosterConditionxAccSize
ADD CONSTRAINT fk_fCondxAccSize_size FOREIGN KEY(idPetSize) REFERENCES petSize (id);

ALTER TABLE fosterConditionxAccSize
ADD CONSTRAINT fk_fCondxAccSize_condition FOREIGN KEY(idFosterCondition) REFERENCES fosterCondition (id);



--fosterConditionxAccType


CREATE TABLE fosterConditionxAccType( 
    idFosterCondition NUMBER(6),
    idPetType NUMBER(6)
    
);



ALTER TABLE fosterConditionxAccType
ADD 
CONSTRAINT pk_fCondxAccType PRIMARY KEY (idFosterCondition,idPetType)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE fosterConditionxAccType
ADD CONSTRAINT fk_fCondxAccType_type FOREIGN KEY(idPetType) REFERENCES petType (id);

ALTER TABLE fosterConditionxAccType
ADD CONSTRAINT fk_fCondxAccType_condition FOREIGN KEY(idFosterCondition) REFERENCES fosterCondition (id);




----------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------

--RescuerxFosterCondition

CREATE TABLE rescuerxfosterCondition(
    idRescuer NUMBER(6), 
    idFosterCondition NUMBER(6)
    
);



ALTER TABLE rescuerxfosterCondition
ADD 
CONSTRAINT pk_rescxfCond PRIMARY KEY (idRescuer, idFosterCondition)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE rescuerxfosterCondition
ADD CONSTRAINT fk_rescxfCond_home FOREIGN KEY(idRescuer) REFERENCES rescuer (idPerson);

ALTER TABLE rescuerxfosterCondition
ADD CONSTRAINT fk_rescxfCond_condition FOREIGN KEY(idFosterCondition) REFERENCES fosterCondition (id);









