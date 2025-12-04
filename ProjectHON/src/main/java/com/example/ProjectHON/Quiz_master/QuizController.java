package com.example.ProjectHON.Quiz_master;

import com.example.ProjectHON.Quiz_Answer.QuizAnswer;
import com.example.ProjectHON.Quiz_Answer.QuizAnswerRepository;
import com.example.ProjectHON.Quiz_Answer.QuizAnswerService;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/user/playquiz/{playWithId}")
    public String playQuiz(Model model , @PathVariable("playWithId") Long playWithId,
                           HttpSession session){
        System.out.println("-----inside get mapping for quiz section------");
        UserMaster playWithUserId = userMasterRepository.findById(playWithId).orElse(null);
         Long userId = (Long)session.getAttribute("userId");
        List<QuizQuestion> list = quizService.getQuizForUsers(userId , playWithId);
        Boolean quizcomplete = quizAnswerService.isCountComplete(userId , playWithId);
        model.addAttribute("quizcomplete" , quizcomplete);
         model.addAttribute("questions" , list);
        model.addAttribute("playWithUserId",playWithUserId);
        return "MergePart/playquiz";
    }

    //if multiple data is coming from then it will come like key value pair.
    @PostMapping("/user/saveAnswers")
    public String saveAnswers(Model model , HttpSession session ,
                              @RequestParam Map<String , String> answers,
                              @RequestParam("playWithId") Long playWithId){
        System.out.println("-------inside post mapping------------");
         Long userId = (Long) session.getAttribute("userId");
        UserMaster userMaster = userMasterRepository.findById(userId).orElse(null);//153 , 154

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


           }
        });

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






    //@RequestParam Map<String, String> params,
//    @PostMapping("/quizwithcrush")
//    public String quizWithCrush(@RequestParam("selectedOption") String selectedOption,
//                                @RequestParam("questionId") int questionId,
//                                HttpSession session) {
//
//        UserMaster current = (UserMaster) session.getAttribute("user_master");
//        QuizQuestion question =   quizRepository.findById(questionId).orElse(null);
//
//        QuizAnswer quizAnswer = new QuizAnswer();
////        quizAnswer.setAnswer(answers.get(2));
//        quizAnswer.setQuestion(question);
//        quizAnswer.setAnsweredAt(LocalDateTime.now());
//        quizAnswer.setUser(current);
//        quizAnswerRepository.save(quizAnswer);
//
//        return "redirect:/user/mutual-crush";
//    }



}



