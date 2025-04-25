package com.bajaj.webhookapp.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MutualFollowersService {

    public List<List<Integer>> findMutualFollowers(List<List<Integer>> followers) {
        Set<List<Integer>> mutualFollowersPairs = new HashSet<>();

        // Keep track of who follows whom
        Map<Integer, Set<Integer>> followersMap = new HashMap<>();

        // Build the followers map
        for (List<Integer> pair : followers) {
            int follower = pair.get(0);
            int followee = pair.get(1);

            followersMap.computeIfAbsent(follower, k -> new HashSet<>()).add(followee);
        }

        // Check for mutual followers
        for (List<Integer> pair : followers) {
            int follower = pair.get(0);
            int followee = pair.get(1);

            // If B follows A and A follows B, they are mutual followers
            if (followersMap.containsKey(followee) &&
                    followersMap.get(followee).contains(follower)) {

                // Add the pair with smaller ID first
                List<Integer> mutualPair = Arrays.asList(
                        Math.min(follower, followee),
                        Math.max(follower, followee));

                mutualFollowersPairs.add(mutualPair);
            }
        }

        // Convert to list and sort
        return mutualFollowersPairs.stream()
                .sorted(Comparator.comparing((List<Integer> list) -> list.get(0))
                        .thenComparing(list -> list.get(1)))
                .collect(Collectors.toList());
    }
}