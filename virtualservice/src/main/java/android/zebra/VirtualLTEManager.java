package android.zebra;

import android.content.Context;
import android.os.RemoteException;
import android.zebra.IVirtualLTEService;

public class VirtualLTEManager
{
    private static final String TAG = "VirtualLTEManager";

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