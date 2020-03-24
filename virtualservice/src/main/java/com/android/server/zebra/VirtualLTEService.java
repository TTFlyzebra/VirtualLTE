package com.android.server.zebra;

import android.content.Context;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.zebra.FlyLog;
import android.zebra.IVirtualLTEListener;
import android.zebra.IVirtualLTEService;
import android.zebra.VirtualLTEManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @hide
 */
public class VirtualLTEService extends IVirtualLTEService.Stub implements IVlteRecvMessage {
    private static final String TAG = "VirtualLTEService";
    private Context mContext;
    private VlteSocketTask vlteSocketTask;
    private static RemoteCallbackList<IVirtualLTEListener> ltelisteners = new RemoteCallbackList<>();
    public static AtomicInteger vlteStatus = new AtomicInteger(-1);
    public static AtomicInteger signal = new AtomicInteger(9999);

    public VirtualLTEService(Context context) {
        super();
        vlteSocketTask = new VlteSocketTask(context);
        vlteSocketTask.register(this);
        vlteSocketTask.start();
    }

    @Override
    public void configureVLTE(String vlteLanInfoStr) throws RemoteException {
        vlteSocketTask.sendMessage("configure#" + vlteLanInfoStr);
    }

    @Override
    public void openVirtualLTE() throws RemoteException {
        if (vlteStatus.get() != VirtualLTEManager.CONNECT && vlteStatus.get()!= VirtualLTEManager.RECONNECT) {
            vlteStatus.set(VirtualLTEManager.RECONNECT);
            vlteSocketTask.sendMessage("openvlte##");
        }
    }

    @Override
    public void closeVirtualLTE() throws RemoteException {
        if (vlteStatus.get() != VirtualLTEManager.DISCONNECT && vlteStatus.get()!= VirtualLTEManager.RECONNECT) {
            vlteSocketTask.sendMessage("closevlte#");
        }
    }

    @Override
    public void runCommand(String command) throws RemoteException {
        vlteSocketTask.sendMessage("command###" + command);
    }

    @Override
    public int getVlteStatus() throws RemoteException {
        return vlteStatus.get();
    }

    @Override
    public void register(IVirtualLTEListener lteListener) throws RemoteException {
        ltelisteners.register(lteListener);
    }

    @Override
    public void unregister(IVirtualLTEListener lteListener) throws RemoteException {
        ltelisteners.unregister(lteListener);
    }

    public void recvVlteMessage(String message) {
        FlyLog.d("recv:" + message);
        if (message.startsWith("[{CONNECT}]")) {
            vlteStatus.set(1);
            final int N = ltelisteners.beginBroadcast();
            for (int i = 0; i < N; i++) {
                try {
                    ltelisteners.getBroadcastItem(i).notifyVlteStatus(vlteStatus.get());
                } catch (RemoteException e) {
                    FlyLog.e(e.toString());
                } catch (Exception e) {
                    FlyLog.e(e.toString());
                }
            }
            ltelisteners.finishBroadcast();
        } else if (message.startsWith("[{DISCONNECT}]")) {
            vlteStatus.set(-1);
            final int N = ltelisteners.beginBroadcast();
            for (int i = 0; i < N; i++) {
                try {
                    ltelisteners.getBroadcastItem(i).notifyVlteStatus(vlteStatus.get());
                } catch (RemoteException e) {
                    FlyLog.e(e.toString());
                } catch (Exception e) {
                    FlyLog.e(e.toString());
                }
            }
            ltelisteners.finishBroadcast();
        } else if (message.startsWith("[{SIGNAL}]")) {
            try {
                signal.set(Integer.valueOf(message.substring(6)));
                final int N = ltelisteners.beginBroadcast();
                for (int i = 0; i < N; i++) {
                    try {
                        ltelisteners.getBroadcastItem(i).notifyVlteStatus(signal.get());
                    } catch (RemoteException e) {
                        FlyLog.e(e.toString());
                    } catch (Exception e) {
                        FlyLog.e(e.toString());
                    }
                }
                ltelisteners.finishBroadcast();
            } catch (Exception e) {
                FlyLog.e(e.toString());
            }
        }

        final int N = ltelisteners.beginBroadcast();
        for (int i = 0; i < N; i++) {
            try {
                ltelisteners.getBroadcastItem(i).recvMessage(message);
            } catch (RemoteException e) {
                FlyLog.e(e.toString());
            } catch (Exception e) {
                FlyLog.e(e.toString());
            }
        }
        ltelisteners.finishBroadcast();
    }

    private static native void openVLTE_native();

    private static native void closeVLTE_native();
}
