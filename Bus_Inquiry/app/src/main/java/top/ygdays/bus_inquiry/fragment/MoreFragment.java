package top.ygdays.bus_inquiry.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import com.android.volley.Response;
import org.json.JSONException;
import org.json.JSONObject;
import top.ygdays.bus_inquiry.*;
import top.ygdays.bus_inquiry.net.AddStopRequest;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button btn_login;
    private TextView tv_email;
    private Context mContext;
    private Button btn_logout;
    private TextView tv_add_stop;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoreFragment newInstance(String param1, String param2) {
        MoreFragment fragment = new MoreFragment();
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
        return inflater.inflate(R.layout.fragment_more, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_email = (TextView)view.findViewById(R.id.tv_email);
        tv_add_stop = (TextView)view.findViewById(R.id.tv_add_stop);
        tv_add_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Preference.isAdmin()){
                    final EditText et_stopname = new EditText(mContext);
                    et_stopname.setSingleLine();
                    new AlertDialog.Builder(mContext)
                            .setTitle(R.string.title_add_stop)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setView(et_stopname)
                            .setPositiveButton(R.string.Add, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String stopName = et_stopname.getText().toString().trim();
                                    if(stopName.isEmpty()){
                                        Toast.makeText(mContext, R.string.toast_stop_name_empty, Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    Response.Listener<String> rl = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                if(jsonObject.getBoolean("success")){
                                                    Toast.makeText(mContext, R.string.toast_add_stop_succeed, Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Toast.makeText(mContext, R.string.toast_add_stop_fail, Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    AddStopRequest asr = new AddStopRequest(stopName, rl);
                                    MainActivity.requestQueue.add(asr);
                                }
                            })
                            .setNegativeButton(R.string.cancel, null)
                            .show();
                }else {
                    Toast.makeText(mContext, R.string.toast_login_hint, Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_login = (Button)view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        btn_logout = (Button) view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preference.logout();
                btn_login.setVisibility(View.VISIBLE);
                btn_logout.setVisibility(View.INVISIBLE);
                Toast.makeText(mContext, R.string.toast_logout_success, Toast.LENGTH_SHORT).show();
                tv_email.setText(Preference.getUserInfo());
            }
        });
        Util.hideSoftKeyboard(view);

        String email = Preference.getUserInfo();
        Log.i("Email", email);
        if(email.equals("null")){
            btn_logout.setVisibility(View.INVISIBLE);
        }else {
            btn_login.setVisibility(View.INVISIBLE);
        }
        tv_email.setText(email);
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
}
