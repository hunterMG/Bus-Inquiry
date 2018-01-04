package top.ygdays.bus_inquiry.net;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
/**
 * @Author: Guang Yan
 * @Description: launch a http POST request to search a transfer method
 * @Date: Created in 下午10:30 2018/1/2
 */
public class TransferRequest extends StringRequest{

    private static final String TRANSFER_REQUEST_URL = "http://ygdays.top/bus_inquiry/queryTransfer.php";
    private Map<String, String> params;

    public TransferRequest(String startStop, String endStop, Response.Listener<String> listener) {
        super(Method.POST, TRANSFER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("startStop", startStop);
        params.put("endStop", endStop);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
