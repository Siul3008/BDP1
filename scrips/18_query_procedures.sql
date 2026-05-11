-----------------------------------------------------------------------------------------------
--Students:
--Meylin Carvajal Baltodano
--Luis D. Rivas Castro
--Ma. Fernanda Vega Acosta

-----------------------------------------------------------------------------------------------

--Creation of the procedures of the queries required in the project 


----------------------------------------------------------------------------------------------------------------------------
--pets query
--
--Query to get a pet or group of pets filtered

CREATE OR REPLACE PROCEDURE getPet 
(pId IN NUMBER, pLocID IN NUMBER, pChip IN VARCHAR2, pIdType IN NUMBER,
pIdBreed IN NUMBER , pDate DATE, pIDAssociation IN NUMBER, pIDRescuer IN NUMBER, pPetCursor OUT SYS_REFCURSOR)


AS
BEGIN
    OPEN pPetCursor FOR
    SELECT *
    FROM pet p
    LEFT JOIN rescuerxpet r
    ON p.id = r.idPet
    WHERE p.id = NVL(pId, p.id) AND p.idLocation = NVL(pLocID, p.idLocation) AND 
            LOWER(NVL(p.chip, '')) = LOWER(NVL(pChip, NVL(p.chip, ''))) AND
            p.idPetType =  NVL(pIdType,p.idPetType) AND p.idBreed = NVL(pIdBreed, p.idBreed) 
            AND TRUNC(p.eventDate) = NVL(TRUNC(pDate), TRUNC(p.eventDate)) AND 
            r.idRescuer =  NVL(pIDRescuer,r.idRescuer)
    ORDER BY p.eventDate DESC;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Could not get pet(s). Error ' ||   SQLERRM );
    

END;
/


----------------------------------------------------------------------------------------------------------------------------

--donation query

--Query to get the donations filtered

CREATE OR REPLACE PROCEDURE getDonation 
(pFrom  DATE , pTo  DATE,  pidDonator IN NUMBER , pidAssociation IN NUMBER, pDonationCursor OUT SYS_REFCURSOR)

AS
BEGIN
    OPEN pDonationCursor FOR
    SELECT d.amount, p.firstName || ' ' || p.firstLastName AS donorName, d.donationDate, a.name AS associationName
    FROM donation d
    INNER JOIN associationxdonation ad
    ON d.id = ad.idDonation
    INNER JOIN personxdonation pd
    ON d.id = pd.idDonation
    INNER JOIN person p
    ON pd.idPerson = p.Id
    INNER JOIN association a
    ON ad.idAssociation = a.Id
    WHERE donationDate BETWEEN pFrom AND pTo OR pd.idPerson = NVL(pidDonator,pd.idPerson)
    OR ad.idAssociation = NVL(pidAssociation, ad.idAssociation);

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Could not donation(s). Error ' ||   SQLERRM );
    


END;
/



------------------------------------------------------------------------------------------------------------------------




--matches query

--query that makes the matches between reported pets

CREATE OR REPLACE PROCEDURE getMatch
(pIdType IN NUMBER,pIdBreed IN NUMBER,pIdSize IN NUMBER,pIdColor IN NUMBER, pMatchCursor OUT SYS_REFCURSOR)
AS
BEGIN
    OPEN pMatchCursor FOR
    SELECT p.id AS id_Pet, p.name , t.name as Pet_Type, b.name as Breed, s.name AS Pet_Size, c.name AS Color,  m.id as Match 
    FROM pet p
    INNER JOIN petType t
    ON p.idPettype = t.id
    INNER JOIN breed b
    ON p.idBreed = b.id
    INNER JOIN petSize s
    ON p.idSize = s.id
    INNER JOIN color c
    ON p.idColor = c.id
    INNER JOIN petReport m
    ON p.idColor = m.idColor
    AND  p.idPetType = m.idPetType
    AND  p.idBreed = m.idBreed
    AND  p.idSize = m.idSize
    WHERE p.idPetType =  NVL(pIdType,p.idPetType) AND p.idBreed = NVL(pIdBreed, p.idBreed) 
            AND p.idColor = NVL(pIdColor,p.idColor) AND 
            p.idSize = NVL(pIdSize,p.idSize);

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Could not get matches. Error ' ||   SQLERRM );
    

END;
/






-------------------------------------------------------------------------------------------------------



-- blacklist query


--query to get the blacklist 

CREATE OR REPLACE PROCEDURE getBlackListInfo 
(pIdReporter IN NUMBER, pIdReportee IN NUMBER, pBLCursor OUT SYS_REFCURSOR)


