-----------------------------------------------------------------------------------------------
--Students:
--Meylin Carvajal Baltodano
--Luis D. Rivas Castro
--Ma. Fernanda Vega Acosta

-----------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------

--What was used to create this:

-- Costa Rica administrative divisions for 2026.
-- Created from: DTA-TABLA POR PROVINCIA-CANTÓN-DISTRITO 2026 (1).xlsx
-- Totals: 7 provinces, 84 cantons, 494 districts.
-- Longest name: 28 characters, 28 UTF-8 bytes.

SET DEFINE OFF;

-- The original schema uses VARCHAR(25). These changes prevent failures with long official names.
ALTER TABLE province MODIFY name VARCHAR2(80);
ALTER TABLE canton MODIFY name VARCHAR2(80);
ALTER TABLE district MODIFY name VARCHAR2(80);

-- Provinces
MERGE INTO province p
USING (SELECT 1 AS id, 'San José' AS name FROM dual) src
ON (p.id = src.id)
WHEN MATCHED THEN
    UPDATE SET p.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, name)
    VALUES (src.id, src.name);

MERGE INTO province p
USING (SELECT 2 AS id, 'Alajuela' AS name FROM dual) src
ON (p.id = src.id)
WHEN MATCHED THEN
    UPDATE SET p.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, name)
    VALUES (src.id, src.name);

MERGE INTO province p
USING (SELECT 3 AS id, 'Cartago' AS name FROM dual) src
ON (p.id = src.id)
WHEN MATCHED THEN
    UPDATE SET p.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, name)
    VALUES (src.id, src.name);

MERGE INTO province p
USING (SELECT 4 AS id, 'Heredia' AS name FROM dual) src
ON (p.id = src.id)
WHEN MATCHED THEN
    UPDATE SET p.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, name)
    VALUES (src.id, src.name);

MERGE INTO province p
USING (SELECT 5 AS id, 'Guanacaste' AS name FROM dual) src
ON (p.id = src.id)
WHEN MATCHED THEN
    UPDATE SET p.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, name)
    VALUES (src.id, src.name);

MERGE INTO province p
USING (SELECT 6 AS id, 'Puntarenas' AS name FROM dual) src
ON (p.id = src.id)
WHEN MATCHED THEN
    UPDATE SET p.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, name)
    VALUES (src.id, src.name);

MERGE INTO province p
USING (SELECT 7 AS id, 'Limón' AS name FROM dual) src
ON (p.id = src.id)
WHEN MATCHED THEN
    UPDATE SET p.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, name)
    VALUES (src.id, src.name);

