package com.example.ProjectHON.Quiz_Answer;

import com.example.ProjectHON.Quiz_master.QuizQuestion;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Integer> {


    @Query("SELECT qa FROM QuizAnswer qa" +
        " WHERE qa.user=:userBy AND" +
        " qa.playWith=:userTo AND" +
        " qa.question=:quizQuestion")
    Optional<QuizAnswer>findPlayWithUserQuestionByUser(@Param("userBy")UserMaster userBy,
                                                       @Param("userTo")UserMaster userTo,
                                                       @Param("quizQuestion")QuizQuestion quizQuestion);
}
