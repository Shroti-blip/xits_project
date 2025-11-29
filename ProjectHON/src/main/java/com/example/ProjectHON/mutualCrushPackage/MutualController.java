package com.example.ProjectHON.mutualCrushPackage;

import com.example.ProjectHON.Post_masterpackage.PostMaster;
import com.example.ProjectHON.Post_masterpackage.PostRepository;
import com.example.ProjectHON.Quiz_master.QuizQuestion;
import com.example.ProjectHON.Quiz_master.QuizRepository;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        //for showing first question
        QuizQuestion firstQ = quizRepository.findFirstByOrderByIdAsc();
        model.addAttribute("firstQuestion",firstQ);

        //for showing list of all users
//        List<MutualCrushMaster> mutualCrushMasterList = mutualCrushRepo.findByUserAsMutual(currentUser);
//        model.addAttribute("mutualCrushList" , mutualCrushMasterList);

        List<MutualCrushMaster> master = mutualCrushRepo.findByUserAsMutual(currentUser);


//        System.out.println("--------------------"+master );

        List<MutualCrushMaster> masters = master.stream()
                .filter(MutualCrushMaster::isAccepted) // only accepted crushes
                .filter(m -> m.getRequestBy().getUserId().equals(currentUser.getUserId())
                || m.getRequestTo().getUserId().equals(currentUser.getUserId())) // requests sent by current user and request to.
                .toList();

        System.out.println("Current user id: " + currentUser.getUserId());//152


//        System.out.println(masters+ "==============================data");

        model.addAttribute("master" , masters);
//        System.out.println("Value of mutualCrushList is : " + master);

        return "MergePart/mutualcrush";
    }

}

//List<MutualCrushMaster> masters = master.stream()
//        .filter(MutualCrushMaster::isAccepted)
//        .filter(m -> m.getRequestBy().getUserId() == currentUser.getUserId())
//        .toList();


//        List<MutualCrushMaster> masters = master.stream()
//        .filter(m->m.isAccepted())
//        .filter(m -> m.getRequestBy().getUserId() == currentUser.getUserId())
//        .toList();



//        List<MutualCrushMaster> masters = master.stream()
//                .filter(MutualCrushMaster::isAccepted)//take only accepted one
//                .filter(m ->
//                        m.getRequestBy().getUserId() == currentUser.getUserId() ||
//                                m.getRequestTo().getUserId() == currentUser.getUserId()
//                )
//                .toList();


//List<UserMaster> myAccepted = master.stream().
//        filter(m -> m.isAccepted())
//        .filter(m -> m.getRequestBy().getUserId() == currentUser.getUserId())  // YOU requested
//        .map(MutualCrushMaster::getRequestTo)
//        .toList();

//List<MutualCrushMaster> masters = master.stream()
//        .filter(m ->
//                m.getRequestBy().getUserId().equals(currentUser.getUserId()) ||
//                        m.getRequestTo().getUserId().equals(currentUser.getUserId())
//        )
//        .toList();

//        List<MutualCrushMaster> masters = master.stream()
//                .filter(MutualCrushMaster::isAccepted) // only mutual crushes accepted
//                .filter(m ->
//                        m.getRequestBy().getUserId().equals(currentUser.getUserId()) ||
//                                m.getRequestTo().getUserId().equals(currentUser.getUserId())
//                )
//                .toList();

