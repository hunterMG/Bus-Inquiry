package top.ygdays.bus_inquiry.net;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Guang Yan
 * @Description: launch a request to server to add a stop
 * @Date: Created in 上午10:33 2018/1/8
 */
public class AddStopRequest extends StringRequest{
    private static final String ADD_STOP_REQUEST_URL = "http://ygdays.top/bus_inquiry/addStop.php";
    private Map<String, String> params;

    public AddStopRequest(String stopName,  Response.Listener<String> listener) {
        super(Method.POST, ADD_STOP_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("stopName", stopName);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
