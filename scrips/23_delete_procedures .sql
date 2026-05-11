-----------------------------------------------------------------------------------------------
--Students:
--Meylin Carvajal Baltodano
--Luis D. Rivas Castro
--Ma. Fernanda Vega Acosta

-----------------------------------------------------------------------------------------------


--Creation of delete procedures


------------------------------------------------------------------------------------------------------------------------



-- EMAIL
CREATE OR REPLACE PROCEDURE deleteEmail(pId IN NUMBER)
AS
    e_invalid_email EXCEPTION;
BEGIN
    DELETE FROM personxemail
    WHERE   idEmail = pId;
    DELETE FROM petcontactxemail
    WHERE   idEmail = pId;
    DELETE FROM veterinarianxemail
    WHERE   idEmail = pId;
    DELETE FROM email
    WHERE   id = pId;
    IF SQL%NOTFOUND THEN
        RAISE e_invalid_email;
    END IF;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Delete failed');
    WHEN e_invalid_email THEN 
        DBMS_OUTPUT.PUT_LINE('No such person. Delete failed');

    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Delete failed. Error ' ||   SQLERRM );
END deleteEmail;
/








-- PERSON
CREATE OR REPLACE PROCEDURE deletePerson(pId IN NUMBER)
AS
    e_invalid_person EXCEPTION;
BEGIN
    DELETE FROM adopter
    WHERE   idPerson = pId;
    DELETE FROM personxdonation
    WHERE   idPerson = pId;
    DELETE FROM personxemail
    WHERE   idPerson = pId;
    DELETE FROM personxjournal
    WHERE   idPerson = pId;
    DELETE FROM personxphone
    WHERE   idPerson = pId;

    DELETE FROM rescuerxFostercondition
    WHERE   idRescuer = pId;
    DELETE FROM rescuerxpet
    WHERE   idRescuer = pId;
    DELETE FROM rescuer
    WHERE   idPerson = pId;
    DELETE FROM fosterHomexfosterCondition
    WHERE   idFosterHome = pId;
    DELETE FROM fosterHome
    WHERE   idPerson = pId;    
    DELETE FROM person
    WHERE   id = pId;
    IF SQL%NOTFOUND THEN
        RAISE e_invalid_person;
    END IF;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Delete failed');
    WHEN e_invalid_person THEN 
        DBMS_OUTPUT.PUT_LINE('No such person. Delete failed');

    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Delete failed. Error ' ||   SQLERRM );

END deletePerson;
/






-- PET
CREATE OR REPLACE PROCEDURE deletePet(pidPet IN NUMBER)
AS
    e_invalid_pet EXCEPTION;
BEGIN
    DELETE FROM adoptionxpet
    WHERE   idpet = pidPet;
    DELETE FROM petxhealthstatus
    WHERE   idpet = pidPet;
    DELETE FROM petxveterinarian
    WHERE   idpet = pidPet;
    DELETE FROM rescuerxpet
    WHERE   idpet = pidPet;
    DELETE FROM pet
    WHERE   id = pidPet;
    IF SQL%NOTFOUND THEN
        RAISE e_invalid_pet;
    END IF;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Delete failed');
    WHEN e_invalid_pet THEN 
        DBMS_OUTPUT.PUT_LINE('No such person. Delete failed');

    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Delete failed. Error ' ||   SQLERRM );

END deletePet;
/









-- PHONE
CREATE OR REPLACE PROCEDURE deletePhone(pId IN NUMBER)
AS
    e_invalid_phone EXCEPTION;
BEGIN
    DELETE FROM personxphone
    WHERE   idphone = pId;
    DELETE FROM petcontactxphone
    WHERE   idphone = pId;
    DELETE FROM veterinarianxphone
    WHERE   idphone = pId;
    DELETE FROM phone
    WHERE   id = pId;
    IF SQL%NOTFOUND THEN
        RAISE e_invalid_phone;
    END IF;

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Delete failed');
    WHEN e_invalid_phone THEN 
        DBMS_OUTPUT.PUT_LINE('No such person. Delete failed');

    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Delete failed. Error ' ||   SQLERRM );

END deletePhone;
/





