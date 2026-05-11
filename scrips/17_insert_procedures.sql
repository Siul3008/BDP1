-----------------------------------------------------------------------------------------------
--Students:
--Meylin Carvajal Baltodano
--Luis D. Rivas Castro
--Ma. Fernanda Vega Acosta

-----------------------------------------------------------------------------------------------


--Creation of insertion procedures


------------------------------------------------------------------------------------------------------------------------
-- ADOPTER
CREATE OR REPLACE PROCEDURE insertAdopter(pIdPerson IN NUMBER, pIdStarRating IN NUMBER, pNote IN VARCHAR2)
AS
BEGIN
    INSERT INTO ADOPTER(idPerson, idStarRating, note)
    VALUES(pIdPerson, pIdStarRating, pNote);
    COMMIT;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );
END insertAdopter;
/

-- ADOPTERRATING
CREATE OR REPLACE PROCEDURE insertAdopterRating(pName IN VARCHAR2, pStar IN VARCHAR2, pRatingDate IN DATE, pNote IN VARCHAR2)
AS
BEGIN
    INSERT INTO ADOPTERRATING(id, name, star, ratingDate, note)
    VALUES(sAdopterRating.nextval, pName, pStar, pRatingDate, pNote);
    COMMIT;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );
END insertAdopterRating;
/

-- ADOPTION
CREATE OR REPLACE PROCEDURE insertAdoption(pIdApplication IN NUMBER, pIdAdopterRating IN NUMBER, pAdoptionDate IN DATE,
    pAdoptionNotes IN VARCHAR2, pFollowUpNotes IN VARCHAR2)
AS
BEGIN
    INSERT INTO ADOPTION(id, idApplication, idAdopterRating, adoptionDate, adopterNotes, followUpNotes)
    VALUES(sAdoption.nextval, pIdApplication, pIdAdopterRating, pAdoptionDate, pAdoptionNotes, pFollowUpNotes);
    COMMIT;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );
END insertAdoption;
/

-- ADOPTIONAPPLICATION
CREATE OR REPLACE PROCEDURE insertAdoptionApplication(pYard IN VARCHAR2, pExerciseTime IN VARCHAR2, pAnswers IN VARCHAR2,
    pOtherPets IN VARCHAR2, pHousingType IN VARCHAR2)
AS
BEGIN
    INSERT INTO ADOPTIONAPPLICATION(id, yard, exerciseTime, answers, otherPets, housingType)
    VALUES(sAdoptionAppication.nextval, pYard, pExerciseTime, pAnswers, pOtherPets, pHousingType);
    COMMIT;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );
END insertAdoptionApplication;
/

-- ADOPTIONPHOTO
CREATE OR REPLACE PROCEDURE insertAdoptionPhoto(pPhotoType IN VARCHAR2, pPhotoPath IN VARCHAR2)
AS
BEGIN
    INSERT INTO ADOPTIONPHOTO(id, photoType, photoPath)
    VALUES(sAdoptionPhoto.nextval, pPhotoType, pPhotoPath);
    COMMIT;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );
END insertAdoptionPhoto;
/

-- ADOPTIONXADPPHOTO
CREATE OR REPLACE PROCEDURE insertAdoptionxPhoto(pIdAdoption IN NUMBER, pIdPhoto IN NUMBER)
AS
BEGIN
    INSERT INTO ADOPTIONXADPPHOTO(idAdoption, idPhoto)
    VALUES(pIdAdoption, pIdPhoto);
    COMMIT;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );
END insertAdoptionxPhoto;
/

-- ADOPTIONXPET
CREATE OR REPLACE PROCEDURE insertAdoptionxPet(pIdAdoption IN NUMBER, pIdPet IN NUMBER)
AS
BEGIN
    INSERT INTO ADOPTIONXPET(idAdoption, idPet)
    VALUES(pIdAdoption, pIdPet);
    COMMIT;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );
END insertAdoptionxPet;
/

