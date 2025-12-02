package com.example.ProjectHON.mutualCrushPackage;

import com.example.ProjectHON.Post_masterpackage.PostMaster;
import com.example.ProjectHON.Post_masterpackage.PostRepository;
import com.example.ProjectHON.Quiz_Answer.QuizAnswer;
import com.example.ProjectHON.Quiz_Answer.QuizAnswerRepository;
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


//        System.out.println(masters+ "==============================data");151


       // List<List<>>
        model.addAttribute("master" , masters);//153 , 158

//        QuizQuestion firstQ1 = quizRepository.findFirstByOrderByIdAsc();
//        model.addAttribute("firstQuestion",firstQ//1);


        Map<UserMaster, QuizQuestion> nextQuestionMap = new HashMap<>();

        QuizQuestion firstQ = quizRepository.findFirstByOrderByIdAsc();

        for (MutualCrushMaster mcm : masters) {

            UserMaster playWith = mcm.getRequestTo().getUserId() != currentUser.getUserId()
                    ? mcm.getRequestTo()
                    : mcm.getRequestBy();

            // 1. CHECK IF USER ANSWERED THE FIRST QUESTION
            Optional<QuizAnswer> ans =
                    quizAnswerRepository.findPlayWithUserQuestionByUser(currentUser, playWith, firstQ);

            // If user never answered first question → show first question
            if (ans.isEmpty()) {
                nextQuestionMap.put(playWith, firstQ);
                continue;
            }

            // 2. ITERATE UNTIL WE FIND FIRST UNANSWERED QUESTION
            QuizQuestion currentQ = ans.get().getQuestion();
            QuizQuestion nextQ = quizRepository.findNext(currentQ.getId());

            while (nextQ != null) {

                Optional<QuizAnswer> nextAns =
                        quizAnswerRepository.findPlayWithUserQuestionByUser(currentUser, playWith, nextQ);

                if (nextAns.isEmpty()) {
                    // Found next un-answered question
                    nextQuestionMap.put(playWith, nextQ);
                    nextQ = null;
                    break;
                }

                currentQ = nextQ;
                nextQ = quizRepository.findNext(currentQ.getId());
            }

            // 3. IF NO NEXT QUESTION → QUIZ COMPLETED
            if (!nextQuestionMap.containsKey(playWith)) {
                nextQuestionMap.put(playWith, null); // Means completed
            }
        }

        model.addAttribute("nextQuestionMap", nextQuestionMap);


//        Map<UserMaster,List<QuizQuestion>> quizQuestionList=new HashMap();//1 2 3 4
//        for(MutualCrushMaster mcm : masters){
//            UserMaster playWith=mcm.getRequestTo().getUserId()!=currentUser.getUserId() ? mcm.getRequestTo() : mcm.getRequestBy();;
//            Optional<QuizAnswer>ans=quizAnswerRepository.findPlayWithUserQuestionByUser(currentUser,playWith,firstQ);//1
//            List<QuizQuestion>quizAnswers=new ArrayList<>();
//            while (ans.isPresent()){
//                quizAnswers.add(ans.get().getQuestion());
//               // quizQuestionList.put(ans.get().getPlayWith(),ans.get().getQuestion());
//                ans=quizAnswerRepository.findPlayWithUserQuestionByUser(currentUser,playWith,
//                        quizRepository.findNext(ans.get().getQuestion().getId()));
//            }
////            quizQuestionList.put(playWith,quizAnswers);
////            if(!quizQuestionList.get(playWith).isEmpty()) {
////                QuizQuestion lastNextQuestion = quizRepository.findNext(quizQuestionList.get(playWith).get(quizQuestionList.get(playWith).size() + 1).getId());
////                if (lastNextQuestion != null) {
////                    quizAnswers.add(lastNextQuestion);
////                }
////            }
//
//            quizQuestionList.replace(playWith,quizAnswers);
//        }
//
//      //  quizQuestionList.get()
//      //  quizQuestionList.get(currentUser).getLast().getQuestionText()
//
//        model.addAttribute("quizQuestionList",quizQuestionList);

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
