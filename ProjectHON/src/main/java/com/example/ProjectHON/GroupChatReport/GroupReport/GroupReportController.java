package com.example.ProjectHON.GroupChatReport.GroupReport;

import com.example.ProjectHON.Group.GroupMaster;
import com.example.ProjectHON.Group.GroupMasterRepository;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class GroupReportController {

    @Autowired
    GroupReportRepository groupReportRepository;

    @Autowired
    UserMasterRepository userMasterRepository;

    @Autowired
    GroupMasterRepository groupMasterRepository;


    @PostMapping("/user/report/group")
    public String groupReport(@RequestParam String reason,
                              @RequestParam String comment ,
                              @RequestParam("groupId") Long groupId ,
                              @RequestParam("reporterId") Long reporterId,
                              RedirectAttributes redirectAttributes){

        System.out.println("=====inside group report mapping======");
         UserMaster reporter = userMasterRepository.findById(reporterId).orElse(null);
         GroupMaster group = groupMasterRepository.findById(groupId).orElse(null);
            System.out.println("reporter id : " + reporter.getUserId() + " , group admin name : " +group.getGroupAdmin().getUsername());
             if(reporter==null || group==null){
             return "redirect:/login";
              }

        GroupReport groupReport = new GroupReport();
        groupReport.setReason(reason);
        groupReport.setComment(comment);
        groupReport.setReporter(reporter);
        groupReport.setGroupMaster(group);
        groupReport.setDateTime(LocalDateTime.now());
        groupReport.setStatus(GroupReport.ReportStatus.PENDING);

        groupReportRepository.save(groupReport);
        System.out.println("------------data saved.(group report)----------");
        redirectAttributes.addFlashAttribute("groupReported" , true);

        return "redirect:/user/groupchat/"+groupId;
    }


}
