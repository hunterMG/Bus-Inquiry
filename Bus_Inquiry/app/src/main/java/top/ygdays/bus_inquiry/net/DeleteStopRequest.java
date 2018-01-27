package top.ygdays.bus_inquiry.net;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Guang Yan
 * @Description: launch a request to the server to delete a stop
 * @Date: Created in 下午1:18 2018/1/9
 */
public class DeleteStopRequest extends StringRequest{
    private static final String DELETE_STOP_REQUEST_URL = "https://ygdays.top/bus_inquiry/deleteStop.php";
    private Map<String, String> params;

    public DeleteStopRequest(String stopName,  Response.Listener<String> listener) {
        super(Request.Method.POST, DELETE_STOP_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("stopName", stopName);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
