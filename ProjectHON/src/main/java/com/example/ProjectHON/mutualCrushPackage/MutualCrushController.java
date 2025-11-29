package com.example.ProjectHON.mutualCrushPackage;

import com.example.ProjectHON.Post_masterpackage.PostRepository;
import com.example.ProjectHON.Quiz_master.QuizRepository;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MutualCrushController {

    @Autowired
    private MutualCrushRepository mutualRepo;

    @Autowired
    private MutualCrushService crushService;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private UserMasterRepository userRepo;

@PostMapping("/inviteCrush")
public String inviteCrush(@RequestParam("targetId") Long targetId, HttpSession session, Model model) {
    UserMaster current = (UserMaster) session.getAttribute("user_master");
    if (current == null) return "redirect:/login";

    UserMaster target = userRepo.findById(targetId).orElse(null);
    if (target == null) return "redirect:/user/mutual-crush";

    if (!crushService.bothMeetEligibility(current, target)) {
        model.addAttribute("error", "You and " + target.getUsername() + " don’t meet the mutual crush conditions yet.");
//        return "redirect:/profile/" + targetId;
        return "redirect:/user/mutual-crush";
    }


    crushService.sendInvite(current, target);
//    return "redirect:/profile/" + targetId;
    return "redirect:/user/mutual-crush";
}

    @PostMapping("/acceptCrush")
    public String acceptCrush(@RequestParam("inviteId") Long inviteId, HttpSession session) {//152
        Long userId=(Long)session.getAttribute("userId");
        UserMaster current = userRepo.findById(userId).orElse(null);//154
        if (current == null) return "redirect:/login";


        UserMaster user1 = userRepo.findById(inviteId).orElse(null);//152
        System.out.println("---------------------------------After Invite Object");
        mutualRepo.findByRequestBy(user1)
                .forEach(invite -> {
                    if (invite.getRequestTo().getUserId().equals(current.getUserId())) {
                        crushService.acceptInvite(invite);
                    }
                });

        return "redirect:/user/mutual-crush";
    }

    @PostMapping("/ignoreCrush")
    public String ignoreCrush(@RequestParam("inviteId") Long inviteId, HttpSession session) {
        UserMaster current = (UserMaster) session.getAttribute("user_master");
        if (current == null) return "redirect:/login";

        mutualRepo.findById(inviteId).ifPresent(invite -> {
            if (invite.getRequestTo().getUserId().equals(current.getUserId())) {
                crushService.ignoreInvite(invite);
            }
        });
        return "redirect:/user/mutual-crush";
    }

    @PostMapping("/dumpCrush")
    public String removeMutualCrush(@RequestParam("dumpId") Long dumpId, HttpSession session) {
        UserMaster current = (UserMaster) session.getAttribute("user_master");
        if (current == null) return "redirect:/login";

        userRepo.findById(dumpId).ifPresent(o -> crushService.removeMutualCrush(current, o));
        return "redirect:/user/mutual-crush";
    }

    @Autowired
    QuizRepository quizRepository;

//normal model attribute cant handel redirect thing so...redirectattribute use.





}
