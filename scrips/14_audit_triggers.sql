-----------------------------------------------------------------------------------------------
--Students:
--Meylin Carvajal Baltodano
--Luis D. Rivas Castro
--Ma. Fernanda Vega Acosta

-----------------------------------------------------------------------------------------------

--Creation of all the triggers so that the audit fields are updated 
--before insertions and updates in each table

-------------------------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------------------------
--triggers

-------------------------------------------------------------------------------------------------------------------------------------
CREATE OR REPLACE TRIGGER beforeInsertBreed
BEFORE INSERT
ON breed
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertBreed;
/

CREATE OR REPLACE TRIGGER beforeUpdateBreed
BEFORE INSERT
ON breed
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateBreed;
/

----------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE TRIGGER beforeInsertCurrency
BEFORE INSERT
ON currency
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertCurrency;
/

CREATE OR REPLACE TRIGGER beforeUpdateCurrency
BEFORE INSERT
ON currency
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateCurrency;
/

---------------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE TRIGGER beforeInsertAdopter
BEFORE INSERT
ON adopter
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertAdopter;
/

CREATE OR REPLACE TRIGGER beforeUpdateAdopter
BEFORE INSERT
ON adopter
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateAdopter;
/




----------------------------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE TRIGGER beforeInsertAdopterRating
BEFORE INSERT
ON adopterRating
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertAdopterRating;
/

CREATE OR REPLACE TRIGGER beforeUpdateAdopterRating
BEFORE INSERT
ON adopterRating
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateAdopterRating;
/




-----------------------------------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE TRIGGER beforeInsertAdoption
BEFORE INSERT
ON adoption
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertAdoption;
/

CREATE OR REPLACE TRIGGER beforeUpdateAdoption
BEFORE INSERT
ON adoption
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateAdoption;
/



---------------------------------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE TRIGGER beforeInsertAdoptionApp
BEFORE INSERT
ON adoptionApplication
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertAdoptionApp;
/

CREATE OR REPLACE TRIGGER beforeUpdateAdoptionApp
BEFORE INSERT
ON adoptionApplication
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateAdoptionApp;
/




------------------------------------------------------------------------------------------------------------------------------------



CREATE OR REPLACE TRIGGER beforeInsertAdoptionPht
BEFORE INSERT
ON adoptionPhoto
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertAdoptionPht;
/

CREATE OR REPLACE TRIGGER beforeUpdateAdoptionPht
BEFORE INSERT
ON adoptionPhoto
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateAdoptionPht;
/



------------------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE TRIGGER beforeInsertAdpxAdpPht
BEFORE INSERT
ON adoptionxADPphoto
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertAdpxAdpPht;
/

CREATE OR REPLACE TRIGGER beforeUpdateAdpxAdpPht
BEFORE INSERT
ON adoptionxADPphoto
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateAdpxAdpPht;
/




---------------------------------------------------------------------------------------------------------------------------------------



CREATE OR REPLACE TRIGGER beforeInsertAdpxPet
BEFORE INSERT
ON adoptionxpet
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertAdpxPet;
/

CREATE OR REPLACE TRIGGER beforeUpdateAdpxPet
BEFORE INSERT
ON adoptionxpet
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateAdpxPet;
/

--------------------------------------------------------------------------------------------------------------------------------





CREATE OR REPLACE TRIGGER beforeInsertAdpxResc
BEFORE INSERT
ON adoptionxrescuer
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertAdpxResc;
/

CREATE OR REPLACE TRIGGER beforeUpdateAdpxResc
BEFORE INSERT
ON adoptionxrescuer
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateAdpxResc;
/






---------------------------------------------------------------------------------------------------------------------------------------



CREATE OR REPLACE TRIGGER beforeInsertAssociation
BEFORE INSERT
ON association
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertAssociation;
/

CREATE OR REPLACE TRIGGER beforeUpdateAssociation
BEFORE INSERT
ON association
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateAssociation;
/


------------------------------------------------------------------------------------------------------------



CREATE OR REPLACE TRIGGER beforeInsertAssoxDon
BEFORE INSERT
ON associationxdonation
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertAssoxDon;
/

CREATE OR REPLACE TRIGGER beforeUpdateAssoxDon
BEFORE INSERT
ON associationxdonation
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateAssoxDon;
/


------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE TRIGGER beforeInsertBLReport
BEFORE INSERT
ON BlacklistReport
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertBLReport;
/

CREATE OR REPLACE TRIGGER beforeUpdateBLReport
BEFORE INSERT
ON BlacklistReport
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateBLReport;
/




-----------------------------------------------------------------------------------------------------------