-- ADOPTIONXRESCUER
CREATE OR REPLACE PROCEDURE insertAdoptionxRescuer(pIdAdoption IN NUMBER, pIdRescuer IN NUMBER)
AS
BEGIN
    INSERT INTO ADOPTIONXRESCUER(idAdoption, idRescuer)
    VALUES(pIdAdoption, pIdRescuer);
    COMMIT;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );
END insertAdoptionxRescuer;
/

-- ASSOCIATION
CREATE OR REPLACE PROCEDURE insertAssociation(pName IN VARCHAR2)
AS
BEGIN
    INSERT INTO ASSOCIATION(id, name)
    VALUES(sAssociation.nextval, pName);
    COMMIT;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );
END insertAssociation;
/

-- ASSOCIATIONXDONATION
CREATE OR REPLACE PROCEDURE insertAssociationxDonation(pIdAssociation IN NUMBER, pIdDonation IN NUMBER)
AS
BEGIN
    INSERT INTO ASSOCIATIONXDONATION(idAssociation, idDonation)
    VALUES(pIdAssociation, pIdDonation);
    COMMIT;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );
END insertAssociationxDonation;
/

-- BLACKLISTREPORT
CREATE OR REPLACE PROCEDURE insertBlacklist(pIdPerson IN NUMBER, pReason IN VARCHAR2, pActive IN VARCHAR2, pReportDate IN DATE)
AS
BEGIN
    INSERT INTO BLACKLISTREPORT(id, idPerson, reason, active, reportDate)
    VALUES(sBlackistReport.nextval, pIdPerson, pReason, pActive, pReportDate);
    COMMIT;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );
END insertBlacklist;
/

-- BREED
CREATE OR REPLACE PROCEDURE insertBreed(pName IN VARCHAR2, pIdPetType IN NUMBER)
AS
BEGIN
    INSERT INTO BREED(id, name, idPetType)
    VALUES(sBreed.nextval, pName, pIdPetType);
    COMMIT;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );
END insertBreed;
/

-- CANTON
CREATE OR REPLACE PROCEDURE insertCanton(pIdProvince IN NUMBER, pName IN VARCHAR2)
AS
BEGIN
    INSERT INTO CANTON(id, idProvince, name)
    VALUES(sCanton.nextval, pIdProvince, pName);
    COMMIT;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );
END insertCanton;
/

-- COLOR
CREATE OR REPLACE PROCEDURE insertColor(pName IN VARCHAR2)
AS
BEGIN
    INSERT INTO COLOR(id, name)
    VALUES(sColor.nextval, pName);
    COMMIT;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );
END insertColor;
/

-- CURRENCY
CREATE OR REPLACE PROCEDURE insertCurrency(pName IN VARCHAR2, pAcronym IN VARCHAR2)
AS
BEGIN
    INSERT INTO CURRENCY(id, name, acronym)
    VALUES(sCurrency.nextval, pName, pAcronym);
    COMMIT;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );
END insertCurrency;
/

-- DISEASE
CREATE OR REPLACE PROCEDURE insertDisease(pName IN VARCHAR2)
AS
BEGIN
    INSERT INTO DISEASE(id, name)
    VALUES(sDisease.nextval, pName);
    COMMIT;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );
END insertDisease;
/

-- DISTRICT
CREATE OR REPLACE PROCEDURE insertDistrict(pIdCanton IN NUMBER, pName IN VARCHAR2)
AS
BEGIN
    INSERT INTO DISTRICT(id, idCanton, name)
    VALUES(sDistrict.nextval, pIdCanton, pName);
    COMMIT;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );
END insertDistrict;
/

-- DONATION
CREATE OR REPLACE PROCEDURE insertDonation(pIdCurrency IN NUMBER, pIdDonationAllLocation IN NUMBER,
    pDonationDate IN DATE, pAmount IN NUMBER)
AS
BEGIN
    INSERT INTO DONATION(id, idCurrency, IDDONALLLOCATION, donationDate, amount)
    VALUES(sDonation.nextval, pIdCurrency, pIdDonationAllLocation, pDonationDate, pAmount);
    COMMIT;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );
END insertDonation;
/

