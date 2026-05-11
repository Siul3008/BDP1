-----------------------------------------------------------------------------------------------
--Students:
--Meylin Carvajal Baltodano
--Luis D. Rivas Castro
--Ma. Fernanda Vega Acosta

-----------------------------------------------------------------------------------------------


--Modifications to appAccount and Association and some inserts needed


ALTER TABLE appAccount DROP CONSTRAINT ck_appAccount_type;

ALTER TABLE appAccount DROP CONSTRAINT ck_appAccount_owner;

ALTER TABLE appAccount
ADD CONSTRAINT ck_appAccount_type
CHECK (accountType IN ('USER', 'ASSOCIATION', 'ADMIN'));

ALTER TABLE appAccount
ADD CONSTRAINT ck_appAccount_owner CHECK (
    (accountType = 'USER' AND idPerson IS NOT NULL AND idAssociation IS NULL) OR
    (accountType = 'ASSOCIATION' AND idAssociation IS NOT NULL AND idPerson IS NULL) OR
    (accountType = 'ADMIN' AND idPerson IS NULL AND idAssociation IS NULL)
);

-- The original table only allowed 8 characters, which is too short for real association names.
ALTER TABLE association MODIFY name VARCHAR2(80);

-- Initial admin account.
-- Email: admin@bienestar.org
-- Password: Admin123
INSERT INTO appAccount(
    id,
    accountType,
    loginEmail,
    passwordHash,
    identificationValue,
    idPerson,
    idAssociation,
    isActive,
    createdAt
)
SELECT
    sAppAccount.NEXTVAL,
    'ADMIN',
    'admin@bienestar.org',
    '3b612c75a7b5048a435fb6ec81e52ff92d6d795a8b5a9c17070f6a63c97a53b2',
    'ADMIN-001',
    NULL,
    NULL,
    'Y',
    SYSDATE
FROM dual
WHERE NOT EXISTS (
    SELECT 1
    FROM appAccount
    WHERE loginEmail = 'admin@bienestar.org'
       OR identificationValue = 'ADMIN-001'
);

COMMIT;
