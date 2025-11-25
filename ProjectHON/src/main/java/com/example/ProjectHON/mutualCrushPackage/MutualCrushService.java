package com.example.ProjectHON.mutualCrushPackage;

import com.example.ProjectHON.Post_masterpackage.PostRepository;
import com.example.ProjectHON.Rating_masterpackage.RatingRepository;
//import com.example.ProjectHON.Rating_masterpackage.UserRatingRepository;
import com.example.ProjectHON.UserRating.UserRatingRepository;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MutualCrushService {

    private Double minAvgRating = 7.0;
    private long minPostCount = 3L;

    @Autowired
    private RatingRepository ratingRepo;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private MutualCrushRepository mutualRepo;

    @Autowired
    UserMasterRepository userRepo;

    @Autowired
    UserRatingRepository userRatingRepo;


    public double avgRatingFromTo(UserMaster from, UserMaster to) {
        List<Double> ratings = ratingRepo.findByUserFromAndUserTo(from, to)
                .stream()
                .map(r -> r.getRating().doubleValue())
                .collect(Collectors.toList());

        return ratings.isEmpty() ? 0.0 :
                ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public long postCount(UserMaster u) {
        return postRepo.countByUser(u);
    }

    public boolean bothMeetEligibility(UserMaster a, UserMaster b) {
        double avgAtoB = avgRatingFromTo(a, b);
        double avgBtoA = avgRatingFromTo(b, a);
        long postsA = postCount(a);
        long postsB = postCount(b);

        boolean condA = (avgAtoB > minAvgRating) && (postsB >= minPostCount);
        boolean condB = (avgBtoA > minAvgRating) && (postsA >= minPostCount);


        return condA && condB;
    }

    @Transactional
    public MutualCrushMaster sendInvite(UserMaster requestBy, UserMaster requestTo) {
        Optional<MutualCrushMaster> record = mutualRepo.findByRequestByAndRequestTo(requestBy, requestTo);
        MutualCrushMaster mc = record.orElse(new MutualCrushMaster(requestBy, requestTo));
//        mc.setCanInvite(false);
        mc.setRequested(true);
        mc.setRequestedAt(java.time.LocalDateTime.now());
//        mc.setAccepted(false);
        mc.setIgnored(false);
        mutualRepo.save(mc);
        return mc;
    }

    @Transactional
    public void acceptInvite(MutualCrushMaster invite) {
//        invite.setAccepted(true);
//        invite.setMutualCrush(true);
//        mutualRepo.save(invite);

        UserMaster user1 = userRepo.findById(invite.getRequestBy().getUserId()).orElse(null);
        UserMaster user2 = userRepo.findById(invite.getRequestTo().getUserId()).orElse(null);
        Optional<MutualCrushMaster> record = mutualRepo.findByRequestByAndRequestTo(invite.getRequestBy(), invite.getRequestTo());
        if (record.isPresent()) {
            MutualCrushMaster mc = record.get();
            mc.setAccepted(true);
//            mc.setMutualCrush(true);
//            System.out.println("u1 points = "+user1.getPoints());
//            System.out.println("u2 points = "+user2.getPoints());

            mutualRepo.save(mc);


        } else {
            MutualCrushMaster mc = new MutualCrushMaster(invite.getRequestBy(), invite.getRequestTo());
            mc.setRequested(true);
            mc.setAccepted(true);
//            mc.setMutualCrush(true);
            mutualRepo.save(mc);
        }
        user1.setPoints(user1.getPoints()+50D);
        user2.setPoints(user2.getPoints()+50D);
        userRepo.save(user1);
        userRepo.save(user2);
        MutualCrushNotifier.notifyUsersOfMutual(user1.getUserId(), user2.getUserId());
    }

    @Transactional
    public void ignoreInvite(MutualCrushMaster invite) {
        invite.setIgnored(true);
        mutualRepo.save(invite);
    }

    @Transactional
    public void removeMutualCrush(UserMaster a, UserMaster b) {
        UserMaster user1 = userRepo.findById(a.getUserId()).orElse(null);
        UserMaster user2 = userRepo.findById(b.getUserId()).orElse(null);
        user1.setPoints(user1.getPoints()-50D);
        user2.setPoints(user2.getPoints()-50D);
        userRepo.save(user1);
        userRepo.save(user2);
        mutualRepo.findByRequestByAndRequestTo(a, b).ifPresent(m -> {
//            m.setMutualCrush(false);
            m.setAccepted(false);
            m.setRequested(false);
            mutualRepo.save(m);
        });
        mutualRepo.findByRequestByAndRequestTo(b, a).ifPresent(m -> {
//            m.setMutualCrush(false);
            m.setAccepted(false);
            m.setRequested(false);
            mutualRepo.save(m);
        });

    }

//    public List<MutualCrushMaster> getPendingRequestsFor(UserMaster toUser) {
//        return mutualRepo.findByRequestToAndRequestedTrueAndAcceptedFalseAndIgnoredFalse(toUser);
//    }

    public List<MutualCrushMaster> getMutualCrushes(UserMaster user) {
        List<MutualCrushMaster> crushes = mutualRepo.findByRequestByAndAcceptedTrue(user);
        crushes.addAll(mutualRepo.findByRequestToAndAcceptedTrue(user));
        return crushes;
    }
}
