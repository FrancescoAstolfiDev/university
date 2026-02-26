package progettoispw.letmeknow.bean;

public class DateBean {
    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        if (!date.equals("")) {
            int count = 0;
            for (char elem : date.toCharArray()) if (elem == '-') ++count;
            if (count != 2) {
              this.date=null;
            }
        } else {
            this.date = null;
        }
    }
}
