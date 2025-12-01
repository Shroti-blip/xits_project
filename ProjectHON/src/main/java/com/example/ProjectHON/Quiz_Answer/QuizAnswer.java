package com.example.ProjectHON.Quiz_Answer;

import com.example.ProjectHON.Quiz_master.QuizQuestion;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.mutualCrushPackage.MutualCrushMaster;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "quiz_answers")
public class QuizAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which user answered?
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserMaster user;

    @ManyToOne
    @JoinColumn(name="played_with")
    private UserMaster playWith;

    // Which question was answered?
    @ManyToOne
    @JoinColumn(name = "question_id")
    private QuizQuestion question;

    // What option was selected? A or B
    private String selectedOption;

    private LocalDateTime answeredAt = LocalDateTime.now();


    public QuizAnswer() {
    }

    public QuizAnswer(Long id, UserMaster user, UserMaster playWith, QuizQuestion question, String selectedOption, LocalDateTime answeredAt) {
        this.id = id;
        this.user = user;
        this.playWith = playWith;
        this.question = question;
        this.selectedOption = selectedOption;
        this.answeredAt = answeredAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserMaster getUser() {
        return user;
    }

    public void setUser(UserMaster user) {
        this.user = user;
    }

    public QuizQuestion getQuestion() {
        return question;
    }

    public void setQuestion(QuizQuestion question) {
        this.question = question;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public LocalDateTime getAnsweredAt() {
        return answeredAt;
    }

    public void setAnsweredAt(LocalDateTime answeredAt) {
        this.answeredAt = answeredAt;
    }

    public UserMaster getPlayWith() {
        return playWith;
    }

    public void setPlayWith(UserMaster playWith) {
        this.playWith = playWith;
    }
}






//.filter(m -> m.getRequestBy().getId() == current.getUserId())
//        .filter(MutualCrushMaster::isMutualCrush)
//              .collect(Collectors.toList());

// .filter(m -> m.isMutualCrush())                         // accepted
//        .filter(m -> m.getRequestBy().getId() == currentId)     // I requested
//        .map(m -> m.getRequestTo())                             // show only other person
//        .toList();



//List<MutualCrushMaster> masters = master.stream()
//        .filter(MutualCrushMaster::isAccepted) // only accepted
//        .filter(m ->
//                m.getRequestBy().getUserId() == currentUser.getUserId() ||
//                        m.getRequestTo().getUserId() == currentUser.getUserId()
//        )
//        .map(m -> {
//            // return the "other" person, not the logged-in user
//            return m.getRequestBy().getUserId() == currentUser.getUserId()
//                    ? m.getRequestTo()
//                    : m.getRequestBy();
//        })
//        .toList();













