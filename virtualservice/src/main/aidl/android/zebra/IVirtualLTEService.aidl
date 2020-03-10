package android.zebra;
import android.zebra.IVirtualLTEListener;

interface IVirtualLTEService {

    void setVirtualLTEInfo(String vlteLanInfo);

    void openVirtualLTE();

    void closeVirtualLTE();

    void register(IVirtualLTEListener lteListener);

    void unregister(IVirtualLTEListener lteListener);
}