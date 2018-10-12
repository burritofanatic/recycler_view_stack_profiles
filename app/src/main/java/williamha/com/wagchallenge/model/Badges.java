package williamha.com.wagchallenge.model;

public class Badges {

    private Integer gold;
    private Integer silver;
    private Integer bronze;

    public Badges(Integer gold, Integer silver, Integer bronze) {
        this.gold = gold;
        this.silver = silver;
        this.bronze = bronze;
    }

    public Integer getGold() {
        return gold;
    }


    public Integer getSilver() {
        return silver;
    }


    public Integer getBronze() {
        return bronze;
    }
}
