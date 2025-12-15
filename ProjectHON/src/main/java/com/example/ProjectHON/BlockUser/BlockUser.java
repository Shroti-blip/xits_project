package com.example.ProjectHON.BlockUser;

import com.example.ProjectHON.User_masterpackage.UserMaster;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class BlockUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private LocalDateTime blockedAt = LocalDateTime.now();


    @ManyToOne
    @JoinColumn(name = "blocker")
    private UserMaster blocker;

    @ManyToOne
    @JoinColumn(name = "blockeduser_id")
    private UserMaster blockedUser;


    public BlockUser(Long id, LocalDateTime blockedAt, UserMaster blocker, UserMaster blockedUser) {
        this.id = id;
        this.blockedAt = blockedAt;
        this.blocker = blocker;
        this.blockedUser = blockedUser;
    }

    public BlockUser() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getBlockedAt() {
        return blockedAt;
    }

    public void setBlockedAt(LocalDateTime blockedAt) {
        this.blockedAt = blockedAt;
    }

    public UserMaster getBlocker() {
        return blocker;
    }

    public void setBlocker(UserMaster blocker) {
        this.blocker = blocker;
    }

    public UserMaster getBlockedUser() {
        return blockedUser;
    }

    public void setBlockedUser(UserMaster blockedUser) {
        this.blockedUser = blockedUser;
    }
}
