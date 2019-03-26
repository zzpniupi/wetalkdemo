package com.example.wetalk.server;

import android.os.Message;

import com.example.wetalk.ChatMsg;
import com.example.wetalk.Msg;
import com.example.wetalk.activity.chatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ReceiveChatMsg {

    void delChatMsg(String msg) {

        String sendName = null;
        String content = null;
        String avatarID = null;
        String fileType = null;
        String group = null;
        String receiveMsg = null;

        ServerManager.getServerManager().setMessage(null);
        String p = "\\[TRANSMSGS\\]:\\[(.*), (.*), (.*)\\]";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {
            int senderID = Integer.parseInt(matcher.group(1));
            if(senderID == chatActivity.friendID){
                receiveMsg  = matcher.group(3);
                Msg rMsg = new Msg(receiveMsg,0);

                chatActivity.msgList.add(rMsg);
                Message message = new Message();
                message.what = 1;
                chatActivity.handler.sendMessage(message);
            }
            //chatActivity.receiveMsgs(rMsg);
            //AtyChatRoom.chatMsgList.add(chatMsg);
            //ChatMsg.chatMsgList.add(receiveMsg);
        }
    }
}


