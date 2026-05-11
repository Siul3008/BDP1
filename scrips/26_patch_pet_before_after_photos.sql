--------------------------------------------------------------------------------
-- Allows each pet to keep separate Before and After photos.
--
-- The original model linked one photo through pet.idPetPhoto. This patch keeps
-- that link as the main "Before" image and adds optional petPhoto rows related
-- directly to the pet with a photoType value such as BEFORE or AFTER.
--------------------------------------------------------------------------------

DECLARE
    PROCEDURE add_column_if_missing(
        p_table_name  IN VARCHAR2,
        p_column_name IN VARCHAR2,
        p_definition  IN VARCHAR2
    ) IS
        v_exists NUMBER;
    BEGIN
        SELECT COUNT(*)
          INTO v_exists
          FROM user_tab_columns
         WHERE table_name = UPPER(p_table_name)
           AND column_name = UPPER(p_column_name);

        IF v_exists = 0 THEN
            EXECUTE IMMEDIATE
                'ALTER TABLE ' || p_table_name || ' ADD (' || p_column_name || ' ' || p_definition || ')';
        END IF;
    END;

    PROCEDURE add_fk_if_missing IS
        v_exists NUMBER;
    BEGIN
        SELECT COUNT(*)
          INTO v_exists
          FROM user_constraints
         WHERE constraint_name = 'FK_PETPHOTO_PET'
           AND table_name = 'PETPHOTO';

        IF v_exists = 0 THEN
            EXECUTE IMMEDIATE
                'ALTER TABLE petPhoto ADD CONSTRAINT fk_petPhoto_pet FOREIGN KEY(idPet) REFERENCES pet(id)';
        END IF;
    END;
BEGIN
    add_column_if_missing('petPhoto', 'idPet', 'NUMBER(6)');
    add_column_if_missing('petPhoto', 'photoType', 'VARCHAR2(20)');
    add_fk_if_missing;

    EXECUTE IMMEDIATE q'[
        UPDATE petPhoto pp
           SET pp.photoType = 'BEFORE'
         WHERE pp.photoType IS NULL
           AND EXISTS (
               SELECT 1
                 FROM pet p
                WHERE p.idPetPhoto = pp.id
           )
    ]';

    EXECUTE IMMEDIATE q'[
        UPDATE petPhoto pp
           SET pp.idPet = (
               SELECT p.id
                 FROM pet p
                WHERE p.idPetPhoto = pp.id
                  AND ROWNUM = 1
           )
         WHERE pp.idPet IS NULL
           AND EXISTS (
               SELECT 1
                 FROM pet p
                WHERE p.idPetPhoto = pp.id
           )
    ]';

    COMMIT;
END;
/
