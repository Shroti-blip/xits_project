package com.example.ProjectHON.User_Report;

import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserReportService {


    @Autowired
    UserReportRepository userReportRepository;

    @Autowired
    UserMasterRepository userMasterRepository;

    public UserReport saveReport(String reason , String comment , Long reporterId , Long ownerId){

        UserMaster reporter = userMasterRepository.findById(reporterId).orElse(null);
        UserMaster owner = userMasterRepository.findById(ownerId).orElse(null);

        System.out.println("----------reporterId is ----------" + reporter.getUserId());
        System.out.println("-------------ownerId is-------------"+owner.getUserId());
        UserReport u = new UserReport();
        u.setDateTime(LocalDateTime.now());
        u.setReason(reason);
        u.setComment(comment);
        u.setReporterUser(reporter);
        u.setOwnerUser(owner);

        return userReportRepository.save(u);
    }
}
