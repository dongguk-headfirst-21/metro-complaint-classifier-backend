package edu.dongguk.complaint.orchestrator.service.sse;

import edu.dongguk.complaint.orchestrator.domain.file.FileStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseEmitterService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe() {
        String id = UUID.randomUUID().toString();
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(id, emitter);
        emitter.onCompletion(() -> emitters.remove(id));
        emitter.onTimeout(() -> emitters.remove(id));
        return emitter;
    }

    public void notify(Long fileId, FileStatus status) {
        Map<String, Object> data = Map.of("fileId", fileId, "status", status);
        emitters.forEach((id, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name("file-status").data(data));
            } catch (IOException e) {
                emitters.remove(id);
            }
        });
    }
}