DROP PROCEDURE IF EXISTS addContractColumn;

DELIMITER //
CREATE PROCEDURE addContractColumn()
BEGIN
    SELECT COUNT(*)
    INTO @EXISTS
    FROM information_schema.COLUMNS
    WHERE TABLE_NAME = 'EMPLOYERS'
      AND COLUMN_NAME = 'CONTRACT';

    IF @EXISTS = 0 THEN
        ALTER TABLE EMPLOYERS
            ADD COLUMN CONTRACT VARCHAR(56);

        UPDATE EMPLOYERS
        SET CONTRACT = 'SELF_EMPLOYMENT'
        WHERE SELF_EMPLOYMENT = true;

        ALTER TABLE EMPLOYERS
            DROP COLUMN SELF_EMPLOYMENT;

    ELSE
        SET @QUERY = CONCAT('SELECT \'In EMPLOYERS - column CONTRACT already exists\' status ');
        PREPARE stmt FROM @QUERY;
        EXECUTE stmt;
    END IF;

END //

DELIMITER ;

CALL addContractColumn();