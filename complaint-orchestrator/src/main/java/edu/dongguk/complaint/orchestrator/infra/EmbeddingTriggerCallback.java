package edu.dongguk.complaint.orchestrator.infra;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.callback.Context;
import org.flywaydb.core.api.callback.Event;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmbeddingTriggerCallback implements Callback {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private boolean v15WasApplied = false;

    @Override
    public boolean supports(Event event, Context context) {
        return event == Event.AFTER_EACH_MIGRATE || event == Event.AFTER_MIGRATE;
    }

    @Override
    public boolean canHandleInTransaction(Event event, Context context) {
        return false;
    }

    @Override
    public void handle(Event event, Context context) {
        if (event == Event.AFTER_EACH_MIGRATE) {
            MigrationInfo info = context.getMigrationInfo();
            if ("15".equals(info.getVersion().getVersion())) {
                v15WasApplied = true;
            }
        }

        if (event == Event.AFTER_MIGRATE && v15WasApplied) {
            kafkaTemplate.send("embedding-trigger", "start");
        }
    }

    @Override
    public String getCallbackName() {
        return EmbeddingTriggerCallback.class.getSimpleName();
    }
}
