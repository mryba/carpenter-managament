CREATE TABLE IF NOT EXISTS WORKING_DAYS
(
    ID          BIGINT AUTO_INCREMENT,
    CREATE_DATE DATETIME,
    CREATE_BY   VARCHAR(128),
    DELETE_DATE DATETIME,
    DELETED_BY  VARCHAR(128),
    HOURS       INT,
    CLIENT_ID   BIGINT,

    CONSTRAINT FK_CLIENT_WORKING_DAY FOREIGN KEY (CLIENT_ID) REFERENCES CLIENTS (ID),
    PRIMARY KEY (ID)
);



CREATE TABLE IF NOT EXISTS EMPLOYEES_DAYS
(
    WORKING_DAY_ID BIGINT,
    EMPLOYEE_ID    BIGINT,

    CONSTRAINT FK_WORKING_DAY_EMPLOYEES_DAYS FOREIGN KEY (WORKING_DAY_ID) REFERENCES WORKING_DAYS (ID),
    CONSTRAINT FK_EMPLOYEE_ID_EMPLOYEES_DAYS FOREIGN KEY (EMPLOYEE_ID) REFERENCES EMPLOYEES (ID),


    PRIMARY KEY (WORKING_DAY_ID, EMPLOYEE_ID)
)

