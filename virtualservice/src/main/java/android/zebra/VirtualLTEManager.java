package android.zebra;

import android.content.Context;
import android.os.RemoteException;

public class VirtualLTEManager
{
    private static final String TAG = "VirtualLTEManager";

    public void setVirtualLTEInfo(VlteLanInfo vlteLanInfo)
    {
        try {
            SystemPropTools.set("persist.sys.vlte.ssid",vlteLanInfo.wifissid);
            SystemPropTools.set("persist.sys.vlte.psk",vlteLanInfo.wifipsk);
            SystemPropTools.set("persist.sys.vlte.ip",vlteLanInfo.ipAddress);
            SystemPropTools.set("persist.sys.vlte.mask",vlteLanInfo.ipMask);
            SystemPropTools.set("persist.sys.vlte.gateway",vlteLanInfo.gateway);
            SystemPropTools.set("persist.sys.vlte.dns1",vlteLanInfo.dns1);
            SystemPropTools.set("persist.sys.vlte.dns2",vlteLanInfo.dns2);
            SystemPropTools.set("persist.sys.vlte.netid",vlteLanInfo.network+"");
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