package top.ygdays.bus_inquiry.net;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
/**
 * @Author: Guang Yan
 * @Description: launch a request to the server for route query
 * @Date: Created in 下午4:26 2017/12/30
 */
public class RouteRequest extends StringRequest{

    private static final String ROUTE_REQUEST_URL = "http://ygdays.top/bus_inquiry/queryRoute.php";
    private Map<String, String> params;

    public RouteRequest(String routeName, Response.Listener<String> listener) {
        super(Method.POST, ROUTE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("routeName", routeName);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
