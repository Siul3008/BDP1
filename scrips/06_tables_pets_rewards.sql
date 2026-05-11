
-----------------------------------------------------------------------------------------------
--Students:
--Meylin Carvajal Baltodano
--Luis D. Rivas Castro
--Ma. Fernanda Vega Acosta

-----------------------------------------------------------------------------------------------



--Creation of the table pet and all the rest of needed additional tables related to it





----------------------------------------------------------------

--Pet and its related tables

------------------------------------------------------


-----------------------------------------------------------

--Breed


CREATE TABLE breed(
    id     NUMBER(6) ,
    name    VARCHAR(25) CONSTRAINT breed_name_nn NOT NULL,
    idPetType  NUMBER(6) CONSTRAINT breed_ptype_nn NOT NULL
);


ALTER TABLE breed
ADD 
CONSTRAINT pk_breed PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);



ALTER TABLE breed
ADD CONSTRAINT fk_breed_petType FOREIGN KEY(idPetType) REFERENCES petType (id);

---------------------------------------------------------

--petPhoto

CREATE TABLE petPhoto(
    id     NUMBER(6) ,
    photoPath  VARCHAR(255) CONSTRAINT petPhoto_path_nn NOT NULL
);


ALTER TABLE petPhoto
ADD 
CONSTRAINT pk_petPhoto PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);

--------------------------------------------------------------------

--petContact


CREATE TABLE petContact(
    id     NUMBER(6) 
);


ALTER TABLE petContact
ADD 
CONSTRAINT pk_petContact PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);

---------------------------------------------------------------

--PetStatus


CREATE TABLE petStatus(
    id     NUMBER(6) ,
    status  VARCHAR(25) CONSTRAINT petStatus_Status_nn NOT NULL
);


ALTER TABLE petStatus
ADD 
CONSTRAINT pk_petStatus PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


------------------------------------------------

--trainingEase

CREATE TABLE trainingEase(
    id     NUMBER(6) ,
    name  VARCHAR(25) CONSTRAINT trainingEase_name_nn NOT NULL
);


ALTER TABLE trainingEase
ADD 
CONSTRAINT pk_trainingEase PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);




---------------------------------------------------------------------------


--Pet


CREATE TABLE pet(
    id     NUMBER(6) ,
    idPetType NUMBER (6) CONSTRAINT pet_type_nn NOT NULL,
    idBreed NUMBER (6),
    idPetPhoto NUMBER (6),
    idPetContact NUMBER (6),
    idPetStatus NUMBER (6),
    idTrainingEase NUMBER (6),
    idLocation NUMBER (6),
    name  VARCHAR(25) CONSTRAINT pet_name_nn NOT NULL, 
    description VARCHAR (50),
    needSpace VARCHAR(20),
    energyLevel VARCHAR (10),
    idColor NUMBER(6),
    idSize NUMBER(6),
    age NUMBER(2),
    chip VARCHAR2(30),
    eventDate DATE
    
    
);






ALTER TABLE pet
ADD 
CONSTRAINT pk_pet PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE pet
ADD CONSTRAINT fk_pet_idPetSize FOREIGN KEY(idSize) REFERENCES petSize (id);

ALTER TABLE pet
ADD CONSTRAINT fk_pet_idPetColor FOREIGN KEY(idColor) REFERENCES color (id);


ALTER TABLE pet
ADD CONSTRAINT fk_idPetType FOREIGN KEY(idPetType) REFERENCES petType (id);

ALTER TABLE pet
ADD CONSTRAINT fk_idBreed FOREIGN KEY(idBreed) REFERENCES breed (id);


ALTER TABLE pet
ADD CONSTRAINT fk_idPetPhoto FOREIGN KEY(idPetPhoto) REFERENCES petPhoto (id);


ALTER TABLE pet
ADD CONSTRAINT fk_idPetContact FOREIGN KEY(idPetContact) REFERENCES petContact (id);


ALTER TABLE pet
ADD CONSTRAINT fk_idPetStatus FOREIGN KEY(idPetStatus) REFERENCES petStatus (id);


ALTER TABLE pet
ADD CONSTRAINT fk_idTrainingEase FOREIGN KEY(idTrainingEase) REFERENCES trainingEase (id);



ALTER TABLE pet
ADD CONSTRAINT fk_idLocation FOREIGN KEY(idLocation) REFERENCES district (id);


-----------------------------------------------------------
--reward

CREATE TABLE reward(
    id     NUMBER(6) ,
    idPet NUMBER(6) CONSTRAINT reward_idpet_nn NOT NULL,
    idCurrency NUMBER(3) CONSTRAINT reward_currency_nn NOT NULL,
    amount NUMBER(6)  CONSTRAINT reward_amount_nn NOT NULL, CONSTRAINT reward_amount_min CHECK (amount > 0)
    
);


ALTER TABLE reward
ADD 
CONSTRAINT pk_reward PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);



ALTER TABLE reward
ADD CONSTRAINT fk_reward_idPet FOREIGN KEY(idPet) REFERENCES pet (id);

ALTER TABLE reward
ADD CONSTRAINT fk_reward_idCurrency FOREIGN KEY(idCurrency) REFERENCES currency (id);

----------------------------------------------------

--petx healthstatus


CREATE TABLE petxHealthStatus( 
    idPet NUMBER(6),
    idHealthStatus NUMBER(6)
    
);

ALTER TABLE petxHealthStatus
ADD 
CONSTRAINT pk_petxHealthStatus PRIMARY KEY (idPet,idHealthStatus)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE petxHealthStatus
ADD CONSTRAINT fk_petxHealthSt_pet FOREIGN KEY(idPet) REFERENCES pet (id);

ALTER TABLE petxHealthStatus
ADD CONSTRAINT fk_petxHealthSt_hs FOREIGN KEY(idHealthStatus) REFERENCES healthStatus (id);

  


-------------------------------------------------------------------------


--Pet x veterinarian



CREATE TABLE petxVeterinarian( 
    idPet NUMBER(6),
    idVeterinarian NUMBER(6)
    
);

ALTER TABLE petxVeterinarian
ADD 
CONSTRAINT pk_petxVeterinarian PRIMARY KEY (idPet,idVeterinarian)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE petxVeterinarian
ADD CONSTRAINT fk_petxVeterinarian_pet FOREIGN KEY(idPet) REFERENCES pet (id);

ALTER TABLE petxVeterinarian
ADD CONSTRAINT fk_petxVeterinarian_vet FOREIGN KEY(idVeterinarian) REFERENCES veterinarian (id);

  