CREATE OR REPLACE TRIGGER beforeInsertCanton
BEFORE INSERT
ON Canton
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertCanton;
/

CREATE OR REPLACE TRIGGER beforeUpdateCanton
BEFORE INSERT
ON Canton
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateCanton;
/


--------------------------------------------------------------------------------------------


CREATE OR REPLACE TRIGGER beforeInsertDisease
BEFORE INSERT
ON Disease
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertDisease;
/

CREATE OR REPLACE TRIGGER beforeUpdateDisease
BEFORE INSERT
ON Disease
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateDisease;
/



-------------------------------------------------------------------------------------------------------


CREATE OR REPLACE TRIGGER beforeInsertDistrict
BEFORE INSERT
ON District
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertDistrict;
/

CREATE OR REPLACE TRIGGER beforeUpdateDistrict
BEFORE INSERT
ON District
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateDistrict;
/



-----------------------------------------------------------------------------------------------



CREATE OR REPLACE TRIGGER beforeInsertDonation
BEFORE INSERT
ON Donation
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertDonation;
/

CREATE OR REPLACE TRIGGER beforeUpdateDonation
BEFORE INSERT
ON Donation
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateDonation;
/


-----------------------------------------------------------------------------------------------



CREATE OR REPLACE TRIGGER beforeInsertDonAllloc
BEFORE INSERT
ON DonationAlllocation
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertDonAllloc;
/

CREATE OR REPLACE TRIGGER beforeUpdateDonAllloc
BEFORE INSERT
ON DonationAlllocation
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateDonAllloc;
/



----------------------------------------------------------------------------------------------------------


CREATE OR REPLACE TRIGGER beforeInsertEmail
BEFORE INSERT
ON Email
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertEmail;
/

CREATE OR REPLACE TRIGGER beforeUpdateEmail
BEFORE INSERT
ON Email
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateEmail;
/



---------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE TRIGGER beforeInsertFCondition
BEFORE INSERT
ON FosterCondition
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertFCondition;
/

CREATE OR REPLACE TRIGGER beforeUpdateFCondition
BEFORE INSERT
ON FosterCondition
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateFCondition;
/



--------------------------------------------------------------------------------




CREATE OR REPLACE TRIGGER beforeInsertFHome
BEFORE INSERT
ON FosterHome
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertFHome;
/

CREATE OR REPLACE TRIGGER beforeUpdateFHome
BEFORE INSERT
ON FosterHome
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateFHome;
/




-------------------------------------------------------------------------------------------------


CREATE OR REPLACE TRIGGER beforeInsertFHxFC
BEFORE INSERT
ON FosterHomexFosterCondition
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertFHxFC;
/

CREATE OR REPLACE TRIGGER beforeUpdateFHxFC
BEFORE INSERT
ON FosterHomexFosterCondition
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateFHxFC;
/



--------------------------------------------------------------------------------------------------------------



CREATE OR REPLACE TRIGGER beforeInsertHealthSt
BEFORE INSERT
ON HealthStatus
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertHealthSt;
/

CREATE OR REPLACE TRIGGER beforeUpdateHealthSt
BEFORE INSERT
ON HealthStatus
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateHealthSt;
/




------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE TRIGGER beforeInsertHStxMed
BEFORE INSERT
ON HealthStxMedicine
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertHStxMed;
/

CREATE OR REPLACE TRIGGER beforeUpdateHStxMed
BEFORE INSERT
ON HealthStxMedicine
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateHStxMed;
/



------------------------------------------------------------------------------------------------------------------



CREATE OR REPLACE TRIGGER beforeInsertHStxTreat
BEFORE INSERT
ON HealthStxTreatment
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertHStxTreat;
/

CREATE OR REPLACE TRIGGER beforeUpdateHStxTreat
BEFORE INSERT
ON HealthStxTreatment
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateHStxTreat;
/




-------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE TRIGGER beforeInsertPetReport
BEFORE INSERT
ON petReport
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertMatch;
/

CREATE OR REPLACE TRIGGER beforeUpdatePetReport
BEFORE INSERT
ON petReport
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdatePetReport;
/




---------------------------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE TRIGGER beforeInsertMedicine
BEFORE INSERT
ON medicine
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertMedicine;
/

CREATE OR REPLACE TRIGGER beforeUpdateMedicine
BEFORE INSERT
ON medicine
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateMedicine;
/




------------------------------------------------------------------------------------------------------



CREATE OR REPLACE TRIGGER beforeInsertColor
BEFORE INSERT
ON color
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertColor;
/

CREATE OR REPLACE TRIGGER beforeUpdateColor
BEFORE INSERT
ON color
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateColor;
/




