#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <sys/wait.h>
#include <sys/un.h>
#include <cutils/sockets.h>
#include <utils/Log.h>
#include <android/log.h>


#define TAG "VLTE"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__)
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__)

#define SOCKET_NAME "vlte"

int main(int argc, char **argv){
    char log[200];
    int connect_number = 5;
    int fdListen = -1, new_fd = -1;
    int ret;
    struct sockaddr_un peeraddr;
    socklen_t socklen = sizeof (peeraddr);
    int numbytes ;
    char buff[256];
    LOGD("vlte start running.");

//    LOGD("VirtualLTE open wlan...");
//    system("/system/bin/insmod /system/lib/modules/wlan.ko");
//    system("/system/bin/ip link set wlan0 name rmnet_data9");
//    system("/system/bin/ifconfig rmnet_data9 up");
//    system("/system/bin/setprop ctl.start wpa_supplicant_vlte");

//    LOGD("VirtualLTE close wlan...");
//    system("/system/bin/setprop ctl.stop wpa_supplicant_fly");
//    system("/system/bin/ifconfig rmnet_data9 down");
//    system("/system/bin/ip link set rmnet_data9 name wlan0");
//    system("/system/bin/rmmod /system/lib/modules/wlan.ko");

    fdListen = android_get_control_socket(SOCKET_NAME);
    if (fdListen < 0) {
        LOGD("open socket file failed.");
	    exit(-1);
    }
    ret = listen(fdListen, connect_number);
    if (ret < 0) {
        LOGE("listen error!");
        exit(-1);
    }
    new_fd = accept(fdListen, (struct sockaddr *) &peeraddr, &socklen);
    if (new_fd < 0 ) {
        LOGE("accept error!");
        exit(-1);
    }
	
    while(1){
        if((numbytes = recv(new_fd,buff,sizeof(buff),0))==-1){
            LOGE("recv error!");
            continue;
        }

//        if(send(new_fd,buff,strlen(buff),0)==-1)
//        {
//            close(new_fd);
//            exit(0);
//        }
    }
    
    close(new_fd);
    close(fdListen);
    LOGD("vlte is stop.");
    return 0;
}
