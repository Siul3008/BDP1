-----------------------------------------------------------------------------------------------
--Students:
--Meylin Carvajal Baltodano
--Luis D. Rivas Castro
--Ma. Fernanda Vega Acosta

-----------------------------------------------------------------------------------------------



--Creation of other tables needed



-------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------
--log and its related tables

CREATE TABLE journal(
    id     NUMBER(6) ,
    fieldName VARCHAR(25) CONSTRAINT journal_fieldname_nn NOT NULL,
    previousValue VARCHAR(25) CONSTRAINT journal_prevState_nn NOT NULL,
    currentValue VARCHAR(25) CONSTRAINT journal_currValue_nn NOT NULL,
    changeDate DATE NOT NULL, 
    changedBy VARCHAR(25)CONSTRAINT journal_changedBy_nn NOT NULL,
    createdBy VARCHAR(25) CONSTRAINT journal_createdBy_nn NOT NULL,
    modifiedBy VARCHAR(25) CONSTRAINT journal_modBy_nn NOT NULL,
    createdAt VARCHAR(25) CONSTRAINT journal_createdAt_nn NOT NULL,
    modifiedAt VARCHAR(25) CONSTRAINT journal_modAt_nn NOT NULL
);


ALTER TABLE journal
ADD 
CONSTRAINT pk_journal PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);




--PersonxJournal

CREATE TABLE personxJournal( 
    idPerson NUMBER(6),
    idJournal NUMBER(6)
    
);



ALTER TABLE personxJournal
ADD 
CONSTRAINT pk_personxJournal PRIMARY KEY (idPerson,idJournal)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE personxJournal
ADD CONSTRAINT fk_personxJournal_journal FOREIGN KEY(idJournal) REFERENCES journal (id);

ALTER TABLE personxJournal
ADD CONSTRAINT fk_personxJournal_person FOREIGN KEY(idPerson) REFERENCES person (id);


-------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------

--Email and its related tables


CREATE TABLE email(
    id     NUMBER(6) ,
    emailAddress VARCHAR2(100) CONSTRAINT email_address_nn NOT NULL

);


ALTER TABLE email
ADD 
CONSTRAINT pk_email PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


-------------------------------------------------------------------------------
--personxEmail

CREATE TABLE personxEmail( 
    idPerson NUMBER(6),
    idEmail NUMBER(6)
    
);

ALTER TABLE personxEmail
ADD 
CONSTRAINT pk_personxEmail PRIMARY KEY (idPerson,idEmail)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE personxEmail
ADD CONSTRAINT fk_personxEmail_email FOREIGN KEY(idEmail) REFERENCES email (id);

ALTER TABLE personxEmail
ADD CONSTRAINT fk_personxEmail_person FOREIGN KEY(idPerson) REFERENCES person (id);

------------------------------------------------------------------


--veterinarianxEmail

CREATE TABLE veterinarianxEmail( 
    idVeterinarian NUMBER(6),
    idEmail NUMBER(6)
    
);

ALTER TABLE veterinarianxEmail
ADD 
CONSTRAINT pk_veterinarianxEmail PRIMARY KEY (idVeterinarian,idEmail)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE veterinarianxEmail
ADD CONSTRAINT fk_vetxEmail_email FOREIGN KEY(idEmail) REFERENCES email (id);

ALTER TABLE veterinarianxEmail
ADD CONSTRAINT fk_vetxEmail_vet FOREIGN KEY(idVeterinarian) REFERENCES veterinarian (id);



----------------------------------------------


--petContactxEmail

CREATE TABLE petContactxEmail( 
    idPetContact NUMBER(6),
    idEmail NUMBER(6)
    
);

ALTER TABLE petContactxEmail
ADD 
CONSTRAINT pk_petContactxEmail PRIMARY KEY (idPetContact,idEmail)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE petContactxEmail
ADD CONSTRAINT fk_pContxEmail_email FOREIGN KEY(idEmail) REFERENCES email (id);

ALTER TABLE petContactxEmail
ADD CONSTRAINT fk_pContxEmail_cont FOREIGN KEY(idPetContact) REFERENCES petContact (id);



----------------------------------------------------------------------------------
--------------------------------------------------------------------------------------

--Phone and its related tables


-----------------------------------------------
--phone