-- DONATIONALLLOCATION
CREATE OR REPLACE PROCEDURE insertDonationAllLocation(pAllLocatedAmount IN NUMBER, pPercentage IN NUMBER)
AS
BEGIN
    INSERT INTO DONATIONALLLOCATION(id, allocatedAmount, percentage)
    VALUES(sDonationAllLocation.nextval, pAllLocatedAmount, pPercentage);
    COMMIT;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );
END insertDonationAllLocation;
/

-- EMAIL
CREATE OR REPLACE PROCEDURE insertEmail(pEmailAddress IN VARCHAR2)
AS
BEGIN
    INSERT INTO EMAIL(id, emailAddress)
    VALUES(sEmail.nextval, pEmailAddress);
    COMMIT;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );
END insertEmail;
/
-- FOSTERCONDITION
CREATE OR REPLACE PROCEDURE insertFosterCondition(pIdFoodDonation IN NUMBER, pNotes IN VARCHAR2)
AS
BEGIN
    INSERT INTO FOSTERCONDITION(id, idFoodDonation, notes)
    VALUES(sFosterCondition.nextval, pIdFoodDonation, pNotes);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertFosterCondition;
/

-- FOSTERCONDITIONXACCSIZE
CREATE OR REPLACE PROCEDURE insertFosterConditionxAccSize(pIdFosterCondition IN NUMBER, pIdPetSize IN NUMBER)
AS
BEGIN
    INSERT INTO FOSTERCONDITIONXACCSIZE(idFosterCondition, idPetSize)
    VALUES(pIdFosterCondition, pIdPetSize);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertFosterConditionxAccSize;
/

-- FOSTERCONDITIONXACCTYPE
CREATE OR REPLACE PROCEDURE insertFosterConditionxAccType(pIdFosterCondition IN NUMBER, pIdPetType IN NUMBER)
AS
BEGIN
    INSERT INTO FOSTERCONDITIONXACCTYPE(idFosterCondition, idPetType)
    VALUES(pIdFosterCondition, pIdPetType);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertFosterConditionxAccType;
/

-- FOSTERHOME
CREATE OR REPLACE PROCEDURE insertFosterHome(pIdPerson IN NUMBER)
AS
BEGIN
    INSERT INTO FOSTERHOME(idPerson)
    VALUES(pIdPerson);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertFosterHome;
/

-- FOSTERHOMEXFOSTERCONDITION
CREATE OR REPLACE PROCEDURE insertFHomexFCondition(pIdFosterHome IN NUMBER, pIdFosterCondition IN NUMBER)
AS
BEGIN
    INSERT INTO FOSTERHOMEXFOSTERCONDITION(idFosterHome, idFosterCondition)
    VALUES(pIdFosterHome, pIdFosterCondition);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertFHomexFCondition;
/

-- HEALTHSTATUS
CREATE OR REPLACE PROCEDURE insertHealthStatus(pIllnesState IN VARCHAR2, pDescription IN VARCHAR2)
AS
BEGIN
    INSERT INTO HEALTHSTATUS(id, illnessState, description)
    VALUES(sHealthStatus.nextval, pIllnesState, pDescription);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertHealthStatus;
/

-- HEALTHXDISEASE
CREATE OR REPLACE PROCEDURE insertHealthxDisease(pIdHealthStatus IN NUMBER, pIdDisease IN NUMBER, pDescription IN VARCHAR2)
AS
BEGIN
    INSERT INTO HEALTHSTXDISEASE(idHealthStatus, idDisease, description)
    VALUES(pIdHealthStatus, pIdDisease, pDescription);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertHealthxDisease;
/

-- HEALTHXMEDICINE
CREATE OR REPLACE PROCEDURE insertHealthxMedicine(pIdHealthStatus IN NUMBER, pIdMedicine IN NUMBER, pDose IN VARCHAR2)
AS
BEGIN
    INSERT INTO HEALTHSTXMEDICINE(idHealthStatus, idMedicine, dose)
    VALUES(pIdHealthStatus, pIdMedicine, pDose);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertHealthxMedicine;
/

