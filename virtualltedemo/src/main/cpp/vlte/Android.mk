LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES := \
     vlte.cpp \

LOCAL_C_INCLUDES += \
	$(LOCAL_PATH) \

LOCAL_SHARED_LIBRARIES := \
        libcrypto \
        libutils \
        libdl \
        libhardware_legacy \
        liblog \
        liblogwrap \
        libmdnssd \
        libnetutils \
        libsysutils \
        libwpa_client \
        libcutils \
        libqsap_sdk\
        libstlport \
        libstdc++ \

LOCAL_LDLIBS += -L$(SYSROOT)/usr/lib -llog
LOCAL_CFLAGS := -DANDROID_CHANGES -DHAVE_CONFIG_H -DHAVE_OPENSSL_ENGINE_H
LOCAL_CFLAGS += -Wno-sign-compare -Wno-missing-field-initializers
LOCAL_SHARED_LIBRARIES := libcutils liblog libcrypto libdl
LOCAL_STRIP_MODULE := false
LOCAL_MODULE := vlte
LOCAL_C_INCLUDES += external/stlport/stlport 
LOCAL_C_INCLUDES += bionic

include $(BUILD_EXECUTABLE)

