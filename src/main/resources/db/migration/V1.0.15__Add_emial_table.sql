CREATE TABLE IF NOT EXISTS EMAIL_STORAGE
(
    ID          BIGINT AUTO_INCREMENT,
    CREATE_DATE DATETIME,
    CREATE_BY   VARCHAR(128),
    DELETE_DATE DATETIME,
    DELETED_BY  VARCHAR(128),
    RECIPIENTS  VARCHAR(256),
    SUBJECT     VARCHAR(256),
    CONTENT     VARCHAR(9000),
    SENT        BIT(1),
    COMMIT_DATE DATETIME,
    STATUS      VARCHAR(16),
    ATTEMPTS    INTEGER,

    PRIMARY KEY (ID)
) CHARACTER
      SET utf8
  DEFAULT COLLATE utf8_unicode_ci;
