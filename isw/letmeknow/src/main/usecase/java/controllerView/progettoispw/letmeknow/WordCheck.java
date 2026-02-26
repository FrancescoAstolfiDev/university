package progettoispw.letmeknow;

public class WordCheck {
    private  int goAhead;
    private int delta;
    public WordCheck(int deltaInput, int goAheadInput){
        delta=deltaInput;
        goAhead=goAheadInput;
    }
    public WordCheck(){
        delta=goAhead=-1;
    }
    public  String check(String inputText) {
        if(inputText==null)return null;
        String add;
        String before;
        int count=goAhead-delta;
        for (int k = goAhead-delta; k < inputText.length(); k = k+1) {
            if(inputText.toCharArray()[k]=='\n'){
                count=0;
            }
            if (count==goAhead){
                add = inputText.substring(k);
                before = inputText.substring(0, k);
                inputText = before + "\n" + add;
                count =0;
            } else if(((count)>(goAhead-delta)) && inputText.toCharArray()[k]== 32){
                add = inputText.substring(k+1);
                before = inputText.substring(0, k);
                inputText = before + "\n" + add;
                count=0;
            }else{
                count++;
            }
        }
        return  inputText;
    }
    public String replace(String inputText, char old,String immetti){
        if(inputText==null)return null;
        String add;
        String before;
        int start=inputText.indexOf(old);
        int k=0;
        for(char c : inputText.toCharArray()){
            if(c==old && k!=start)
            {
                    add = inputText.substring(k);
                    before = inputText.substring(0, k);
                    inputText = before + immetti + add;
                    ++k;

            }++k;
        }
        return inputText;
    }
    public Integer goAheadCounter(String inputText){
        Integer contAhead=1;
        for(char c : inputText.toCharArray()){
            if(c=='\n'){
                contAhead++;
            }
        }
        return contAhead;
    }
    public int highText(String text) {
        int linee= goAheadCounter(text);
        if(linee>4){
            return linee* 21;
        }
        return 60;
    }
    public  String checkLen(String inputText,int lenMax){
        if(inputText==null)return " ";
        inputText=inputText.replace("\\n","");
        inputText=inputText.replace("\\r","");
        inputText=inputText.replace("\n","");
        if(inputText.length()<lenMax)return inputText;
        else {
            return inputText.substring(0,lenMax)+"...";
        }
    }
}