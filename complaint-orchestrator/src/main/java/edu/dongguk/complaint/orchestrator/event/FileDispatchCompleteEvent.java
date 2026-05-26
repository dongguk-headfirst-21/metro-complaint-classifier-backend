package edu.dongguk.complaint.orchestrator.event;

import edu.dongguk.complaint.orchestrator.domain.file.FileStatus;

public record FileDispatchCompleteEvent(Long fileId, FileStatus status) {}
