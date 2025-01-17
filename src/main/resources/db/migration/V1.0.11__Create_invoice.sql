CREATE TABLE IF NOT EXISTS INVOICES
(
    ID                  BIGINT AUTO_INCREMENT,
    CREATE_DATE         DATETIME,
    CREATE_BY           VARCHAR(256),
    DELETE_DATE         DATETIME,
    DELETED_BY          VARCHAR(256),
    NUMBER_OF_INVOICE   VARCHAR(10),
    EMPLOYEE_ID         BIGINT,
    CLIENT_ID           BIGINT,
    INVOICE_AMOUNT_TYPE VARCHAR(32),
    NET_VALUE           DECIMAL(13, 2),
    GROSS_VALUE         DECIMAL(13, 2),
    INVOICE_TYPE        VARCHAR(32),
    DESCRIPTION         VARCHAR(512),
    VAT_RATE            DECIMAL(13, 2),
    PAYMENT_TYPE        VARCHAR(56),
    PAYMENT_DUE         DATETIME,
    PLACE_OF_CREATION VARCHAR(128),

    CONSTRAINT FK_EMPLOYEE_ID_INVOICES FOREIGN KEY (EMPLOYEE_ID) REFERENCES EMPLOYEES (ID),
    CONSTRAINT FK_CLIENT_ID_INVOICES FOREIGN KEY (CLIENT_ID) REFERENCES CLIENTS (ID),

    PRIMARY KEY (ID)
) CHARACTER
      SET utf8
  DEFAULT COLLATE utf8_unicode_ci;