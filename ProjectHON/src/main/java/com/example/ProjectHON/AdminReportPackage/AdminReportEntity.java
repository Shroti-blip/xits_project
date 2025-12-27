package com.example.ProjectHON.AdminReportPackage;

import com.example.ProjectHON.BlockUser.BlockUser;
import com.example.ProjectHON.Chat_Report.ChatReport;
import com.example.ProjectHON.User_Report.UserReport;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AdminReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    private UserReport userReport;

    @ManyToOne
    private ChatReport chatReport;

    @ManyToOne
    private BlockUser blockUser;

    @ManyToOne
    @JoinColumn(name = "user_master")
    private UserMaster userMaster;


    private LocalDateTime actionAt = LocalDateTime.now();


    public AdminReportEntity() {
    }

    public AdminReportEntity(Long id, UserReport userReport, ChatReport chatReport, BlockUser blockUser, UserMaster userMaster) {
        this.id = id;
        this.userReport = userReport;
        this.chatReport = chatReport;
        this.blockUser = blockUser;
        this.userMaster = userMaster;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserReport getUserReport() {
        return userReport;
    }

    public void setUserReport(UserReport userReport) {
        this.userReport = userReport;
    }

    public ChatReport getChatReport() {
        return chatReport;
    }

    public void setChatReport(ChatReport chatReport) {
        this.chatReport = chatReport;
    }

    public BlockUser getBlockUser() {
        return blockUser;
    }

    public void setBlockUser(BlockUser blockUser) {
        this.blockUser = blockUser;
    }

    public UserMaster getUserMaster() {
        return userMaster;
    }

    public void setUserMaster(UserMaster userMaster) {
        this.userMaster = userMaster;
    }

    public LocalDateTime getActionAt() {
        return actionAt;
    }

    public void setActionAt(LocalDateTime actionAt) {
        this.actionAt = actionAt;
    }
}




//======================================================

//report user id
//report whisper id
//block user id
