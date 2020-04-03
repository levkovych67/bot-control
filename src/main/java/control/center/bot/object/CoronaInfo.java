package control.center.bot.object;

public class CoronaInfo {

    private String region;
    private Integer cases;
    private Integer death;


    public CoronaInfo(String region, Integer cases, Integer death) {
        this.region = region;
        this.cases = cases;
        this.death = death;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getCases() {
        return cases;
    }

    public void setCases(Integer cases) {
        this.cases = cases;
    }

    public Integer getDeath() {
        return death;
    }

    public void setDeath(Integer death) {
        this.death = death;
    }
}
