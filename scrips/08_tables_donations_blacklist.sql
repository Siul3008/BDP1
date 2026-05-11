-----------------------------------------------------------------------------------------------
--Students:
--Meylin Carvajal Baltodano
--Luis D. Rivas Castro
--Ma. Fernanda Vega Acosta

-----------------------------------------------------------------------------------------------



--Creation of tables association, blacklist, donation and related tables


---------------------------------------------------------
--Association

CREATE TABLE association(
    id     NUMBER(6) ,
    name VARCHAR(8)CONSTRAINT association_name_nn NOT NULL
);


ALTER TABLE association
ADD 
CONSTRAINT pk_association PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);



--------------------------------------------------------
--Donation All Ocation

CREATE TABLE donationAllLocation(
    id     NUMBER(6) ,
    allocatedAmount NUMBER(10,2),
    percentage NUMBER(3)
);


ALTER TABLE donationAllLocation
ADD 
CONSTRAINT pk_donationAllLocation PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);




----------------------------------------------------------------
--Donation


CREATE TABLE donation(
    id     NUMBER(6) ,
    idCurrency NUMBER(3),
    idDonAllLocation NUMBER(6),
    donationDate DATE NOT NULL ,
    amount NUMBER(10,2)  CONSTRAINT donation_amount_nn NOT NULL, CONSTRAINT donation_amount_min CHECK (amount > 0)
    );


ALTER TABLE donation
ADD 
CONSTRAINT pk_donAllLocation PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE donation
ADD CONSTRAINT fk_donat_idCurrency FOREIGN KEY(idCurrency) REFERENCES currency (id);

ALTER TABLE donation
ADD CONSTRAINT fk_idDonAllLocation FOREIGN KEY(idDonAllLocation) REFERENCES donationAllLocation (id);

  

--------------------------------------------------------------------------
--Association x donation


CREATE TABLE associationxDonation( 
    idAssociation NUMBER(6),
    idDonation NUMBER(6)
    
);

ALTER TABLE associationxDonation
ADD 
CONSTRAINT pk_assocxDonation PRIMARY KEY (idAssociation,idDonation)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE associationxDonation
ADD CONSTRAINT fk_assocxDonation_as FOREIGN KEY(idAssociation) REFERENCES association (id);

ALTER TABLE associationxDonation
ADD CONSTRAINT fk_assocxDonation_don FOREIGN KEY(idDonation) REFERENCES donation (id);

 
---------------------------------------------------------------------------------------------------
--PersonxDonation

CREATE TABLE personxDonation( 
    idPerson NUMBER(6),
    idDonation NUMBER(6)
    
);

ALTER TABLE personxDonation
ADD 
CONSTRAINT pk_personxDonation PRIMARY KEY (idPerson,idDonation)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE personxDonation
ADD CONSTRAINT fk_personxDonation_as FOREIGN KEY(idperson) REFERENCES person (id);

ALTER TABLE personxDonation
ADD CONSTRAINT fk_personxDonation_don FOREIGN KEY(idDonation) REFERENCES donation (id);






---------------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------

--BlacklistReport

CREATE TABLE blacklistReport( 
    id NUMBER(6),
    idPerson NUMBER(6),
    reason VARCHAR(60)CONSTRAINT blList_reason_nn NOT NULL,
    active  VARCHAR(10),
    reportDate DATE NOT NULL

    
);

ALTER TABLE blacklistReport
ADD 
CONSTRAINT pk_blacklistReport PRIMARY KEY (id)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE blacklistReport
ADD CONSTRAINT fk_bl_idPerson FOREIGN KEY(idPerson) REFERENCES person (id);



-----------------------------------------------------------------------------------------------------------------------
--personxBListRep

CREATE TABLE personxBListRep( 
    idPerson NUMBER(6),
    idBLReport NUMBER(6)
    
);

ALTER TABLE personxBListRep
ADD 
CONSTRAINT pk_personxBListRep PRIMARY KEY (idPerson,idBLReport)
USING INDEX TABLESPACE PROJECTI_IDX
PCTFREE 20
STORAGE (INITIAL 10K NEXT 10K PCTINCREASE 0);


ALTER TABLE personxBListRep
ADD CONSTRAINT fk_personxBListRep_per FOREIGN KEY(idperson) REFERENCES person (id);

ALTER TABLE personxBListRep
ADD CONSTRAINT fk_personxBListRep_bl FOREIGN KEY(idBLReport) REFERENCES blacklistReport (id);






