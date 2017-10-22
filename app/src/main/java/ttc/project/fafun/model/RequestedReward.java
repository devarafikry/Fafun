package ttc.project.fafun.model;

/**
 * Created by Fikry-PC on 10/21/2017.
 */

public class RequestedReward {
    String reward_name, reward_photo_link;
    int reward_cost_point;
    long requested_time;

    public RequestedReward(){

    }

    public RequestedReward(String reward_name, String reward_photo_link, int reward_cost_point, long requested_time) {
        this.reward_name = reward_name;
        this.reward_photo_link = reward_photo_link;
        this.reward_cost_point = reward_cost_point;
        this.requested_time = requested_time;
    }

    public String getReward_name() {
        return reward_name;
    }

    public void setReward_name(String reward_name) {
        this.reward_name = reward_name;
    }

    public String getReward_photo_link() {
        return reward_photo_link;
    }

    public void setReward_photo_link(String reward_photo_link) {
        this.reward_photo_link = reward_photo_link;
    }

    public int getReward_cost_point() {
        return reward_cost_point;
    }

    public void setReward_cost_point(int reward_cost_point) {
        this.reward_cost_point = reward_cost_point;
    }

    public long getRequested_time() {
        return requested_time;
    }

    public void setRequested_time(long requested_time) {
        this.requested_time = requested_time;
    }
}
