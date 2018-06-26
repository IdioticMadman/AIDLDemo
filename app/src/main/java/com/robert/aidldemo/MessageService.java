package com.robert.aidldemo;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.robert.aidldemo.bean.MessageModel;

import java.util.concurrent.atomic.AtomicBoolean;

public class MessageService extends Service {

    private static final String TAG = MessageService.class.getSimpleName();

    private AtomicBoolean serviceStop = new AtomicBoolean(false);
    /*
     * RemoteCallbackList专门用来管理多进程回调接口
     * 这个不能使用arrayList进行保存
     */

    private RemoteCallbackList<MessageReceiver> mListenerList = new RemoteCallbackList<>();

    IBinder messageSender = new MessageSender.Stub() {

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String packageName = null;
            String[] packages = getPackageManager().getPackagesForUid(getCallingUid());
            if (packages != null && packages.length > 0) {
                packageName = packages[0];
            }
            if (packageName == null || !packageName.startsWith("com.robert.aidldemo")) {
                Log.d("onTransact", "拒绝调用：" + packageName);
                return false;
            }
            return super.onTransact(code, data, reply, flags);
        }

        @Override
        public void sendMessage(MessageModel msg) throws RemoteException {
            Log.e(TAG, "sendMessage: " + msg.toString());
        }

        @Override
        public void registerReceiverListener(MessageReceiver receiver) throws RemoteException {
            mListenerList.register(receiver);
        }

        @Override
        public void unRegisterReceiverListener(MessageReceiver receiver) throws RemoteException {
            mListenerList.unregister(receiver);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new FakeTcpTask()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //自定义permission方式检查权限
        if (checkCallingOrSelfPermission("com.robert.aidldemo.permission.REMOTE_SERVICE_PERMISSION")
                == PackageManager.PERMISSION_DENIED) {
            return null;
        }
        return messageSender;
    }

    @Override
    public void onDestroy() {
        serviceStop.set(true);
        super.onDestroy();
    }

    private class FakeTcpTask implements Runnable {

        @Override
        public void run() {
            while (!serviceStop.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MessageModel msg = MessageModel.create("Server",
                        "Client", String.valueOf(System.currentTimeMillis()));
                int count = mListenerList.beginBroadcast();
                Log.d(TAG, "Listener Count = " + count);
                for (int i = 0; i < count; i++) {
                    MessageReceiver item = mListenerList.getBroadcastItem(i);
                    if (null != item) {
                        try {
                            item.onMessageReceived(msg);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
                mListenerList.finishBroadcast();
            }
        }
    }
}
