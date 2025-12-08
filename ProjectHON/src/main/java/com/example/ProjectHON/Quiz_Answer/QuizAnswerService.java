package com.example.ProjectHON.Quiz_Answer;

import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class QuizAnswerService {

    @Autowired
    QuizAnswerRepository quizAnswerRepository;

    @Autowired
    UserMasterRepository userMasterRepository;

    public Boolean isCountComplete(Long userId  , Long playwithId){
       int data =  quizAnswerRepository.countUserAttempts(userId , playwithId);
        return data>=5;
    }

    public Map<String , Integer> getPoints(Long userId , Long playWithId){
        List<QuizAnswer> answers = quizAnswerRepository.getAllAnswersOfBoth(userId, playWithId);//all ans in list.
        //now seprate both answer/quizids for both user A AND B .

        Map<Long , String> user1answers = new HashMap<>();
        Map<Long , String> user2answers = new HashMap<>();

        for(QuizAnswer q : answers){
              Long qid =  q.getQuestion().getId();
              if(q.getUser().getUserId().equals(userId)){
                  user1answers.put(qid , q.getSelectedOption());
              }
              else{
                  user2answers.put(qid ,q.getSelectedOption());
              }

        }

//        System.out.println("================Found the value of user1answer and user2answers value ===================");

        //now check wheather user1 matches user2 answers

        UserMaster u1 = userMasterRepository.findById(userId).orElse(null);
        UserMaster u2 = userMasterRepository.findById(playWithId).orElse(null);

        int scoreU1 = 0;
        int scoreU2 =0;
        int matchingCount =0;
        int totalquestion =user1answers.size();
        for(Long qid : user1answers.keySet()){
             String a1 =user1answers.get(qid);
             String a2 = user2answers.get(qid);

             if(a1!=null && a1.equals(a2)){
                 scoreU1 +=5;
                 scoreU2 +=5;
                 matchingCount++;
                 System.out.println("----------scoreU1 : " + scoreU1);
                 System.out.println("------------scoreU2: " + scoreU2);
             }
        }

        if(u1!=null){
            u1.setPoints(u1.getPoints() + scoreU1);
            System.out.println("----------scoreU1 ------- : " + u1.getPoints());
        }
        if(u2!=null){
            u2.setPoints(u2.getPoints() + scoreU2);
            System.out.println("----------scoreU2------- : " + u2.getPoints());
        }


        Integer percentage = (matchingCount*100)/totalquestion;
        System.out.println("---------compaitablility is ---------------" + percentage);

        Map<String , Integer> result = new HashMap<>();
        result.put("scoreU1" , scoreU1);
        result.put("scoreU2" , scoreU2);
        result.put("percentage" ,percentage );
        return result;

    }



    //for checking if both completed the quiz so we can show compatibility.
    public Boolean areBothPlayerDone(Long u1 , Long u2){
        Long c1 = quizAnswerRepository.completeByUser1(u1, u2);
        Long c2 = quizAnswerRepository.completedByUser2(u1 , u2);

        return (c1>0 && c2>0);
    }

    //


}


// btn disable
//popup
//notification
//