-- HEALTHXTREATMENT
CREATE OR REPLACE PROCEDURE insertHealthxTreatment(pIdHealthStatus IN NUMBER, pIdTreatment IN NUMBER)
AS
BEGIN
    INSERT INTO HEALTHSTXTREATMENT(idHealthStatus, idTreatment)
    VALUES(pIdHealthStatus, pIdTreatment);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertHealthxTreatment;
/

-- JOURNAL
CREATE OR REPLACE PROCEDURE insertJournal(pFieldName IN VARCHAR2, pPreviousValue IN VARCHAR2, pCurrentValue IN VARCHAR2,
                                          pChangeDate IN DATE, pChangedBy IN VARCHAR2, pCreatedBy IN VARCHAR2, pModifiedBy IN VARCHAR2,
                                          pCreatedAt IN VARCHAR2, pModifiedAt IN VARCHAR2)
AS
BEGIN
    INSERT INTO JOURNAL(id, fieldName, previousValue, currentValue, changeDate, changedBy, createdBy, modifiedBy, createdAt, modifiedAt)
    VALUES(sJournal.nextval, pFieldName, pPreviousValue, pCurrentValue, pChangeDate, pChangedBy, pCreatedBy, pModifiedBy, pCreatedAt, pModifiedAt);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertJournal;
/

-- MEDICINE
CREATE OR REPLACE PROCEDURE insertMedicine(pName IN VARCHAR2)
AS
BEGIN
    INSERT INTO MEDICINE(id, name)
    VALUES(sMedicine.nextval, pName);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertMedicine;
/

-- PERSON
CREATE OR REPLACE PROCEDURE insertPerson(pFirstName IN VARCHAR2, pSecondName IN VARCHAR2, pFirstLastName IN VARCHAR2, pSecondLastName IN VARCHAR2)
AS
BEGIN
    INSERT INTO PERSON(id, firstName, secondName, firstLastName, secondLastName)
    VALUES(sPerson.nextval, pFirstName, pSecondName, pFirstLastName, pSecondLastName);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertPerson;
/

-- PERSONXDONATION
CREATE OR REPLACE PROCEDURE insertPersonxDonation(pIdPerson IN NUMBER, pIdDonation IN NUMBER)
AS
BEGIN
    INSERT INTO PERSONXDONATION(idPerson, idDonation)
    VALUES(pIdPerson, pIdDonation);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertPersonxDonation;
/

-- PERSONXEMAIL
CREATE OR REPLACE PROCEDURE insertPersonxEmail(pIdPerson IN NUMBER, pIdEmail IN NUMBER)
AS
BEGIN
    INSERT INTO PERSONXEMAIL(idPerson, idEmail)
    VALUES(pIdPerson, pIdEmail);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertPersonxEmail;
/

-- PERSONXJOURNAL
CREATE OR REPLACE PROCEDURE insertPersonxJournal(pIdPerson IN NUMBER, pIdJournal IN NUMBER)
AS
BEGIN
    INSERT INTO PERSONXJOURNAL(idPerson, idJournal)
    VALUES(pIdPerson, pIdJournal);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertPersonxJournal;
/

-- PERSONXPHONE
CREATE OR REPLACE PROCEDURE insertPersonxPhone(pIdPerson IN NUMBER, pIdPhone IN NUMBER)
AS
BEGIN
    INSERT INTO PERSONXPHONE(idPerson, idPhone)
    VALUES(pIdPerson, pIdPhone);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertPersonxPhone;
/

