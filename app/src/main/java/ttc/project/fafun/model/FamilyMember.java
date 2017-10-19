package ttc.project.fafun.model;

/**
 * Created by Fikry-PC on 10/14/2017.
 */

public class FamilyMember {
    String user_id, family_id, name, age, role, avatar_link;
    int user_point_lifetime, user_point, task_completed;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFamily_id() {
        return family_id;
    }

    public void setFamily_id(String family_id) {
        this.family_id = family_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAvatar_link() {
        return avatar_link;
    }

    public void setAvatar_link(String avatar_link) {
        this.avatar_link = avatar_link;
    }

    public int getUser_point_lifetime() {
        return user_point_lifetime;
    }

    public void setUser_point_lifetime(int user_point_lifetime) {
        this.user_point_lifetime = user_point_lifetime;
    }

    public int getUser_point() {
        return user_point;
    }

    public void setUser_point(int user_point) {
        this.user_point = user_point;
    }

    public int getTask_completed() {
        return task_completed;
    }

    public void setTask_completed(int task_completed) {
        this.task_completed = task_completed;
    }

    public FamilyMember(){

    }

    public FamilyMember(String user_id, String family_id, String name, String age, String role, String avatar_link, int user_point_lifetime, int user_point, int task_completed) {
        this.user_id = user_id;
        this.family_id = family_id;
        this.name = name;
        this.age = age;
        this.role = role;
        this.avatar_link = avatar_link;
        this.user_point_lifetime = user_point_lifetime;
        this.user_point = user_point;
        this.task_completed = task_completed;
    }
}
