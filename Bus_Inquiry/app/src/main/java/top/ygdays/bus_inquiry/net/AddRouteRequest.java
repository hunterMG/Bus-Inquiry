package top.ygdays.bus_inquiry.net;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Guang Yan
 * @Description:
 * @Date: Created in 上午10:49 2018/1/10
 */
public class AddRouteRequest extends StringRequest{
    private static final String ADD_ROUTE_REQUEST_URL = "https://ygdays.top/bus_inquiry/addRoute.php";
    private Map<String, String> params;

    public AddRouteRequest(String routeName, String price, String startTime, String endTime, String stopNum, Response.Listener<String> listener) {
        super(Method.POST, ADD_ROUTE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("routeName", routeName);
        params.put("price", price);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("stopNum", stopNum);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
