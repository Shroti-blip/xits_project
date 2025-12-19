package com.example.ProjectHON.User_Report;

import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserReportController {

    @Autowired
    UserReportService userReportService;

    @Autowired
    UserReportEmailService userReportEmailService;

    @Autowired
    UserMasterRepository userMasterRepository;

    @Autowired
    UserReportRepository userReportRepository;

    @PostMapping("/user/userReport")
    public String userReport(@RequestParam("reason") String reason,
                             @RequestParam("comment") String comment ,
                             @RequestParam("reporterId") Long reporterId,
                             @RequestParam("ownerId") Long ownerId,
                             Model model, RedirectAttributes redirectAttributes){

        System.out.println("-----------inside userReport mapping------------");

        UserMaster owner = userMasterRepository.findById(ownerId).orElse(null);

        userReportService.saveReport(reason , comment , reporterId , ownerId);
        owner.setAccountStatus(UserMaster.AccountStatus.REPORTED);
        userMasterRepository.save(owner);
        //for 3 counts logic.
        int totalReportCount = userReportRepository.countTotalReportsForUser(ownerId);
        System.out.println("-------total count is for user report is : " + totalReportCount);
        System.out.println("------ownerId " + ownerId +","+ owner.getUserId() + "," + reporterId);
        if(totalReportCount>3){
            try{
                owner.setReported(true);//updating entity obj.
                userMasterRepository.save(owner);//saving in db.
                System.out.println("----is reported or not : " + owner.getUserId());
                userReportEmailService.sendAfterUserGettingThreeReports(owner.getEmail());

            }catch (Exception e){
                System.out.println("----------Exception while sending last mail to reported user-----------");
            }
        }
        else{
            try{
                userReportEmailService.sendAfterUserGetReported(owner.getEmail());
            }catch (Exception e){
                System.out.println("----------Exception while sending mail to reported user-----------");
            }
        }
        redirectAttributes.addFlashAttribute("success", "Successfully Reported.");
        return "redirect:/user/whisper";
    }

    @GetMapping("/user/getAllReports")
    public String getAllReports(HttpSession session , Model model){

        Long reporterId = (Long)session.getAttribute("userId");
        List<UserReport> report = userReportRepository.findAllByReporterId(reporterId);

        model.addAttribute("userReportList" , report);

        System.out.println("current user reports : ===" + report.size());

        return "MergePart/showingAllUserReports";

    }

}
