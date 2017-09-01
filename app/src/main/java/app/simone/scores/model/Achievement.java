package app.simone.scores.model;

/**
 * Created by nicola on 01/09/2017.
 */

public class Achievement {

    private int achievementId;
    private int classicServerId;
    private int hardServerId;

    public Achievement(int achievementId, int classicServerId, int hardServerId) {
        this(achievementId, classicServerId);
        this.hardServerId = hardServerId;
    }

    public Achievement(int achievementId, int classicServerId) {
        this.achievementId = achievementId;
        this.classicServerId = classicServerId;
    }

    public int getAchievementId() {
        return achievementId;
    }

    public int getClassicServerId() {
        return classicServerId;
    }

    public int getHardServerId() {
        return hardServerId;
    }
}

