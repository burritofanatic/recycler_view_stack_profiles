package williamha.com.wagchallenge.model;

public class StackProfile {

    private String displayName;
    private Badges badges;
    private String gravatar;

    public StackProfile(String displayName, Badges badges, String gravatar) {
        this.displayName = displayName;
        this.badges = badges;
        this.gravatar = gravatar;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Badges getBadges() {
        return badges;
    }

    public String getGravatar() {
        return gravatar;
    }
}
