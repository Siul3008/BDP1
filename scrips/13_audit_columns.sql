-----------------------------------------------------------------------------------------------
--Students:
--Meylin Carvajal Baltodano
--Luis D. Rivas Castro
--Ma. Fernanda Vega Acosta

-----------------------------------------------------------------------------------------------

--Addition of Audit Fields to all the tables created



-------------------------------------------------------------------------------------------------------------

ALTER TABLE breed
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE currency
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE adopter
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE adopterRating
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE adoption
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE adoptionApplication
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE adoptionPhoto
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE adoptionxadpphoto
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE adoptionxpet
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE adoptionxrescuer
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE association
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE associationxdonation
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE blacklistreport
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE canton
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE disease
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
);

-------------------------------------------------------------------------------------------------------------
ALTER TABLE district
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE donation
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE donationalllocation
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE email
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE fosterCondition
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE fosterHome
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE fosterHomexfosterCondition
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE healthStatus
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE healthStxDisease
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE healthStxMedicine
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE healthStxTreatment
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE petReport
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE medicine
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE person
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE personxBListrep
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE personxDonation
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE personxEmail
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE personxPhone
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE pet
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE petContact
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE petContactxEmail
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE petContactxPhone
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE petPhoto
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE petStatus
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE petType
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE petxHealthStatus
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE petxVeterinarian
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE Phone
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE Province
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE RequiresFoodDonation
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE Rescuer
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE RescuerxFosterCondition
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE RescuerxPet
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE Reward
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE StarRating
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE SysParameter
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE TrainingEase
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE Treatment
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE Veterinarian
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE VeterinarianxEmail
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE VeterinarianxPhone
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE color
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 

-------------------------------------------------------------------------------------------------------------
ALTER TABLE petSize
ADD(
    Creation_date DATE, 
    Created_by VARCHAR2(10), 
    Updated_date DATE, 
    Updated_by VARCHAR2(10) 
); 
