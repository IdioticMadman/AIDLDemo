// MessageSender.aidl
package com.robert.aidldemo;
import com.robert.aidldemo.bean.MessageModel;
import com.robert.aidldemo.MessageReceiver;
// Declare any non-default types here with import statements

interface MessageSender {
    /**
     * 发送消息
     */
    void sendMessage(in MessageModel msg);

    void registerReceiverListener(MessageReceiver receiver);

    void unRegisterReceiverListener(MessageReceiver receiver);
}
