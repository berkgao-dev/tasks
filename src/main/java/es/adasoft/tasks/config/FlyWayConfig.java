package es.adasoft.tasks.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FlyWayConfig {
    @Value("${spring.flyway.enabled}")
    @Bean(initMethod = "migrate")
    @ConditionalOnProperty(name = "spring.flyway.enabled", havingValue = "true")
    public Flyway flyway(){
        return Flyway.configure()
                .baselineOnMigrate(true)
                .dataSource("jdbc:h2:mem:tasks", "sa", "password")
                .locations("classpath:db/migration")
                .load();
    }
}