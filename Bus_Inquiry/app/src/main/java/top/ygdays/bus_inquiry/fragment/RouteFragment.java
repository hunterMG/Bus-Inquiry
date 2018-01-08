package top.ygdays.bus_inquiry.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.*;

import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import top.ygdays.bus_inquiry.MainActivity;
import top.ygdays.bus_inquiry.R;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import top.ygdays.bus_inquiry.Util;
import top.ygdays.bus_inquiry.data.Route;
import top.ygdays.bus_inquiry.net.RouteRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RouteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RouteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RouteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EditText et_route;
    private Button btn_search_route;
    private ExpandableListView routeExpandableListView;
    private Context mContext;

    public RouteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RouteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RouteFragment newInstance(String param1, String param2) {
        RouteFragment fragment = new RouteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();   // get the context of the fragment
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_route, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        et_route = (EditText) view.findViewById(R.id.et_route);
        et_route.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    if(!TextUtils.isEmpty(et_route.getText())){
                        Util.hideSoftKeyboard(view);
                        searchRoute(view);
                    }else {
                        et_route.requestFocus();
                    }
                }
                return false;
            }
        });
        et_route.requestFocus();
        routeExpandableListView = (ExpandableListView) view.findViewById(R.id.route_ExpandableListView);
    }
    /*
     * Search route use "POST" method(volley)
     */
    public void searchRoute(final View view){
        String routeName = et_route.getText().toString().trim();
        // resolve the route query result
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("route", response);
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success){
                        Toast.makeText(mContext, R.string.toast_route_succeed, Toast.LENGTH_SHORT).show();
                        Log.i("route", "succeed");
                        int num_route = jsonResponse.getInt("num_route");
                        if(num_route > 0){
                            List<Route> routeList = Util.getRouteList(jsonResponse);
                            //Display routes in ExpandableListView
                            Util.displayRoutes(routeList, view, mContext, routeExpandableListView);
                            Route r = routeList.get(0);
                            Log.i("route", r.toString());
                        }else {
                            Toast.makeText(mContext, R.string.toast_0_route, Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(mContext, R.string.toast_route_fail, Toast.LENGTH_SHORT).show();
                        Log.i("route", "fail");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        RouteRequest routeRequest = new RouteRequest(routeName, responseListener);
//        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        MainActivity.requestQueue.add(routeRequest);
        Log.i("route" , "route query has been launched (name: "+routeName+") ");
    }

}
