CREATE TABLE IF NOT EXISTS ADDRESSES
(
    ID            BIGINT AUTO_INCREMENT,
    CREATE_DATE   DATETIME,
    CREATE_BY       VARCHAR(256),
    DELETE_DATE   DATETIME,
    DELETED_BY     VARCHAR(256),
    CITY          VARCHAR(40),
    POSTAL_CODE   VARCHAR(10),
    STREET        VARCHAR(100),
    STREET_NUMBER VARCHAR(30),
    HOUSE_NUMBER  VARCHAR(30),
    COUNTRY       VARCHAR(64),
    EMPLOYEE_ID   BIGINT,

    CONSTRAINT FK_EMPLOYEE_ID_ADDRESSES FOREIGN KEY (EMPLOYEE_ID) REFERENCES EMPLOYEES (ID),

    PRIMARY KEY (ID)
) CHARACTER
      SET utf8
  DEFAULT COLLATE utf8_unicode_ci;