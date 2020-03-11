package com.android.server.zebra;

import android.content.Context;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.zebra.FlyLog;
import android.zebra.IVirtualLTEListener;
import android.zebra.IVirtualLTEService;
import android.zebra.VlteLanInfo;

/**
 * @hide
 */
public class VirtualLTEService extends IVirtualLTEService.Stub implements IVlteRecvMessage{
    private static final String TAG = "VirtualLTEService";
    private Context mContext;
    private VlteSocketTask vlteSocketTask;
    private static RemoteCallbackList<IVirtualLTEListener> ltelisteners = new RemoteCallbackList<>();

    public VirtualLTEService(Context context) {
        super();
        vlteSocketTask = new VlteSocketTask(context);
        vlteSocketTask.register(this);
        vlteSocketTask.start();
    }

    public void setVirtualLTEInfo(String vlteLanInfoStr) throws RemoteException
    {
        vlteSocketTask.sendMessage("setvltelan"+vlteLanInfoStr);
    }

    @Override
    public void openVirtualLTE() throws RemoteException
    {
        vlteSocketTask.sendMessage("openvlte##");
    }

    @Override
    public void closeVirtualLTE() throws RemoteException
    {
        vlteSocketTask.sendMessage("closevlte#");
    }

    @Override
    public void runCommand(String command) throws RemoteException {
        vlteSocketTask.sendMessage("command###"+command);
    }

    @Override
    public void register(IVirtualLTEListener lteListener) throws RemoteException {
        ltelisteners.register(lteListener);
    }

    @Override
    public void unregister(IVirtualLTEListener lteListener) throws RemoteException {
        ltelisteners.unregister(lteListener);
    }

    public void recvVlteMessage(String message){
        FlyLog.d("recv:"+message);
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