-- PET
CREATE OR REPLACE PROCEDURE insertPet(
    pIdPetType      IN NUMBER,
    pIdBreed        IN NUMBER,
    pIdPetPhoto     IN NUMBER,
    pIdPetContact   IN NUMBER,
    pIdPetStatus    IN NUMBER,
    pIdTrainingEase IN NUMBER,
    pIdLocation     IN NUMBER,
    pName           IN VARCHAR2,
    pDescription    IN VARCHAR2,
    pNeedSpace      IN VARCHAR2,
    pEnergyLevel    IN VARCHAR2,
    pIdColor        IN NUMBER,
    pIdSize         IN NUMBER,
    pAge            IN NUMBER,
    pChip           IN VARCHAR2,
    pEventDate      IN DATE
)
AS
BEGIN
    INSERT INTO PET(id, idPetType, idBreed, idPetPhoto, idPetContact, idPetStatus, idTrainingEase, idLocation,
        name, description, needSpace, energyLevel, idColor, idSize, age, chip, eventDate)
    VALUES(sPet.nextval, pIdPetType, pIdBreed, pIdPetPhoto, pIdPetContact, pIdPetStatus, pIdTrainingEase, pIdLocation,
        pName, pDescription, pNeedSpace, pEnergyLevel, pIdColor, pIdSize, pAge, pChip, pEventDate);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertPet;
/

-- PETCONTACT
CREATE OR REPLACE PROCEDURE insertPetContact
AS
BEGIN
    INSERT INTO PETCONTACT(id)
    VALUES(sPetContact.nextval);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertPetContact;
/

-- PETCONTACTXEMAIL
CREATE OR REPLACE PROCEDURE insertPetContactxEmail(pIdPetContact IN NUMBER, pIdEmail IN NUMBER)
AS
BEGIN
    INSERT INTO PETCONTACTXEMAIL(idPetContact, idEmail)
    VALUES(pIdPetContact, pIdEmail);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertPetContactxEmail;
/

-- PETCONTACTXPHONE
CREATE OR REPLACE PROCEDURE insertPetContactxPhone(pIdPetContact IN NUMBER, pIdPhone IN NUMBER)
AS
BEGIN
    INSERT INTO PETCONTACTXPHONE(idPetContact, idPhone)
    VALUES(pIdPetContact, pIdPhone);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertPetContactxPhone;
/

-- PETPHOTO
CREATE OR REPLACE PROCEDURE insertPetPhoto(pPhotoPath IN VARCHAR2)
AS
BEGIN
    INSERT INTO PETPHOTO(id, photoPath)
    VALUES(sPetPhoto.nextval, pPhotoPath);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertPetPhoto;
/

-- PETSTATUS
CREATE OR REPLACE PROCEDURE insertPetStatus(pStatus IN VARCHAR2)
AS
BEGIN
    INSERT INTO PETSTATUS(id, status)
    VALUES(sPetStatus.nextval, pStatus);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertPetStatus;
/

-- PETTYPE
CREATE OR REPLACE PROCEDURE insertPetType(pName IN VARCHAR2)
AS
BEGIN
    INSERT INTO PETTYPE(id, name)
    VALUES(sPetType.nextval, pName);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertPetType;
/

-- PETSIZE
CREATE OR REPLACE PROCEDURE insertPetSize(pName IN VARCHAR2)
AS
BEGIN
    INSERT INTO PETSIZE(id, name)
    VALUES(sPetSize.nextval, pName);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertPetSize;
/

-- PETXHEALTHSTATUS
CREATE OR REPLACE PROCEDURE insertPetxHealthStatus(pIdPet IN NUMBER, pIdHealthStatus IN NUMBER)
AS
BEGIN
    INSERT INTO PETXHEALTHSTATUS(idPet, idHealthStatus)
    VALUES(pIdPet, pIdHealthStatus);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertPetxHealthStatus;
/

-- PETXVETERINARIAN
CREATE OR REPLACE PROCEDURE insertPetxVeterinarian(pIdPet IN NUMBER, pIdVeterinarian IN NUMBER)
AS
BEGIN
    INSERT INTO PETXVETERINARIAN(idPet, idVeterinarian)
    VALUES(pIdPet, pIdVeterinarian);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertPetxVeterinarian;
/

-- PHONE
CREATE OR REPLACE PROCEDURE insertPhone(pPhoneNumber IN VARCHAR2)
AS
BEGIN
    INSERT INTO PHONE(id, phoneNumber)
    VALUES(sPhone.nextval, pPhoneNumber);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertPhone;
/

-- PROVINCE
CREATE OR REPLACE PROCEDURE insertProvince(pName IN VARCHAR2)
AS
BEGIN
    INSERT INTO PROVINCE(id, name)
    VALUES(sProvince.nextval, pName);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertProvince;
