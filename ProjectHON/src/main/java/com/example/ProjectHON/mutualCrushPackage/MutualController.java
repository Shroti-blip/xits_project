package com.example.ProjectHON.mutualCrushPackage;

import com.example.ProjectHON.Post_masterpackage.PostMaster;
import com.example.ProjectHON.Post_masterpackage.PostRepository;
import com.example.ProjectHON.Quiz_Answer.BetweenRepo;
import com.example.ProjectHON.Quiz_Answer.QuizAnswer;
import com.example.ProjectHON.Quiz_Answer.QuizAnswerRepository;
import com.example.ProjectHON.Quiz_Answer.QuizAnswerService;
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

import java.util.*;
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
    QuizAnswerRepository quizAnswerRepository;

    @Autowired
    QuizAnswerService quizAnswerService;

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    BetweenRepo betweenRepo;



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

        //for showing list of all users


        List<MutualCrushMaster> master = mutualCrushRepo.findByUserAsMutual(currentUser);


//        System.out.println("--------------------"+master );

        List<MutualCrushMaster> masters = master.stream()
                .filter(MutualCrushMaster::isAccepted) // only accepted crushes
                .filter(m -> m.getRequestBy().getUserId().equals(currentUser.getUserId())
                || m.getRequestTo().getUserId().equals(currentUser.getUserId())) // requests sent by current user and request to.
                .toList();
//
//        System.out.println("Current user id: " + currentUser.getUserId());//152
//
//
//
//        model.addAttribute("master" , masters);//153 , 158

        List<MutualCrushDTO> dtoList = new ArrayList<>();

        System.out.println("Current User Id "+currentUser.getUserId()+" And Total Mutual Crush "+masters.size());




        for (MutualCrushMaster m : masters) {

            System.out.println("=**********************************************************************");
            System.out.println("Current User Id "+currentUser.getUserId());
            System.out.println("Request To Id "+m.getRequestTo().getUserId());
            System.out.println("Request By Id "+m.getRequestBy().getUserId());


            System.out.println("=**********************************************************************");


            Long otherUserId =
                    !m.getRequestTo().getUserId().equals(currentUser.getUserId())//to is 165 !=  current is 165
                            ? m.getRequestTo().getUserId()
                            : m.getRequestBy().getUserId();//163

//            Long oUi;
//            if(m.)

            boolean currentUserPlayed =
                    betweenRepo.hasUserPlayed(currentUser.getUserId(), otherUserId);

            System.out.println("Current User Played "+currentUserPlayed);
            System.out.println("Current User Id "+currentUser.getUserId());

            boolean otherUserPlayed =
                    betweenRepo.hasUserPlayed(otherUserId, currentUser.getUserId());


            System.out.println("Other User Played "+otherUserPlayed);
            System.out.println("Other User Id "+otherUserId);

            MutualCrushDTO dto = new MutualCrushDTO(m, currentUserPlayed, otherUserPlayed);
            dtoList.add(dto);
        }

        model.addAttribute("crushList", dtoList);




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


//
//List<MutualCrushMaster> masters = new ArrayList<>();
//
//for (MutualCrushMaster m : master) {
//
//        // First filter: only accepted mutual crushes
//        if (!m.isAccepted()) {
//        continue; // skip if not accepted
//        }
//
//// Second filter: only crushes where current user is involved
//boolean isCurrentUserSender = m.getRequestBy().getUserId().equals(currentUser.getUserId());
//boolean isCurrentUserReceiver = m.getRequestTo().getUserId().equals(currentUser.getUserId());
//
//    if (isCurrentUserSender || isCurrentUserReceiver) {
//        masters.add(m);
//    }
//            }
