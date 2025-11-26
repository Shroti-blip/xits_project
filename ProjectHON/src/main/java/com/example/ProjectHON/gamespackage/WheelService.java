package com.example.ProjectHON.gamespackage;

import com.example.ProjectHON.UserRating.RatingCategory;
import com.example.ProjectHON.UserRating.RatingCategoryRepository;
import com.example.ProjectHON.UserRating.UserRating;
import com.example.ProjectHON.UserRating.UserRatingRepository;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class WheelService {

    @Autowired
    private UserMasterRepository userRepo;

    @Autowired
    private UserRatingRepository ratingRepo;

    @Autowired
    private RatingCategoryRepository categoryRepo;

    // ratingCategoryId for SPIN_REWARD
    private static final Long WHEEL_CATEGORY_ID = 8L;  // Wheel category id
    private static final Long CASINO_CATEGORY_ID = 9L;  // Wheel category id

    public Map<String, Object> handleSpin(Long userId, int reward,String categoryType) {

        UserMaster user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RatingCategory category;
        int spinsThisWeek;

        if(categoryType.equals("CASINO")) {
            category = categoryRepo.findById(CASINO_CATEGORY_ID)
                    .orElseThrow(() -> new RuntimeException("Spin category missing"));
            // Check weekly limit
            spinsThisWeek = ratingRepo.getWeeklySpinCount(userId, CASINO_CATEGORY_ID);
            System.out.println("Inside the Casino part");
        }else{
            category = categoryRepo.findById(WHEEL_CATEGORY_ID)
                    .orElseThrow(() -> new RuntimeException("Spin category missing"));
            // Check weekly limit
            spinsThisWeek = ratingRepo.getWeeklySpinCount(userId, WHEEL_CATEGORY_ID);
            System.out.println("Inside the Wheel part");
        }
        int leftChances=0;
        if(spinsThisWeek==0){
            leftChances=3;
        }else if(spinsThisWeek==1){
            leftChances=2;
        }else if(spinsThisWeek==2){
            leftChances=1;
        }else{
            leftChances=0;
        }

        int wheelSpins = ratingRepo.getWeeklySpinCount(userId, WHEEL_CATEGORY_ID);
        int casinoSpins = ratingRepo.getWeeklySpinCount(userId, CASINO_CATEGORY_ID);



        System.out.println("Wheel spins "+wheelSpins);
        System.out.println("Casino spins "+casinoSpins);
        if (spinsThisWeek >= 3) {
            return Map.of(
                    "status", "success",
                    "reward", reward,
                    "wheelSpins", wheelSpins,
                    "casinoSpins", casinoSpins,
                    "leftChancesWheel", 3 - wheelSpins,
                    "leftChancesCasino", 3 - casinoSpins
            );
        }

        // Save spin reward into user_rating
        UserRating rating = new UserRating();
        rating.setUser(user);
        rating.setRatingCategory(category);
        rating.setPoints(reward);
        rating.setDateTime(LocalDateTime.now());

        ratingRepo.save(rating);

        user.setPoints(user.getPoints()+reward);  // Update use point.....
        userRepo.save(user);

        // Total points
        int totalPoints = ratingRepo.getTotalPoints(userId);


        return Map.of(
                "status", "success",
                "reward", reward,
                "wheelSpins", wheelSpins,
                "casinoSpins", casinoSpins,
                "leftChancesWheel", 3 - wheelSpins,
                "leftChancesCasino", 3 - casinoSpins
        );
    }

}
