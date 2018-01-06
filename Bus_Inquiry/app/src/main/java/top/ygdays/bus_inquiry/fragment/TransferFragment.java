package top.ygdays.bus_inquiry.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import top.ygdays.bus_inquiry.MainActivity;
import top.ygdays.bus_inquiry.R;
import top.ygdays.bus_inquiry.Util;
import top.ygdays.bus_inquiry.net.TransferRequest;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TransferFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TransferFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransferFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText et_start;
    private EditText et_end;
    private Button btn_exchange;
    private Context mContext;
    private ExpandableListView transfer_ExpandableListView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TransferFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransferFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransferFragment newInstance(String param1, String param2) {
        TransferFragment fragment = new TransferFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transfer, container, false);
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
        Util.hideSoftKeyboard(view);
        et_start = (EditText) view.findViewById(R.id.et_start);
        et_end = (EditText) view.findViewById(R.id.et_end);
        btn_exchange = (Button) view.findViewById(R.id.btn_exchange);
        transfer_ExpandableListView = (ExpandableListView) view.findViewById(R.id.transfer_ExpandableListView);
        btn_exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1 = et_start.getText().toString();
                String str2 = et_end.getText().toString();
                et_start.setText(str2);
                et_end.setText(str1);
            }
        });
        et_start.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    Util.hideSoftKeyboard(view);
                    searchTransfer();
                }
                return false;
            }
        });
        et_end.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    Util.hideSoftKeyboard(view);
                    searchTransfer();
                }
                return false;
            }
        });
    }
    public void searchTransfer(){
        String startStop = et_start.getText().toString().trim();
        String endStop = et_end.getText().toString().trim();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("Transfer", response);
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("success") == true){
                        int num_route = jsonObject.getInt("num_route");
                        Toast.makeText(mContext, R.string.toast_transfer_succeed, Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray = jsonObject.getJSONArray("routes");
                        displayTransferList(jsonArray);
                    }else {
                        Toast.makeText(mContext, R.string.toast_transfer_fail, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        TransferRequest transferRequest = new TransferRequest(startStop, endStop, responseListener);
//        RequestQueue queue = Volley.newRequestQueue(mContext);
        MainActivity.requestQueue.add(transferRequest);
    }
    public void displayTransferList(final JSONArray jsonArray){
        Log.i("Transfer", jsonArray.toString());
        ExpandableListAdapter routesAdapter = new BaseExpandableListAdapter() {
            @Override
            public int getGroupCount() {
                return jsonArray.length();
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return 1;
            }

            @Override
            public Object getGroup(int groupPosition) {
                JSONObject jsonObject;
                try {
                    jsonObject = jsonArray.getJSONObject(groupPosition);
                    String routeName = jsonObject.getString("routeName");
                    int num_stop = jsonObject.getInt("num_stop");
                    if(num_stop == 1){
                        return routeName+" ("+num_stop+" "+getString(R.string.single_stop)+") ";
                    }else {
                        return routeName+" ("+num_stop+" "+getString(R.string.multi_stop)+") ";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                if (childPosition == 0){
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(groupPosition);
                        JSONArray jsonArray1 = jsonObject.getJSONArray("stops");
                        Log.i("TransferStops", jsonArray1.toString());
                        return getString(R.string.multi_stop)+": "+jsonArray1.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                }else {
                    return null;
                }
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                TextView textView = new TextView(mContext);
                textView.setText(getGroup(groupPosition).toString());
                textView.setTextSize(20);
                return textView;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                TextView textView = new TextView(mContext);
                textView.setText(getChild(groupPosition, childPosition).toString());
                return textView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return true;
            }
        };
        transfer_ExpandableListView.setAdapter(routesAdapter);
    }
}
