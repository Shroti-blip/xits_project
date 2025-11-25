package com.example.ProjectHON.mutualCrushPackage;

import com.example.ProjectHON.Post_masterpackage.PostMaster;
import com.example.ProjectHON.Post_masterpackage.PostRepository;
import com.example.ProjectHON.Quiz_master.QuizRepository;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MutualController {

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private UserMasterRepository userRepo;

    @Autowired
    private MutualCrushRepository mutualCrushRepo;

    @Autowired
    private MutualCrushService crushService;


    @Autowired
    QuizRepository quizRepository;
    @GetMapping("/user/mutual-crush")
    public String showDashboard(HttpSession session, Model model) {
        UserMaster currentUser = (UserMaster) session.getAttribute("user_master");
        if (currentUser == null) return "redirect:/login";

        List<PostMaster> myPosts = postRepo.findByUser(currentUser);

        // Incoming crush requests
        List<MutualCrushMaster> crushRequests =
                mutualCrushRepo.findByRequestToAndRequestedTrueAndAcceptedFalseAndIgnoredFalse(currentUser);

        // Mutual crushes
        List<MutualCrushMaster> mutualCrushes =
                mutualCrushRepo.findByUserAsMutual(currentUser);

        // All requests sent by current user
        List<MutualCrushMaster> sentRequests =
                mutualCrushRepo.findByRequestByAndRequestedTrueAndAcceptedFalseAndIgnoredFalse(currentUser);

        // Invite list
        List<UserMaster> allUsers = userRepo.findAll();
        List<UserMaster> canInvite = new ArrayList<>();

        for (UserMaster u : allUsers) {
            if (u.getUserId().equals(currentUser.getUserId())) continue;

            boolean alreadyMutual = mutualCrushes.stream()
                    .anyMatch(m -> m.getRequestBy().getUserId().equals(u.getUserId())
                            || m.getRequestTo().getUserId().equals(u.getUserId()));

            boolean alreadyRequestedByCurrent = sentRequests.stream()
                    .anyMatch(mc -> mc.getRequestTo().getUserId().equals(u.getUserId()));

            boolean alreadyRequestedToCurrent = crushRequests.stream()
                    .anyMatch(mc -> mc.getRequestBy().getUserId().equals(u.getUserId()));

            if (!alreadyMutual && !alreadyRequestedByCurrent && !alreadyRequestedToCurrent
                    && crushService.bothMeetEligibility(currentUser, u)) {
                canInvite.add(u);
            }
        }
        boolean showMutualPopup = MutualCrushNotifier.shouldShowPopup(currentUser.getUserId());
        if (showMutualPopup) {
            MutualCrushNotifier.clearPopup(currentUser.getUserId());
            model.addAttribute("showMutualPopup", true);
        }

        List<MutualCrushMaster> list = mutualCrushRepo.findByFirstUser(currentUser);

        model.addAttribute("firstUse",list.isEmpty());
        model.addAttribute("posts", myPosts);
        model.addAttribute("mutualCrushes", mutualCrushes);
        model.addAttribute("pendingRequests", crushRequests);
        model.addAttribute("canInvite", canInvite);
        model.addAttribute("userSession", currentUser);
        model.addAttribute("allquiz",quizRepository.findAll());

        return "MergePart/mutualcrush";
    }

}

