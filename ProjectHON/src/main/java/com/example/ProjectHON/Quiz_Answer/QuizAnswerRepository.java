package com.example.ProjectHON.Quiz_Answer;

import com.example.ProjectHON.Quiz_master.QuizQuestion;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Integer> {


    @Query("SELECT qa FROM QuizAnswer qa" +
        " WHERE qa.user=:userBy AND" +
        " qa.playWith=:userTo AND" +
        " qa.question=:quizQuestion")
    Optional<QuizAnswer>findPlayWithUserQuestionByUser(@Param("userBy")UserMaster userBy,
                                                       @Param("userTo")UserMaster userTo,
                                                       @Param("quizQuestion")QuizQuestion quizQuestion);
//    Boolean existsByUserIdAndPlayedWithAndQuizCompleteTrue();

    @Query("SELECT COUNT(a) FROM QuizAnswer a WHERE a.user.userId = :userId AND a.playWith.userId = :playWithId")
    int countUserAttempts(Long userId, Long playWithId);

    @Query(value = "select q from QuizAnswer q where (q.user.userId = :u1 And q.playWith.userId =: u2) OR (q.user.userId = :u2 And q.playWith.userId=:u1)")
    List<QuizAnswer> getAllAnswersOfBoth(Long u1 , Long u2);

    @Query("select count(q) from QuizAnswer q where q.user.userId=:u1 and q.playWith.userId=:u2 and q.quizComplete = true")
    Long completeByUser1(Long u1, Long u2);

    @Query("select count(q) from QuizAnswer q where q.user.userId=:u2 and q.playWith.userId=:u1 and q.quizComplete = true")
    Long completedByUser2(Long u1, Long u2);



//    Boolean existByUserIdAndPlayWithAndQuizCompleteTrue();


    @Query("select count(a) >0 from QuizAnswer a where (a.user.userId =:u1 and a.playWith.userId =:u2 and a.quizComplete =true)")
    boolean hasCompleted(Long u1 , Long u2);


}
