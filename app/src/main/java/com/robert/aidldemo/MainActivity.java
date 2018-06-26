package com.robert.aidldemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.robert.aidldemo.bean.MessageModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MessageSender mMessageSender;

    IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {

        @Override
        public void binderDied() {
            Log.e(TAG, "binderDied: ");
            if (mMessageSender != null) {
                mMessageSender.asBinder().unlinkToDeath(this, 0);
                mMessageSender = null;
            }
            setupService();
        }
    };

    //服务连接
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected: ");
            mMessageSender = MessageSender.Stub.asInterface(service);
            try {
                mMessageSender.asBinder().linkToDeath(mDeathRecipient, 0);
                mMessageSender.registerReceiverListener(mMessageReceiver);
                mMessageSender.sendMessage(MessageModel.create("client user id",
                        "receiver user id", "This is message content"));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected: ");
        }
    };

    //接受到消息回调
    private MessageReceiver mMessageReceiver = new MessageReceiver.Stub() {
        @Override
        public void onMessageReceived(MessageModel messageModel) throws RemoteException {
            Log.e(TAG, "onMessageReceived: " + messageModel.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.text).setOnClickListener(v -> {
            Intent intent = new Intent(this, SecondActivity.class);
            intent.putExtra("data", "data");
            startActivity(intent);

        });
        setupService();
    }

    private void setupService() {
        Intent intent = new Intent(this, MessageService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onDestroy() {
        if (mMessageSender != null && mMessageSender.asBinder().isBinderAlive()) {
            try {
                mMessageSender.unRegisterReceiverListener(mMessageReceiver);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mServiceConnection);
        super.onDestroy();
    }
}
