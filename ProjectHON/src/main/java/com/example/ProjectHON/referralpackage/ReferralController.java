package com.example.ProjectHON.referralpackage;

import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class ReferralController {

    @Autowired
    UserMasterRepository userMasterRepository;

    @Autowired
    ReferralRepository referralRepository;

    @GetMapping("/referral")
    public String getReferrals(HttpSession session, Model model){
        Long userId=(Long)session.getAttribute("userId");
        UserMaster user=userMasterRepository.findById(userId).orElse(null);

        List<ReferralMaster> referredByMe=referralRepository.findByReferredFromUser(user);



        List<Long> referredUserIds = referredByMe.stream()
                .map(r -> r.getReferredToUser().getUserId())
                .collect(Collectors.toList());

        List<UserMaster>referredUsers=userMasterRepository.getUsersByUserIds(referredUserIds);

        System.out.println("Referred User Size "+referredUsers.size());
//        model.addAttribute("referredUsers",referredByMe);
        model.addAttribute("referredUsers",referredUsers);
        model.addAttribute("user_master",user);


        return "MergePart/showreferral";
    }
}
