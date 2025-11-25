package com.example.ProjectHON.mutualCrushPackage;

import java.util.HashSet;
import java.util.Set;

public class MutualCrushNotifier {
    private static Set<Long> notifiedUsers = new HashSet<>();

    public static void notifyUsersOfMutual(Long user1Id, Long user2Id) {
        notifiedUsers.add(user1Id);
        notifiedUsers.add(user2Id);
    }

    public static boolean shouldShowPopup(Long userId) {
        return notifiedUsers.contains(userId);
    }

    public static void clearPopup(Long userId) {
        notifiedUsers.remove(userId);
    }
}
