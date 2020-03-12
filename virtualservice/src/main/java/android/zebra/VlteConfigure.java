package android.zebra;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;


public class VlteConfigure {
    public String wifissid = "";
    public String wifipsk = "";
    public String ipAddress = "";
    public String ipMask = "";
    public String gateway = "";
    public String dns1 = "";
    public String dns2 = "";
    public static VlteConfigure createByJsonString(String json){
        if(TextUtils.isEmpty(json)) return null;
        VlteConfigure vlteLanInfo = new VlteConfigure();
        try {
            JSONObject jsonObject = new JSONObject(json);
            if(jsonObject.has("wifissid")){
                vlteLanInfo.wifissid =jsonObject.getString("wifissid");
            }
            if(jsonObject.has("wifipsk")){
                vlteLanInfo.wifipsk = jsonObject.getString("wifipsk");
            }
            if(jsonObject.has("ipAddress")){
                vlteLanInfo.ipAddress =jsonObject.getString("ipAddress");
            }
            if(jsonObject.has("ipMask")){
                vlteLanInfo.ipMask = jsonObject.getString("ipMask");
            }
            if(jsonObject.has("gateway")){
                vlteLanInfo.gateway =jsonObject.getString("gateway");
            }
            if(jsonObject.has("dns1")){
                vlteLanInfo.dns1 = jsonObject.getString("dns1");
            }
            if(jsonObject.has("dns2")){
                vlteLanInfo.dns2 = jsonObject.getString("dns2");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vlteLanInfo;
    }

    public String toJsonString(){
        return  "{\"wifissid\":\""+wifissid+"\","+
                "\"wifipsk\":\""+wifipsk+"\","+
                "\"ipAddress\":\""+ipAddress+"\","+
                "\"ipMask\":\""+ipMask+"\","+
                "\"gateway\":\""+gateway+"\","+
                "\"dns1\":\""+dns1+"\","+
                "\"dns2\":"+dns2+"}";
    }
}
