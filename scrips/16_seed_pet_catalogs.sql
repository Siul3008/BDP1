-----------------------------------------------------------------------------------------------
--Students:
--Meylin Carvajal Baltodano
--Luis D. Rivas Castro
--Ma. Fernanda Vega Acosta

-----------------------------------------------------------------------------------------------

-- Pet catalog seed only.
-- This script does not alter tables or create constraints.
-- If breed.idPetType exists, breeds are linked to petType. Otherwise, names keep a type prefix.

DECLARE
    v_count NUMBER;
    v_id NUMBER;
    v_type_id NUMBER;
    v_has_breed_type NUMBER;

    PROCEDURE next_safe_id(p_table IN VARCHAR2, p_sequence IN VARCHAR2, p_id OUT NUMBER)
    AS
    BEGIN
        EXECUTE IMMEDIATE 'SELECT ' || p_sequence || '.NEXTVAL FROM dual' INTO p_id;
    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            EXECUTE IMMEDIATE 'SELECT NVL(MAX(id), 0) + 1 FROM ' || p_table INTO p_id;
    END;

    PROCEDURE seed_catalog(p_table IN VARCHAR2, p_label_column IN VARCHAR2, p_sequence IN VARCHAR2, p_value IN VARCHAR2)
    AS
    BEGIN
        EXECUTE IMMEDIATE
            'SELECT COUNT(*) FROM ' || p_table || ' WHERE LOWER(' || p_label_column || ') = LOWER(:value)'
            INTO v_count
            USING p_value;

        IF v_count = 0 THEN
            next_safe_id(p_table, p_sequence, v_id);

            BEGIN
                EXECUTE IMMEDIATE
                    'INSERT INTO ' || p_table || '(id, ' || p_label_column || ') VALUES (:id, :value)'
                    USING v_id, p_value;
            EXCEPTION
                WHEN DUP_VAL_ON_INDEX THEN
                    EXECUTE IMMEDIATE 'SELECT NVL(MAX(id), 0) + 1 FROM ' || p_table INTO v_id;
                    EXECUTE IMMEDIATE
                        'INSERT INTO ' || p_table || '(id, ' || p_label_column || ') VALUES (:id, :value)'
                        USING v_id, p_value;
            END;
        END IF;
    END;

    PROCEDURE seed_currency(p_name IN VARCHAR2, p_acronym IN VARCHAR2)
    AS
    BEGIN
        SELECT COUNT(*)
        INTO v_count
        FROM currency
        WHERE LOWER(acronym) = LOWER(p_acronym);

        IF v_count = 0 THEN
            next_safe_id('currency', 'sCurrency', v_id);

            BEGIN
                INSERT INTO currency(id, name, acronym)
                VALUES(v_id, p_name, p_acronym);
            EXCEPTION
                WHEN DUP_VAL_ON_INDEX THEN
                    SELECT NVL(MAX(id), 0) + 1 INTO v_id FROM currency;
                    INSERT INTO currency(id, name, acronym)
                    VALUES(v_id, p_name, p_acronym);
            END;
        END IF;
    END;

    PROCEDURE get_type_id(p_type_name IN VARCHAR2, p_id OUT NUMBER)
    AS
    BEGIN
        SELECT id
        INTO p_id
        FROM petType
        WHERE LOWER(name) = LOWER(p_type_name)
          AND ROWNUM = 1;
    END;

    PROCEDURE seed_breed(p_type_name IN VARCHAR2, p_breed_name IN VARCHAR2, p_prefixed_name IN VARCHAR2)
    AS
    BEGIN
        IF v_has_breed_type = 1 THEN
            get_type_id(p_type_name, v_type_id);

            SELECT COUNT(*)
            INTO v_count
            FROM breed
            WHERE LOWER(name) = LOWER(p_breed_name)
              AND idPetType = v_type_id;

            IF v_count = 0 THEN
                SELECT COUNT(*)
                INTO v_count
                FROM breed
                WHERE LOWER(name) = LOWER(p_prefixed_name)
                  AND idPetType IS NULL;

                IF v_count > 0 THEN
                    UPDATE breed
                    SET idPetType = v_type_id,
                        name = p_breed_name
                    WHERE LOWER(name) = LOWER(p_prefixed_name)
                      AND idPetType IS NULL
                      AND ROWNUM = 1;
                ELSE
                    next_safe_id('breed', 'sBreed', v_id);

                    BEGIN
                        INSERT INTO breed(id, name, idPetType)
                        VALUES(v_id, p_breed_name, v_type_id);
                    EXCEPTION
                        WHEN DUP_VAL_ON_INDEX THEN
                            SELECT NVL(MAX(id), 0) + 1 INTO v_id FROM breed;
                            INSERT INTO breed(id, name, idPetType)
                            VALUES(v_id, p_breed_name, v_type_id);
                    END;
                END IF;
            END IF;
        ELSE
            seed_catalog('breed', 'name', 'sBreed', p_prefixed_name);
        END IF;
    END;
