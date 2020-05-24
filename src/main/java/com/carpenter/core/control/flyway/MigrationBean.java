package com.carpenter.core.control.flyway;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.configuration.FluentConfiguration;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.Map;

@Singleton
@Startup
@Slf4j
@TransactionManagement(TransactionManagementType.BEAN)
public class MigrationBean {

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void init() {
        long start = System.nanoTime();
        log.info("Flyway migration started: {}", start);
        Map<String, Object> properties = entityManager.getEntityManagerFactory().getProperties();
        Object datasourceObject = properties.get("hibernate.connection.datasource");

        if (!(datasourceObject instanceof DataSource)) {
            log.error("Cannot get datasource, {}", datasourceObject);
            return;
        }

        DataSource dataSource = (DataSource) datasourceObject;

        Flyway flyway = new Flyway(
                new FluentConfiguration()
                        .table("_FLYWAY")
                        .dataSource(dataSource)
                        .locations("classpath:db/migration")
                        .baselineOnMigrate(true)
        );
        flyway.migrate();
        MigrationInfo migrationInfo = flyway.info().current();

        if (migrationInfo == null) {
            log.info("There is no existing database at actual datasource");
        } else {
            log.info("Found the database with the version: {}", migrationInfo.getVersion());
            log.info("Migration finished in: {}", (System.nanoTime() - start) / 1000);
        }
    }
}
