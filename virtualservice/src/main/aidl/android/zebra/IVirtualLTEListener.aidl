// IVirtualLTEListener.aidl
package android.zebra;

// Declare any non-default types here with import statements
/**
 * @hide
 */
interface IVirtualLTEListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void notifyVlteStatus(int status);

    void notifySignal(int signal);

    void recvMessage(String message);

}
