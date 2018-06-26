// MessageReceiver.aidl
package com.robert.aidldemo;
import com.robert.aidldemo.bean.MessageModel;
// Declare any non-default types here with import statements

interface MessageReceiver {

    void onMessageReceived(in MessageModel messageModel);
}
