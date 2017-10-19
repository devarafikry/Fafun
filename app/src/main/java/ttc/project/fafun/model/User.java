package ttc.project.fafun.model;

/**
 * Created by Fikry-PC on 10/12/2017.
 */

public class User {
    //user type 0 untuk kepala keluarga (admin)
    //user type 1 untuk anggota keluarga
    String user_email, family_id;
    int user_type;

    public User(){

    }

    public User(String user_email, String family_id, int user_type) {
        this.user_email = user_email;
        this.family_id = family_id;
        this.user_type = user_type;
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

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }
}
