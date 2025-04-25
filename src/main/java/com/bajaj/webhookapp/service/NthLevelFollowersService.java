package com.bajaj.webhookapp.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NthLevelFollowersService {

    public List<Integer> findNthLevelFollowers(List<List<Integer>> followers, Integer userId, Integer n) {
        // Build adjacency list for the followers graph
        Map<Integer, List<Integer>> graph = new HashMap<>();

        for (List<Integer> pair : followers) {
            int follower = pair.get(0);
            int followee = pair.get(1);

            graph.computeIfAbsent(follower, k -> new ArrayList<>()).add(followee);
        }

        // Use BFS to find followers at exactly n levels away
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> levels = new HashMap<>();

        queue.offer(userId);
        visited.add(userId);
        levels.put(userId, 0);

        Set<Integer> nthLevelUsers = new HashSet<>();

        while (!queue.isEmpty()) {
            int current = queue.poll();
            int currentLevel = levels.get(current);

            if (currentLevel == n) {
                nthLevelUsers.add(current);
                continue; // No need to explore further from this node
            }

            if (currentLevel > n) {
                break; // We've gone beyond the required level
            }

            // Get the followers of the current user
            List<Integer> currentFollowers = graph.getOrDefault(current, Collections.emptyList());

            for (int follower : currentFollowers) {
                if (!visited.contains(follower)) {
                    queue.offer(follower);
                    visited.add(follower);
                    levels.put(follower, currentLevel + 1);
                }
            }
        }

        // Remove the starting user if it's part of the result (shouldn't happen but
        // just in case)
        nthLevelUsers.remove(userId);

        // Convert to sorted list
        return nthLevelUsers.stream()
                .sorted()
                .collect(Collectors.toList());
    }
}