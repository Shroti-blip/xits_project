package com.example.ProjectHON.GroupChatReport;


import com.example.ProjectHON.Group.GroupChatMaster;
import com.example.ProjectHON.Group.GroupChatRepository;
import com.example.ProjectHON.Group.GroupMaster;
import com.example.ProjectHON.Group.GroupMasterRepository;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class GroupChatReportController {


    @Autowired
    GroupChatReportRepository groupChatReportRepository;

    @Autowired
    UserMasterRepository userMasterRepository;

    @Autowired
    GroupChatRepository groupChatRepository;

    @Autowired
    GroupMasterRepository groupMasterRepository;

    @PostMapping("/user/report/group/chat")
    public String groupChatReport(@RequestParam("reason") String reason,
                                  @RequestParam("comment") String comment,
                                  @RequestParam("ownerId") Long ownerId,
                                  @RequestParam("reporterId") Long reporterId,
                                  @RequestParam("whisperId") Long whisperId ,
                                  @RequestParam("groupId") Long circleId,
                                  RedirectAttributes redirectAttributes){

        System.out.println("----------------inside group chat report-----------------");

        UserMaster owner =  userMasterRepository.findById(ownerId).orElse(null);
        UserMaster reporter = userMasterRepository.findById(reporterId).orElse(null);
        GroupChatMaster groupChatMaster = groupChatRepository.findById(whisperId).orElse(null);
        GroupMaster circle = groupMasterRepository.findById(circleId).orElse(null);
        System.out.println("---owner "+  owner.getUserId()+" and reporter id " +reporter.getUserId());


        if(owner==null && reporter==null && groupChatMaster==null && circle==null){
            return "redirect:/login";
        }
        GroupChatReportEntity groupChatReport = new GroupChatReportEntity();
        groupChatReport.setReason(reason);
        groupChatReport.setComment(comment);
        groupChatReport.setDateTime(LocalDateTime.now());
        groupChatReport.setReporter(reporter);
        groupChatReport.setOwner(owner);
        groupChatReport.setGroupChatMaster(groupChatMaster);
        groupChatReport.setGroupMaster(circle);
        groupChatReportRepository.save(groupChatReport);
        System.out.println("------data submit----------");
        redirectAttributes.addFlashAttribute("success" ,true);

        return "redirect:/user/groupchat/"+circleId;
    }
}
