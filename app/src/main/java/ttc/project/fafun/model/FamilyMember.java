package ttc.project.fafun.model;

/**
 * Created by Fikry-PC on 10/14/2017.
 */

public class FamilyMember {
    String user_id, family_id, name, age, role;

    public FamilyMember(String user_id, String family_id, String name, String age, String role) {
        this.user_id = user_id;
        this.family_id = family_id;
        this.name = name;
        this.age = age;
        this.role = role;
    }

    public FamilyMember(){

    }

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
}
