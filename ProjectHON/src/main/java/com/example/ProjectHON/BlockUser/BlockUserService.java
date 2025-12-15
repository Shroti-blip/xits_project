package com.example.ProjectHON.BlockUser;

import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BlockUserService {

    @Autowired
    BlockUserRepository blockUserRepository;

    @Autowired
    UserMasterRepository userMasterRepository;

    public BlockUser saveInfo(Long blockedUserId , Long blockerId){

       UserMaster blockedUser =  userMasterRepository.findById(blockedUserId).orElse(null);
       UserMaster blocker = userMasterRepository.findById(blockerId).orElse(null);

       System.out.println("----inside service blockedUser ID is : " + blockedUser.getUserId());
       System.out.println("-----inside service blocker Id is : " + blocker.getUserId());
       BlockUser b = new BlockUser();
       b.setBlockedAt(LocalDateTime.now());
       b.setBlockedUser(blockedUser);
       b.setBlocker(blocker);

       return blockUserRepository.save(b);

    }


}