-- Cantons
MERGE INTO canton c
USING (SELECT 101 AS id, 1 AS idProvince, 'San José' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 102 AS id, 1 AS idProvince, 'Escazú' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 103 AS id, 1 AS idProvince, 'Desamparados' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 104 AS id, 1 AS idProvince, 'Puriscal' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 105 AS id, 1 AS idProvince, 'Tarrazú' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 106 AS id, 1 AS idProvince, 'Aserrí' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 107 AS id, 1 AS idProvince, 'Mora' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 108 AS id, 1 AS idProvince, 'Goicoechea' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 109 AS id, 1 AS idProvince, 'Santa Ana' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 110 AS id, 1 AS idProvince, 'Alajuelita' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 111 AS id, 1 AS idProvince, 'Vázquez de Coronado' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 112 AS id, 1 AS idProvince, 'Acosta' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 113 AS id, 1 AS idProvince, 'Tibás' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 114 AS id, 1 AS idProvince, 'Moravia' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 115 AS id, 1 AS idProvince, 'Montes de Oca' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 116 AS id, 1 AS idProvince, 'Turrubares' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 117 AS id, 1 AS idProvince, 'Dota' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 118 AS id, 1 AS idProvince, 'Curridabat' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 119 AS id, 1 AS idProvince, 'Pérez Zeledón' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 120 AS id, 1 AS idProvince, 'León Cortés Castro' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 201 AS id, 2 AS idProvince, 'Alajuela' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 202 AS id, 2 AS idProvince, 'San Ramón' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 203 AS id, 2 AS idProvince, 'Grecia' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 204 AS id, 2 AS idProvince, 'San Mateo' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 205 AS id, 2 AS idProvince, 'Atenas' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 206 AS id, 2 AS idProvince, 'Naranjo' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 207 AS id, 2 AS idProvince, 'Palmares' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 208 AS id, 2 AS idProvince, 'Poás' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 209 AS id, 2 AS idProvince, 'Orotina' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 210 AS id, 2 AS idProvince, 'San Carlos' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 211 AS id, 2 AS idProvince, 'Zarcero' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 212 AS id, 2 AS idProvince, 'Sarchí' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 213 AS id, 2 AS idProvince, 'Upala' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 214 AS id, 2 AS idProvince, 'Los Chiles' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 215 AS id, 2 AS idProvince, 'Guatuso' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 216 AS id, 2 AS idProvince, 'Río Cuarto' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 301 AS id, 3 AS idProvince, 'Cartago' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 302 AS id, 3 AS idProvince, 'Paraíso' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 303 AS id, 3 AS idProvince, 'La Unión' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 304 AS id, 3 AS idProvince, 'Jiménez' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 305 AS id, 3 AS idProvince, 'Turrialba' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 306 AS id, 3 AS idProvince, 'Alvarado' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 307 AS id, 3 AS idProvince, 'Oreamuno' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 308 AS id, 3 AS idProvince, 'El Guarco' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 401 AS id, 4 AS idProvince, 'Heredia' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 402 AS id, 4 AS idProvince, 'Barva' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 403 AS id, 4 AS idProvince, 'Santo Domingo' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 404 AS id, 4 AS idProvince, 'Santa Bárbara' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 405 AS id, 4 AS idProvince, 'San Rafael' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 406 AS id, 4 AS idProvince, 'San Isidro' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 407 AS id, 4 AS idProvince, 'Belén' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 408 AS id, 4 AS idProvince, 'Flores' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 409 AS id, 4 AS idProvince, 'San Pablo' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 410 AS id, 4 AS idProvince, 'Sarapiquí' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 501 AS id, 5 AS idProvince, 'Liberia' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 502 AS id, 5 AS idProvince, 'Nicoya' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 503 AS id, 5 AS idProvince, 'Santa Cruz' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 504 AS id, 5 AS idProvince, 'Bagaces' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 505 AS id, 5 AS idProvince, 'Carrillo' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 506 AS id, 5 AS idProvince, 'Cañas' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 507 AS id, 5 AS idProvince, 'Abangares' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 508 AS id, 5 AS idProvince, 'Tilarán' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 509 AS id, 5 AS idProvince, 'Nandayure' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 510 AS id, 5 AS idProvince, 'La Cruz' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 511 AS id, 5 AS idProvince, 'Hojancha' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 601 AS id, 6 AS idProvince, 'Puntarenas' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 602 AS id, 6 AS idProvince, 'Esparza' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 603 AS id, 6 AS idProvince, 'Buenos Aires' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 604 AS id, 6 AS idProvince, 'Montes de Oro' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 605 AS id, 6 AS idProvince, 'Osa' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 606 AS id, 6 AS idProvince, 'Quepos' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 607 AS id, 6 AS idProvince, 'Golfito' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 608 AS id, 6 AS idProvince, 'Coto Brus' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 609 AS id, 6 AS idProvince, 'Parrita' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 610 AS id, 6 AS idProvince, 'Corredores' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 611 AS id, 6 AS idProvince, 'Garabito' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 612 AS id, 6 AS idProvince, 'Monteverde' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 613 AS id, 6 AS idProvince, 'Puerto Jiménez' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 701 AS id, 7 AS idProvince, 'Limón' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 702 AS id, 7 AS idProvince, 'Pococí' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 703 AS id, 7 AS idProvince, 'Siquirres' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 704 AS id, 7 AS idProvince, 'Talamanca' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 705 AS id, 7 AS idProvince, 'Matina' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

MERGE INTO canton c
USING (SELECT 706 AS id, 7 AS idProvince, 'Guácimo' AS name FROM dual) src
ON (c.id = src.id)
WHEN MATCHED THEN
    UPDATE SET c.idProvince = src.idProvince, c.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idProvince, name)
    VALUES (src.id, src.idProvince, src.name);