BEGIN
    SELECT COUNT(*)
    INTO v_has_breed_type
    FROM user_tab_columns
    WHERE table_name = 'BREED'
      AND column_name = 'IDPETTYPE';

    seed_catalog('petType', 'name', 'sPetType', 'Dog');
    seed_catalog('petType', 'name', 'sPetType', 'Cat');
    seed_catalog('petType', 'name', 'sPetType', 'Turtle');
    seed_catalog('petType', 'name', 'sPetType', 'Bird');
    seed_catalog('petType', 'name', 'sPetType', 'Spider');
    seed_catalog('petType', 'name', 'sPetType', 'Snake');
    seed_catalog('petType', 'name', 'sPetType', 'Other');

    seed_catalog('color', 'name', 'sColor', 'Black');
    seed_catalog('color', 'name', 'sColor', 'White');
    seed_catalog('color', 'name', 'sColor', 'Brown');
    seed_catalog('color', 'name', 'sColor', 'Gray');
    seed_catalog('color', 'name', 'sColor', 'Golden');
    seed_catalog('color', 'name', 'sColor', 'Cream');
    seed_catalog('color', 'name', 'sColor', 'Orange');
    seed_catalog('color', 'name', 'sColor', 'Mixed');
    seed_catalog('color', 'name', 'sColor', 'Spotted');
    seed_catalog('color', 'name', 'sColor', 'Striped');

    seed_catalog('petSize', 'name', 'sPetSize', 'Small');
    seed_catalog('petSize', 'name', 'sPetSize', 'Medium');
    seed_catalog('petSize', 'name', 'sPetSize', 'Large');
    seed_catalog('petSize', 'name', 'sPetSize', 'Extra Large');
    seed_catalog('petSize', 'name', 'sPetSize', 'Toy');

    seed_catalog('petStatus', 'status', 'sPetStatus', 'Lost');
    seed_catalog('petStatus', 'status', 'sPetStatus', 'Found');
    seed_catalog('petStatus', 'status', 'sPetStatus', 'For Adoption');
    seed_catalog('petStatus', 'status', 'sPetStatus', 'Adopted');

    seed_catalog('trainingEase', 'name', 'sTrainingEase', 'Easy');
    seed_catalog('trainingEase', 'name', 'sTrainingEase', 'Moderate');
    seed_catalog('trainingEase', 'name', 'sTrainingEase', 'Difficult');
    seed_catalog('trainingEase', 'name', 'sTrainingEase', 'Unknown');

    seed_currency('Costa Rican Colon', 'CRC');
    seed_currency('US Dollar', 'USD');

    seed_breed('Dog', 'Labrador', 'Dog - Labrador');
    seed_breed('Dog', 'Golden Retriever', 'Dog - Golden Retriever');
    seed_breed('Dog', 'German Shepherd', 'Dog - German Shepherd');
    seed_breed('Dog', 'French Bulldog', 'Dog - French Bulldog');
    seed_breed('Dog', 'Poodle', 'Dog - Poodle');
    seed_breed('Dog', 'Beagle', 'Dog - Beagle');
    seed_breed('Dog', 'Chihuahua', 'Dog - Chihuahua');
    seed_breed('Dog', 'Schnauzer', 'Dog - Schnauzer');
    seed_breed('Dog', 'Boxer', 'Dog - Boxer');
    seed_breed('Dog', 'Mixed Breed', 'Dog - Mixed Breed');

    seed_breed('Cat', 'Shorthair', 'Cat - Shorthair');
    seed_breed('Cat', 'Longhair', 'Cat - Longhair');
    seed_breed('Cat', 'Siamese', 'Cat - Siamese');
    seed_breed('Cat', 'Persian', 'Cat - Persian');
    seed_breed('Cat', 'Maine Coon', 'Cat - Maine Coon');
    seed_breed('Cat', 'Bengal', 'Cat - Bengal');
    seed_breed('Cat', 'Ragdoll', 'Cat - Ragdoll');
    seed_breed('Cat', 'Sphynx', 'Cat - Sphynx');
    seed_breed('Cat', 'British Shorthair', 'Cat - British Shorthair');
    seed_breed('Cat', 'Russian Blue', 'Cat - Russian Blue');

    seed_breed('Turtle', 'Red-eared Slider', 'Turtle - Red-eared');
    seed_breed('Turtle', 'Box Turtle', 'Turtle - Box');
    seed_breed('Turtle', 'Russian Tortoise', 'Turtle - Russian');
    seed_breed('Turtle', 'Greek Tortoise', 'Turtle - Greek');
    seed_breed('Turtle', 'Leopard Tortoise', 'Turtle - Leopard');
    seed_breed('Turtle', 'Sulcata Tortoise', 'Turtle - Sulcata');
    seed_breed('Turtle', 'Map Turtle', 'Turtle - Map');
    seed_breed('Turtle', 'Painted Turtle', 'Turtle - Painted');
    seed_breed('Turtle', 'Hermanns Tortoise', 'Turtle - Hermanns');
    seed_breed('Turtle', 'Musk Turtle', 'Turtle - Musk');

    seed_breed('Bird', 'Budgerigar', 'Bird - Budgerigar');
    seed_breed('Bird', 'Canary', 'Bird - Canary');
    seed_breed('Bird', 'Cockatiel', 'Bird - Cockatiel');
    seed_breed('Bird', 'Lovebird', 'Bird - Lovebird');
    seed_breed('Bird', 'Amazon Parrot', 'Bird - Amazon Parrot');
    seed_breed('Bird', 'Macaw', 'Bird - Macaw');
    seed_breed('Bird', 'Zebra Finch', 'Bird - Zebra Finch');
    seed_breed('Bird', 'Monk Parakeet', 'Bird - Monk Parakeet');
    seed_breed('Bird', 'Cockatoo', 'Bird - Cockatoo');
    seed_breed('Bird', 'Finch', 'Bird - Finch');

    seed_breed('Spider', 'Chilean Rose', 'Spider - Chilean Rose');
    seed_breed('Spider', 'Mexican Red-knee', 'Spider - Red-knee');
    seed_breed('Spider', 'Costa Rican Zebra', 'Spider - Costa Rican');
    seed_breed('Spider', 'Salmon Pink', 'Spider - Salmon Pink');
    seed_breed('Spider', 'Cobalt Blue', 'Spider - Cobalt Blue');
    seed_breed('Spider', 'Curly Hair', 'Spider - Curly Hair');
    seed_breed('Spider', 'Ornamental', 'Spider - Ornamental');
    seed_breed('Spider', 'Orange Baboon', 'Spider - Orange Baboon');
    seed_breed('Spider', 'Golden Knee', 'Spider - Golden Knee');
    seed_breed('Spider', 'Pink Toe', 'Spider - Pink Toe');

    seed_breed('Snake', 'Corn Snake', 'Snake - Corn');
    seed_breed('Snake', 'Ball Python', 'Snake - Ball Python');
    seed_breed('Snake', 'Boa Constrictor', 'Snake - Boa Constrictor');
    seed_breed('Snake', 'Kingsnake', 'Snake - Kingsnake');
    seed_breed('Snake', 'Milk Snake', 'Snake - Milk Snake');
    seed_breed('Snake', 'Garter Snake', 'Snake - Garter');
    seed_breed('Snake', 'Rosy Boa', 'Snake - Rosy Boa');
    seed_breed('Snake', 'Carpet Python', 'Snake - Carpet Python');
    seed_breed('Snake', 'Rat Snake', 'Snake - Rat Snake');
    seed_breed('Snake', 'Hognose', 'Snake - Hognose');

    seed_breed('Other', 'Unique Breed', 'Other - Unique Breed');
    seed_breed('Other', 'Mixed', 'Other - Mixed');
    seed_breed('Other', 'Unknown', 'Other - Unknown');
    seed_breed('Other', 'Not Applicable', 'Other - Not Applicable');
    seed_breed('Other', 'Domestic', 'Other - Domestic');
    seed_breed('Other', 'Exotic', 'Other - Exotic');
    seed_breed('Other', 'Small', 'Other - Small');
    seed_breed('Other', 'Medium', 'Other - Medium');
    seed_breed('Other', 'Large', 'Other - Large');
    seed_breed('Other', 'Unclassified', 'Other - Unclassified');

    COMMIT;
END;
/
