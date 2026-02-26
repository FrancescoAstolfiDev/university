package progettoispw.letmeknow.bean;

public class StringBean {
    String pass;
    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        if(pass.equals("")|| pass.equals(" ")){
            this.pass=null;
        }
        else {
            this.pass = pass;
        }
    }
}