/

-- RESCUER
CREATE OR REPLACE PROCEDURE insertRescuer(pIdPerson IN NUMBER)
AS
BEGIN
    INSERT INTO RESCUER(idPerson)
    VALUES(pIdPerson);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertRescuer;
/

-- RESCUERXFOSTERCONDITION
CREATE OR REPLACE PROCEDURE insertRescuerxFosterCondition(pIdRescuer IN NUMBER, pIdFosterCondition IN NUMBER)
AS
BEGIN
    INSERT INTO RESCUERXFOSTERCONDITION(idRescuer, idFosterCondition)
    VALUES(pIdRescuer, pIdFosterCondition);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertRescuerxFosterCondition;
/

-- RESCUERXPET
CREATE OR REPLACE PROCEDURE insertRescuerxPet(pIdRescuer IN NUMBER, pIdPet IN NUMBER)
AS
BEGIN
    INSERT INTO RESCUERXPET(idRescuer, idPet)
    VALUES(pIdRescuer, pIdPet);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertRescuerxPet;
/

-- REWARD
CREATE OR REPLACE PROCEDURE insertReward(pIdPet IN NUMBER, pIdCurrency IN NUMBER, pAmount IN NUMBER)
AS
BEGIN
    INSERT INTO REWARD(id, idPet, idCurrency, amount)
    VALUES(sReward.nextval, pIdPet, pIdCurrency, pAmount);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertReward;
/

-- STARRATING
CREATE OR REPLACE PROCEDURE insertStarRating(pName IN VARCHAR2, pStar IN VARCHAR2, pRatingDate IN DATE)
AS
BEGIN
    INSERT INTO STARRATING(id, name, star, ratingDate)
    VALUES(sStarRating.nextval, pName, pStar, pRatingDate);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertStarRating;
/

-- SYSPARAMETER
CREATE OR REPLACE PROCEDURE insertSysParameter(pName IN VARCHAR2, pDescription IN VARCHAR2, pValue IN VARCHAR2)
AS
BEGIN
    INSERT INTO SYSPARAMETER(id, name, description, value)
    VALUES(sSysParameter.nextval, pName, pDescription, pValue);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertSysParameter;
/

-- TRAININGEASE
CREATE OR REPLACE PROCEDURE insertTrainingEase(pName IN VARCHAR2)
AS
BEGIN
    INSERT INTO TRAININGEASE(id, name)
    VALUES(sTrainingEase.nextval, pName);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertTrainingEase;
/

-- TREATMENT
CREATE OR REPLACE PROCEDURE insertTreatment(pName IN VARCHAR2)
AS
BEGIN
    INSERT INTO TREATMENT(id, name)
    VALUES(sTreatment.nextval, pName);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertTreatment;
/

-- VETERINARIAN
CREATE OR REPLACE PROCEDURE insertVeterinarian(pName IN VARCHAR2)
AS
BEGIN
    INSERT INTO VETERINARIAN(id, name)
    VALUES(sVeterinarian.nextval, pName);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertVeterinarian;
/

-- VETERINARIANXEMAIL
CREATE OR REPLACE PROCEDURE insertVeterinarianxEmail(pIdVeterinarian IN NUMBER, pIdEmail IN NUMBER)
AS
BEGIN
    INSERT INTO VETERINARIANXEMAIL(idVeterinarian, idEmail)
    VALUES(pIdVeterinarian, pIdEmail);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertVeterinarianxEmail;
/

-- VETERINARIANXPHONE
CREATE OR REPLACE PROCEDURE insertVeterinarianxPhone(pIdVeterinarian IN NUMBER, pIdPhone IN NUMBER)
AS
BEGIN
    INSERT INTO VETERINARIANXPHONE(idVeterinarian, idPhone)
    VALUES(pIdVeterinarian, pIdPhone);
    COMMIT;
EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Insert failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Insert failed. Error ' ||   SQLERRM );

END insertVeterinarianxPhone;
/