------------------------------------------------------------------------------------------------------




CREATE OR REPLACE TRIGGER beforeInsertPetSize
BEFORE INSERT
ON petSize
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertPetSize;
/

CREATE OR REPLACE TRIGGER beforeUpdatePetSize
BEFORE INSERT
ON petSize
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdatePetSize;
/




------------------------------------------------------------------------------------------------------

CREATE OR REPLACE TRIGGER beforeInsertPerson
BEFORE INSERT
ON person
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertPerson;
/

CREATE OR REPLACE TRIGGER beforeUpdatePerson
BEFORE INSERT
ON person
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdatePerson;
/



------------------------------------------------------------------------------------------------------------------



CREATE OR REPLACE TRIGGER beforeInsertPerxBLRep
BEFORE INSERT
ON PersonxBListRep
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertPerxBLRep;
/

CREATE OR REPLACE TRIGGER beforeUpdatePerxBLRep
BEFORE INSERT
ON PersonxBListRep
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdatePerxBLRep;
/





----------------------------------------------------------------------------------------------------



CREATE OR REPLACE TRIGGER beforeInsertPerxDon
BEFORE INSERT
ON PersonxDonation
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertPerxDon;
/

CREATE OR REPLACE TRIGGER beforeUpdatePerxDon
BEFORE INSERT
ON PersonxDonation
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdatePerxDon;
/


-------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE TRIGGER beforeInsertPerxEmail
BEFORE INSERT
ON PersonxEmail
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertPerxEmail;
/

CREATE OR REPLACE TRIGGER beforeUpdatePerxEmail
BEFORE INSERT
ON PersonxEmail
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdatePerxEmail;
/




-------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE TRIGGER beforeInsertPerxPhone
BEFORE INSERT
ON PersonxPhone
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertPerxPhone;
/

CREATE OR REPLACE TRIGGER beforeUpdatePerxPhone
BEFORE INSERT
ON PersonxPhone
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdatePerxPhone;
/



------------------------------------------------------------------------------------------------------------------------------



CREATE OR REPLACE TRIGGER beforeInsertPet
BEFORE INSERT
ON Pet
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertPet;
/

CREATE OR REPLACE TRIGGER beforeUpdatePet
BEFORE INSERT
ON Pet
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdatePet;
/




------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE TRIGGER beforeInsertPetContact
BEFORE INSERT
ON PetContact
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertPetContact;
/

CREATE OR REPLACE TRIGGER beforeUpdatePetContact
BEFORE INSERT
ON PetContact
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdatePetContact;
/



-----------------------------------------------------------------------------------------------------------------------



CREATE OR REPLACE TRIGGER beforeInsertPtCntEmail
BEFORE INSERT
ON PetContactxEmail
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertPtCntEmail;
/

CREATE OR REPLACE TRIGGER beforeUpdatePtCntEmail
BEFORE INSERT
ON PetContactxEmail
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdatePtCntEmail;
/



-------------------------------------------------------------------------------------------------------------------------



CREATE OR REPLACE TRIGGER beforeInsertPtCntPhone
BEFORE INSERT
ON PetContactxPhone
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertPtCntPhone;
/

CREATE OR REPLACE TRIGGER beforeUpdatePtCntPhone
BEFORE INSERT
ON PetContactxPhone
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdatePtCntPhone;
/




-------------------------------------------------------------------------------------------------------------------------



CREATE OR REPLACE TRIGGER beforeInsertPetPhoto
BEFORE INSERT
ON PetPhoto
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertPetPhoto;
/

CREATE OR REPLACE TRIGGER beforeUpdatePetPhoto
BEFORE INSERT
ON PetPhoto
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdatePetPhoto;
/





---------------------------------------------------------------------------------------------------------



CREATE OR REPLACE TRIGGER beforeInsertPetStatus
BEFORE INSERT
ON PetStatus
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertPetStatus;
/

CREATE OR REPLACE TRIGGER beforeUpdatePetStatus
BEFORE INSERT
ON PetStatus
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdatePetStatus;
/





-----------------------------------------------------------------------------------------------




CREATE OR REPLACE TRIGGER beforeInsertPetType
BEFORE INSERT
ON PetType
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertPetType;
/

CREATE OR REPLACE TRIGGER beforeUpdatePetType
BEFORE INSERT
ON PetType
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdatePetType;
/




------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE TRIGGER beforeInsertPetxHSt
BEFORE INSERT
ON PetxHealthStatus
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertPetxHSt;
/

CREATE OR REPLACE TRIGGER beforeUpdatePetxHst
BEFORE INSERT
ON PetxHealthStatus
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdatePetxHst;
/



--------------------------------------------------------------------------------------------------

