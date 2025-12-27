package com.example.ProjectHON.Post_Report;

import com.example.ProjectHON.Post_masterpackage.PostMaster;
import com.example.ProjectHON.Post_masterpackage.PostRepository;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Controller
public class PostReportController {

    @Autowired
    PostReportRepository postReportRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserMasterRepository userMasterRepository;

    @Autowired
    PostReportEmailService postReportEmailService;


    @PostMapping("/user/reportPost")
//    @ResponseBody
    public String saveReport(@RequestParam("reason") String reason ,
                             @RequestParam("comment") String comment,
                             @RequestParam("postId") Long postId,
                             HttpSession session,
                             RedirectAttributes redirectAttributes){

        System.out.println("====================Inside report Post=============");
        //login user id who is reporting
        UserMaster reporter = (UserMaster) session.getAttribute("user_master");
        Long userId = reporter.getUserId();
        PostMaster postMaster = postRepository.findById(postId).orElse(null);

        Boolean alreadyExists =  postReportRepository.existsByReporterUserIdAndPostreport_PostId(userId , postId );

        if(alreadyExists){
            System.out.println("=====😒😒😒😒😒😒😒😒😒=====already exists====");
            return "redirect:/user/rating?duplicateReport=true";
//            return ResponseEntity.ok(Map.of("alreadyExists",true));
        }

        //        Long postImageId = postMaster.getPostId();
        PostReport postReport = new PostReport();
        postReport.setReason(reason);
        postReport.setComment(comment);
        postReport.setContentType("IMAGES");
        postReport.setTimestamp(LocalDate.now());
        postReport.setPostreport(postMaster);
        postReport.setReporter(reporter);
        postReport.setOffender(postMaster.getUser());
        postReportRepository.save(postReport);
        System.out.println("====================After report Post=============");
            redirectAttributes.addFlashAttribute("reportSuccess",true);

        return "redirect:/user/rating";
//        return ResponseEntity.ok(Map.of("reportSuccess",true));
    }


    @GetMapping("/user/get-all-post-report")
    public String getReport(Model model ){

        List<PostReport> list =  postReportRepository.findAll();
//        System.out.println("=========== list ============");
        model.addAttribute("postReports" , list);

        return "MergePart/show_post_report";
    }


//    @GetMapping("/user/report/post/{id}")
//    public String getReviewPage(@PathVariable("id") Integer reportId, Model model) {
//        PostReport report = postReportRepository.findById(reportId).orElseThrow(() -> new RuntimeException("Report not found"));
//        Long totalReports = postReportRepository.countByPostreport(report.getPostreport());
//        model.addAttribute("report", report);
//        model.addAttribute("totalReports", totalReports);
//        return "MergePart/review_post_report";
//    }


    @GetMapping("/user/report/post/{id}")
    public String getReviewPage(@PathVariable("id") Integer reportId, Model model) {
        // Simply redirect to the main review mapping
        return reviewPostReport(reportId, model);
    }


    @GetMapping("/user/review-post-report/{id}")
    public String reviewPostReport(@PathVariable int id, Model model) {

        // Get the specific report
        PostReport report = postReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        PostMaster post = report.getPostreport();

        // Count total users and total reports for this post
        long totalUsers = userMasterRepository.count();
        long totalReportsOnPost = postReportRepository.countByPostreport(post);

        // Calculate percentage of users who reported this post
        double reportPercentage = totalUsers == 0 ? 0 :
                ((double) totalReportsOnPost / totalUsers) * 100;

        // Determine if delete button should be enabled
        boolean canDeletePost = reportPercentage >= 5;

        // Add attributes to model for Thymeleaf
        model.addAttribute("report", report);
        model.addAttribute("totalReports", totalReportsOnPost);
        model.addAttribute("reportPercentage", reportPercentage);
        model.addAttribute("reportPercentageText", String.format("%.2f", reportPercentage));
        model.addAttribute("canDeletePost", canDeletePost);

        return "MergePart/review_post_report";
    }


    @PostMapping("/user/reports/reject/{id}")
    public String rejectReport(@PathVariable int id , RedirectAttributes redirectAttributes) {

        PostReport report = postReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        report.setStatus(PostReport.ReportStatus.REJECTED);
        postReportRepository.save(report);
//        postReportRepository.delete(report);

        redirectAttributes.addFlashAttribute(
                "successReject",
                "Post rejected successfully "
        );

        return "redirect:/user/get-all-post-report";
    }


    @PostMapping("/user/reports/warn/{id}")
    public String warnUser(@PathVariable int id , RedirectAttributes redirectAttributes) {

        PostReport report = postReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        report.setStatus(PostReport.ReportStatus.WARNED);
        UserMaster offender = report.getOffender();

//        offender.getWarningCount()==0
//                ? 0
//                :
        int currentWarnings =  offender.getWarningCount();

        offender.setWarningCount(currentWarnings + 1);
        userMasterRepository.save(offender);

        postReportRepository.save(report);
//        postReportRepository.delete(report);

        try{
            postReportEmailService.sendWarning(report.getOffender().getEmail());

        }catch (Exception e){
            System.out.println("==========😒😒😒😒😒");
        }

        redirectAttributes.addFlashAttribute(
                "successWarn",
                " successfully warned."
        );

        return "redirect:/user/get-all-post-report";
    }



    @PostMapping("/user/reports/delete/{id}")
    @Transactional
    public String deleteReportedPost(@PathVariable int id,
                                     RedirectAttributes redirectAttributes ) {

        //  Find the report
        PostReport report = postReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        // actual post
        PostMaster post = report.getPostreport();

        //  Delete ALL reports related to this post (important)
        postReportRepository.deleteByPostreport(post);


        postRepository.delete(post);

        try{
            postReportEmailService.sendAfterDeletingPostToReporter(report.getReporter().getEmail());
            postReportEmailService.sendAfterDeletingPostToOwner(report.getOffender().getEmail());
        }catch (Exception e){
            System.out.println("-----------in email -----------");
        }

        // 5. Success message
        redirectAttributes.addFlashAttribute(
                "success",
                "Post deleted successfully due to multiple reports."
        );

        // 6. Redirect to reports list / dashboard
        return "redirect:/user/get-all-post-report";
    }


    @GetMapping("/user/get-all-reports")
    public String getAll(Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login"; // safety
        }

        List<PostReport> reports =
                postReportRepository.findAllByReporter_UserId(userId);

        long pendingCount =
                postReportRepository.countByReporter_UserIdAndStatus(
                        userId,
                        PostReport.ReportStatus.PENDING
                );

        model.addAttribute("reports", reports);
        model.addAttribute("pendingCount", pendingCount);

        return "MergePart/reported_post_user";
    }

    @GetMapping("/post/image/{postId}")
    public ResponseEntity<byte[]> getPostImage(@PathVariable Long postId) {

        PostMaster post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (post.getPhoto() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(post.getPhoto());
    }


//=======================================================================================

//    @GetMapping("/admin/review-post-report/{id}")
//    public String reviewPostReport(
//            @PathVariable int id,
//            Model model) {
//
//        PostReport report = postReportRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Report not found"));
//
//        if (report.getPostreport().getPhoto() != null) {
//            String base64Image = Base64.getEncoder()
//                    .encodeToString(report.getPostreport().getPhoto());
//
//            model.addAttribute("postImage", base64Image);
//        }
//
//        model.addAttribute("report", report);
//        return "MergePart/review_post_report";
//    }


}

