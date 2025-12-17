package com.example.ProjectHON.AdminReportPackage;


import com.example.ProjectHON.Chat_Report.ChatReport;
import com.example.ProjectHON.Chat_Report.ChatReportRepository;
import com.example.ProjectHON.User_Report.UserReport;
import com.example.ProjectHON.User_Report.UserReportRepository;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class AdminReportController {


    @Autowired
    AdminReportRepository adminReportRepository;

    @Autowired
    UserReportRepository userReportRepository;

    @Autowired
    ChatReportRepository chatReportRepository;

    @Autowired
    UserMasterRepository userMasterRepository;

    @Autowired
    AdminEmailService adminEmailService;

    @GetMapping("/user/adminReportPage")
    public String adminReportPage(Model model){

       long userReport =  userReportRepository.count();
//        System.out.println("---------total user reports are-------" +userReport );

        long chatReport = chatReportRepository.count();
//        System.out.println("-------------total number of chatReport---------" + chatReport);

        List<UserReport> reportedUserList = userReportRepository.findAll();
        List<ChatReport> reportedChatList = chatReportRepository.findAll();


        model.addAttribute("userReport" , userReport);
        model.addAttribute("chatReport" , chatReport);
        model.addAttribute("reportedUserList" ,reportedUserList);
        model.addAttribute("reportedChatList" , reportedChatList);

        return "MergePart/report_dashboard_admin";
    }


    @GetMapping("/user/reportReview/{reporterId}/{ownerId}/{reportId}")
    public String reportReview(Model model ,
                               @PathVariable Long reporterId ,
                               @PathVariable Long ownerId ,
                               @PathVariable Long reportId ,
                               HttpSession session){

        UserMaster reporter = userMasterRepository.findById(reporterId).orElse(null);
        UserMaster owner = userMasterRepository.findById(ownerId).orElse(null);
        UserReport report = userReportRepository.findById(reportId).orElse(null);

//        System.out.println("=========value of reporter name and id is : " + reporter.getUsername() +" , " + reporter.getUserId());
//        System.out.println("========value of owner name and id is : " + owner.getUsername() + " , " + owner.getUserId());
//        System.out.println("=========value of reportID is : " + report.getId());
        System.out.println("=====value of DateTime is : " + report.getDateTime());
//        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        int totalCount = userReportRepository.countTotalReportsForUser(ownerId);
         System.out.println("====Total reports for that user is : " + totalCount);

        model.addAttribute("reporter" , reporter);
        model.addAttribute("owner" , owner);
        model.addAttribute("report" , report);
        model.addAttribute("totalCount" , totalCount);

        session.setAttribute("owner" , owner);
        session.setAttribute("reporter" , reporter);

        return "MergePart/report_review_admin";
    }

    @PostMapping("/warningMail")
    public String sendWarningMail(HttpSession session , RedirectAttributes redirectAttributes){

       UserMaster owner = (UserMaster) session.getAttribute("owner" );
        UserMaster reporter = (UserMaster) session.getAttribute("reporter");
        try{
            adminEmailService.sendLastWarningMail(owner.getEmail());
        }catch (Exception e){
            System.out.println("--------Inside last email exception." + e.getMessage());
        }

        redirectAttributes.addFlashAttribute("warningSuccess" , true);


//        adminEmailService.sendLastWarningMail(email);
        return "redirect:/user/adminReportPage";//success popup() + confirmation popup(done)
    }

    @PostMapping("/rejectReportWarning")
    public String rejectReportWarning(HttpSession session , RedirectAttributes redirectAttributes){

        UserMaster owner = (UserMaster) session.getAttribute("owner" );//multi user?
        UserMaster reporter = (UserMaster) session.getAttribute("reporter");

        userReportRepository.deleteFromUserReport(owner.getUserId() , reporter.getUserId());
        System.out.println("---successfully deleted -----");

        redirectAttributes.addFlashAttribute("successMsgReport" , true);

        return "redirect:/user/adminReportPage";//success popup + before confirmation popup(with info)
    }

    @PostMapping("/blockReportedUser")
    public String blockReportedUser(HttpSession session){
        UserMaster owner = (UserMaster) session.getAttribute("owner" );
        owner.setAccountStatus(UserMaster.AccountStatus.BLOCKED);
        userMasterRepository.save(owner);

        return "redirect:/user/adminReportPage";//success popup + before confirmation popup(with info)
    }

    @GetMapping("/user/blockeduser")
    public String blockedUser(){
        return "MergePart/blockeduser";
    }
}


//warn user -> confirm popup + success popup