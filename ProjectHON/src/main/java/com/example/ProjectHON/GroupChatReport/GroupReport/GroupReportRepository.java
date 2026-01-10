package com.example.ProjectHON.GroupChatReport.GroupReport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GroupReportRepository extends JpaRepository<GroupReport , Long> {


    @Query("DELETE FROM GroupReport gm WHERE gm.groupMaster.groupId = :groupId")
    void deleteByGroupId(Long groupId);
}
