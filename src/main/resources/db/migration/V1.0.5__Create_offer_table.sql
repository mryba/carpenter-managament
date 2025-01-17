CREATE TABLE IF NOT EXISTS OFFERS
(
    ID                 BIGINT AUTO_INCREMENT,
    CREATE_DATE        DATETIME,
    CREATE_BY            VARCHAR(256),
    DELETE_DATE        DATETIME,
    DELETED_BY          VARCHAR(256),
    ARCHITECTURE_TYPE  VARCHAR(56),
    BUILDING_DIMENSION DOUBLE,
    EMAIL              VARCHAR(256),
    IS_READ            BIT(1),
    PHONE              VARCHAR(16),
    WORK_CITY          VARCHAR(64),
    WORK_DATE_FROM     DATETIME,
    WORK_DATE_TO       DATETIME,
    COMPANY_ID         BIGINT,

    CONSTRAINT FK_COMPANY_ID_OFFERS FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY (ID),

    PRIMARY KEY (ID)
) CHARACTER
      SET utf8
  DEFAULT COLLATE utf8_unicode_ci;