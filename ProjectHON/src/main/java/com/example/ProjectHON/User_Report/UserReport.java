package com.example.ProjectHON.User_Report;


import com.example.ProjectHON.User_masterpackage.UserMaster;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class UserReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String reason;

    private String comment;

    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "reporter_user")
    private UserMaster reporterUser;

    @ManyToOne
    @JoinColumn(name="owner_user")
    private UserMaster ownerUser;

    public UserReport(Long id, String reason, String comment, LocalDateTime dateTime, UserMaster reporterUser, UserMaster ownerUser) {
        this.id = id;
        this.reason = reason;
        this.comment = comment;
        this.dateTime = dateTime;
        this.reporterUser = reporterUser;
        this.ownerUser = ownerUser;
    }

    public UserReport() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public UserMaster getReporterUser() {
        return reporterUser;
    }

    public void setReporterUser(UserMaster reporterUser) {
        this.reporterUser = reporterUser;
    }

    public UserMaster getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(UserMaster ownerUser) {
        this.ownerUser = ownerUser;
    }
}
