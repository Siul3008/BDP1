-----------------------------------------------------------------------------------------------
--Students:
--Meylin Carvajal Baltodano
--Luis D. Rivas Castro
--Ma. Fernanda Vega Acosta

-----------------------------------------------------------------------------------------------

-- Creation of the tablespaces for data and index

CREATE TABLESPACE PROJECTI_DATA
DATAFILE 'D:\APP\LUISD\ORADATA\DBPRUEBA\PROJECTI_data01.dbf'
SIZE 100M
AUTOEXTEND ON NEXT 10M MAXSIZE 500M;

CREATE TABLESPACE PROJECTI_IDX
DATAFILE 'D:\APP\LUISD\ORADATA\DBPRUEBA\PROJECTI_idx01.dbf'
SIZE 50M
AUTOEXTEND ON NEXT 10M MAXSIZE 200M;