-- Districts
MERGE INTO district d
USING (SELECT 10101 AS id, 101 AS idCanton, 'Carmen' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10102 AS id, 101 AS idCanton, 'Merced' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10103 AS id, 101 AS idCanton, 'Hospital' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10104 AS id, 101 AS idCanton, 'Catedral' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10105 AS id, 101 AS idCanton, 'Zapote' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10106 AS id, 101 AS idCanton, 'San Francisco de Dos Ríos' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10107 AS id, 101 AS idCanton, 'Uruca' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10108 AS id, 101 AS idCanton, 'Mata Redonda' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10109 AS id, 101 AS idCanton, 'Pavas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10110 AS id, 101 AS idCanton, 'Hatillo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10111 AS id, 101 AS idCanton, 'San Sebastián' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10201 AS id, 102 AS idCanton, 'Escazú' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10202 AS id, 102 AS idCanton, 'San Antonio' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10203 AS id, 102 AS idCanton, 'San Rafael' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10301 AS id, 103 AS idCanton, 'Desamparados' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10302 AS id, 103 AS idCanton, 'San Miguel' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10303 AS id, 103 AS idCanton, 'San Juan de Dios' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10304 AS id, 103 AS idCanton, 'San Rafael Arriba' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10305 AS id, 103 AS idCanton, 'San Antonio' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10306 AS id, 103 AS idCanton, 'Frailes' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10307 AS id, 103 AS idCanton, 'Patarrá' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10308 AS id, 103 AS idCanton, 'San Cristobal' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10309 AS id, 103 AS idCanton, 'Rosario' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10310 AS id, 103 AS idCanton, 'Damas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10311 AS id, 103 AS idCanton, 'San Rafael Abajo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10312 AS id, 103 AS idCanton, 'Gravilias' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10313 AS id, 103 AS idCanton, 'Los Guido' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10401 AS id, 104 AS idCanton, 'Santiago' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10402 AS id, 104 AS idCanton, 'Mercedes Sur' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10403 AS id, 104 AS idCanton, 'Barbacoas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10404 AS id, 104 AS idCanton, 'Grifo Alto' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10405 AS id, 104 AS idCanton, 'San Rafael' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10406 AS id, 104 AS idCanton, 'Candelarita' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10407 AS id, 104 AS idCanton, 'Desamparaditos' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10408 AS id, 104 AS idCanton, 'San Antonio' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10409 AS id, 104 AS idCanton, 'Chires' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10501 AS id, 105 AS idCanton, 'San Marcos' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10502 AS id, 105 AS idCanton, 'San Lorenzo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10503 AS id, 105 AS idCanton, 'San Carlos' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10601 AS id, 106 AS idCanton, 'Aserrí' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10602 AS id, 106 AS idCanton, 'Tarbaca' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10603 AS id, 106 AS idCanton, 'Vuelta de Jorco' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10604 AS id, 106 AS idCanton, 'San Gabriel' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10605 AS id, 106 AS idCanton, 'Legua' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10606 AS id, 106 AS idCanton, 'Monterrey' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10607 AS id, 106 AS idCanton, 'Salitrillos' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10701 AS id, 107 AS idCanton, 'Colón' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10702 AS id, 107 AS idCanton, 'Guayabo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10703 AS id, 107 AS idCanton, 'Tabarcia' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10704 AS id, 107 AS idCanton, 'Piedras Negras' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10705 AS id, 107 AS idCanton, 'Picagres' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10706 AS id, 107 AS idCanton, 'Jaris' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10707 AS id, 107 AS idCanton, 'Quitirrisí' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10801 AS id, 108 AS idCanton, 'Guadalupe' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10802 AS id, 108 AS idCanton, 'San Francisco' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10803 AS id, 108 AS idCanton, 'Calle Blancos' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10804 AS id, 108 AS idCanton, 'Mata de Plátano' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10805 AS id, 108 AS idCanton, 'Ipís' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10806 AS id, 108 AS idCanton, 'Rancho Redondo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10807 AS id, 108 AS idCanton, 'Purral' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10901 AS id, 109 AS idCanton, 'Santa Ana' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10902 AS id, 109 AS idCanton, 'Salitral' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10903 AS id, 109 AS idCanton, 'Pozos' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10904 AS id, 109 AS idCanton, 'Uruca' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10905 AS id, 109 AS idCanton, 'Piedades' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 10906 AS id, 109 AS idCanton, 'Brasil' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11001 AS id, 110 AS idCanton, 'Alajuelita' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11002 AS id, 110 AS idCanton, 'San Josecito' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11003 AS id, 110 AS idCanton, 'San Antonio' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11004 AS id, 110 AS idCanton, 'Concepción' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11005 AS id, 110 AS idCanton, 'San Felipe' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11101 AS id, 111 AS idCanton, 'San Isidro' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11102 AS id, 111 AS idCanton, 'San Rafael' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11103 AS id, 111 AS idCanton, 'Dulce Nombre de Jesús' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11104 AS id, 111 AS idCanton, 'Patalillo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11105 AS id, 111 AS idCanton, 'Cascajal' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11201 AS id, 112 AS idCanton, 'San Ignacio' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11202 AS id, 112 AS idCanton, 'Guaitil' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11203 AS id, 112 AS idCanton, 'Palmichal' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11204 AS id, 112 AS idCanton, 'Cangrejal' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11205 AS id, 112 AS idCanton, 'Sabanillas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11301 AS id, 113 AS idCanton, 'San Juan' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11302 AS id, 113 AS idCanton, 'Cinco Esquinas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11303 AS id, 113 AS idCanton, 'Anselmo Llorente' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11304 AS id, 113 AS idCanton, 'León XIII' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11305 AS id, 113 AS idCanton, 'Colima' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11401 AS id, 114 AS idCanton, 'San Vicente' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11402 AS id, 114 AS idCanton, 'San Jerónimo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11403 AS id, 114 AS idCanton, 'La Trinidad' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11501 AS id, 115 AS idCanton, 'San Pedro' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11502 AS id, 115 AS idCanton, 'Sabanilla' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11503 AS id, 115 AS idCanton, 'Mercedes' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11504 AS id, 115 AS idCanton, 'San Rafael' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11601 AS id, 116 AS idCanton, 'San Pablo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11602 AS id, 116 AS idCanton, 'San Pedro' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11603 AS id, 116 AS idCanton, 'San Juan de Mata' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11604 AS id, 116 AS idCanton, 'San Luis' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11605 AS id, 116 AS idCanton, 'Carara' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11701 AS id, 117 AS idCanton, 'Santa María' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11702 AS id, 117 AS idCanton, 'Jardín' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11703 AS id, 117 AS idCanton, 'Copey' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11801 AS id, 118 AS idCanton, 'Curridabat' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11802 AS id, 118 AS idCanton, 'Granadilla' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11803 AS id, 118 AS idCanton, 'Sánchez' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11804 AS id, 118 AS idCanton, 'Tirrases' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11901 AS id, 119 AS idCanton, 'San Isidro de El General' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11902 AS id, 119 AS idCanton, 'El General' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11903 AS id, 119 AS idCanton, 'Daniel Flores' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11904 AS id, 119 AS idCanton, 'Rivas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11905 AS id, 119 AS idCanton, 'San Pedro' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11906 AS id, 119 AS idCanton, 'Platanares' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11907 AS id, 119 AS idCanton, 'Pejivalle' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11908 AS id, 119 AS idCanton, 'Cajón' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11909 AS id, 119 AS idCanton, 'Barú' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11910 AS id, 119 AS idCanton, 'Río Nuevo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11911 AS id, 119 AS idCanton, 'Páramo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 11912 AS id, 119 AS idCanton, 'La  Amistad' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 12001 AS id, 120 AS idCanton, 'San Pablo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 12002 AS id, 120 AS idCanton, 'San Andrés' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 12003 AS id, 120 AS idCanton, 'Llano Bonito' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 12004 AS id, 120 AS idCanton, 'San Isidro' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 12005 AS id, 120 AS idCanton, 'Santa Cruz' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 12006 AS id, 120 AS idCanton, 'San Antonio' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20101 AS id, 201 AS idCanton, 'Alajuela' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20102 AS id, 201 AS idCanton, 'San José' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20103 AS id, 201 AS idCanton, 'Carrizal' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20104 AS id, 201 AS idCanton, 'San Antonio' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20105 AS id, 201 AS idCanton, 'Guácima' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20106 AS id, 201 AS idCanton, 'San Isidro' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20107 AS id, 201 AS idCanton, 'Sabanilla' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20108 AS id, 201 AS idCanton, 'San Rafael' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20109 AS id, 201 AS idCanton, 'Río Segundo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20110 AS id, 201 AS idCanton, 'Desamparados' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20111 AS id, 201 AS idCanton, 'Turrúcares' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20112 AS id, 201 AS idCanton, 'Tambor' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20113 AS id, 201 AS idCanton, 'Garita' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20114 AS id, 201 AS idCanton, 'Sarapiquí' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20201 AS id, 202 AS idCanton, 'San Ramón' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20202 AS id, 202 AS idCanton, 'Santiago' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20203 AS id, 202 AS idCanton, 'San Juan' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20204 AS id, 202 AS idCanton, 'Piedades Norte' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20205 AS id, 202 AS idCanton, 'Piedades Sur' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20206 AS id, 202 AS idCanton, 'San Rafael' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20207 AS id, 202 AS idCanton, 'San Isidro' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20208 AS id, 202 AS idCanton, 'Ángeles' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20209 AS id, 202 AS idCanton, 'Alfaro' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20210 AS id, 202 AS idCanton, 'Volio' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20211 AS id, 202 AS idCanton, 'Concepción' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20212 AS id, 202 AS idCanton, 'Zapotal' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20213 AS id, 202 AS idCanton, 'Peñas Blancas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20214 AS id, 202 AS idCanton, 'San Lorenzo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20301 AS id, 203 AS idCanton, 'Grecia' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20302 AS id, 203 AS idCanton, 'San Isidro' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20303 AS id, 203 AS idCanton, 'San José' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20304 AS id, 203 AS idCanton, 'San Roque' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20305 AS id, 203 AS idCanton, 'Tacares' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20307 AS id, 203 AS idCanton, 'Puente de Piedra' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20308 AS id, 203 AS idCanton, 'Bolivar' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20401 AS id, 204 AS idCanton, 'San Mateo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20402 AS id, 204 AS idCanton, 'Desmonte' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20403 AS id, 204 AS idCanton, 'Jesús María' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20404 AS id, 204 AS idCanton, 'Labrador' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20501 AS id, 205 AS idCanton, 'Atenas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20502 AS id, 205 AS idCanton, 'Jesús' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20503 AS id, 205 AS idCanton, 'Mercedes' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20504 AS id, 205 AS idCanton, 'San Isidro' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20505 AS id, 205 AS idCanton, 'Concepción' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20506 AS id, 205 AS idCanton, 'San José' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20507 AS id, 205 AS idCanton, 'Santa Eulalia' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20508 AS id, 205 AS idCanton, 'Escobal' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20601 AS id, 206 AS idCanton, 'Naranjo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20602 AS id, 206 AS idCanton, 'San Miguel' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20603 AS id, 206 AS idCanton, 'San José' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20604 AS id, 206 AS idCanton, 'Cirrí Sur' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20605 AS id, 206 AS idCanton, 'San Jerónimo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20606 AS id, 206 AS idCanton, 'San Juan' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20607 AS id, 206 AS idCanton, 'El Rosario' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20608 AS id, 206 AS idCanton, 'Palmitos' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20701 AS id, 207 AS idCanton, 'Palmares' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20702 AS id, 207 AS idCanton, 'Zaragoza' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20703 AS id, 207 AS idCanton, 'Buenos Aires' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20704 AS id, 207 AS idCanton, 'Santiago' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20705 AS id, 207 AS idCanton, 'Candelaria' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20706 AS id, 207 AS idCanton, 'Esquipulas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20707 AS id, 207 AS idCanton, 'La Granja' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20801 AS id, 208 AS idCanton, 'San Pedro' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20802 AS id, 208 AS idCanton, 'San Juan' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20803 AS id, 208 AS idCanton, 'San Rafael' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20804 AS id, 208 AS idCanton, 'Carrillos' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20805 AS id, 208 AS idCanton, 'Sabana Redonda' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20901 AS id, 209 AS idCanton, 'Orotina' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20902 AS id, 209 AS idCanton, 'El Mastate' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20903 AS id, 209 AS idCanton, 'Hacienda Vieja' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20904 AS id, 209 AS idCanton, 'Coyolar' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 20905 AS id, 209 AS idCanton, 'La Ceiba' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21001 AS id, 210 AS idCanton, 'Quesada' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21002 AS id, 210 AS idCanton, 'Florencia' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21003 AS id, 210 AS idCanton, 'Buenavista' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21004 AS id, 210 AS idCanton, 'Aguas Zarcas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21005 AS id, 210 AS idCanton, 'Venecia' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21006 AS id, 210 AS idCanton, 'Pital' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21007 AS id, 210 AS idCanton, 'La Fortuna' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21008 AS id, 210 AS idCanton, 'La Tigra' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21009 AS id, 210 AS idCanton, 'La Palmera' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21010 AS id, 210 AS idCanton, 'Venado' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21011 AS id, 210 AS idCanton, 'Cutris' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21012 AS id, 210 AS idCanton, 'Monterrey' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21013 AS id, 210 AS idCanton, 'Pocosol' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21101 AS id, 211 AS idCanton, 'Zarcero' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21102 AS id, 211 AS idCanton, 'Laguna' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21103 AS id, 211 AS idCanton, 'Tapesco' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21104 AS id, 211 AS idCanton, 'Guadalupe' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21105 AS id, 211 AS idCanton, 'Palmira' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21106 AS id, 211 AS idCanton, 'Zapote' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21107 AS id, 211 AS idCanton, 'Brisas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21201 AS id, 212 AS idCanton, 'Sarchí Norte' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21202 AS id, 212 AS idCanton, 'Sarchí Sur' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21203 AS id, 212 AS idCanton, 'Toro Amarillo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21204 AS id, 212 AS idCanton, 'San Pedro' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21205 AS id, 212 AS idCanton, 'Rodríguez' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21301 AS id, 213 AS idCanton, 'Upala' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21302 AS id, 213 AS idCanton, 'Aguas Claras' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21303 AS id, 213 AS idCanton, 'San José O Pizote' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21304 AS id, 213 AS idCanton, 'Bijagua' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21305 AS id, 213 AS idCanton, 'Delicias' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21306 AS id, 213 AS idCanton, 'Dos Ríos' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21307 AS id, 213 AS idCanton, 'Yolillal' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21308 AS id, 213 AS idCanton, 'Canalete' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21401 AS id, 214 AS idCanton, 'Los Chiles' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21402 AS id, 214 AS idCanton, 'Caño Negro' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21403 AS id, 214 AS idCanton, 'El Amparo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21404 AS id, 214 AS idCanton, 'San Jorge' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21501 AS id, 215 AS idCanton, 'San Rafael' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21502 AS id, 215 AS idCanton, 'Buenavista' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21503 AS id, 215 AS idCanton, 'Cote' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21504 AS id, 215 AS idCanton, 'Katira' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21601 AS id, 216 AS idCanton, 'Río Cuarto' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21602 AS id, 216 AS idCanton, 'Santa Rita' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 21603 AS id, 216 AS idCanton, 'Santa Isabel' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30101 AS id, 301 AS idCanton, 'Oriental' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30102 AS id, 301 AS idCanton, 'Occidental' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30103 AS id, 301 AS idCanton, 'Carmen' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30104 AS id, 301 AS idCanton, 'San Nicolás' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30105 AS id, 301 AS idCanton, 'Aguacaliente o San Francisco' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30106 AS id, 301 AS idCanton, 'Guadalupe o Arenilla' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30107 AS id, 301 AS idCanton, 'Corralillo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30108 AS id, 301 AS idCanton, 'Tierra Blanca' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30109 AS id, 301 AS idCanton, 'Dulce Nombre' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30110 AS id, 301 AS idCanton, 'Llano Grande' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30111 AS id, 301 AS idCanton, 'Quebradilla' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30201 AS id, 302 AS idCanton, 'Paraíso' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30202 AS id, 302 AS idCanton, 'Santiago' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30203 AS id, 302 AS idCanton, 'Orosi' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30204 AS id, 302 AS idCanton, 'Cachí' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30205 AS id, 302 AS idCanton, 'Llanos de Santa Lucía' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30206 AS id, 302 AS idCanton, 'Birrisito' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30301 AS id, 303 AS idCanton, 'Tres Ríos' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30302 AS id, 303 AS idCanton, 'San Diego' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30303 AS id, 303 AS idCanton, 'San Juan' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30304 AS id, 303 AS idCanton, 'San Rafael' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30305 AS id, 303 AS idCanton, 'Concepción' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30306 AS id, 303 AS idCanton, 'Dulce Nombre' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30307 AS id, 303 AS idCanton, 'San Ramón' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30308 AS id, 303 AS idCanton, 'Río Azul' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30401 AS id, 304 AS idCanton, 'Juan Viñas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30402 AS id, 304 AS idCanton, 'Tucurrique' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30403 AS id, 304 AS idCanton, 'Pejibaye' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30404 AS id, 304 AS idCanton, 'La Victoria' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30501 AS id, 305 AS idCanton, 'Turrialba' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30502 AS id, 305 AS idCanton, 'La Suiza' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30503 AS id, 305 AS idCanton, 'Peralta' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30504 AS id, 305 AS idCanton, 'Santa Cruz' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30505 AS id, 305 AS idCanton, 'Santa Teresita' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30506 AS id, 305 AS idCanton, 'Pavones' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30507 AS id, 305 AS idCanton, 'Tuis' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30508 AS id, 305 AS idCanton, 'Tayutic' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30509 AS id, 305 AS idCanton, 'Santa Rosa' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30510 AS id, 305 AS idCanton, 'Tres Equis' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30511 AS id, 305 AS idCanton, 'La Isabel' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30512 AS id, 305 AS idCanton, 'Chirripó' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30601 AS id, 306 AS idCanton, 'Pacayas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30602 AS id, 306 AS idCanton, 'Cervantes' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30603 AS id, 306 AS idCanton, 'Capellades' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30701 AS id, 307 AS idCanton, 'San Rafael' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30702 AS id, 307 AS idCanton, 'Cot' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30703 AS id, 307 AS idCanton, 'Potrero Cerrado' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30704 AS id, 307 AS idCanton, 'Cipreses' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30705 AS id, 307 AS idCanton, 'Santa Rosa' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30801 AS id, 308 AS idCanton, 'El Tejar' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30802 AS id, 308 AS idCanton, 'San Isidro' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30803 AS id, 308 AS idCanton, 'Tobosi' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 30804 AS id, 308 AS idCanton, 'Patio de Agua' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40101 AS id, 401 AS idCanton, 'Heredia' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40102 AS id, 401 AS idCanton, 'Mercedes' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40103 AS id, 401 AS idCanton, 'San Francisco' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40104 AS id, 401 AS idCanton, 'Ulloa' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40105 AS id, 401 AS idCanton, 'Varablanca' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40201 AS id, 402 AS idCanton, 'Barva' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40202 AS id, 402 AS idCanton, 'San Pedro' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40203 AS id, 402 AS idCanton, 'San Pablo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40204 AS id, 402 AS idCanton, 'San Roque' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40205 AS id, 402 AS idCanton, 'Santa Lucía' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40206 AS id, 402 AS idCanton, 'San José de la Montaña' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40207 AS id, 402 AS idCanton, 'Puente Salas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40301 AS id, 403 AS idCanton, 'Santo Domingo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40302 AS id, 403 AS idCanton, 'San Vicente' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40303 AS id, 403 AS idCanton, 'San Miguel' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40304 AS id, 403 AS idCanton, 'Paracito' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40305 AS id, 403 AS idCanton, 'Santo Tomás' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40306 AS id, 403 AS idCanton, 'Santa Rosa' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40307 AS id, 403 AS idCanton, 'Tures' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40308 AS id, 403 AS idCanton, 'Pará' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40401 AS id, 404 AS idCanton, 'Santa Bárbara' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40402 AS id, 404 AS idCanton, 'San Pedro' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40403 AS id, 404 AS idCanton, 'San Juan' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40404 AS id, 404 AS idCanton, 'Jesús' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40405 AS id, 404 AS idCanton, 'Santo Domingo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40406 AS id, 404 AS idCanton, 'Purabá' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40501 AS id, 405 AS idCanton, 'San Rafael' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40502 AS id, 405 AS idCanton, 'San Josecito' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40503 AS id, 405 AS idCanton, 'Santiago' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40504 AS id, 405 AS idCanton, 'Ángeles' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40505 AS id, 405 AS idCanton, 'Concepción' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40601 AS id, 406 AS idCanton, 'San Isidro' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40602 AS id, 406 AS idCanton, 'San José' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40603 AS id, 406 AS idCanton, 'Concepción' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40604 AS id, 406 AS idCanton, 'San Francisco' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40701 AS id, 407 AS idCanton, 'San Antonio' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40702 AS id, 407 AS idCanton, 'La Ribera' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40703 AS id, 407 AS idCanton, 'La Asunción' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40801 AS id, 408 AS idCanton, 'San Joaquín' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40802 AS id, 408 AS idCanton, 'Barrantes' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40803 AS id, 408 AS idCanton, 'Llorente' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40901 AS id, 409 AS idCanton, 'San Pablo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 40902 AS id, 409 AS idCanton, 'Rincón de Sabanilla' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 41001 AS id, 410 AS idCanton, 'Puerto Viejo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 41002 AS id, 410 AS idCanton, 'La Virgen' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 41003 AS id, 410 AS idCanton, 'Las Horquetas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 41004 AS id, 410 AS idCanton, 'Llanuras del Gaspar' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 41005 AS id, 410 AS idCanton, 'Cureña' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50101 AS id, 501 AS idCanton, 'Liberia' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50102 AS id, 501 AS idCanton, 'Cañas Dulces' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50103 AS id, 501 AS idCanton, 'Mayorga' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50104 AS id, 501 AS idCanton, 'Nacascolo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50105 AS id, 501 AS idCanton, 'Curubandé' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50201 AS id, 502 AS idCanton, 'Nicoya' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50202 AS id, 502 AS idCanton, 'Mansión' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50203 AS id, 502 AS idCanton, 'San Antonio' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50204 AS id, 502 AS idCanton, 'Quebrada Honda' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50205 AS id, 502 AS idCanton, 'Sámara' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50206 AS id, 502 AS idCanton, 'Nosara' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50207 AS id, 502 AS idCanton, 'Belén de Nosarita' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50301 AS id, 503 AS idCanton, 'Santa Cruz' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50302 AS id, 503 AS idCanton, 'Bolsón' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50303 AS id, 503 AS idCanton, 'Veintisiete de Abril' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50304 AS id, 503 AS idCanton, 'Tempate' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50305 AS id, 503 AS idCanton, 'Cartagena' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50306 AS id, 503 AS idCanton, 'Cuajiniquil' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50307 AS id, 503 AS idCanton, 'Diriá' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50308 AS id, 503 AS idCanton, 'Cabo Velas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50309 AS id, 503 AS idCanton, 'Tamarindo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50401 AS id, 504 AS idCanton, 'Bagaces' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50402 AS id, 504 AS idCanton, 'La Fortuna' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50403 AS id, 504 AS idCanton, 'Mogote' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50404 AS id, 504 AS idCanton, 'Río Naranjo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50405 AS id, 504 AS idCanton, 'Pijije' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50501 AS id, 505 AS idCanton, 'Filadelfia' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50502 AS id, 505 AS idCanton, 'Palmira' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50503 AS id, 505 AS idCanton, 'Sardinal' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50504 AS id, 505 AS idCanton, 'Belén' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50601 AS id, 506 AS idCanton, 'Cañas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50602 AS id, 506 AS idCanton, 'Palmira' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50603 AS id, 506 AS idCanton, 'San Miguel' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50604 AS id, 506 AS idCanton, 'Bebedero' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50605 AS id, 506 AS idCanton, 'Porozal' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50701 AS id, 507 AS idCanton, 'Las Juntas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50702 AS id, 507 AS idCanton, 'Sierra' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50703 AS id, 507 AS idCanton, 'San Juan' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50704 AS id, 507 AS idCanton, 'Colorado' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50801 AS id, 508 AS idCanton, 'Tilarán' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50802 AS id, 508 AS idCanton, 'Quebrada Grande' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50803 AS id, 508 AS idCanton, 'Tronadora' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50804 AS id, 508 AS idCanton, 'Santa Rosa' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50805 AS id, 508 AS idCanton, 'Líbano' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50806 AS id, 508 AS idCanton, 'Tierras Morenas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50807 AS id, 508 AS idCanton, 'Arenal' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50808 AS id, 508 AS idCanton, 'Cabeceras' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50901 AS id, 509 AS idCanton, 'Carmona' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50902 AS id, 509 AS idCanton, 'Santa Rita' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50903 AS id, 509 AS idCanton, 'Zapotal' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50904 AS id, 509 AS idCanton, 'San Pablo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50905 AS id, 509 AS idCanton, 'Porvenir' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 50906 AS id, 509 AS idCanton, 'Bejuco' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 51001 AS id, 510 AS idCanton, 'La Cruz' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 51002 AS id, 510 AS idCanton, 'Santa Cecilia' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 51003 AS id, 510 AS idCanton, 'La Garita' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 51004 AS id, 510 AS idCanton, 'Santa Elena' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 51101 AS id, 511 AS idCanton, 'Hojancha' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 51102 AS id, 511 AS idCanton, 'Monte Romo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 51103 AS id, 511 AS idCanton, 'Puerto Carrillo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 51104 AS id, 511 AS idCanton, 'Huacas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 51105 AS id, 511 AS idCanton, 'Matambú' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60101 AS id, 601 AS idCanton, 'Puntarenas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60102 AS id, 601 AS idCanton, 'Pitahaya' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60103 AS id, 601 AS idCanton, 'Chomes' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60104 AS id, 601 AS idCanton, 'Lepanto' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60105 AS id, 601 AS idCanton, 'Paquera' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60106 AS id, 601 AS idCanton, 'Manzanillo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60107 AS id, 601 AS idCanton, 'Guacimal' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60108 AS id, 601 AS idCanton, 'Barranca' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60110 AS id, 601 AS idCanton, 'Isla del Coco' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60111 AS id, 601 AS idCanton, 'Cóbano' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60112 AS id, 601 AS idCanton, 'Chacarita' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60113 AS id, 601 AS idCanton, 'Chira' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60114 AS id, 601 AS idCanton, 'Acapulco' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60115 AS id, 601 AS idCanton, 'El Roble' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60116 AS id, 601 AS idCanton, 'Arancibia' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60201 AS id, 602 AS idCanton, 'Espíritu Santo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60202 AS id, 602 AS idCanton, 'San Juan Grande' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60203 AS id, 602 AS idCanton, 'Macacona' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60204 AS id, 602 AS idCanton, 'San Rafael' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60205 AS id, 602 AS idCanton, 'San Jerónimo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60206 AS id, 602 AS idCanton, 'Caldera' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60301 AS id, 603 AS idCanton, 'Buenos Aires' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60302 AS id, 603 AS idCanton, 'Volcán' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60303 AS id, 603 AS idCanton, 'Potrero Grande' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60304 AS id, 603 AS idCanton, 'Boruca' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60305 AS id, 603 AS idCanton, 'Pilas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60306 AS id, 603 AS idCanton, 'Colinas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60307 AS id, 603 AS idCanton, 'Chánguena' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60308 AS id, 603 AS idCanton, 'Biolley' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60309 AS id, 603 AS idCanton, 'Brunka' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60310 AS id, 603 AS idCanton, 'Cabagra' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60401 AS id, 604 AS idCanton, 'Miramar' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60402 AS id, 604 AS idCanton, 'La Unión' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60403 AS id, 604 AS idCanton, 'San Isidro' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60501 AS id, 605 AS idCanton, 'Puerto Cortés' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60502 AS id, 605 AS idCanton, 'Palmar' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60503 AS id, 605 AS idCanton, 'Sierpe' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60504 AS id, 605 AS idCanton, 'Bahía Ballena' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60505 AS id, 605 AS idCanton, 'Piedras Blancas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60506 AS id, 605 AS idCanton, 'Bahía Drake' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60601 AS id, 606 AS idCanton, 'Quepos' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60602 AS id, 606 AS idCanton, 'Savegre' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60603 AS id, 606 AS idCanton, 'Naranjito' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60701 AS id, 607 AS idCanton, 'Golfito' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60703 AS id, 607 AS idCanton, 'Guaycará' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60704 AS id, 607 AS idCanton, 'Pavón' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60801 AS id, 608 AS idCanton, 'San Vito' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60802 AS id, 608 AS idCanton, 'Sabalito' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60803 AS id, 608 AS idCanton, 'Aguabuena' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60804 AS id, 608 AS idCanton, 'Limoncito' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60805 AS id, 608 AS idCanton, 'Pittier' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60806 AS id, 608 AS idCanton, 'Gutiérrez Braun' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 60901 AS id, 609 AS idCanton, 'Parrita' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 61001 AS id, 610 AS idCanton, 'Corredor' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 61002 AS id, 610 AS idCanton, 'La Cuesta' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 61003 AS id, 610 AS idCanton, 'Canoas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 61004 AS id, 610 AS idCanton, 'Laurel' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 61101 AS id, 611 AS idCanton, 'Jacó' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 61102 AS id, 611 AS idCanton, 'Tárcoles' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 61103 AS id, 611 AS idCanton, 'Lagunillas' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 61201 AS id, 612 AS idCanton, 'Monteverde' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 61301 AS id, 613 AS idCanton, 'Puerto Jiménez' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70101 AS id, 701 AS idCanton, 'Limón' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70102 AS id, 701 AS idCanton, 'Valle La Estrella' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70103 AS id, 701 AS idCanton, 'Río Blanco' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70104 AS id, 701 AS idCanton, 'Matama' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70201 AS id, 702 AS idCanton, 'Guápiles' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70202 AS id, 702 AS idCanton, 'Jiménez' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70203 AS id, 702 AS idCanton, 'Rita' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70204 AS id, 702 AS idCanton, 'Roxana' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70205 AS id, 702 AS idCanton, 'Cariari' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70206 AS id, 702 AS idCanton, 'Colorado' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70207 AS id, 702 AS idCanton, 'La Colonia' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70301 AS id, 703 AS idCanton, 'Siquirres' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70302 AS id, 703 AS idCanton, 'Pacuarito' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70303 AS id, 703 AS idCanton, 'Florida' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70304 AS id, 703 AS idCanton, 'Germania' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70305 AS id, 703 AS idCanton, 'El Cairo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70306 AS id, 703 AS idCanton, 'Alegría' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70307 AS id, 703 AS idCanton, 'Reventazón' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70401 AS id, 704 AS idCanton, 'Bratsi' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70402 AS id, 704 AS idCanton, 'Sixaola' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70403 AS id, 704 AS idCanton, 'Cahuita' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70404 AS id, 704 AS idCanton, 'Telire' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70501 AS id, 705 AS idCanton, 'Matina' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70502 AS id, 705 AS idCanton, 'Batán' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70503 AS id, 705 AS idCanton, 'Carrandí' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70601 AS id, 706 AS idCanton, 'Guácimo' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70602 AS id, 706 AS idCanton, 'Mercedes' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70603 AS id, 706 AS idCanton, 'Pocora' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70604 AS id, 706 AS idCanton, 'Río Jiménez' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

MERGE INTO district d
USING (SELECT 70605 AS id, 706 AS idCanton, 'Duacarí' AS name FROM dual) src
ON (d.id = src.id)
WHEN MATCHED THEN
    UPDATE SET d.idCanton = src.idCanton, d.name = src.name
WHEN NOT MATCHED THEN
    INSERT (id, idCanton, name)
    VALUES (src.id, src.idCanton, src.name);

COMMIT;

PROMPT Location data loaded. Expected totals: 7 provinces, 84 cantons, 494 districts.
SELECT COUNT(*) AS province_count FROM province;
SELECT COUNT(*) AS canton_count FROM canton;
SELECT COUNT(*) AS district_count FROM district;