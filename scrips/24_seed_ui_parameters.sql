MERGE INTO sysParameter target
USING (SELECT 'pet.energy.1' AS name, 'Pet energy option' AS description, 'Athletic' AS value FROM dual) source
ON (target.name = source.name)
WHEN MATCHED THEN
    UPDATE SET target.description = source.description, target.value = source.value
WHEN NOT MATCHED THEN
    INSERT (id, name, description, value)
    VALUES (sSysParameter.NEXTVAL, source.name, source.description, source.value);

MERGE INTO sysParameter target
USING (SELECT 'pet.energy.2' AS name, 'Pet energy option' AS description, 'Runner' AS value FROM dual) source
ON (target.name = source.name)
WHEN MATCHED THEN
    UPDATE SET target.description = source.description, target.value = source.value
WHEN NOT MATCHED THEN
    INSERT (id, name, description, value)
    VALUES (sSysParameter.NEXTVAL, source.name, source.description, source.value);

MERGE INTO sysParameter target
USING (SELECT 'pet.energy.3' AS name, 'Pet energy option' AS description, 'Walker' AS value FROM dual) source
ON (target.name = source.name)
WHEN MATCHED THEN
    UPDATE SET target.description = source.description, target.value = source.value
WHEN NOT MATCHED THEN
    INSERT (id, name, description, value)
    VALUES (sSysParameter.NEXTVAL, source.name, source.description, source.value);

MERGE INTO sysParameter target
USING (SELECT 'pet.energy.4' AS name, 'Pet energy option' AS description, 'Calm' AS value FROM dual) source
ON (target.name = source.name)
WHEN MATCHED THEN
    UPDATE SET target.description = source.description, target.value = source.value
WHEN NOT MATCHED THEN
    INSERT (id, name, description, value)
    VALUES (sSysParameter.NEXTVAL, source.name, source.description, source.value);

MERGE INTO sysParameter target
USING (SELECT 'pet.energy.5' AS name, 'Pet energy option' AS description, 'Low' AS value FROM dual) source
ON (target.name = source.name)
WHEN MATCHED THEN
    UPDATE SET target.description = source.description, target.value = source.value
WHEN NOT MATCHED THEN
    INSERT (id, name, description, value)
    VALUES (sSysParameter.NEXTVAL, source.name, source.description, source.value);

MERGE INTO sysParameter target
USING (SELECT 'adopt.yard' AS name, 'Adoption question' AS description, 'Patio disponible' AS value FROM dual) source
ON (target.name = source.name)
WHEN MATCHED THEN
    UPDATE SET target.description = source.description, target.value = source.value
WHEN NOT MATCHED THEN
    INSERT (id, name, description, value)
    VALUES (sSysParameter.NEXTVAL, source.name, source.description, source.value);

MERGE INTO sysParameter target
USING (SELECT 'adopt.home' AS name, 'Adoption question' AS description, 'Tipo de vivienda' AS value FROM dual) source
ON (target.name = source.name)
WHEN MATCHED THEN
    UPDATE SET target.description = source.description, target.value = source.value
WHEN NOT MATCHED THEN
    INSERT (id, name, description, value)
    VALUES (sSysParameter.NEXTVAL, source.name, source.description, source.value);

MERGE INTO sysParameter target
USING (SELECT 'adopt.exercise' AS name, 'Adoption question' AS description, 'Tiempo de ejercicio' AS value FROM dual) source
ON (target.name = source.name)
WHEN MATCHED THEN
    UPDATE SET target.description = source.description, target.value = source.value
WHEN NOT MATCHED THEN
    INSERT (id, name, description, value)
    VALUES (sSysParameter.NEXTVAL, source.name, source.description, source.value);

MERGE INTO sysParameter target
USING (SELECT 'adopt.pets' AS name, 'Adoption question' AS description, 'Otras mascotas' AS value FROM dual) source
ON (target.name = source.name)
WHEN MATCHED THEN
    UPDATE SET target.description = source.description, target.value = source.value
WHEN NOT MATCHED THEN
    INSERT (id, name, description, value)
    VALUES (sSysParameter.NEXTVAL, source.name, source.description, source.value);

MERGE INTO sysParameter target
USING (SELECT 'adopt.answers' AS name, 'Adoption question' AS description, 'Respuestas de solicitud' AS value FROM dual) source
ON (target.name = source.name)
WHEN MATCHED THEN
    UPDATE SET target.description = source.description, target.value = source.value
WHEN NOT MATCHED THEN
    INSERT (id, name, description, value)
    VALUES (sSysParameter.NEXTVAL, source.name, source.description, source.value);

COMMIT;
