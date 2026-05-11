--------------------------------------------------------------------------------
-- Adds database-backed image storage for pet and adoption photos.
--
-- Existing photoPath values stay valid. The new BLOB columns let the JavaFX app
-- store the selected image bytes in Oracle so other computers can display the
-- same photo without needing the original local file path.
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
BEGIN
    add_column_if_missing('petPhoto', 'fileName', 'VARCHAR2(255)');
    add_column_if_missing('petPhoto', 'mimeType', 'VARCHAR2(80)');
    add_column_if_missing('petPhoto', 'photoData', 'BLOB');

    add_column_if_missing('adoptionPhoto', 'fileName', 'VARCHAR2(255)');
    add_column_if_missing('adoptionPhoto', 'mimeType', 'VARCHAR2(80)');
    add_column_if_missing('adoptionPhoto', 'photoData', 'BLOB');

    COMMIT;
END;
/
