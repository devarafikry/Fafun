package ttc.project.fafun.model;

/**
 * Created by Fikry-PC on 10/12/2017.
 */

public class User {
    //user type 0 untuk kepala keluarga (admin)
    //user type 1 untuk anggota keluarga
    String user_name, user_email, family_id, avatar_link;
    int user_type, user_point, user_task_completed;

    public User(){

    }

    public User(String user_name, String user_email, String family_id, String avatar_link, int user_type, int user_point, int user_task_completed) {
        this.user_name = user_name;
        this.user_email = user_email;
        this.family_id = family_id;
        this.avatar_link = avatar_link;
        this.user_type = user_type;
        this.user_point = user_point;
        this.user_task_completed = user_task_completed;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getFamily_id() {
        return family_id;
    }

    public void setFamily_id(String family_id) {
        this.family_id = family_id;
    }

    public String getAvatar_link() {
        return avatar_link;
    }

    public void setAvatar_link(String avatar_link) {
        this.avatar_link = avatar_link;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public int getUser_point() {
        return user_point;
    }

    public void setUser_point(int user_point) {
        this.user_point = user_point;
    }

    public int getUser_task_completed() {
        return user_task_completed;
    }

    public void setUser_task_completed(int user_task_completed) {
        this.user_task_completed = user_task_completed;
    }
}
