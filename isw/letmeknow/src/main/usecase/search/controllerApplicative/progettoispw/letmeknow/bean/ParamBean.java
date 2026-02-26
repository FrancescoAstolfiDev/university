package progettoispw.letmeknow.bean;

public class ParamBean {
    int [] param;
    public void setParam(int[] param) {
        this.param = param;
    }
    public void setParam(String[] param) {
        this.param=new int[3];
        if(param!=null) {
            for (int i = 0; i < param.length; i++) {
                this.param[i] = Integer.parseInt(param[i]);
            }
        }
        else this.param=null;
    }
    public int[] getParam() {
        return param;
    }
}
