package com.example.ProjectHON.mutualCrushPackage;

import com.example.ProjectHON.User_masterpackage.UserMaster;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class MutualCrushMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "request_by")
    private UserMaster requestBy; //user1 that has requested another user

    @ManyToOne
    @JoinColumn(name = "request_to")
    private UserMaster requestTo; // user2 that is requested
    private boolean requested = false; // user1 can send or invite another once the condition is met, after request this will be true

    @Column(name = "mutual_crush")
    private boolean accepted = false; // can only be true if user2 accepts the invitation (within time duration)

    private boolean ignored = false; // if user chose to ignore the invitation from user1(for requested is true), if requested is false, user1 will not be suggested to invite
    private LocalDateTime requestedAt = LocalDateTime.now(); // the date time when user1 has requested or sent an invitation

    public MutualCrushMaster() {
    }



    public MutualCrushMaster(Long id, UserMaster requestBy, UserMaster requestTo, boolean requested, boolean accepted, boolean ignored, boolean isMutualCrush, LocalDateTime requestedAt) {
        this.id = id;
        this.requestBy = requestBy;
        this.requestTo = requestTo;
        this.requested = requested;
        this.accepted = accepted;
        this.ignored = ignored;
        this.requestedAt = requestedAt;
    }

    public MutualCrushMaster(UserMaster requestBy, UserMaster requestTo) {
        this.requestBy = requestBy;
        this.requestTo = requestTo;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setRequestBy(UserMaster requestBy) {
        this.requestBy = requestBy;
    }

    public void setRequestTo(UserMaster requestTo) {
        this.requestTo = requestTo;
    }

    public boolean isRequested() {
        return requested;
    }

    public void setRequested(boolean requested) {
        this.requested = requested;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isIgnored() {
        return ignored;
    }

    public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public Long getId() {
        return id;
    }

    public UserMaster getRequestBy() {
        return requestBy;
    }

    public UserMaster getRequestTo() {
        return requestTo;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }
}
