package com.example.ProjectHON.AdminCircleReport;

import com.example.ProjectHON.BlockUser.BlockUser;
import com.example.ProjectHON.Chat_Report.ChatReport;
import com.example.ProjectHON.GroupChatReport.GroupChatReportEntity;
import com.example.ProjectHON.User_Report.UserReport;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AdminCircleReportEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    private GroupChatReportEntity groupChatReport;


    private LocalDateTime dateTime;


    public AdminCircleReportEntity(Long id, GroupChatReportEntity groupChatReport, LocalDateTime dateTime) {
        this.id = id;
        this.groupChatReport = groupChatReport;
        this.dateTime = dateTime;
    }

    public AdminCircleReportEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GroupChatReportEntity getGroupChatReport() {
        return groupChatReport;
    }

    public void setGroupChatReport(GroupChatReportEntity groupChatReport) {
        this.groupChatReport = groupChatReport;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
