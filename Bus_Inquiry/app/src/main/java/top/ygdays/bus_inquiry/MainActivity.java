package top.ygdays.bus_inquiry;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import top.ygdays.bus_inquiry.fragment.RouteFragment;
import top.ygdays.bus_inquiry.fragment.StopFragment;
import top.ygdays.bus_inquiry.fragment.TransferFragment;

public class MainActivity extends AppCompatActivity implements TransferFragment.OnFragmentInteractionListener,RouteFragment.OnFragmentInteractionListener,StopFragment.OnFragmentInteractionListener{

    private TextView mTextMessage;
    private FrameLayout transferLayout;

    private TransferFragment transferFragment;
    private RouteFragment routeFragment;
    private StopFragment stopFragment;

    private FragmentManager fragmentManager;

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

        fragmentManager = getSupportFragmentManager();
        setChoiceItem(0);
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
                if(transferFragment == null){
                    transferFragment = new TransferFragment();
                    fragmentTransaction.add(R.id.content, transferFragment);
                }else {
                    fragmentTransaction.show(transferFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }
}
