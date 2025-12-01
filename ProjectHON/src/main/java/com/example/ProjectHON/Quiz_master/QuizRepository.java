package com.example.ProjectHON.Quiz_master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizRepository extends JpaRepository<QuizQuestion, Integer> {

QuizQuestion findFirstByOrderByIdAsc();

    @Query(value = "SELECT id, question_text, option_a, option_b, category FROM quiz_question WHERE id > :currentId ORDER BY id ASC LIMIT 1", nativeQuery = true)
    QuizQuestion findNext(@Param("currentId") Long currentId);

    @Query("SELECT q FROM QuizQuestion q WHERE q.id <> :currentId ORDER BY function('RAND')")
    List<QuizQuestion> findRandomNext(@Param("currentId") Long currentId);


    // Load next question by ID
//    @Query("SELECT q FROM QuizQuestion q WHERE q.id > :currentId ORDER BY q.id ASC")
//    List<QuizQuestion> findNext(@Param("currentId") int currentId);
}
