package com.android.server.zebra;

import android.content.Context;
import android.os.RemoteException;
import android.zebra.FlyLog;
import android.zebra.IVirtualLTEService;
import android.zebra.VlteLanInfo;

/**
 * @hide
 */
public class VirtualLTEService extends IVirtualLTEService.Stub implements IVlteRecvMessage{
    private static final String TAG = "VirtualLTEService";
    private Context mContext;
    private VlteSocketTask vlteSocketTask;

    public VirtualLTEService(Context context) {
        super();
        vlteSocketTask = new VlteSocketTask(context);
        vlteSocketTask.register(this);
        vlteSocketTask.start();
    }

    public void setVirtualLTEInfo(String vlteLanInfoStr) throws RemoteException
    {
        VlteLanInfo vlteLanInfo = VlteLanInfo.createByJsonString(vlteLanInfoStr);
        vlteSocketTask.sendMessage("vlteLanInfo");
    }

    public void closeVirtualLTE() throws RemoteException
    {
        vlteSocketTask.sendMessage("closevlte");
        closeVLTE_native();
    }

    public void openVirtualLTE() throws RemoteException
    {
        vlteSocketTask.sendMessage("openvlte#");
        openVLTE_native();
    }

    public void recvVlteMessage(String message){
        FlyLog.d("recv:"+message);
    }

    private static native void openVLTE_native();
    private static native void closeVLTE_native();
}
