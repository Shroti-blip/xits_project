package com.example.ProjectHON.BlockUser;

import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BlockUserController {

    @Autowired
    UserMasterRepository userMasterRepository;

    @Autowired
    BlockUserService blockUserService;

    @Autowired
    BlockUserRepository blockUserRepository;

    @PostMapping("/user/blockUser")
    public String blockUser(@RequestParam("blockedUser") Long blockedUser ,
                            @RequestParam("blocker") Long blocker, Model model){

        blockUserService.saveInfo(blockedUser , blocker);
        model.addAttribute("isBlocked" , true);
        System.out.println("---blockedUser id is : " + blockedUser);
        System.out.println("-----blocker id is  : " + blocker);


        return "redirect:/whisper/"+blockedUser;//inside whisper websocket controller.(changes.)
    }

    @PostMapping("/user/unblockUser")
    public String unblockUser( @RequestParam("blocker") Long blocker,
                             @RequestParam("blockedUser") Long blockedUser ,
                              Model model){

        System.out.println("---blockedUser id inside delete one is  : " + blockedUser);
        System.out.println("-----blocker id inside delete one is   : " + blocker);

        blockUserRepository.deleteByBlockerAndBlocked(blocker ,blockedUser);
        return "redirect:/whisper/"+blockedUser;
    }
}
