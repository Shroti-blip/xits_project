package com.example.ProjectHON.Quiz_master;

import com.example.ProjectHON.Quiz_Answer.QuizAnswer;
import com.example.ProjectHON.Quiz_Answer.QuizAnswerRepository;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuizService {

    @Autowired
    QuizAnswerRepository quizAnswerRepository;

    @Autowired
    UserMasterRepository userMasterRepository;

    @Autowired
    QuizRepository quizRepository;


    public List<QuizQuestion> getQuizForUsers(Long userId , Long playWithId){
        //taking ids of questions.
          List<Long> savedQuizQuestionIds =  quizRepository.getSavedQuizQuestionIds(userId , playWithId);
          //now check

        if(!savedQuizQuestionIds.isEmpty()){
          return quizRepository.findQuestionsByIds(savedQuizQuestionIds);
        }
        //get 5 random quiz if null.
        List<QuizQuestion> randomFive = quizRepository.findRandomFive();

        return randomFive;
    }



//    public QuizAnswer saveAnswer(Long quizId, String option, Long byUser,Long playWithId) {
//System.out.println("After Dynasore code");
//        QuizAnswer ans = new QuizAnswer();
//        ans.setQuestion(quizRepository.findById(quizId.intValue()).orElse(null));
//        ans.setUser(userMasterRepository.findById(byUser).orElse(null));
//        ans.setSelectedOption(option);
//        ans.setPlayWith(userMasterRepository.findById(playWithId).orElse(null));
//        ans.setAnsweredAt(LocalDateTime.now());
//        return quizAnswerRepository.save(ans);
//    }



}
