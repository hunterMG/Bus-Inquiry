package top.ygdays.bus_inquiry.net;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Guang Yan
 * @Description:
 * @Date: Created in 下午11:34 2018/1/11
 */
public class ModifyRouteRequest extends StringRequest{
    private static final String MODIFY_ROUTE_REQUEST_URL = "https://ygdays.top/bus_inquiry/modifyRoute.php";
    private Map<String, String> params;

    public ModifyRouteRequest(String routeID, String routeName, String price, String startTime, String endTime, String stopNum, Response.Listener<String> listener) {
        super(Method.POST, MODIFY_ROUTE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("routeID", routeID);
        params.put("routeName", routeName);
        params.put("price", price);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("stopNum", stopNum);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
