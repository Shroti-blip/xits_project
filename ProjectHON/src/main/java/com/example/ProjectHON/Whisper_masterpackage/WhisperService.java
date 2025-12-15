package com.example.ProjectHON.Whisper_masterpackage;

import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WhisperService {

    @Autowired
    private WhisperRepository whisperRepository;

    @Autowired
    private UserMasterRepository userRepo;


    public WhisperMaster saveWhisper(String senderUsername, String receiverUsername, String content, LocalDateTime dateTime) {

        UserMaster sender = userRepo.findByUsername(senderUsername).orElse(null);
        UserMaster receiver = userRepo.findByUsername(receiverUsername).orElse(null);

        WhisperMaster w = new WhisperMaster(sender, receiver, content, dateTime);

        return whisperRepository.save(w);
    }

    public WhisperMaster saveWhisper(String senderName, String receiverName, String content, byte[] img, LocalDateTime dateTime, Boolean seen){
        UserMaster sender = userRepo.findByUsername(senderName).orElse(null);
        UserMaster receiver = userRepo.findByUsername(receiverName).orElse(null);

        WhisperMaster w = new WhisperMaster(sender, receiver, content, img, seen, dateTime);
        System.out.println("Inside WhisperService saveWhisper");
        return whisperRepository.save(w);
    }



}