CREATE TABLE phone(
    id     NUMBER(6) ,
    phoneNumber VARCHAR(8)CONSTRAINT phone_number_nn NOT NULL, CONSTRAINT phone_number_min CHECK (LENGTH(phoneNumber) = 8)

);


ALTER TABLE phone
ADD 
CONSTRAINT pk_phone PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);

--------------------------------------------------------------

--personxEmail

CREATE TABLE personxPhone( 
    idPerson NUMBER(6),
    idPhone NUMBER(6)
    
);

ALTER TABLE personxPhone
ADD 
CONSTRAINT pk_personxPhone PRIMARY KEY (idPerson,idPhone)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE personxPhone
ADD CONSTRAINT fk_personxPhone_phone FOREIGN KEY(idPhone) REFERENCES phone (id);

ALTER TABLE personxPhone
ADD CONSTRAINT fk_personxPhone_person FOREIGN KEY(idPerson) REFERENCES person (id);


--------------------------------------------------------------

--vetrinarianxPhone

CREATE TABLE veterinarianxPhone( 
    idVeterinarian NUMBER(6),
    idPhone NUMBER(6)
    
);

ALTER TABLE veterinarianxPhone
ADD 
CONSTRAINT pk_veterinarianxPhone PRIMARY KEY (idVeterinarian,idPhone)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE veterinarianxPhone
ADD CONSTRAINT fk_vetxPhone_phone FOREIGN KEY(idPhone) REFERENCES phone (id);

ALTER TABLE veterinarianxPhone
ADD CONSTRAINT fk_vetxPhone_vet FOREIGN KEY(idVeterinarian) REFERENCES veterinarian (id);

-------------------------------------------------------------------

--petContactxPhone

CREATE TABLE petContactxPhone( 
    idPetContact NUMBER(6),
    idPhone NUMBER(6)
    
);

ALTER TABLE petContactxPhone
ADD 
CONSTRAINT pk_petContactxPhone PRIMARY KEY (idPetContact,idPhone)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE petContactxPhone
ADD CONSTRAINT fk_pContxPhone_phone FOREIGN KEY(idPhone) REFERENCES phone (id);

ALTER TABLE petContactxPhone
ADD CONSTRAINT fk_pContxPhone_cont FOREIGN KEY(idPetContact) REFERENCES petContact (id);






-----------------------------------------------------------------------------------------
--match

CREATE TABLE petReport( 
    id NUMBER(6),
    idColor NUMBER(6),
    idSize NUMBER(6),
    idPetStatus NUMBER (6),
    idBreed NUMBER(6),
    idPetType NUMBER(6),
    reportDate DATE NOT NULL 
    
    
);




ALTER TABLE petReport
ADD 
CONSTRAINT pk_petreport PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE PetReport
ADD CONSTRAINT fk_report_idSize FOREIGN KEY(idSize) REFERENCES petSize (id);

ALTER TABLE PetReport
ADD CONSTRAINT fk_report_idColor FOREIGN KEY(idColor) REFERENCES color (id);

ALTER TABLE PetReport
ADD CONSTRAINT fk_report_idPstatus FOREIGN KEY(idPetStatus) REFERENCES petStatus (id);

ALTER TABLE PetReport
ADD CONSTRAINT fk_report_idBreed FOREIGN KEY(idBreed) REFERENCES breed (id);

ALTER TABLE PetReport
ADD CONSTRAINT fk_report_idPetType FOREIGN KEY(idPetType) REFERENCES petType (id);



-----------------------------------------------------------------------------------------------------------------------------------------------------
--rescuerxPet

CREATE TABLE rescuerxPet( 
    idRescuer NUMBER(6),
    idPet NUMBER(6)
    
);

ALTER TABLE rescuerxPet
ADD 
CONSTRAINT pk_rescuerxPet PRIMARY KEY (idRescuer,idPet)
USING INDEX TABLESPACE PROJECTI_IDX 
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE rescuerxPet
ADD CONSTRAINT fk_personxBListRep_ad FOREIGN KEY(idRescuer) REFERENCES rescuer (idPerson);

ALTER TABLE rescuerxPet
ADD CONSTRAINT fk_rescuerxPet_pet FOREIGN KEY(idPet) REFERENCES pet (id);






