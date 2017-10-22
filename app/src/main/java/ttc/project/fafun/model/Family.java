package ttc.project.fafun.model;

/**
 * Created by Fikry-PC on 10/14/2017.
 */

public class Family {
    String famlity_id, family_name;
    int highestLifetimePoint;

    public Family(){

    }

    public Family(String famlity_id, String family_name, int highestLifetimePoint) {
        this.famlity_id = famlity_id;
        this.family_name = family_name;
        this.highestLifetimePoint = highestLifetimePoint;
    }

    public String getFamlity_id() {
        return famlity_id;
    }

    public void setFamlity_id(String famlity_id) {
        this.famlity_id = famlity_id;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public int getHighestLifetimePoint() {
        return highestLifetimePoint;
    }

    public void setHighestLifetimePoint(int highestLifetimePoint) {
        this.highestLifetimePoint = highestLifetimePoint;
    }
}
