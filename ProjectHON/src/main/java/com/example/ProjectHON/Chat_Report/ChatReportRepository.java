package com.example.ProjectHON.Chat_Report;

import com.example.ProjectHON.Whisper_masterpackage.WhisperMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import software.amazon.awssdk.services.rekognition.endpoints.internal.Value;

public interface ChatReportRepository extends JpaRepository<ChatReport , Integer> {
    @Query("SELECT COUNT(c) FROM ChatReport c WHERE c.owner.userId = :ownerId")
    int countTotalReportsForUser(@Param("ownerId") Long ownerId);


//    @Query("SELECT c.reason FROM ChatReport c WHERE c.whisper.whisperId = :whisperId")
//    String findReasonByWhisperId(@Param("whisperId") Long whisperId);


}
