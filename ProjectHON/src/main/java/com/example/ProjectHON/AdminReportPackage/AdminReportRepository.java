package com.example.ProjectHON.AdminReportPackage;

import com.example.ProjectHON.User_Report.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminReportRepository extends JpaRepository<AdminReportEntity , Integer> {

//    Optional<AdminReportEntity> findByUserReport(UserReport userReport);

    @Query("SELECT a FROM AdminReportEntity a WHERE a.userReport.id = :reportId")
    Optional<AdminReportEntity> findByUserReportId(@Param("reportId") Long reportId);

}
