package top.ygdays.bus_inquiry;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import top.ygdays.bus_inquiry.fragment.MoreFragment;
import top.ygdays.bus_inquiry.fragment.RouteFragment;
import top.ygdays.bus_inquiry.fragment.StopFragment;
import top.ygdays.bus_inquiry.fragment.TransferFragment;

public class MainActivity extends AppCompatActivity implements TransferFragment.OnFragmentInteractionListener,RouteFragment.OnFragmentInteractionListener,StopFragment.OnFragmentInteractionListener,MoreFragment.OnFragmentInteractionListener{

    private TransferFragment transferFragment;
    private RouteFragment routeFragment;
    private StopFragment stopFragment;
    private MoreFragment moreFragment;

    private FragmentManager fragmentManager;

    public static RequestQueue requestQueue;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_transfer:
                    setChoiceItem(0);
                    return true;
                case R.id.navigation_route:
                    setChoiceItem(1);
                    return true;
                case R.id.navigation_stop:
                    setChoiceItem(2);
                    return true;
                case R.id.navigation_more:
                    setChoiceItem(3);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        Preference.init(getApplicationContext());

        fragmentManager = getSupportFragmentManager();

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        if(email != null){
            Log.i("intent", email);
            setChoiceItem(3);
        }else {
            setChoiceItem(0);
        }
        //        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void hideFragment(FragmentTransaction fragmentTransaction){
        if(transferFragment != null){
            fragmentTransaction.hide(transferFragment);
        }
        if(routeFragment != null){
            fragmentTransaction.hide(routeFragment);
        }
        if(stopFragment != null){
            fragmentTransaction.hide(stopFragment);
        }
        if(moreFragment != null){
            fragmentTransaction.hide(moreFragment);
        }
    }

    private void setChoiceItem(int index){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragment(fragmentTransaction);
        switch (index){
            case 0:
                if(transferFragment == null){
                    transferFragment = new TransferFragment();
                    fragmentTransaction.add(R.id.content, transferFragment);
                }else {
                    fragmentTransaction.show(transferFragment);
                }
                break;
            case 1:
                if(routeFragment == null){
                    routeFragment = new RouteFragment();
                    fragmentTransaction.add(R.id.content, routeFragment);
                }else {
                    fragmentTransaction.show(routeFragment);
                }
                break;
            case 2:
                if(stopFragment == null){
                    stopFragment = new StopFragment();
                    fragmentTransaction.add(R.id.content, stopFragment);
                }else {
                    fragmentTransaction.show(stopFragment);
                }
                break;
            case 3:
                if(moreFragment == null){
                    moreFragment = new MoreFragment();
                    fragmentTransaction.add(R.id.content, moreFragment);
                }else {
                    fragmentTransaction.show(moreFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }
}
