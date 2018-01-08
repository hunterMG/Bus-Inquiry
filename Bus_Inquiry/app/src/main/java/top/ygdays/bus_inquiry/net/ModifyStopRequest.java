package top.ygdays.bus_inquiry.net;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Guang Yan
 * @Description:
 * @Date: Created in 下午2:25 2018/1/8
 */
public class ModifyStopRequest extends StringRequest {
    private static final String MODIFY_STOP_REQUEST_URL = "http://ygdays.top/bus_inquiry/modifyStop.php";
    private Map<String, String> params;

    public ModifyStopRequest(String oldStopName, String newStopName, Response.Listener<String> listener) {
        super(Method.POST, MODIFY_STOP_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("newStopName", newStopName);
        params.put("oldStopName", oldStopName);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
