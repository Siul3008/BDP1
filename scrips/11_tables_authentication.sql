-----------------------------------------------------------------------------------------------
--Students:
--Meylin Carvajal Baltodano
--Luis D. Rivas Castro
--Ma. Fernanda Vega Acosta

-----------------------------------------------------------------------------------------------

--Creation of a table for the autentication of users
--so that users can log in


CREATE SEQUENCE sAppAccount
START WITH 1
INCREMENT BY 1
MINVALUE 1
MAXVALUE 5000
NOCACHE
NOCYCLE;

CREATE TABLE appAccount(
    id NUMBER(6),
    accountType VARCHAR2(20) CONSTRAINT appAccount_type_nn NOT NULL,
    loginEmail VARCHAR2(100) CONSTRAINT appAccount_email_nn NOT NULL,
    passwordHash VARCHAR2(255) CONSTRAINT appAccount_pwd_nn NOT NULL,
    identificationValue VARCHAR2(30) CONSTRAINT appAccount_ident_nn NOT NULL,
    idPerson NUMBER(6),
    idAssociation NUMBER(6),
    isActive VARCHAR2(1) DEFAULT 'Y' CONSTRAINT appAccount_active_nn NOT NULL,
    createdAt DATE DEFAULT SYSDATE NOT NULL,
    lastLoginAt DATE,
    CONSTRAINT pk_appAccount PRIMARY KEY (id),
    CONSTRAINT uk_appAccount_email UNIQUE (loginEmail),
    CONSTRAINT uk_appAccount_ident UNIQUE (identificationValue),
    CONSTRAINT ck_appAccount_type CHECK (accountType IN ('USER', 'ASSOCIATION', 'ADMIN')),
    CONSTRAINT ck_appAccount_active CHECK (isActive IN ('Y', 'N')),
    CONSTRAINT ck_appAccount_owner CHECK (
        (accountType = 'USER' AND idPerson IS NOT NULL AND idAssociation IS NULL) OR
        (accountType = 'ASSOCIATION' AND idAssociation IS NOT NULL AND idPerson IS NULL) OR
        (accountType = 'ADMIN' AND idPerson IS NULL AND idAssociation IS NULL)
    ),
    CONSTRAINT fk_appAccount_person FOREIGN KEY(idPerson) REFERENCES person(id),
    CONSTRAINT fk_appAccount_association FOREIGN KEY(idAssociation) REFERENCES association(id)
);

CREATE INDEX idx_appAccount_person ON appAccount(idPerson);
CREATE INDEX idx_appAccount_association ON appAccount(idAssociation);

-- Optional blacklist extension for associations, based on the same control idea used for people.
CREATE TABLE associationBlacklistReport(
    id NUMBER(6),
    idAssociation NUMBER(6) CONSTRAINT assocBl_association_nn NOT NULL,
    reason VARCHAR2(60) CONSTRAINT assocBl_reason_nn NOT NULL,
    active VARCHAR2(10),
    reportDate DATE NOT NULL,
    CONSTRAINT pk_associationBlacklistReport PRIMARY KEY (id),
    CONSTRAINT fk_assocBl_association FOREIGN KEY(idAssociation) REFERENCES association(id)
);
