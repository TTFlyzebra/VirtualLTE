package android.zebra;
import android.zebra.IVirtualLTEListener;

/**
 * @hide
 */
interface IVirtualLTEService {

    void configureVLTE(String jsonConfigure);

    void openVirtualLTE();

    void closeVirtualLTE();

    void runCommand(String command);

    int getVlteStatus();

    void register(IVirtualLTEListener lteListener);

    void unregister(IVirtualLTEListener lteListener);
}