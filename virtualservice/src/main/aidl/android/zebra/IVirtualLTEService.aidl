package android.zebra;
import android.zebra.IVirtualLTEListener;

interface IVirtualLTEService {

    void configureVLTE(String jsonConfigure);

    void openVirtualLTE();

    void closeVirtualLTE();

    void runCommand(String command);

    void register(IVirtualLTEListener lteListener);

    void unregister(IVirtualLTEListener lteListener);
}