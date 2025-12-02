package com.example.ProjectHON.Quiz_master;

import com.example.ProjectHON.Quiz_Answer.QuizAnswer;
import com.example.ProjectHON.Quiz_Answer.QuizAnswerRepository;
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


    @PostMapping("/quiz/submit")
    public ResponseEntity<Map<String,Object>> submitQuiz(
            @RequestParam("quizId") Long quizId,
            @RequestParam("option") String option,
            @RequestParam("playWithId") Long playWithId ,HttpSession session) {

         Long userId = (Long) session.getAttribute("userId");
        // Save user’s answer
        QuizAnswer saved = quizService.saveAnswer(quizId, option,userId, playWithId);

        System.out.println("-----------after answer service----------");
        // Fetch next question
        QuizQuestion next = quizRepository.findNext(quizId);

        System.out.println("---------------after next question------------");

        if (next == null) {
            return ResponseEntity.ok().body(Map.of("noQuestion",true)); // no more questions
        }
        //

        System.out.println("----------------after response---------------");
        return ResponseEntity.ok().body(Map.of("noQuestion",false,"next",next));
    }




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

    //for showing mutual_crush profile_pic.
//    @GetMapping("/user/profilePic/{id}")
//    public ResponseEntity<byte[]> getCrushProfilePic(@PathVariable("id") Long id){
////        Long userId = (Long) session.getAttribute("user_id");
//        System.out.println("---------------value of id is : " +id);//153 , 154
//        UserMaster userMaster =  userMasterRepository.findById(id).orElse(null);
//
//        if(userMaster==null){
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok()
//                .contentType(MediaType.IMAGE_JPEG)
//                .body(userMaster.getProfilePhoto());
//    }


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



