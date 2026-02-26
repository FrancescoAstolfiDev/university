package progettoispw.letmeknow.bean;

public class TwoStringsBean {
    private String string1;
    public boolean setStrings(String string1,String string2) {
        this.string1=string1;
        if(string1==null||string1.equals(""))return false;
        return string1.equals(string2);
    }
    public String getString1() {
        return string1;
    }
}
