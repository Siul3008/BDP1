-----------------------------------------------------------------------------------------------
--Students:
--Meylin Carvajal Baltodano
--Luis D. Rivas Castro
--Ma. Fernanda Vega Acosta

-----------------------------------------------------------------------------------------------
--Creation of the scheme/user for the project

CREATE USER PROJECT1 IDENTIFIED BY PBD1
DEFAULT TABLESPACE PROJECTI_DATA
TEMPORARY TABLESPACE TEMP
QUOTA UNLIMITED ON PROJECTI_DATA
QUOTA UNLIMITED ON PROJECTI_IDX;