AS
BEGIN
    OPEN pBLCursor FOR
    SELECT bl.id AS id_Reporte,  pRpr.firstName || ' ' || pRpr.firstLastName AS Reporter,
    pRpe.firstName || ' ' || pRpe.firstLastName AS Reportee, r.Star AS Star_Rating, bl.reason AS Reason,
    (SELECT
    
        AVG( sr.Star)
        FROM blackListReport bls
        JOIN starRating sr
        On bls.idStarRating = sr.id
        WHERE bls.idReportee = bl.idReportee
    
    )AS Reportee_Star_Prom
    FROM blackListReport bl
    LEFT JOIN starRating r
    ON bl.idStarRating = r.id
    INNER JOIN person pRpr
    ON bl.idReporter = pRpr.id
    INNER JOIN person pRpe
    ON bl.idReportee = pRpe.id    
    WHERE bl.idReporter = NVL(pIdReporter, bl.idReporter) AND bl.idReportee = NVL( pIdReportee, bl.idReportee ) 
    ;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Could not get information from blacklist. Error ' ||   SQLERRM );
       

END;
/




--Additional queries
------------------------------------------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------------------------------------------

--Selected queries and statistics

------------------------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------------------------

--Top 10 rescuers 


CREATE OR REPLACE PROCEDURE getTopRescuers
( pTopRescuersCursor OUT SYS_REFCURSOR)


AS
BEGIN
    OPEN pTopRescuersCursor FOR
    SELECT *

    FROM 
    (
    SELECT  p.id, p.firstname, p.secondName, p.firstLastName, p.secondLastName, COUNT(rxp.idPet) AS TOP_10_Rescuers 
    FROM rescuerxpet rxp
    INNER JOIN rescuer r
    ON rxp.idRescuer = r.idPerson
    INNER JOIN person p
    ON r.idPerson = p.id
    GROUP BY p.id, p.firstname, p.secondName, p.firstLastName, p.secondLastName
    ORDER BY COUNT(rxp.idPet) DESC
    )
    
    WHERE ROWNUM <= 10;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Could not get top rescuers information. Error ' ||   SQLERRM );
    

END;
/




--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

--query Foster Home by accepted Type



CREATE OR REPLACE PROCEDURE getFosterHomebyPetType
( pFHbyPT OUT SYS_REFCURSOR)


AS
BEGIN
    OPEN pFHbyPT FOR
    
    SELECT fh.idPerson AS id, p.firstName || ' ' || p.firstLastName AS Foster_Home, COUNT(DISTINCT pt.id) AS Accepted_Type_Total
    
    FROM fosterHomexFosterCondition fhxfc
    INNER JOIN fosterHome fh
    ON fhxfc.idFosterHome = fh.idPerson
    INNER JOIN fosterCondition fc
    ON fhxfc.idFosterCondition = fc.id
    INNER JOIN person p
    ON fh.idPerson = p.id
    INNER JOIN fosterConditionxAccType fcxat
    ON fc.id = fcxat.idFosterCondition
    INNER JOIN petType pt
    ON fcxat.idPetType = pt.id
    
    GROUP BY fh.idPerson, p.firstName, p.firstLastName
    ORDER BY  COUNT(DISTINCT pt.id) DESC;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Could not get foster home(s) information. Error ' ||   SQLERRM );
          

END;
/



-------------------------------------------------------------------------------------------------------------------------

---Critical state pets in adoption




CREATE OR REPLACE PROCEDURE getCritPetInAdopt
( pType IN NUMBER, pCritPetinAdopt OUT SYS_REFCURSOR)


AS
BEGIN
    OPEN pCritPetinAdopt FOR
    
    SELECT p.id, p.name AS Name, p.age AS Age , b.name AS Breed, s.name AS Pet_Size, hst.illnessState AS Health_Status, hst.description AS Health_Status_Description, st.status AS Current_Status, l.name AS Pet_Location
    
    FROM pet p
    INNER JOIN breed b
    ON p.idBreed = b.id
    INNER JOIN petsize s 
    ON p.idSize = s.id
    INNER JOIN petStatus st
    ON p.idPetStatus = st.id
    INNER JOIN district l
    ON p.idLocation = l.id
    INNER JOIN petxHealthStatus pxhst
    ON p.id = pxhst.idPet 
    INNER JOIN healthStatus hst
    ON pxhst.idHEalthStatus = hst.id
    
    WHERE LOWER(st.status) IN ('for adoption', 'en adopcion')
    AND (
        LOWER(hst.illnessState) LIKE '%critical%'
        OR LOWER(hst.illnessState) LIKE '%critico%'
        OR LOWER(hst.illnessState) LIKE '%grave%'
    )
    AND p.idPetType = NVL(pType, p.idPetType)
    ;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Could not get pet(s). Error ' ||   SQLERRM );
          

END;
/

