package com.example.ProjectHON.BlockUser;

import com.example.ProjectHON.User_masterpackage.UserMaster;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

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


    @GetMapping("/user/show-all-block-user")
    public String showAllBlockedUser(Model model , HttpSession session){
        Long user = (Long) session.getAttribute("userId");
          List<BlockUser> blockedList= blockUserRepository.findAllBYBlockerId(user);
          System.out.println("===== list of block user =====" + blockedList.isEmpty());
          model.addAttribute("blockedUserList" , blockedList);

        return "MergePart/show_all_blocked_user";
    }


    @PostMapping("/user/unblock-setting")
    public String unblockUserSetting(@RequestParam("blocker") Long blocker,
                                     @RequestParam("blockedUser") Long blockedUser ,
                                     Model model , RedirectAttributes redirectAttributes){

        System.out.println("---blockedUser id inside delete one is  : " + blockedUser);
        System.out.println("-----blocker id inside delete one is   : " + blocker);

        blockUserRepository.deleteByBlockerAndBlocked(blocker ,blockedUser);
        redirectAttributes.addFlashAttribute("success", true);

        return "redirect:/user/show-all-block-user";
    }
}
