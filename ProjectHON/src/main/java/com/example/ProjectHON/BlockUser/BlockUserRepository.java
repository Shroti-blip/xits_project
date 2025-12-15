package com.example.ProjectHON.BlockUser;

import com.example.ProjectHON.User_masterpackage.UserMaster;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import software.amazon.awssdk.services.rekognition.endpoints.internal.Value;

public interface BlockUserRepository extends JpaRepository<BlockUser , Integer> {

    boolean existsByBlocker_UserIdAndBlockedUser_UserId(Long blockerId, Long blockedId);

//    boolean deleteByBlocker_UserIdAndBlockedUser_UserId(Long blockerId , Long blockedUserId);

    @Modifying
    @Transactional
    @Query("DELETE FROM BlockUser bu WHERE bu.blocker.userId = :blockerId AND bu.blockedUser.userId = :blockedId")
    int deleteByBlockerAndBlocked(@Param("blockerId") Long blockerId, @Param("blockedId") Long blockedId);



}



//void deleteByBlocker_UserIdAndBlockedUser_UserId(Long blockerId, Long blockedId);
