package edu.dongguk.complaint.orchestrator.global.config;

import edu.dongguk.complaint.orchestrator.infra.EmbeddingTriggerCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.flyway.autoconfigure.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FlywayConfig {

    private final EmbeddingTriggerCallback embeddingTriggerCallback;

    @Bean
    public FlywayConfigurationCustomizer flywayConfigCustomizer() {
        return config -> config.callbacks(embeddingTriggerCallback);
    }
}