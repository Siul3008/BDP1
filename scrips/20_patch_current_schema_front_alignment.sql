-----------------------------------------------------------------------------------------------
--Students:
--Meylin Carvajal Baltodano
--Luis D. Rivas Castro
--Ma. Fernanda Vega Acosta

-----------------------------------------------------------------------------------------------

-- Safe patch for databases created before the latest front-end alignment.
-- It can be re-run: each structural change checks the current schema first.

DECLARE
    v_count NUMBER;
    v_length NUMBER;

    FUNCTION column_exists(p_table_name IN VARCHAR2, p_column_name IN VARCHAR2) RETURN BOOLEAN IS
        v_found NUMBER;
    BEGIN
        SELECT COUNT(*)
        INTO v_found
        FROM user_tab_columns
        WHERE table_name = UPPER(p_table_name)
          AND column_name = UPPER(p_column_name);

        RETURN v_found > 0;
    END;

    FUNCTION constraint_exists(p_constraint_name IN VARCHAR2) RETURN BOOLEAN IS
        v_found NUMBER;
    BEGIN
        SELECT COUNT(*)
        INTO v_found
        FROM user_constraints
        WHERE constraint_name = UPPER(p_constraint_name);

        RETURN v_found > 0;
    END;
BEGIN
    IF column_exists('email', 'emailAddress') THEN
        SELECT data_length
        INTO v_length
        FROM user_tab_columns
        WHERE table_name = 'EMAIL'
          AND column_name = 'EMAILADDRESS';

        IF v_length < 100 THEN
            EXECUTE IMMEDIATE 'ALTER TABLE email MODIFY (emailAddress VARCHAR2(100))';
        END IF;
    END IF;

    IF NOT column_exists('pet', 'chip') THEN
        EXECUTE IMMEDIATE 'ALTER TABLE pet ADD (chip VARCHAR2(30))';
    END IF;

    IF NOT column_exists('pet', 'eventDate') THEN
        EXECUTE IMMEDIATE 'ALTER TABLE pet ADD (eventDate DATE)';
    END IF;

    EXECUTE IMMEDIATE 'ALTER TABLE association MODIFY (name VARCHAR2(80))';
    EXECUTE IMMEDIATE 'ALTER TABLE donation MODIFY (amount NUMBER(10,2))';
    EXECUTE IMMEDIATE 'ALTER TABLE donationAllLocation MODIFY (allocatedAmount NUMBER(10,2))';

    IF column_exists('fosterCondition', 'notes') THEN
        EXECUTE IMMEDIATE 'ALTER TABLE fosterCondition MODIFY (notes VARCHAR2(200))';
    END IF;

    IF column_exists('blacklistReport', 'idPerson')
       AND NOT column_exists('blacklistReport', 'idReportee') THEN
        EXECUTE IMMEDIATE 'ALTER TABLE blacklistReport RENAME COLUMN idPerson TO idReportee';
    END IF;

    IF NOT column_exists('blacklistReport', 'idReporter') THEN
        EXECUTE IMMEDIATE 'ALTER TABLE blacklistReport ADD (idReporter NUMBER(6))';
    END IF;

    IF NOT column_exists('blacklistReport', 'idStarRating') THEN
        EXECUTE IMMEDIATE 'ALTER TABLE blacklistReport ADD (idStarRating NUMBER(6))';
    END IF;

    IF column_exists('blacklistReport', 'idReportee')
       AND constraint_exists('fk_bl_idPerson')
       AND NOT constraint_exists('fk_bl_idReportee') THEN
        EXECUTE IMMEDIATE 'ALTER TABLE blacklistReport RENAME CONSTRAINT fk_bl_idPerson TO fk_bl_idReportee';
    END IF;

    IF column_exists('blacklistReport', 'idReportee')
       AND NOT constraint_exists('bl_reportee_nn') THEN
        EXECUTE IMMEDIATE 'SELECT COUNT(*) FROM blacklistReport WHERE idReportee IS NULL'
        INTO v_count;

        IF v_count = 0 THEN
            EXECUTE IMMEDIATE 'ALTER TABLE blacklistReport MODIFY (idReportee CONSTRAINT bl_reportee_nn NOT NULL)';
        END IF;
    END IF;

    IF column_exists('blacklistReport', 'idStarRating')
       AND NOT constraint_exists('bllist_star_nn') THEN
        EXECUTE IMMEDIATE 'SELECT COUNT(*) FROM blacklistReport WHERE idStarRating IS NULL'
        INTO v_count;

        IF v_count = 0 THEN
            EXECUTE IMMEDIATE 'ALTER TABLE blacklistReport MODIFY (idStarRating CONSTRAINT blList_star_nn NOT NULL)';
        END IF;
    END IF;

    IF column_exists('blacklistReport', 'idReporter')
       AND NOT constraint_exists('fk_bl_idReporter') THEN
        EXECUTE IMMEDIATE 'ALTER TABLE blacklistReport ADD CONSTRAINT fk_bl_idReporter FOREIGN KEY(idReporter) REFERENCES person (id)';
    END IF;

    IF column_exists('blacklistReport', 'idStarRating')
       AND NOT constraint_exists('fk_bl_idStarRat') THEN
        EXECUTE IMMEDIATE 'ALTER TABLE blacklistReport ADD CONSTRAINT fk_bl_idStarRat FOREIGN KEY(idStarRating) REFERENCES starRating (id)';
    END IF;
END;
/

INSERT INTO requiresFoodDonation(id, name)
SELECT sRequiresFoodDonation.NEXTVAL, 'Not required'
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM requiresFoodDonation WHERE LOWER(name) = 'not required'
);

INSERT INTO requiresFoodDonation(id, name)
SELECT sRequiresFoodDonation.NEXTVAL, 'Optional'
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM requiresFoodDonation WHERE LOWER(name) = 'optional'
);

INSERT INTO requiresFoodDonation(id, name)
SELECT sRequiresFoodDonation.NEXTVAL, 'Required'
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM requiresFoodDonation WHERE LOWER(name) = 'required'
);

COMMIT;

-- personxBListRep is intentionally left in place to avoid deleting existing data.
-- The newer blacklist query reads reporter/reportee directly from blackListReport.

CREATE OR REPLACE PROCEDURE insertBlacklist
(
    pIdReporter   IN NUMBER,
    pIdReportee   IN NUMBER,
    pIdStarRating IN NUMBER,
    pReason       IN VARCHAR2,
    pActive       IN VARCHAR2,
    pReportDate   IN DATE,
    pCreationDate IN DATE,
    pCreatedBy    IN VARCHAR2,
    pUpdatedDate  IN DATE,
    pUpdatedBy    IN VARCHAR2
)
AS
BEGIN
    INSERT INTO blackListReport(
        id,
        idReporter,
        idReportee,
        idStarRating,
        reason,
        active,
        reportDate,
        creation_Date,
        created_By,
        updated_Date,
        updated_By
    )
    VALUES(
        sBlackistReport.NEXTVAL,
        pIdReporter,
        pIdReportee,
        pIdStarRating,
        pReason,
        pActive,
        pReportDate,
        pCreationDate,
        pCreatedBy,
        pUpdatedDate,
        pUpdatedBy
    );
    COMMIT;
END insertBlacklist;
/
