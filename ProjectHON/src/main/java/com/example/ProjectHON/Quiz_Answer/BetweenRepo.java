package com.example.ProjectHON.Quiz_Answer;

import com.example.ProjectHON.User_masterpackage.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BetweenRepo extends JpaRepository<BetweenQuiz,Long> {


//    @Query("select m.isPlayed from BetweenQuiz m.fromQuiz=:fromQuiz and m.toQuiz=:toQuiz")
//    Boolean getFindResult(UserMaster fromQuiz,UserMaster toQuiz);

//    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM BetweenQuiz b"+
//   " WHERE b.user.id = :userId AND b.playedWith = :otherId AND b.quizComplete = true")
//    boolean hasUserPlayed(@Param("userId") Long userId, @Param("otherId") Long otherId);

        @Query("""
        SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
        FROM BetweenQuiz b
        WHERE b.fromQuiz.userId = :userId
          AND b.toQuiz.userId = :otherId
          AND b.isPlayed = true
    """)
        boolean hasUserPlayed(@Param("userId") Long userId,
                              @Param("otherId") Long otherId);
}


