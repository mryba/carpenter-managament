CREATE TABLE IF NOT EXISTS EMPLOYEES
(
    ID                  BIGINT AUTO_INCREMENT,
    CREATE_DATE         DATETIME,
    CREATE_BY             VARCHAR(256),
    DELETE_DATE         DATETIME,
    DELETED_BY           VARCHAR(256),
    FIRST_NAME          VARCHAR(50),
    LAST_NAME           VARCHAR(50),
    EMAIL               VARCHAR(256) UNIQUE,
    PASSWORD            VARCHAR(256),
    NIP_NUMBER          VARCHAR(10),
    BANK_ACCOUNT_NUMBER VARCHAR(35),
    BIRTH_DATE          DATETIME,
    GENDER              VARCHAR(10),
    PHONE_NUMBER        VARCHAR(15),
    CONTRACT            VARCHAR(56),
    COMPANY_ID          BIGINT,
    ACCOUNT_ACTIVE      BIT(1),

    CONSTRAINT FK_COMPANY_ID_EMPLOYEES FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY (ID),

    PRIMARY KEY (ID)

) CHARACTER
      SET utf8
  DEFAULT COLLATE utf8_unicode_ci;