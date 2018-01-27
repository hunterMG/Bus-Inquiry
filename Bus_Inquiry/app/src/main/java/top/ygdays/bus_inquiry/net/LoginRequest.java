package top.ygdays.bus_inquiry.net;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Guang Yan
 * @Description: launch a login request to login users' accounts
 * @Date: Created in 下午5:06 2018/1/5
 */
public class LoginRequest extends StringRequest{
    private static final String LOGIN_REQUEST_URL = "https://ygdays.top/bus_inquiry/login.php";
    private Map<String, String> params;

    public LoginRequest(String email, String pswd, Response.Listener<String> listener) {
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("email", email);
        params.put("pswd", pswd);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
