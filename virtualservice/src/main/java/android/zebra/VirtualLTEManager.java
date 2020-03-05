package android.zebra;

import android.content.Context;
import android.os.RemoteException;

public class VirtualLTEManager
{
    private static final String TAG = "VirtualLTEManager";

    public void setVirtualLTEInfo(VlteLanInfo vlteLanInfo)
    {
        try {
            SystemPropTools.set("persist.sys.vlte.wifissid",vlteLanInfo.wifissid);
            SystemPropTools.set("persist.sys.vlte.wifipsk",vlteLanInfo.wifipsk);
            SystemPropTools.set("persist.sys.vlte.ipAddress",vlteLanInfo.ipAddress);
            SystemPropTools.set("persist.sys.vlte.ipMask",vlteLanInfo.ipMask);
            SystemPropTools.set("persist.sys.vlte.gateway",vlteLanInfo.gateway);
            SystemPropTools.set("persist.sys.vlte.dns1",vlteLanInfo.dns1);
            SystemPropTools.set("persist.sys.vlte.dns2",vlteLanInfo.dns2);
            mService.setVirtualLTEInfo(vlteLanInfo.toJsonString());
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

    public VirtualLTEManager(Context context, IVirtualLTEService service) {
        mService = service;
    }

    IVirtualLTEService mService;
}