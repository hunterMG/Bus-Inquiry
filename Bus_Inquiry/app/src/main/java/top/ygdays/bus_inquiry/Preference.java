package top.ygdays.bus_inquiry;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @Author: Guang Yan
 * @Description: save and update user info
 * @Date: Created in 下午12:41 2018/1/6
 */
public class Preference {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    public static void init(Context context){
        sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static void setUserInfo(String email, boolean isAdmin){
        editor.putString("email", email);
        editor.putBoolean("isAdmin", isAdmin);
        editor.commit();
    }

    public static String getUserInfo(){
        return sharedPreferences.getString("email", "null");
    }

    public static boolean isLoggedin(){
        if(sharedPreferences.getString("email", "null") != "null"){
            return true;
        }
        return false;
    }

    public static boolean isAdmin(){
        return sharedPreferences.getBoolean("isAdmin", false);
    }

    public static void logout(){
        editor.putString("email", "null");
        editor.putBoolean("isAdmin", false);
        editor.commit();
    }
}
