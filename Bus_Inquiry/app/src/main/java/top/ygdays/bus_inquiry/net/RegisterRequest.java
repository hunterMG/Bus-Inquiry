package top.ygdays.bus_inquiry.net;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Guang Yan
 * @Description: register a user accout using email address
 * @Date: Created in 下午1:14 2018/1/6
 */
public class RegisterRequest extends StringRequest{
    private static final String REGISTER_REQUEST_URL = "https://ygdays.top/bus_inquiry/register.php";
    private Map<String, String> params;

    public RegisterRequest(String email, String pswd, Response.Listener<String> listener) {
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("email", email);
        params.put("pswd", pswd);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
