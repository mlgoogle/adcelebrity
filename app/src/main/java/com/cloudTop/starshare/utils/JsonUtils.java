package com.cloudTop.starshare.utils;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {
    public JsonUtils() {
    }

    public static JSONObject safePut(JSONObject var0, String var1, String var2) {
        try {
            return var0.put(var1, var2);
        } catch (JSONException var4) {
            return var0;
        }
    }

    public static JSONObject safePut(JSONObject var0, String var1, int var2) {
        try {
            return var0.put(var1, var2);
        } catch (JSONException var4) {
            return var0;
        }
    }

    public static JSONObject safePut(JSONObject var0, String var1, long var2) {
        try {
            return var0.put(var1, var2);
        } catch (JSONException var5) {
            return var0;
        }
    }

    public static JSONObject safePut(JSONObject var0, String var1, boolean var2) {
        try {
            return var0.put(var1, var2);
        } catch (JSONException var4) {
            return var0;
        }
    }

    public static JSONObject safePut(JSONObject var0, String var1, double var2) {
        try {
            return var0.put(var1, var2);
        } catch (JSONException var5) {
            return var0;
        }
    }

    public static JSONObject safePut(JSONObject var0, String var1, JSONObject var2) {
        try {
            return var0.put(var1, var2);
        } catch (JSONException var4) {
            return var0;
        }
    }

    public static JSONObject safePut(JSONObject var0, String var1, JSONArray var2) {
        try {
            return var0.put(var1, var2);
        } catch (JSONException var4) {
            return var0;
        }
    }

    public static JSONArray safePut(JSONArray var0, Object var1) {
        return var0.put(var1);
    }

    public static JSONArray safeRemove(JSONArray var0, int var1) {
        JSONArray var2 = new JSONArray();

        try {
            for(int var3 = 0; var3 < var0.length(); ++var3) {
                if(var3 != var1) {
                    safePut(var2, var0.get(var3));
                }
            }
        } catch (JSONException var4) {
            ;
        }

        return var2;
    }
}
