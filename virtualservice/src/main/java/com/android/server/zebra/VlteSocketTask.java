package com.android.server.zebra;

import android.content.Context;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.zebra.FlyLog;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @hide
 * ClassName: VlteSocketTask
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-10 上午9:00
 */
public class VlteSocketTask implements Runnable {
    private Thread mThread;
    private final static String SOCKET_NAME = "vlte";
    private final static int R_CONNET_TIME = 2000; //SOCKET断掉重连时间间隔
    private OutputStream mOutputStream;
    private final Object mDaemonLock = new Object();
    private int BUFFER_SIZE = 4096;
    private static final String TAG = "VlteSocketTask";
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private AtomicBoolean isRun = new AtomicBoolean(false);
    private List<IVlteRecvMessage> onRecvMessageList = new ArrayList<IVlteRecvMessage>();

    public void register(IVlteRecvMessage onRecvMessage) {
        onRecvMessageList.add(onRecvMessage);
    }

    public void unRegister(IVlteRecvMessage onRecvMessage) {
        onRecvMessageList.remove(onRecvMessage);
    }

    private void notifyRecvMessage(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (IVlteRecvMessage onRecvMessage : onRecvMessageList) {
                    onRecvMessage.recvVlteMessage(message);
                }
            }
        });
    }

    public VlteSocketTask(Context context) {
    }

    @Override
    public void run() {
        FlyLog.d("VlteSocketTask start! ");
        isRun.set(true);
        while (isRun.get()) {
            try {
                //开始监听ratd并交互
                listenToSocket();
            } catch (Exception e) {
                FlyLog.e("Error in VlteSocketTask: " + e);
                notifyRecvMessage("socket_error");
                SystemClock.sleep(R_CONNET_TIME);
            }
        }
        isRun.set(false);
        FlyLog.e("VlteSocketTask stop...");
    }

    private void listenToSocket() throws Exception {
        LocalSocket socket = null;
        try {
            socket = new LocalSocket();
//            socket.setSoTimeout(6000);
            LocalSocketAddress address = new LocalSocketAddress(SOCKET_NAME, LocalSocketAddress.Namespace.RESERVED);
            socket.connect(address);
            InputStream inputStream = socket.getInputStream();
            synchronized (mDaemonLock) {
                mOutputStream = socket.getOutputStream();
            }
            notifyRecvMessage("socket_connect");
            byte[] buffer = new byte[BUFFER_SIZE];
            while (isRun.get()) {
                int count = inputStream.read(buffer, 0, BUFFER_SIZE);
                if (count < 0) {
                    break;
                }
                ByteBuffer byteBuffer = ByteBuffer.allocate(count);
                byteBuffer.put(buffer, 0, count);
                String tempStr = new String(byteBuffer.array(), "UTF-8");
                int start = -1;
                do {
                    start = tempStr.indexOf("}]");
                    if (start != tempStr.length() - 2) {
                        String retStr = tempStr.substring(0, start + 2);
                        tempStr = tempStr.substring(start + 2);
                        notifyRecvMessage(retStr);
                    } else {
                        notifyRecvMessage(tempStr);
                        break;
                    }
                } while (start == -1);

            }
        } catch (Exception ex) {
            FlyLog.d("Communications error: " + ex);
            throw ex;
        } finally {
            synchronized (mDaemonLock) {
                if (mOutputStream != null) {
                    try {
                        FlyLog.d("closing stream for " + SOCKET_NAME);
                        mOutputStream.close();
                    } catch (IOException e) {
                        FlyLog.d("Failed closing output stream: " + e);
                    }
                    mOutputStream = null;
                }
            }

            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ex) {
                FlyLog.d("Failed closing socket: " + ex);
            }
        }
    }

    public boolean sendMessage(String message) {
        FlyLog.d("send:" + message);
        final String sendMessage = "[{"+message+"}]";
        synchronized (mDaemonLock) {
            if (mOutputStream == null) {
                FlyLog.e("ratd socket error! mOutputStream = null");
                return false;
            } else {
                try {
                    mOutputStream.write(sendMessage.getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    FlyLog.d(e.toString());
                    return false;
                }
            }
        }
        return true;
    }

    public void onCreate() {

    }

    public void start(){
        if(isRun.get()){
            FlyLog.e("VlteSocketTask is Running...");
            return;
        }
        mThread = new Thread(this, TAG);
        mThread.setDaemon(true);
        mThread.start();
    }

    public void stop() {
        if(isRun.get()){
            isRun.set(false);
        }
        mHandler.removeCallbacksAndMessages(null);
    }

    public void onDestory() {
        isRun.set(false);
        mHandler.removeCallbacksAndMessages(null);
    }
}
