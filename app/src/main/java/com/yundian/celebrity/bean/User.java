package com.yundian.celebrity.bean;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import com.yundian.celebrity.utils.JsonUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String email;
    private String name;
    private String avatar;

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public String getAvatarUrl() {
        return this.avatar;
    }

    public User(String var1, String var2, String var3) {
        this.email = var1;
        this.name = var2;
        this.avatar = var3;
    }

    public static List fromJsonArray(JSONArray var0) {
        ArrayList var1 = new ArrayList();
        if(var0 == null) {
            return var1;
        } else {
            for(int var2 = 0; var2 < var0.length(); ++var2) {
                JSONObject var3 = var0.optJSONObject(var2);
                var1.add(fromJSONObject(var3));
            }

            return var1;
        }
    }

    public static User fromString(String var0) {
        try {
            return fromJSONObject(new JSONObject(var0));
        } catch (JSONException var2) {
            return new User("", "", "");
        }
    }

    public static User fromJSONObject(JSONObject var0) {
        if(var0 == null) {
            return new User("", "", "");
        } else {
            String var1 = var0.optString("email");
            String var2 = var0.optString("name");
            String var3 = var0.optString("avatar");
            return new User(var1, var2, var3);
        }
    }

    public JSONObject toJSONObject() {
        JSONObject var1 = new JSONObject();
        JsonUtils.safePut(var1, "email", this.email);
        JsonUtils.safePut(var1, "name", this.name);
        JsonUtils.safePut(var1, "avatar", this.avatar);
        return var1;
    }

    public static JSONArray toJson(List var0) {
        JSONArray var1 = new JSONArray();
        if(var0 != null) {
            Iterator var2 = var0.iterator();

            while(var2.hasNext()) {
                User var3 = (User)var2.next();
                JSONObject var4 = var3.toJSONObject();
                var1.put(var4);
            }
        }

        return var1;
    }

    public String toString() {
        return "User{email=\'" + this.email + '\'' + ", name=\'" + this.name + '\'' + ", avatar=\'" + this.avatar + '\'' + '}';
    }

    public boolean equals(Object var1) {
        if(this == var1) {
            return true;
        } else if(var1 != null && this.getClass() == var1.getClass()) {
            User var2;
            label45: {
                var2 = (User)var1;
                if(this.avatar != null) {
                    if(this.avatar.equals(var2.avatar)) {
                        break label45;
                    }
                } else if(var2.avatar == null) {
                    break label45;
                }

                return false;
            }

            label38: {
                if(this.email != null) {
                    if(this.email.equals(var2.email)) {
                        break label38;
                    }
                } else if(var2.email == null) {
                    break label38;
                }

                return false;
            }

            if(this.name != null) {
                if(!this.name.equals(var2.name)) {
                    return false;
                }
            } else if(var2.name != null) {
                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int var1 = this.email != null?this.email.hashCode():0;
        var1 = 31 * var1 + (this.name != null?this.name.hashCode():0);
        var1 = 31 * var1 + (this.avatar != null?this.avatar.hashCode():0);
        return var1;
    }
}
