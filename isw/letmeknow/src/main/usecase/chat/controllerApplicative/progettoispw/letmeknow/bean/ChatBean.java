package progettoispw.letmeknow.bean;

import progettoispw.letmeknow.controller.ChatController;
import progettoispw.letmeknow.controller.Factory;
import progettoispw.letmeknow.controller.chat.Message;
import progettoispw.letmeknow.controller.chat.Messages;
import progettoispw.letmeknow.controller.search.Search;
import progettoispw.letmeknow.controller.usruser.UsrUser;

import java.util.ArrayList;

public class ChatBean{
    private ChatController controller;
    private String with;
    public ChatBean() {
        controller=new ChatController();
        with= controller.getWith() ;
    }
    public String getWith(){
        return with;
    }
    public String[]getMSG(){
        ArrayList<Message> chat= (ArrayList<Message>) controller.getChat();
        String [] returnStr =new String[chat.size()*2];
        int index=0;
        for(Message msg:chat){
            returnStr[index++]= msg.getText();
            if(msg.getReciver().equals(with))returnStr[index++]="i am the sender";
            else returnStr[index++]="i am the receiver";
        }
        return returnStr;
    }
    public String getUid(){
        return controller.getUID();
    }

}
