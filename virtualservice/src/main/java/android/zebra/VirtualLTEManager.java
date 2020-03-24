package android.zebra;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

public class VirtualLTEManager {
    private static final String TAG = "VirtualLTEManager";
    private IVirtualLTEService mService;
    private static final int NOTIFY_VLTESTATUS = 1;
    private static final int NOTIFY_SIGNAL = 2;
    private static final int NOTIFY_RECVMESSAG = 3;
    private List<VirtualLTEListener> mVirtualLTEListeners = new ArrayList<>();
    private final Object mListenerLock = new Object();
    private static DhcpResult dhcpResult = new DhcpResult();

    public static final int NARMAL = 0;
    public static final int CONNECT = 1;
    public static final int RECONNECT = 2;
    public static final int DISCONNECT = -1;

    private Handler mHandler = new MainHandler(Looper.myLooper());
    private IVirtualLTEListener iVirtualLTEListener = new IVirtualLTEListener.Stub() {
        @Override
        public void notifyVlteStatus(int status) throws RemoteException {
            Message.obtain(mHandler, NOTIFY_VLTESTATUS, status).sendToTarget();
        }

        @Override
        public void notifySignal(int signal) throws RemoteException {
            Message.obtain(mHandler, NOTIFY_SIGNAL, signal).sendToTarget();
        }

        @Override
        public void recvMessage(String message) throws RemoteException {
            Message.obtain(mHandler, NOTIFY_SIGNAL, NOTIFY_RECVMESSAG).sendToTarget();
        }
    };

    private class MainHandler extends Handler {
        public MainHandler(Looper mainLooper) {
            super(mainLooper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NOTIFY_VLTESTATUS:
                    synchronized (mListenerLock) {
                        for (VirtualLTEListener virtualLTEListener : mVirtualLTEListeners) {
                            virtualLTEListener.notifyVlteStatus((Integer) msg.obj);
                        }
                    }
                    break;
                case NOTIFY_SIGNAL:
                    synchronized (mListenerLock) {
                        for (VirtualLTEListener virtualLTEListener : mVirtualLTEListeners) {
                            virtualLTEListener.notifySignal((Integer) msg.obj);
                        }
                    }
                    break;
                case NOTIFY_RECVMESSAG:
                    for (VirtualLTEListener virtualLTEListener : mVirtualLTEListeners) {
                        virtualLTEListener.recvMessage((String) msg.obj);
                    }
                    break;
            }
        }
    }

    public interface VirtualLTEListener {
        void notifyVlteStatus(int status);

        void notifySignal(int signal);

        void recvMessage(String message);
    }

    public void addVirtualLTEListener(VirtualLTEListener lteListener) {
        synchronized (mListenerLock) {
            mVirtualLTEListeners.add(lteListener);
        }
    }

    public void removeVirtualLTEListener(VirtualLTEListener lteListener) {
        synchronized (mListenerLock) {
            mVirtualLTEListeners.remove(lteListener);
        }
    }


    public VirtualLTEManager(Context context, IVirtualLTEService service) {
        mService = service;
        try {
            mService.register(iVirtualLTEListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void configureVLTE(VlteConfigure vlteLanInfo) {
        try {
            SystemPropTools.set("persist.sys.vlte.ssid", vlteLanInfo.wifissid);
            SystemPropTools.set("persist.sys.vlte.psk", vlteLanInfo.wifipsk);
            SystemPropTools.set("persist.sys.vlte.ip", vlteLanInfo.ipAddress);
            SystemPropTools.set("persist.sys.vlte.mask", vlteLanInfo.ipMask);
            SystemPropTools.set("persist.sys.vlte.gateway", vlteLanInfo.gateway);
            SystemPropTools.set("persist.sys.vlte.dns1", vlteLanInfo.dns1);
            SystemPropTools.set("persist.sys.vlte.dns2", vlteLanInfo.dns2);
            mService.configureVLTE(vlteLanInfo.toJsonString());
        } catch (RemoteException e) {
            FlyLog.e("openVirtualLTE error!");
        }
    }

    public void openVirtualLTE() {
        try {
            mService.openVirtualLTE();
        } catch (RemoteException e) {
            FlyLog.e("openVirtualLTE error!");
        }
    }

    public void closeVirtualLTE() {
        try {
            mService.closeVirtualLTE();
        } catch (RemoteException e) {
            FlyLog.e("closeVirtualLTE error!");
        }
    }

    public void runCommand(String command) {
        try {
            mService.runCommand(command);
        } catch (RemoteException e) {
            FlyLog.e("openVirtualLTE error!");
        }
    }

    public int getVlteStatus() {
        try {
            return mService.getVlteStatus();
        } catch (RemoteException e) {
            FlyLog.e("openVirtualLTE error!");
            return NARMAL;
        }
    }

    public DhcpResult getDhcpResult() {
        dhcpResult.dns1 = SystemPropTools.get("dhcp.rmnet_data9.dns1", "");
        dhcpResult.dns2 = SystemPropTools.get("dhcp.rmnet_data9.dns2", "");
        dhcpResult.dns3 = SystemPropTools.get("dhcp.rmnet_data9.dns3", "");
        dhcpResult.dns4 = SystemPropTools.get("dhcp.rmnet_data9.dns4", "");
        dhcpResult.domain = SystemPropTools.get("dhcp.rmnet_data9.domain", "");
        dhcpResult.gateway = SystemPropTools.get("dhcp.rmnet_data9.gateway", "");
        dhcpResult.ipaddress = SystemPropTools.get("dhcp.rmnet_data9.ipaddress", "");
        dhcpResult.mask = SystemPropTools.get("dhcp.rmnet_data9.mask", "");
        dhcpResult.mtu = SystemPropTools.get("dhcp.rmnet_data9.mtu", "");
        return dhcpResult;
    }
}