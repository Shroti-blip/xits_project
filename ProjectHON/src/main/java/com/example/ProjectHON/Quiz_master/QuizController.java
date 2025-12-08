package com.example.ProjectHON.Quiz_master;

import com.example.ProjectHON.Quiz_Answer.*;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import com.example.ProjectHON.mutualCrushPackage.MutualCrushMaster;
import com.example.ProjectHON.mutualCrushPackage.MutualCrushRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class QuizController {

    @Autowired
    QuizRepository quizRepository;
    @Autowired
    QuizAnswerRepository quizAnswerRepository;

    @Autowired
    UserMasterRepository userMasterRepository;

    @Autowired
    QuizService quizService;

    @Autowired
    QuizAnswerService quizAnswerService;

    @Autowired
    MutualCrushRepository mutualCrushRepository;

    @Autowired
    BetweenRepo betweenRepo;

    @GetMapping("/user/playquiz/{playWithId}")
    public String playQuiz(Model model , @PathVariable("playWithId") Long playWithId,
                           HttpSession session){
        System.out.println("-----inside get mapping for quiz section------");
        UserMaster playWithUserId = userMasterRepository.findById(playWithId).orElse(null);
         Long userId = (Long)session.getAttribute("userId");
        List<QuizQuestion> list = quizService.getQuizForUsers(userId , playWithId);

        mutualCrushRepository.markInviteSent(userId,playWithId);

         model.addAttribute("questions" , list);
        model.addAttribute("playWithUserId",playWithUserId);
        return "MergePart/playquiz";
    }

    //if multiple data is coming from then it will come like key value pair.
    @PostMapping("/user/saveAnswers")
    public String saveAnswers(Model model , HttpSession session ,
                              @RequestParam Map<String , String> answers,
                              @RequestParam("playWithId") Long playWithId,
                              RedirectAttributes redirectAttributes){
        System.out.println("-------inside post mapping------------");
         Long userId = (Long) session.getAttribute("userId");
        UserMaster userMaster = userMasterRepository.findById(userId).orElse(null);//153 , 154
        UserMaster userPlayQuiz=userMasterRepository.findById(playWithId).orElse(null);

        System.out.println("--------------before key/value loop------------");

        answers.forEach((key , value)->{
           if(key.startsWith("answers")){
               Integer quizId = Integer.parseInt(key.substring(8,key.length()-1));

               System.out.println("------quizId , userId , value , playWithId is : " + quizId + " , " + userId+ " , " + value + " , " +playWithId);
               QuizAnswer quizAnswer = new QuizAnswer();
               quizAnswer.setAnsweredAt(LocalDateTime.now());
               quizAnswer.setSelectedOption(value);
               quizAnswer.setQuestion(quizRepository.findById(quizId).orElse(null));
               quizAnswer.setUser(userMasterRepository.findById(userId).orElse(null));
               quizAnswer.setPlayWith(userMasterRepository.findById(playWithId).orElse(null));
               quizAnswer.setQuizComplete(true);
               quizAnswerRepository.save(quizAnswer);
               System.out.println("-------after saving data------");

               MutualCrushMaster mutual = mutualCrushRepository.getPair(userId , playWithId);

               Boolean done = quizAnswerService.areBothPlayerDone(userId ,playWithId);
               model.addAttribute("quizComplete" , done);



               if(done){
                   Map<String , Integer > data = quizAnswerService.getPoints(userId, playWithId);

                   mutual.setQuizPlayed(true);
                    mutual.setPercentage(data.get("percentage"));
                    mutualCrushRepository.save(mutual);

                    //no use.
                    model.addAttribute("scoreU1" , data.get("scoreU1"));
                   model.addAttribute("scoreU2" ,  data.get("scoreU2"));
                   model.addAttribute("percentage" , mutual.getPercentage());
                   model.addAttribute("quizPlayed" , mutual.isQuizPlayed());

               }


               System.out.println("-------after fetching obj. data------" );


           }
        });


        BetweenQuiz betweenQuiz=new BetweenQuiz();
        betweenQuiz.setFromQuiz(userMaster);
        betweenQuiz.setToQuiz(userPlayQuiz);
        betweenQuiz.setPlayed(true);

        betweenRepo.save(betweenQuiz);


        return "redirect:/user/mutual-crush";
    }



//    @PostMapping("/quiz/submitAll")
//    public String saveQuiz(Model model , @RequestParam("quizId") Long quizId,
//                           @RequestParam("option") String option,
//                           @RequestParam("playWithId") Long playWithId ,
//                           HttpSession session){
//
//        System.out.println("----------before save / inside quiz/submitall mapping-------------");
//        Long userId = (Long)session.getAttribute("userId");
//        quizService.saveAnswer(quizId , option , userId , playWithId);
//
//        System.out.println("--------------after saving -----------------");
//
//
//        return "redirect:/user/mutual-crush";
//    }


//    @PostMapping("/quiz/submit")
//    public ResponseEntity<Map<String,Object>> submitQuiz(
//            @RequestParam("quizId") Long quizId,
//            @RequestParam("option") String option,
//            @RequestParam("playWithId") Long playWithId ,HttpSession session) {
//
//         Long userId = (Long) session.getAttribute("userId");
//        // Save user’s answer
//        QuizAnswer saved = quizService.saveAnswer(quizId, option,userId, playWithId);
//        System.out.println("-----------after answer service----------");
//        // Fetch next question
//        QuizQuestion next = quizRepository.findNext(quizId);
//        System.out.println("---------------after next question------------");
//
//        if (next == null) {
//            return ResponseEntity.ok().body(Map.of("noQuestion",true)); // no more questions
//        }
//        System.out.println("----------------after response---------------");
//        return ResponseEntity.ok().body(Map.of("noQuestion",false,"next",next));
//    }




    @GetMapping("/crushpic/{id}")
    public ResponseEntity<byte[]> userPhoto(@PathVariable("id")Long id){

        UserMaster userMaster = userMasterRepository.findById(id).orElse(null);//153 , 154

        if(userMaster == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(userMaster.getProfilePhoto());
    }






}



