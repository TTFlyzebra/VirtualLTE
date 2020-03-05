package android.zebra;

interface IVirtualLTEService {

    void setVirtualLTEInfo(String vlteLanInfo);

	void openVirtualLTE();

	void closeVirtualLTE();
}