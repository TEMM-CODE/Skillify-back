package com.temm.skillify.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.dto.response.LevelProgressResponseDTO;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.UserRepository;

@Service
public class GamificationService {
    private static final Logger logger = LoggerFactory.getLogger(GamificationService.class);

    @Autowired
    private UserRepository userRepository;

    // Precomputed thresholds for levels 1 to 1000
    private static final int[] LEVEL_THRESHOLDS;

    static {
        int cacheSize = 1000; // Covers levels 1 to 1000
        LEVEL_THRESHOLDS = new int[cacheSize + 1];
        for (int i = 1; i <= cacheSize; i++) {
            // Quadratic progression: threshold = 100 * level^2
            LEVEL_THRESHOLDS[i] = 100 * i * i;
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
     * Calculates the XP threshold for a given level.
     * @param level The level (must be >= 1).
     * @return The XP threshold for the level.
     */
    private static int getLevelThreshold(int level) {
        if (level < 1) return 0;
        if (level < LEVEL_THRESHOLDS.length) {
            return LEVEL_THRESHOLDS[level];
        }
        // Fallback for levels beyond cache
        return 100 * level * level;
    }

    /**
     * Awards XP to a user and updates their level.
     * @param user The user to award XP to.
     * @param xp The amount of XP to award.
     */
    public void awardXp(User user, int xp) {
        int newXp = user.getXp() + xp;
        user.setXp(newXp);
        int newLevel = calculateLevel(newXp);
        user.setLevel(newLevel);
        userRepository.save(user);
        logger.info("Awarded {} XP to user {}. New XP: {}, New Level: {}", xp, user.getEmail(), newXp, newLevel);
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
     * Calculates the level based on the user's XP using a quadratic progression.
     * @param xp The user's total XP.
     * @return The corresponding level (minimum 1).
     */
    private int calculateLevel(int xp) {
        if (xp <= 0) return 1;
        // Find the level where threshold <= xp
        int level = 1;
        while (level < LEVEL_THRESHOLDS.length && LEVEL_THRESHOLDS[level] <= xp) {
            level++;
        }
        // If xp exceeds the last cached threshold, calculate dynamically
        if (level >= LEVEL_THRESHOLDS.length) {
            level = (int) Math.floor(Math.sqrt(xp / 100.0)) + 1;
        }
        return Math.max(1, level - 1); // Return the highest level achieved
    }

    /**
     * Calculates the amount of XP needed to reach the next level.
     * @param user The user to calculate for.
     * @return The XP needed to reach the next level.
     */
    public int getXpToNextLevel(User user) {
        int currentXp = user.getXp();
        int currentLevel = calculateLevel(currentXp);
        int nextLevelThreshold = getLevelThreshold(currentLevel + 1);
        int xpToNextLevel = nextLevelThreshold - currentXp;
        logger.debug("User {}: Current XP: {}, Current Level: {}, Next Level Threshold: {}, XP to Next Level: {}",
                user.getEmail(), currentXp, currentLevel, nextLevelThreshold, xpToNextLevel);
        return Math.max(0, xpToNextLevel);
    }

    /**
     * Retrieves the level progress for a user.
     * @param user The user to calculate progress for.
     * @return The level progress DTO.
     */
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

        logger.info("Level progress for user {}: Level {}, XP {}/{} to next level",
                user.getEmail(), currentLevel, currentXp, nextLevelThreshold);
        return progress;
    }
}