package top.ygdays.bus_inquiry;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.android.volley.Response;
import org.json.JSONException;
import org.json.JSONObject;
import top.ygdays.bus_inquiry.data.Route;
import top.ygdays.bus_inquiry.net.AddRouteRequest;
import top.ygdays.bus_inquiry.net.ModifyRouteRequest;

public class ModifyRouteActivity extends AppCompatActivity {

    private EditText et_routeName;
    private EditText et_routePrice;
    private EditText et_routeStartTime;
    private EditText et_routeEndTime;
    private EditText et_routeStopNum;
    private String mHour, mMinute;
    private Route route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_route);

        et_routeName = (EditText)findViewById(R.id.et_route_name);
        et_routePrice = (EditText)findViewById(R.id.et_route_price);
        et_routeStartTime = (EditText)findViewById(R.id.et_route_starttime);
        et_routeEndTime = (EditText)findViewById(R.id.et_route_endtime);
        et_routeStopNum = (EditText)findViewById(R.id.et_route_stopnum);

        route = Util.getInstance().getRoute();
        et_routeName.setText(route.RouteName);
        et_routePrice.setText(route.Price.toString());
        et_routeStartTime.setText(route.StartTime);
        et_routeEndTime.setText(route.EndTime);
        et_routeStopNum.setText(Integer.toString(route.StopNum));

        et_routeStartTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {return; }
                TimePickerDialog timePickerDialog = new TimePickerDialog(ModifyRouteActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(hourOfDay < 10){
                            mHour = "0"+hourOfDay;
                        }else {
                            mHour = ""+hourOfDay;
                        }
                        if(minute < 10){
                            mMinute = "0"+minute;
                        }else {
                            mMinute = ""+minute;
                        }
                        et_routeStartTime.setText(mHour+":"+mMinute+":00");
                    }
                }, Integer.valueOf(route.StartTime.substring(0,2)), Integer.valueOf(route.StartTime.substring(3,5)), true);
                // 01:34:67
                timePickerDialog.show();
            }
        });

        et_routeEndTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {return; }
                TimePickerDialog timePickerDialog = new TimePickerDialog(ModifyRouteActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(hourOfDay < 10){
                            mHour = "0"+hourOfDay;
                        }else {
                            mHour = ""+hourOfDay;
                        }
                        if(minute < 10){
                            mMinute = "0"+minute;
                        }else {
                            mMinute = ""+minute;
                        }
                        et_routeEndTime.setText(mHour+":"+mMinute+":00");
                    }
                }, Integer.valueOf(route.EndTime.substring(0,2)), Integer.valueOf(route.EndTime.substring(3,5)), true);
                timePickerDialog.show();
            }
        });
        et_routeStopNum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == R.id.modify_route || actionId == EditorInfo.IME_ACTION_DONE){
                    modifyRoute();
                    return true;
                }
                return false;
            }
        });
    }

    private void modifyRoute(){
        et_routeName.setError(null);
        et_routePrice.setError(null);
        et_routeStartTime.setError(null);
        et_routeEndTime.setError(null);
        et_routeStopNum.setError(null);

        String name = et_routeName.getText().toString().trim();
        String price = et_routePrice.getText().toString().trim();
        String startTime = et_routeStartTime.getText().toString().trim();
        String endTime = et_routeEndTime.getText().toString().trim();
        String stopNum = et_routeStopNum.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(name)){
            et_routeName.setError(getString(R.string.error_field_required));
            focusView = et_routeName;
            cancel = true;
        }else if(TextUtils.isEmpty(price)){
            et_routePrice.setError(getString(R.string.error_field_required));
            focusView = et_routePrice;
            cancel = true;
        }else if(TextUtils.isEmpty(startTime)){
            et_routeStartTime.setError(getString(R.string.error_field_required));
            focusView = et_routeStartTime;
            cancel = true;
        }else if(TextUtils.isEmpty(endTime)){
            et_routeEndTime.setError(getString(R.string.error_field_required));
            focusView = et_routeEndTime;
            cancel = true;
        }else if (TextUtils.isEmpty(stopNum)){
            et_routeStopNum.setError(getString(R.string.error_field_required));
            focusView = et_routeStopNum;
            cancel = true;
        }else if(!isPriceValid(price)){
            et_routePrice.setError(getString(R.string.error_num_invalid));
            focusView = et_routePrice;
            cancel = true;
        }else if (!isStopNumValid(stopNum)){
            et_routeStopNum.setError(getString(R.string.error_num_invalid));
            focusView = et_routeStopNum;
            cancel = true;
        }

        if(cancel){
            focusView.requestFocus();
        }else {
            Response.Listener<String> rl = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("ModifyRouteResponse", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getBoolean("success")){
                            Toast.makeText(ModifyRouteActivity.this, R.string.toast_modify_route_succeed, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(ModifyRouteActivity.this, R.string.toast_modify_route_fail, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            ModifyRouteRequest mrr = new ModifyRouteRequest(Integer.toString(route.RouteID), name, price, startTime, endTime, stopNum, rl);
            MainActivity.requestQueue.add(mrr);
        }
    }

    private boolean isPriceValid(String num){
        try {
            float value = Float.parseFloat(num);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private boolean isStopNumValid(String num){
        try {
            int value = Integer.parseInt(num);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
