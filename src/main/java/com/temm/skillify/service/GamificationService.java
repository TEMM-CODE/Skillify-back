package com.temm.skillify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.dto.response.LevelProgressResponseDTO;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.UserRepository;

@Service
public class GamificationService {

    @Autowired
    private UserRepository userRepository;

    // Precomputed thresholds for levels 1 to 1000
    private static final int[] LEVEL_THRESHOLDS;

    static {
        int cacheSize = 1000; // Covers levels 1 to 1000
        LEVEL_THRESHOLDS = new int[cacheSize + 1];
        for (int i = 1; i <= cacheSize; i++) {
            LEVEL_THRESHOLDS[i] = (int) Math.round(100 * Math.log(i) / Math.log(2));
        }
    }

    /**
     * Enum representing entity types and their associated XP values.
     */
    public enum EntityType {
        QUESTION(10),  // A = 10 XP
        ESSAY(50),     // B = 50 XP
        LESSON(30);    // C = 30 XP

        private final int xp;

        EntityType(int xp) {
            this.xp = xp;
        }

        public int getXp() {
            return xp;
        }
    }

    /**
     * Calculates the XP threshold for a given level using a logarithmic progression.
     * @param level The level (must be >= 1).
     * @return The XP threshold for the level.
     */
    private static int getLevelThreshold(int level) {
        if (level < 1) return 0;
        if (level < LEVEL_THRESHOLDS.length) {
            return LEVEL_THRESHOLDS[level];
        }
        // Fallback for levels beyond cache
        return (int) Math.round(100 * Math.log(level) / Math.log(2));
    }

    /**
     * Awards XP to a user and updates their level.
     * @param user The user to award XP to.
     * @param xp The amount of XP to award.
     */
    public void awardXp(User user, int xp) {
        user.setXp(user.getXp() + xp);
        user.setLevel(calculateLevel(user.getXp()));
        userRepository.save(user);
    }

    /**
     * Awards XP to a user based on the entity type.
     * @param user The user to award XP to.
     * @param entityType The type of entity (Question, Essay, Lesson).
     */
    public void awardXpForEntity(User user, EntityType entityType) {
        awardXp(user, entityType.getXp());
    }

    /**
     * Calculates the level based on the user's XP using a logarithmic progression.
     * @param xp The user's total XP.
     * @return The corresponding level (minimum 1).
     */
    private int calculateLevel(int xp) {
        if (xp <= 0) return 1; // Handle negative or zero XP
        // Formula: level = floor(2^(xp * log(2) / 100))
        double log2 = Math.log(2);
        double level = Math.pow(2, xp * log2 / 100.0);
        return (int) Math.floor(level);
    }

    /**
     * Calculates the amount of XP needed to reach the next level.
     * @param user The user to calculate for.
     * @return The XP needed to reach the next level.
     */
    public int getXpToNextLevel(User user) {
        int currentLevel = calculateLevel(user.getXp());
        // Calculate XP threshold for next level
        int nextLevelThreshold = getLevelThreshold(currentLevel + 1);
        // Return difference between next level threshold and current XP
        return Math.max(0, nextLevelThreshold - user.getXp());
    }

    public LevelProgressResponseDTO getLevelProgress(User user) {
        int currentXp = user.getXp();
        int currentLevel = calculateLevel(currentXp);
        int nextLevelThreshold = getLevelThreshold(currentLevel + 1);
        int xpToNextLevel = Math.max(0, nextLevelThreshold - currentXp);
        LevelProgressResponseDTO progress = new LevelProgressResponseDTO();
        progress.currentLevel = currentLevel;
        progress.nextLevel = currentLevel + 1;
        progress.xpToNextLevel = xpToNextLevel;
        progress.currentXp = currentXp;
        return progress;
    }
}