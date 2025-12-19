package com.example.ProjectHON.Chat_Report;


import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import com.example.ProjectHON.Whisper_masterpackage.WhisperMaster;
import com.example.ProjectHON.Whisper_masterpackage.WhisperRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import software.amazon.awssdk.services.rekognition.endpoints.internal.Value;

@Controller
public class ChatReportController {

    @Autowired
    ChatReportRepository chatReportRepository;

    @Autowired
    ChatReportService chatReportService;

    @Autowired
    ChatReportEmailService chatReportEmailService;

    @Autowired
    UserMasterRepository userMasterRepository;

    @Autowired
    WhisperRepository whisperRepository;

    @PostMapping("/user/reportChat")
    public String saveData(@RequestParam("reason") String reason ,
                           @RequestParam("comment") String comment,
                           @RequestParam("reporterId") Long reporterId ,
                           @RequestParam("whisperId") Long whisperId,
                           @RequestParam("ownerId") Long ownerId,
                           RedirectAttributes redirectAttributes,
                           Model model){
       System.out.println("------inside reportChat mapping-------");
       UserMaster owner = userMasterRepository.findById(ownerId).orElse(null);
       chatReportService.saveReportInfo(reason , comment ,ownerId,reporterId, whisperId);
//       model.addAttribute("success" ,"Successfully Reported.");//use session.

        int totalCount = chatReportRepository.countTotalReportsForUser(ownerId);
        System.out.println("--------totalCount of that particular user is : " + totalCount);
        if(totalCount>3){
            try{
                chatReportEmailService.sendAfterGettingThreeReports(owner.getEmail());

            }catch (Exception e){
                System.out.println("------Exception is in Email Sending--------");
            }
        }else{
            try{
                chatReportEmailService.sendAfterGettingReportChat(owner.getEmail());
            }catch (Exception e){
                System.out.println("------Exception is in Email Sending--------");
            }
        }


        redirectAttributes.addFlashAttribute("success", "Successfully Reported.");

        return "redirect:/user/whisper";
    }



    @GetMapping("/user/chatreport/{receiverId}/{ownerId}/{whisperId}/{chatReportId}")
    public String getReportChatPage(@PathVariable("receiverId") Long receiverId ,
                                    @PathVariable("ownerId") Long ownerId ,
                                    @PathVariable("whisperId") Long whisperId,
                                    @PathVariable("chatReportId") Integer chatReportId,
                                    Model model , HttpSession session){

         UserMaster receiver = userMasterRepository.findById(receiverId).orElse(null);
         UserMaster owner = userMasterRepository.findById(ownerId).orElse(null);
         WhisperMaster whisperMaster = whisperRepository.findById(whisperId).orElse(null);
         ChatReport chatReport = chatReportRepository.findById(chatReportId).orElse(null);

//         String chat  = chatReportRepository.findReasonByWhisperId(whisperMaster.getWhisperId());
//         System.out.println("chat : "+chat);
         System.out.println("ReceiverID  , ownerID , WhisperId : " + receiver.getUserId() +","+owner.getUserId() +" , " +whisperMaster.getWhisperId());

         chatReport.setStatus(ChatReport.ReportStatus.IN_REVIEW);
         chatReportRepository.save(chatReport);
         Integer totalChatReport = chatReportRepository.countTotalReportsForUser(owner.getUserId());
         System.out.println("-----total count----"+totalChatReport);

         model.addAttribute("receiver" , receiver);
         model.addAttribute("owner", owner);
         model.addAttribute("whisperMaster" , whisperMaster);
         model.addAttribute("chatReport" , chatReport);
         model.addAttribute("totalChatReports" ,totalChatReport);

         System.out.println("-----whisper -----" + whisperMaster.getWhisper());
         session.setAttribute("receiver",receiver);
         session.setAttribute("owner" , owner);
         session.setAttribute("whisperMaster", whisperMaster);
         session.setAttribute("chatReport",chatReport);
//         model.addAttribute("chat" , chat);


        return "MergePart/chat_report_review_admin";
    }


    @PostMapping("/chatwarningmail")
    public String sendChatWarningMail(HttpSession session , RedirectAttributes redirectAttributes){

        UserMaster receiver = (UserMaster) session.getAttribute("receiver");
        UserMaster owner = (UserMaster) session.getAttribute("owner");
        WhisperMaster whisperMaster = (WhisperMaster) session.getAttribute("whisperMaster");
        ChatReport chatReport = (ChatReport)session.getAttribute("chatReport");


        chatReport.setStatus(ChatReport.ReportStatus.ACTION_TAKEN);
        chatReportRepository.save(chatReport);
        try{
            chatReportEmailService.sendLastWarningMail(owner.getEmail());

        }catch (Exception e){
            System.out.println("----email exception---"+e.getMessage());
        }
        redirectAttributes.addFlashAttribute("warningSuccess" , true);


        return "redirect:/user/adminReportPage";
    }

    @PostMapping("/rejectchatreport")
    public String rejectChatReport(HttpSession session , RedirectAttributes redirectAttributes){

        UserMaster receiver = (UserMaster) session.getAttribute("receiver");
        UserMaster owner = (UserMaster) session.getAttribute("owner");
        WhisperMaster whisperMaster = (WhisperMaster) session.getAttribute("whisperMaster");
        ChatReport chatReport = (ChatReport)session.getAttribute("chatReport");

        chatReport.setStatus(ChatReport.ReportStatus.REJECTED);
        chatReportRepository.save(chatReport);

        redirectAttributes.addFlashAttribute("successMsgReport" , true);

        return "redirect:/user/adminReportPage";
    }



}
