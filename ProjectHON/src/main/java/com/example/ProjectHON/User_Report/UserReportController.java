package com.example.ProjectHON.User_Report;

import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserReportController {

    @Autowired
    UserReportService userReportService;

    @Autowired
    UserReportEmailService userReportEmailService;

    @Autowired
    UserMasterRepository userMasterRepository;

    @PostMapping("/user/userReport")
    public String userReport(@RequestParam("reason") String reason,
                             @RequestParam("comment") String comment ,
                             @RequestParam("reporterId") Long reporterId,
                             @RequestParam("ownerId") Long ownerId,
                             Model model, RedirectAttributes redirectAttributes){

        System.out.println("-----------inside userReport mapping------------");
        try{

        UserMaster owner = userMasterRepository.findById(ownerId).orElse(null);
        //for 3 counts logic. 

        userReportService.saveReport(reason , comment , reporterId , ownerId);
            userReportEmailService.sendAfterUserGetReported(owner.getEmail());
        }catch (Exception e){
            System.out.println("----------Exception while sending mail to reported user-----------");
        }
        redirectAttributes.addFlashAttribute("success" , "Successfully Reported.");
        return "redirect:/user/whisper";
    }
}
