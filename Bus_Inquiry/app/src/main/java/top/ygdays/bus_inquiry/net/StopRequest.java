package top.ygdays.bus_inquiry.net;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Guang Yan
 * @Description:
 * @Date: Created in 下午9:55 2018/1/1
 */
public class StopRequest extends StringRequest{
    private static final String STOP_REQUEST_URL = "http://ygdays.top/bus_inquiry/queryStop.php";
    private Map<String, String> params;

    public StopRequest(String stopName, Response.Listener<String> listener) {
        super(Method.POST, STOP_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("stopName", stopName);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
