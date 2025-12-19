package com.example.ProjectHON.User_Report;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserReportRepository extends JpaRepository<UserReport , Long> {

    @Query("select count(c) from UserReport  c where c.ownerUser.userId =:ownerId")
    int countTotalReportsForUser(@Param("ownerId") Long ownerId);

    @Modifying
    @Transactional
    @Query("delete from UserReport d where (d.ownerUser.userId = :ownerId and d.reporterUser.userId =:reporterId)")
    void deleteFromUserReport(@Param("ownerId") Long ownerId , @Param("reporterId") Long reporterId);

    @Query("SELECT c FROM UserReport c WHERE c.reporterUser.userId = :reporterId")
    List<UserReport> findAllByReporterId(@Param("reporterId") Long reporterId);

}

