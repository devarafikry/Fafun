package ttc.project.fafun.model;

/**
 * Created by Fikry-PC on 10/19/2017.
 */

public class Task {
    String task_name;
    int period;
    int task_reward_point;
    int category;

    public Task(String task_name, int period, int task_reward_point, int category) {
        this.task_name = task_name;
        this.task_reward_point = task_reward_point;
        this.category = category;
    }

    public Task(){

    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getTask_reward_point() {
        return task_reward_point;
    }

    public void setTask_reward_point(int task_reward_point) {
        this.task_reward_point = task_reward_point;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
