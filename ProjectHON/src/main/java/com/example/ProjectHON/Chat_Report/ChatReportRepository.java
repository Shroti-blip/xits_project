package com.example.ProjectHON.Chat_Report;

import org.springframework.data.jpa.repository.JpaRepository;
import software.amazon.awssdk.services.rekognition.endpoints.internal.Value;

public interface ChatReportRepository extends JpaRepository<ChatReport , Integer> {
}