CREATE OR REPLACE TRIGGER beforeInsertPetxVet
BEFORE INSERT
ON PetxVeterinarian
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertPetxVet;
/

CREATE OR REPLACE TRIGGER beforeUpdatePetxVet
BEFORE INSERT
ON PetxVeterinarian
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdatePetxVet;
/



---------------------------------------------------------------------------------------------------------

CREATE OR REPLACE TRIGGER beforeInsertPhone
BEFORE INSERT
ON Phone
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertPhone;
/

CREATE OR REPLACE TRIGGER beforeUpdatePhone
BEFORE INSERT
ON Phone
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdatePhone;
/


----------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE TRIGGER beforeInsertProvince
BEFORE INSERT
ON Province
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertProvince;
/

CREATE OR REPLACE TRIGGER beforeUpdateProvince
BEFORE INSERT
ON Province
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateProvince;
/



-------------------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE TRIGGER beforeInsertRqFoodDon
BEFORE INSERT
ON RequiresFoodDonation
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertRqFoodDon;
/

CREATE OR REPLACE TRIGGER beforeUpdateRqFoodDon
BEFORE INSERT
ON RequiresFoodDonation
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateRqFoodDon;
/



------------------------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE TRIGGER beforeInsertRescuer
BEFORE INSERT
ON Rescuer
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertRescuer;
/

CREATE OR REPLACE TRIGGER beforeUpdateRescuer
BEFORE INSERT
ON Rescuer
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateRescuer;
/



----------------------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE TRIGGER beforeInsertRescxFc
BEFORE INSERT
ON RescuerxFosterCondition
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertRescxFc;
/

CREATE OR REPLACE TRIGGER beforeUpdateRescxFc
BEFORE INSERT
ON RescuerxFosterCondition
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateRescxFc;
/



-----------------------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE TRIGGER beforeInsertRescxPet
BEFORE INSERT
ON RescuerxPet
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertRescxPet;
/

CREATE OR REPLACE TRIGGER beforeUpdateRescxPet
BEFORE INSERT
ON RescuerxPet
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateRescxPet;
/


------------------------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE TRIGGER beforeInsertReward
BEFORE INSERT
ON Reward
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertReward;
/

CREATE OR REPLACE TRIGGER beforeUpdateReward
BEFORE INSERT
ON Reward
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateReward;
/



--------------------------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE TRIGGER beforeInsertStarRat
BEFORE INSERT
ON StarRating
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertStarRat;
/

CREATE OR REPLACE TRIGGER beforeUpdateStarRat
BEFORE INSERT
ON StarRating
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateStarRat;
/



----------------------------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE TRIGGER beforeInsertParameter
BEFORE INSERT
ON SysParameter
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertParameter;
/

CREATE OR REPLACE TRIGGER beforeUpdateParameter
BEFORE INSERT
ON SysParameter
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateParameter;
/


--------------------------------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE TRIGGER beforeInsertTrainingE
BEFORE INSERT
ON Trainingease
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertTrainingE;
/

CREATE OR REPLACE TRIGGER beforeUpdateTrainingE
BEFORE INSERT
ON Trainingease
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateTrainingE;
/


------------------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE TRIGGER beforeInsertTreatment
BEFORE INSERT
ON Treatment
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertTreatment;
/

CREATE OR REPLACE TRIGGER beforeUpdateTreatment
BEFORE INSERT
ON Treatment
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateTreatment;
/



----------------------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE TRIGGER beforeInsertVeterinarian
BEFORE INSERT
ON Veterinarian
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertVeterinarian;
/

CREATE OR REPLACE TRIGGER beforeUpdateVeterinarian
BEFORE INSERT
ON Veterinarian
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateVeterinarian;
/



---------------------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE TRIGGER beforeInsertVetxEmail
BEFORE INSERT
ON VeterinarianxEmail
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertVetxEmail;
/

CREATE OR REPLACE TRIGGER beforeUpdateVetxEmail
BEFORE INSERT
ON VeterinarianxEmail
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateVetxEmail;
/



--------------------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE TRIGGER beforeInsertVetxPhone
BEFORE INSERT
ON VeterinarianxPhone
FOR EACH ROW
BEGIN
    :new.Creation_date := SYSDATE;
    :new.Created_by := USER;

END beforeInsertVetxPhone;
/

CREATE OR REPLACE TRIGGER beforeUpdateVetxPhone
BEFORE INSERT
ON VeterinarianxPhone
FOR EACH ROW
BEGIN

    :new.Updated_date :=SYSDATE;
    :new.Updated_by :=USER ;
END beforeUpdateVetxPhone;
/