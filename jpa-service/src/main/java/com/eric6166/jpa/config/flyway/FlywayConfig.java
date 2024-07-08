package com.eric6166.jpa.config.flyway;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.flyway.enabled", havingValue = "true")
public class FlywayConfig {

    private static final String SCHEMA_HISTORY = "schema_history";
    private static final String FLYWAY_BASELINE_VERSION_DEFAULT = "0.0";
    private static final String FLYWAY_LOCATION_DEFAULT = "classpath:db/migration/";

    private final FlywayProps flywayProps;

    @Bean
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .baselineOnMigrate(flywayProps.getBaselineOnMigrate())
                .baselineVersion(MigrationVersion.fromVersion(
                        Optional.ofNullable(flywayProps.getBaselineVersion())
                                .orElse(FLYWAY_BASELINE_VERSION_DEFAULT)))
                .table(Optional.ofNullable(flywayProps.getTable())
                        .orElse(SCHEMA_HISTORY))
                .locations(Optional.ofNullable(flywayProps.getLocations())
                        .orElse(FLYWAY_LOCATION_DEFAULT))
                .validateOnMigrate(Optional.ofNullable(flywayProps.getValidateOnMigrate())
                        .orElse(Boolean.TRUE))
                .load();
    }

    @Bean
    public FlywayMigrationInitializer flywayMigrationInitializer(Flyway flyway) {
        return new FlywayMigrationInitializer(flyway);
    }
}
