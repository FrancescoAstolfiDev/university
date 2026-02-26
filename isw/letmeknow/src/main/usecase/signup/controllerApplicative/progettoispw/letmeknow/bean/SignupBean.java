package progettoispw.letmeknow.bean;

public class SignupBean {
    String email;
    String password;
    String description;
    String goal;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description=description;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
    public String[] getAll(){
        return new String[] {password,email,description,goal};
    }
}
