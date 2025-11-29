package com.example.ProjectHON.mutualCrushPackage;


import com.example.ProjectHON.User_masterpackage.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MutualCrushRepository extends JpaRepository<MutualCrushMaster, Long> {

    Optional<MutualCrushMaster> findByRequestByAndRequestTo(UserMaster requestBy, UserMaster requestTo);

    List<MutualCrushMaster> findByRequestByAndRequestedTrueAndAcceptedFalseAndIgnoredFalse(UserMaster user);

    List<MutualCrushMaster> findByRequestToAndRequestedTrueAndAcceptedFalseAndIgnoredFalse(UserMaster requestTo);

    List<MutualCrushMaster> findByRequestBy(UserMaster requestBy);

    List<MutualCrushMaster> findByRequestByAndAcceptedTrue(UserMaster requestBy);

    List<MutualCrushMaster> findByRequestTo(UserMaster requestTo);
    List<MutualCrushMaster> findByRequestToAndAcceptedTrue(UserMaster requestTo);

    @Query("SELECT m FROM MutualCrushMaster m WHERE (m.requestBy = :user OR m.requestTo = :user) AND m.accepted = true")
    List<MutualCrushMaster> findByUserAsMutual(UserMaster user);

    @Query("SELECT m FROM MutualCrushMaster m WHERE (m.requestBy = :user AND m.requested= true) OR (m.requestTo = :user AND m.accepted=true)")
    List<MutualCrushMaster> findByFirstUser(UserMaster user);

    @Query("SELECT m FROM MutualCrushMaster m WHERE (m.requestBy = :u1 AND m.requestTo = :u2) OR (m.requestBy = :u2 AND m.requestTo = :u1)")
    MutualCrushMaster findBetweenUsers(@Param("u1") UserMaster u1, @Param("u2") UserMaster u2);

    @Query("SELECT COUNT(m) FROM MutualCrushMaster m " +
            "WHERE (m.requestBy = :user OR m.requestTo = :user) " +
            "AND m.accepted = true")
    long countMutualCrushes(@Param("user") UserMaster user);

//


//    @Query("SELECT m FROM MutualCrushMaster m " +
//            "WHERE m.accepted = true " +
//            "AND (m.requestBy.id = :userId OR m.requestTo.id = :userId)")
//    List<MutualCrushMaster> findMutualCrushesByUser(@Param("userId") Long userId);



}

