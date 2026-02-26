package progettoispw.letmeknow.controller;

import progettoispw.letmeknow.bean.MessageBean;
import progettoispw.letmeknow.controller.chat.Message;
import progettoispw.letmeknow.controller.chat.Messages;

import java.util.ArrayList;
import java.util.List;

public class ChatController {
    private Messages actChat;
    private ArrayList<Message>lastChat;
    //his functional is to store the last messages that were stored in the chat so that in the next scan of messages the
    //controller return only new messages
    private String with;
    public ChatController(){
        actChat= ConcreteUsrUser.getChat();
        with=actChat.getTouched();
    }
    public void  newMSG(MessageBean messageBean){
       actChat.newMessage(messageBean.getText(), messageBean.getReciver());
    }
    public String getUID(){
        return actChat.getUserid();
    }
    public List<Message> getChat() {
        ArrayList<Message> chat;
        ArrayList<Message> newChat;
        chat= (ArrayList<Message>) actChat.chat(with,false);
        if(lastChat!=null){
            newChat=new ArrayList<>();
            for(int i= lastChat.size();i< chat.size();i++){
                newChat.add(chat.get(i));
            }
            lastChat=chat;
        }
        else {
            lastChat=chat;
            newChat=chat;
        }
        return newChat;
    }
    public String getWith() {
        return with;
    }
}
