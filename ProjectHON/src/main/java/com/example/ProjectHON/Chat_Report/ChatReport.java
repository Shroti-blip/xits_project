package com.example.ProjectHON.Chat_Report;


import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.Whisper_masterpackage.WhisperMaster;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ChatReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    private String reason;

    private String comment;

    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name="whisper_id")
    private WhisperMaster whisperMaster;

    @ManyToOne
    @JoinColumn(name="owner_id")
    private UserMaster owner;

    @ManyToOne
    @JoinColumn(name="reporter_id")
    private UserMaster reporter;

    public ChatReport(int id, String reason, String comment, LocalDateTime dateTime, WhisperMaster whisperMaster, UserMaster owner, UserMaster reporter) {
        this.id = id;
        this.reason = reason;
        this.comment = comment;
        this.dateTime = dateTime;
        this.whisperMaster = whisperMaster;
        this.owner = owner;
        this.reporter = reporter;
    }

    public ChatReport() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public WhisperMaster getWhisperMaster() {
        return whisperMaster;
    }

    public void setWhisperMaster(WhisperMaster whisperMaster) {
        this.whisperMaster = whisperMaster;
    }

    public UserMaster getOwner() {
        return owner;
    }

    public void setOwner(UserMaster owner) {
        this.owner = owner;
    }

    public UserMaster getReporter() {
        return reporter;
    }

    public void setReporter(UserMaster reporter) {
        this.reporter = reporter;
    }
}


//id , reason(combo) , comment(optional) , msg , fk(msg id)  , sender id
//notification ->owner + admin(skip for now).
//landing page
//