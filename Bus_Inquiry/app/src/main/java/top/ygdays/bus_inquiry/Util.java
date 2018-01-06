package top.ygdays.bus_inquiry;

import android.content.Context;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import top.ygdays.bus_inquiry.data.Route;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Guang Yan
 * @Description:
 * @Date: Created in 下午10:17 2018/1/1
 */
public class Util {
    /*
     * get a list of routes from a JSONObject
     */
    public static List<Route> getRouteList(JSONObject jsonObject) throws JSONException {
        JSONArray routeJsonArray = jsonObject.getJSONArray("route");
        Log.i("STOPROUTE", routeJsonArray.toString());
        // parse json to Route
        List<Route> routeList = new ArrayList<>();
        for (int i = 0; i<routeJsonArray.length(); i++) {
            JSONObject jObj = routeJsonArray.getJSONObject(i);
            JSONArray jsonStopsArray = jObj.getJSONArray("Stops");
            List<String> Stops = new ArrayList<>();
            for(int j = 0; j<jsonStopsArray.length(); j++){
                Stops.add(jsonStopsArray.get(j).toString());
            }
            Route route = new Route(jObj.getInt("RouteID"), jObj.getString("RouteName"), jObj.getDouble("Price"), jObj.getString("StartTime"), jObj.getString("EndTime"), jObj.getString("StartStop"), jObj.getString("EndStop"), jObj.getInt("StopNum"), Stops);
            routeList.add(route);
            Log.i("routeArray", routeJsonArray.get(i).toString());
        }
        return routeList;
    }
    /*
     * Display routes returned by server in routeExpandableListView
     */
    public static void displayRoutes(final List<Route> routeList, View view, final Context mContext, ExpandableListView routeExpandableListView){
        final ExpandableListAdapter routeAdapter = new BaseExpandableListAdapter() {
            @Override
            public int getGroupCount() {
                return routeList.size();
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return 4;// Stops, startTime, endTime, price
            }

            @Override
            public Object getGroup(int groupPosition) {
                return routeList.get(groupPosition).RouteName;
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                if(childPosition == 0){
                    return mContext.getString(R.string.title_stop)+":"+routeList.get(groupPosition).Stops.toString();
                }
                else if(childPosition == 1)
                    return mContext.getString(R.string.start_time)+routeList.get(groupPosition).StartTime;
                else if(childPosition == 2)
                    return mContext.getString(R.string.end_time)+routeList.get(groupPosition).EndTime;
                else return mContext.getString(R.string.price)+routeList.get(groupPosition).Price;
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                LinearLayout ll = new LinearLayout(mContext);
                ll.setOrientation(1);
                TextView tv_RouteName = new TextView(mContext);
                tv_RouteName.setTextSize(20);
                tv_RouteName.setText(routeList.get(groupPosition).RouteName);
                ll.addView(tv_RouteName);
                TextView tv_stops = new TextView(mContext);
                tv_stops.setText(routeList.get(groupPosition).Stops.toString());
                tv_stops.setSingleLine();
                ll.addView(tv_stops);
                return ll;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                TextView textView = new TextView(mContext);
                textView.setText(getChild(groupPosition, childPosition).toString());
                return textView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return true;
            }
        };

        routeExpandableListView.setAdapter(routeAdapter);
    }
    public static void hideSoftKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        IBinder iBinder = view.getWindowToken();
        imm.hideSoftInputFromWindow(iBinder,InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /*
     * MD5 encryption (32 bit)
     */
    public static String getMD5Str(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(
                            Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}
