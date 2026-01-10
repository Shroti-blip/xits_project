package com.example.ProjectHON.AdminCircleReport;

import com.example.ProjectHON.AdminReportPackage.AdminEmailService;
import com.example.ProjectHON.Group.*;
import com.example.ProjectHON.GroupChatReport.GroupChatReportEntity;
import com.example.ProjectHON.GroupChatReport.GroupChatReportRepository;
import com.example.ProjectHON.GroupChatReport.GroupReport.GroupReport;
import com.example.ProjectHON.GroupChatReport.GroupReport.GroupReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminCircleController {

    @Autowired
    GroupChatReportRepository groupChatReportRepository;

    @Autowired
    CircleEmailService circleEmailService;

    @Autowired
    GroupReportRepository groupReportRepository;

    @Autowired
    GroupMemberMasterRepository groupMemberMasterRepository;


    @Autowired
    GroupMasterRepository groupMasterRepository;

    @Autowired
    GroupService groupService;

    @GetMapping("/user/get/circle/reports")
    public String getCircleReports(Model model){
        List<GroupChatReportEntity> list =  groupChatReportRepository.findAll();
        model.addAttribute("circleReportList",list);
        return "AdminPackage/CircleReport/circle_chat_reportList";
    }

    @GetMapping("/user/circle/chat/report/review/{reportId}")
    public String getReviewChatReportPage(@PathVariable("reportId") Long reportId,
                                          Model model){

        GroupChatReportEntity chatReport = groupChatReportRepository.findById(reportId).orElse(null);
        if(chatReport==null){
            return "redirect:/login";
        }
        GroupChatReportEntity groupChatReport =  groupChatReportRepository.findById(reportId).orElse(null);
        groupChatReport.setStatus(GroupChatReportEntity.ReportStatus.IN_REVIEW);
        groupChatReportRepository.save(groupChatReport);
        model.addAttribute("reportedCircleChat" , chatReport);
        return "AdminPackage/CircleReport/circle_chat_review";
    }


    @PostMapping("/user/warn/circle/chat/report")
    public String sendWarning(@RequestParam("reportId")Long reportId ,
                        RedirectAttributes redirectAttributes){
          GroupChatReportEntity groupChatReport =  groupChatReportRepository.findById(reportId).orElse(null);
          groupChatReport.setStatus(GroupChatReportEntity.ReportStatus.ACTION_TAKEN);
          groupChatReportRepository.save(groupChatReport);
          try{
                circleEmailService.sendCircleWarningMail(groupChatReport.getOwner().getEmail());
          }catch (Exception e){
                System.out.println("--------throwing an exception while sending warning on circle chat report---------------- " +e.getMessage());
          }
          System.out.println("Chat owner get warned.");

        redirectAttributes.addFlashAttribute("warned" , true);
        return "redirect:/user/get/circle/reports";
    }

    @PostMapping("/user/reject/circle/chat/report")
    public String rejectReportCircleChat(@RequestParam("reportId") Long reportId,
                                         RedirectAttributes redirectAttributes){
        GroupChatReportEntity groupChatReport =  groupChatReportRepository.findById(reportId).orElse(null);
        groupChatReport.setStatus(GroupChatReportEntity.ReportStatus.REJECTED);
        groupChatReportRepository.save(groupChatReport);

        System.out.println("Chat get Rejected.");

        redirectAttributes.addFlashAttribute("rejected" , true);
        return "redirect:/user/get/circle/reports";
    }

    @GetMapping("/user/group/report/list")
    public String groupReportList(Model model){

        List<GroupReport> list = groupReportRepository.findAll();

//        List<GroupMemberMaster> members =
//                groupMemberMasterRepository.findActiveMembersByGroupId(groupId);

        model.addAttribute("groupReportList" , list);

        return "AdminPackage/CircleReport/circle_report_list";
    }

    @GetMapping("/user/review/group/report/{reportId}")
    public String reviewPage(@PathVariable("reportId") Long reportId , Model model){
         GroupReport list = groupReportRepository.findById(reportId).orElse(null);

        model.addAttribute("report" , list);
        return "AdminPackage/CircleReport/circle_report_review";
    }

    @PostMapping("/user/reject/group/report/{reportId}")
    public String rejectReport(@PathVariable("reportId") Long reportId , Model model){

          GroupReport groupReport = groupReportRepository.findById(reportId).orElse(null);
          groupReport.setStatus(GroupReport.ReportStatus.REJECTED);
          groupReportRepository.save(groupReport);
        return "redirect:/user/group/report/list";
    }


    @PostMapping("/user/warn/group/report/{reportId}")
    public String warnReportedGroup(@PathVariable("reportId") Long reportId,Model model){

        GroupReport groupReport = groupReportRepository.findById(reportId).orElse(null);
        groupReport.setStatus(GroupReport.ReportStatus.ACTION_TAKEN);
        groupReportRepository.save(groupReport);
        try{
            circleEmailService.sendWarningMailForGroupAdmin(groupReport.getGroupMaster()
                    .getGroupAdmin().getEmail());
        }catch (Exception e){
            System.out.println("--------throwing an exception on circle report---------------- " +e.getMessage());
        }
        return "redirect:/user/group/report/list";
    }



    @PostMapping("/user/delete/reported/group/{reportId}")
    public String deleteReportedGroup(@PathVariable("reportId") Long reportId){

        GroupReport groupReport = groupReportRepository.findById(reportId).orElse(null);

        groupReportRepository.deleteById(groupReport.getGroupMaster().getGroupId());
        groupService.deleteGroup(groupReport.getGroupMaster().getGroupId());
        System.out.println("Group has been deleted!");
//        groupMasterRepository.deleteById(groupReport.getId());
        try{
            circleEmailService.sendAfterDeletingGroup(groupReport.getGroupMaster()
                    .getGroupAdmin().getEmail());
        }catch (Exception e){
            System.out.println("--------throwing an exception on circle report---------------- " +e.getMessage());
        }
        return "redirect:/user/group/report/list";
    }
}
