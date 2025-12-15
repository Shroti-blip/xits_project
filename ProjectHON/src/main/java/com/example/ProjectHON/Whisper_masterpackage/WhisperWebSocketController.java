package com.example.ProjectHON.Whisper_masterpackage;

import com.example.ProjectHON.BlockUser.BlockUserRepository;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import com.example.ProjectHON.mutualCrushPackage.MutualCrushRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
public class WhisperWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private WhisperService whisperService;

    @Autowired
    private UserMasterRepository userRepo;

    @Autowired
    private WhisperRepository whisperRepo;

    @Autowired
    private MutualCrushRepository mutualRepo;

    @Autowired
    private BlockUserRepository blockUserRepository;


    // Chat Page
    @GetMapping("/whisper/{receiverId}")
    public String chatPage(@PathVariable Long receiverId, HttpSession session, Model model) {

        Long userId = (Long) session.getAttribute("user_id");

        UserMaster sender = userRepo.findById(userId).orElse(null);
        if (sender == null) return "redirect:/login";

        UserMaster receiver = userRepo.findById(receiverId).orElse(null);

        String profileImageDataUrl = "";
        if (receiver.getProfilePhoto() != null && receiver.getProfilePhoto().length > 0) {
            String base64 = Base64.getEncoder().encodeToString(receiver.getProfilePhoto());
            profileImageDataUrl = "data:image/jpeg;base64," + base64;
        }

//        for block user
//        Boolean isBlocked  =  blockUserRepository.existsByBlocker_UserIdAndBlockedUser_UserId(
//                sender.getUserId() , receiver.getUserId());


        Boolean senderBlockedReceiver  =  blockUserRepository.existsByBlocker_UserIdAndBlockedUser_UserId(
               sender.getUserId() , receiver.getUserId());

        Boolean receiverBlockedSender = blockUserRepository.existsByBlocker_UserIdAndBlockedUser_UserId(receiverId , sender.getUserId());

        boolean isBlocked = senderBlockedReceiver || receiverBlockedSender;

//        boolean isBlocked = senderBlockedReceiver;
//        boolean getBlocked = receiverBlockedSender;

        model.addAttribute("isBlocked", isBlocked);
//        model.addAttribute("getBlocked" , getBlocked);
        model.addAttribute("receiverImage",profileImageDataUrl);
        model.addAttribute("sender", sender);
        model.addAttribute("receiver", receiver);

//        List<WhisperMaster> messages =
//                whisperRepo.getConversation(sender.getUserId(), receiverId);

        Optional mutual = mutualRepo.findByUserAsMutual(sender,receiver); // MC hai ya nahi

        long min = 2L; // 5 minutes

        if(mutual.isPresent())
            min = 10L;  // MC ko extra time dediya;

        long ms = Duration.ofMinutes(min).toMillis(); // minutes to milliseconds


        LocalDateTime cutoff = LocalDateTime.now().minus(Duration.ofMillis(ms)); // time difference

        List<WhisperMaster> messages =
                whisperRepo.getRecentConversation(sender.getUserId(), receiverId, cutoff);

        model.addAttribute("messages", messages);
        model.addAttribute("wt",ms);

        return "MergePart/whisper";
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload WhisperMessageDTO dto) {

        WhisperMaster saved = whisperService.saveWhisper(
                userRepo.findById(dto.getSenderId()).get().getUsername(),
                userRepo.findById(dto.getReceiverId()).get().getUsername(),
                dto.getContent(),
                dto.getImageBase64() == null ? null : Base64.getDecoder().decode(dto.getImageBase64()),
                LocalDateTime.now(),
                false
        );


        WhisperMessageDTO outgoing = new WhisperMessageDTO(
                saved.getSentBy().getUserId(),
                saved.getSentTo().getUserId(),
                saved.getWhisper(),
                saved.getImage() != null ? Base64.getEncoder().encodeToString(saved.getImage()) : null ,
                saved.getSentAt()
        );

        messagingTemplate.convertAndSend(
                "/topic/inbox/" + dto.getReceiverId(),
                outgoing
        );
    }

    // Image endpoint to serve images separately
    @GetMapping("/whisper/image/{whisperId}")
    @ResponseBody
    public ResponseEntity<byte[]> getWhisperImage(@PathVariable Long whisperId, HttpSession session) {

        // Security check - user has access to this whisper
        Long userId = (Long) session.getAttribute("user_id");
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<WhisperMaster> whisperRecord = whisperRepo.findById(whisperId);

        if (whisperRecord.isPresent()) {
            WhisperMaster whisper = whisperRecord.get();

            // Check if user is sender or receiver
            if (!whisper.getSentBy().getUserId().equals(userId) &&
                    !whisper.getSentTo().getUserId().equals(userId)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            if (whisper.getImage() != null && whisper.getImage().length > 0) {
                byte[] image = whisper.getImage();

                HttpHeaders headers = new HttpHeaders();
                // Try to detect image type, default to JPEG
                headers.setContentType(MediaType.IMAGE_JPEG);
                headers.setContentLength(image.length);
                headers.setCacheControl("max-age=3600");

                return new ResponseEntity<>(image, headers, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @MessageMapping("/sendMessage")
//    public void sendMessage(@Payload WhisperMessageDTO dto) {
//
//        WhisperMaster whisper = new WhisperMaster();
//        whisper.setSentBy(userRepo.findById(dto.getSenderId()).orElse(null));
//        whisper.setSentTo(userRepo.findById(dto.getReceiverId()).orElse(null));
//        whisper.setSentAt(LocalDateTime.now());
//        whisper.setSeen(false);
//
//        if (dto.getContent() != null && !dto.getContent().isEmpty())
//            whisper.setWhisper(dto.getContent()); // encrypted automatically
//
//        if (dto.getImageBase64() != null && !dto.getImageBase64().isEmpty())
//            whisper.setImage(Base64.getDecoder().decode(dto.getImageBase64()));
//
//        WhisperMaster saved = whisperRepo.save(whisper);
//
//        dto.setWhisperId(saved.getWhisperId());
//        dto.setSentAt(saved.getSentAt());
//
//        messagingTemplate.convertAndSend("/topic/inbox/" + dto.getReceiverId(), dto);
//    }

    @MessageMapping("/deleteMessage")
    public void deleteMessage(@Payload WhisperMessageDTO dto) {
        whisperRepo.deleteById(dto.getWhisperId());

        messagingTemplate.convertAndSend("/topic/delete/" + dto.getReceiverId(), dto);
        messagingTemplate.convertAndSend("/topic/delete/" + dto.getSenderId(), dto);
    }


}



