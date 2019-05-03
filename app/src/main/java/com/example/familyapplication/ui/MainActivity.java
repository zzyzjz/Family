package com.example.familyapplication.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familyapplication.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.ui.EaseBaiduMapActivity;
import com.hyphenate.easeui.widget.EaseChatExtendMenu;
import com.hyphenate.easeui.widget.EaseConversationList;



public class MainActivity extends AppCompatActivity {

    protected static final String TAG = "zzzzzjzMain";

//    private ContactFragment cf = new ContactFragment();



    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_message:
                    mTextMessage.setText(R.string.title_message);


                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_view,new MessageFragment()).commit();
                    return true;
                case R.id.navigation_address_list:
                    mTextMessage.setText(R.string.title_address_list);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_view,new ContactFragment()).commit();
                    return true;
                case R.id.navigation_me:
                    mTextMessage.setText(R.string.title_me);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_view,new MeFragment()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e(TAG, "MainActivity" );
        // auto login mode, make sure all group and conversation is loaed before enter the main screen
        if (!EMClient.getInstance().isLoggedInBefore()) {

//            try {
//                Thread.sleep(1500);
//            } catch (InterruptedException e) {
//            }
//

            startActivity(new Intent(MainActivity.this, LoginActivity.class));
//            cf.contactedListInit();
            finish();
        }


        EMClient.getInstance().chatManager().loadAllConversations();
        EMClient.getInstance().groupManager().loadAllGroups();

        //wait
//        try {
//            Thread.sleep(1500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //enter main screen
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null){
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.main_view,new PlaceholderFragment()).commit();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_view,new MessageFragment()).commit();
        }


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }






}
