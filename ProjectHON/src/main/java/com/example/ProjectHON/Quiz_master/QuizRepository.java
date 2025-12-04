package com.example.ProjectHON.Quiz_master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizRepository extends JpaRepository<QuizQuestion, Integer> {

QuizQuestion findFirstByOrderByIdAsc();

    @Query(value = "SELECT id, question_text, option_a, option_b, category FROM quiz_question WHERE id > :currentId ORDER BY id ASC LIMIT 1", nativeQuery = true)
    QuizQuestion findNext(@Param("currentId") Long currentId);

    @Query("SELECT q FROM QuizQuestion q WHERE q.id <> :currentId ORDER BY function('RAND')")
    List<QuizQuestion> findRandomNext(@Param("currentId") Long currentId);

    @Query(value = "SELECT * FROM quiz_question ORDER BY RANDOM() LIMIT 5", nativeQuery = true)
    List<QuizQuestion> findRandomFive();


    @Query("SELECT qa.question.id FROM QuizAnswer qa " +
            "WHERE (qa.user.userId = :u1 AND qa.playWith.userId = :u2) " +
            "   OR (qa.user.userId = :u2 AND qa.playWith.userId = :u1)")
    List<Long> getSavedQuizQuestionIds(Long u1, Long u2);


    @Query("SELECT q FROM QuizQuestion q WHERE q.id IN :ids")
    List<QuizQuestion> findQuestionsByIds(@Param("ids") List<Long> ids);





}
