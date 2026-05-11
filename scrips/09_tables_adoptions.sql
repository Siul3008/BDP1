
-----------------------------------------------------------------------------------------------
--Students:
--Meylin Carvajal Baltodano
--Luis D. Rivas Castro
--Ma. Fernanda Vega Acosta

-----------------------------------------------------------------------------------------------


--Creation of tables needed for the adoption process









--------------------------------------
--Adoption and its related tables


---------------------------------------------------

--AdoptionApplication
CREATE TABLE adoptionApplication(
    id NUMBER(6),
    yard VARCHAR(25) CONSTRAINT application_yard_nn NOT NULL,
    exerciseTime VARCHAR(10) CONSTRAINT application_excTime_nn NOT NULL,
    answers VARCHAR(100) CONSTRAINT application_ans_nn NOT NULL,
    otherPets VARCHAR(50) CONSTRAINT application_otherP_nn NOT NULL,
    housingType VARCHAR(25) CONSTRAINT application_houseT_nn NOT NULL

);



ALTER TABLE adoptionApplication
ADD 
CONSTRAINT pk_adoptionApplication PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);




------------------------------------------------------------------------------------------

--adopterRating

CREATE TABLE adopterRating(
    id NUMBER(6),
    name VARCHAR(25) CONSTRAINT adoptRat_name_nn NOT NULL,
    star      VARCHAR(10) CONSTRAINT adoptRat_star_nn NOT NULL,
    ratingDate       DATE NOT NULL,
    note VARCHAR (50)
    


);



ALTER TABLE adopterRating
ADD 
CONSTRAINT pk_adopterRating PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);




--------------------------------------------------------------------------
--adoption


CREATE TABLE adoption(
    id NUMBER(6),
    idApplication NUMBER(6) CONSTRAINT adopt_application_nn NOT NULL,
    idAdopterRating NUMBER(6) CONSTRAINT adopt_adRating_nn NOT NULL,
    adoptionDate       DATE NOT NULL,
    adopterNotes VARCHAR (100),
    followUpNotes VARCHAR (100)
    


);



ALTER TABLE adoption
ADD 
CONSTRAINT pk_adoption PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE adoption
ADD CONSTRAINT fk_idApplication FOREIGN KEY(idApplication) REFERENCES adoptionApplication (id);

ALTER TABLE adoption
ADD CONSTRAINT fk_idAdopterRating FOREIGN KEY(idAdopterRating) REFERENCES adopterRating (id);




------------------------------------------------------------------------------------------------------
--Adoption photo


CREATE TABLE adoptionPhoto(
    id NUMBER(6),
--    idAdoption NUMBER(6)CONSTRAINT adoptP_adoption_nn NOT NULL,
    photoType VARCHAR(20) CONSTRAINT adoptP_type_nn NOT NULL,
    photoPath VARCHAR(255) CONSTRAINT adoptP_path_nn NOT NULL


);



ALTER TABLE adoptionPhoto
ADD 
CONSTRAINT pk_adoptionPhoto PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);




--Adoption x adoption photo

CREATE TABLE adoptionxAdpPhoto( 
    idAdoption NUMBER(6),
    idPhoto NUMBER(6)
    
);

ALTER TABLE adoptionxAdpPhoto
ADD 
CONSTRAINT pk_adoptionxAdpPhoto PRIMARY KEY (idAdoption,idPhoto)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE adoptionxAdpPhoto
ADD CONSTRAINT fk_adoptxAdpPhoto_ad FOREIGN KEY(idAdoption) REFERENCES adoption (id);

ALTER TABLE adoptionxAdpPhoto
ADD CONSTRAINT fk_adoptxAdpPhoto_pt FOREIGN KEY(idPhoto) REFERENCES adoptionPhoto (id);


----------------------------------------------------------------------------------------------------------------------------------------

--AdoptionxPet

CREATE TABLE adoptionxPet( 
    idAdoption NUMBER(6),
    idPet NUMBER(6)
    
);

ALTER TABLE adoptionxPet
ADD 
CONSTRAINT pk_adoptionxPet PRIMARY KEY (idAdoption,idPet)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE adoptionxPet
ADD CONSTRAINT fk_personxPet_ad FOREIGN KEY(idAdoption) REFERENCES adoption (id);

ALTER TABLE adoptionxPet
ADD CONSTRAINT fk_adoptionxPet_pet FOREIGN KEY(idPet) REFERENCES pet (id);



----------------------------------------------------------------------------------------------------------------------------
--adoptionxRescuer

CREATE TABLE adoptionxRescuer( 
    idAdoption NUMBER(6),
    idRescuer NUMBER(6)
    
);

ALTER TABLE adoptionxRescuer
ADD 
CONSTRAINT pk_adoptionxRescuer PRIMARY KEY (idAdoption,idRescuer)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE adoptionxRescuer
ADD CONSTRAINT fk_adoptionxres_ad FOREIGN KEY(idAdoption) REFERENCES adoption (id);

ALTER TABLE adoptionxRescuer
ADD CONSTRAINT fk_adoptionxres_res FOREIGN KEY(idrescuer) REFERENCES rescuer (idPerson);


