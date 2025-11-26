package com.example.ProjectHON.gamespackage;

import com.example.ProjectHON.UserRating.UserRatingRepository;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class WheelController {

    @Autowired
    private WheelService wheelService;

    @Autowired
    private UserMasterRepository userMasterRepository;

    @Autowired
    private UserRatingRepository ratingRepo;

    @GetMapping("/games")
    public String getWhisper(HttpSession session, Model model){


       UserMaster userMaster=(UserMaster)session.getAttribute("user_master");

       int wheelSpinThisWeek=ratingRepo.getWeeklySpinCount(userMaster.getUserId(),8L);// 8L means Wheel category id..
        int casinoSpinThisWeek=ratingRepo.getWeeklySpinCount(userMaster.getUserId(),9L);// 9L means Casino category id..

        if(wheelSpinThisWeek>=3){
            model.addAttribute("wheelLimit",true);
        }else{
            model.addAttribute("wheelLimit",false);
        }
        if(casinoSpinThisWeek>=3){
            model.addAttribute("casinoLimit",true);
        }else{
            model.addAttribute("casinoLimit",false);
        }
        int wheelChances;
        int casinoChances;
        if(wheelSpinThisWeek==0){
            wheelChances=3;
        }else if(wheelSpinThisWeek==1){
            wheelChances=2;
        }else if(wheelSpinThisWeek==2){
            wheelChances=1;
        }else{
            wheelChances=0;
        }
        if(casinoSpinThisWeek==0){
            casinoChances=3;
        }else if(casinoSpinThisWeek==1){
            casinoChances=2;
        }else if(casinoSpinThisWeek==2){
            casinoChances=1;
        }else{
            casinoChances=0;
        }
        model.addAttribute("wheelLimitCounter",wheelChances);
        model.addAttribute("casinoLimitCounter",casinoChances);

        return "MergePart/games";
    }

    @PostMapping("/spinWheel")
    @ResponseBody
    public ResponseEntity<?> spinWheel(HttpSession session,
                                       @RequestParam("reward") int reward,
                                       @RequestParam("category")String category) {

        System.out.println("Category is "+category);
        Long userId=(Long)session.getAttribute("userId");
        return ResponseEntity.ok(wheelService.handleSpin(userId, reward,category));
    }
}
