package com.example.ProjectHON.GroupChatReport.GroupReport;

import com.example.ProjectHON.Group.GroupChatMaster;
import com.example.ProjectHON.Group.GroupMaster;
import com.example.ProjectHON.GroupChatReport.GroupChatReportEntity;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class GroupReport {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String reason ;
    private String comment;
    private LocalDateTime dateTime;

//    @ManyToOne
//    private UserMaster owner;

    @ManyToOne
    private UserMaster reporter;

//    @ManyToOne
//    private GroupChatMaster groupChatMaster;

    @ManyToOne
    private GroupMaster groupMaster;

    public enum ReportStatus {
        PENDING,        // newly reported, admin hasn't seen it
        IN_REVIEW,      // admin opened it
        ACTION_TAKEN,   // warning / block / delete done
        REJECTED            // invalid / false report
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)//
    private GroupReport.ReportStatus status = GroupReport.ReportStatus.PENDING;


    public GroupReport() {
    }

    public GroupReport(Long id, String reason, String comment, LocalDateTime dateTime,  UserMaster reporter, GroupMaster groupMaster, ReportStatus status) {
        this.id = id;
        this.reason = reason;
        this.comment = comment;
        this.dateTime = dateTime;
        this.reporter = reporter;
        this.groupMaster = groupMaster;
        this.status = status;
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



    public UserMaster getReporter() {
        return reporter;
    }

    public void setReporter(UserMaster reporter) {
        this.reporter = reporter;
    }

    public GroupMaster getGroupMaster() {
        return groupMaster;
    }

    public void setGroupMaster(GroupMaster groupMaster) {
        this.groupMaster = groupMaster;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }
}
