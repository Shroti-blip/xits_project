package com.example.ProjectHON.Chat_Report;

import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import com.example.ProjectHON.Whisper_masterpackage.WhisperMaster;
import com.example.ProjectHON.Whisper_masterpackage.WhisperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatReportService {

    @Autowired
    UserMasterRepository userMasterRepository;

    @Autowired
    WhisperRepository whisperRepository;

    @Autowired
    ChatReportRepository chatReportRepository;

//   for saving data.
        public ChatReport saveReportInfo(String reason ,String comment ,
                                   Long ownerId, Long reporterId , Long whisperId){

            UserMaster reporter = userMasterRepository.findById(reporterId).orElse(null);
            UserMaster owner = userMasterRepository.findById(ownerId).orElse(null);
            WhisperMaster whisperMaster = whisperRepository.findById(whisperId).orElse(null);

          ChatReport c = new ChatReport();
          c.setReason(reason);
          c.setComment(comment);
          c.setDateTime(LocalDateTime.now());
          c.setOwner(owner);
          c.setReporter(reporter);
          c.setWhisperMaster(whisperMaster);
            System.out.println("-------inside saveReport Info service method -----------");
            return chatReportRepository.save(c);
        }

}
