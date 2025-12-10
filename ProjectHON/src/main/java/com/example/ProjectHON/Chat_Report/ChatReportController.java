package com.example.ProjectHON.Chat_Report;


import com.example.ProjectHON.Whisper_masterpackage.WhisperMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ChatReportController {

    @Autowired
    ChatReportRepository chatReportRepository;

    @Autowired
    ChatReportService chatReportService;


    @PostMapping("/user/reportChat")
    public String saveData(@RequestParam("reason") String reason ,
                           @RequestParam("comment") String comment,
                           @RequestParam("reporterId") Long reporterId ,
                           @RequestParam("whisperId") Long whisperId,
                           @RequestParam("ownerId") Long ownerId,
                           RedirectAttributes redirectAttributes,
                           Model model){
       System.out.println("------inside reportChat mapping-------");
       chatReportService.saveReportInfo(reason , comment ,ownerId,reporterId, whisperId);
//       model.addAttribute("success" ,"Successfully Reported.");
        redirectAttributes.addFlashAttribute("success", "Successfully Reported.");

        return "redirect:/user/whisper";
    }


}
