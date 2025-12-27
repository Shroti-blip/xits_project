package com.example.ProjectHON.Post_Report;

import com.example.ProjectHON.Post_masterpackage.PostMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostReportRepository extends JpaRepository<PostReport , Integer> {
    boolean existsByReporterUserIdAndPostreport_PostId(Long reporterId, Long postId);
//    List<PostMaster>findBy


    @Query("SELECT pr.postreport FROM PostReport pr WHERE pr.reporter.userId = :userId")
    List<PostMaster> findPostMasterByUserId(@Param("userId") Long userId);

    void deleteByPostreport(PostMaster post);
    long countByPostreport(PostMaster post);


    List<PostReport> findAllByReporter_UserId(Long userId);

    long countByReporter_UserIdAndStatus(
            Long userId,
            PostReport.ReportStatus status
    );





}
