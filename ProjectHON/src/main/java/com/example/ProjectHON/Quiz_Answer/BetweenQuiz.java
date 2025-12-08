package com.example.ProjectHON.Quiz_Answer;

import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.mutualCrushPackage.MutualCrushMaster;
import com.example.ProjectHON.streakhistorypackage.StreakHistory;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class BetweenQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id", nullable = false)
    private UserMaster fromQuiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id", nullable = false)
    private UserMaster toQuiz;

    private Boolean isPlayed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserMaster getFromQuiz() {
        return fromQuiz;
    }

    public void setFromQuiz(UserMaster fromQuiz) {
        this.fromQuiz = fromQuiz;
    }

    public UserMaster getToQuiz() {
        return toQuiz;
    }

    public void setToQuiz(UserMaster toQuiz) {
        this.toQuiz = toQuiz;
    }

    public Boolean getPlayed() {
        return isPlayed;
    }

    public void setPlayed(Boolean played) {
        isPlayed = played;
    }
}
