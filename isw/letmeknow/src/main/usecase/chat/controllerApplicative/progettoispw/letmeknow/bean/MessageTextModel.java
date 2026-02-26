package progettoispw.letmeknow.bean;

import progettoispw.letmeknow.controller.ConcreteUsrUser;
import progettoispw.letmeknow.controller.Factory;
import progettoispw.letmeknow.controller.chat.Messages;

public class MessageTextModel {
    public String getTextMsg(){
        Messages messages= ConcreteUsrUser.getChat();
        return messages.getTextMsg();
    }
}
