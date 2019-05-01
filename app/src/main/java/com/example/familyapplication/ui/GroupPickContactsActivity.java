package com.example.familyapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.familyapplication.GPContactAdapter;
import com.example.familyapplication.R;
import com.example.familyapplication.db.Contacts;
import com.example.familyapplication.db.ContactsBaseDao;
import com.example.familyapplication.db.Users;
import com.example.familyapplication.db.UsersBaseDao;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.widget.EaseSidebar;

import java.util.ArrayList;
import java.util.List;

public class GroupPickContactsActivity extends AppCompatActivity {

    private static final String TAG = "zjz--groupPickContacts";

    private Button create;

    private List<Users> contactList = new ArrayList<Users>();

    //if this is a new group
    protected boolean isCreatingNewGroup;
    private PickContactAdapter pickContactAdapter;
    //members already in the group
    private List<String> existMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_pick_contacts);

        String groupId = getIntent().getStringExtra("groupId");
        if(groupId == null){
            //创建新群
            isCreatingNewGroup = true;
        }else {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);
            existMembers = group.getMembers();
            existMembers.add(group.getOwner());
            existMembers.addAll(group.getAdminList());

        }
        if (existMembers == null){
            existMembers = new ArrayList<String>();
        }



        contactList.clear();
        initContactList();
        //get contact list
//        final List<Users> allUserList = new ArrayList<Users>();

        ListView listView = findViewById(R.id.pick_contacts_list);
        Log.e(TAG, "-------------67-------------: " );
        pickContactAdapter = new PickContactAdapter(GroupPickContactsActivity.this,
                R.layout.row_contact_with_checkbox,contactList);
        //  !!!!!
        listView.setAdapter(pickContactAdapter);
        Log.e(TAG, "-------------70-------------: " );

        ((EaseSidebar)findViewById(R.id.sidebar)).setListView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "-------------75-------------: " );
                CheckBox checkBox = findViewById(R.id.checkBox);
                checkBox.toggle();
            }
        });


        create = findViewById(R.id.pick_contacts_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> members = getToBeAddMeembers();

                setResult(RESULT_OK,new Intent().putExtra(
                        "newmembers",members.toArray(new String[members.size()])));
//                Log.e(TAG, "pick create  member:" +members.get(0));

                finish();
            }
        });
    }

    private List<String> getToBeAddMeembers(){
        List<String> members = new ArrayList<String>();
        int length = pickContactAdapter.isCheckedArray.length;
        Log.e(TAG, "members length -> "+length );
        for (int i = 0; i < length; i++) {
            String username = ((Users)pickContactAdapter.getItem(i)).getUserId();
            Log.e(TAG, "------110-----user name -> "+username);
            Log.e(TAG, "------111-----isChecked -> "+pickContactAdapter.isCheckedArray[i]);
            if (pickContactAdapter.isCheckedArray[i] ) {
                Log.e(TAG, "------112-----user name -> "+username);
                if(!existMembers.contains(username)){
                    Log.e(TAG, "------114-----user name -> "+username);
                    members.add(username);
                }

            }
        }


        return members;
    }




    public void initContactList(){
        //初始化当前用户的联系人List
        List<Contacts> cons = ContactsBaseDao.searchByUserId(EMClient.getInstance().getCurrentUser());
        for (Contacts con:cons){
            Users u = UsersBaseDao.searchByUserId(con.getContactedId());
            contactList.add(u);
            Log.e(TAG, "initContactList: " +con.getUserId()+"<------->"+con.getContactedId());
        }
    }

    private class PickContactAdapter extends GPContactAdapter {

        private static final String TAG = "zjz-pick adapter";

        private boolean[] isCheckedArray;

        public PickContactAdapter(Context context, int resource, List objects) {
            super(context, resource, objects);
            Log.e(TAG, "-------------130-------------: ");
            isCheckedArray = new boolean[objects.size()];
            Log.e(TAG, "-------------132-------------: "+isCheckedArray.length);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            Log.e(TAG, "Pick Adapter ------154----- " );

            final Users user = (Users) getItem(position);
            Log.e(TAG, "------158--------user -> "+user.getUserId());
            final CheckBox checkBox =  view.findViewById(R.id.checkBox);
            ImageView avatarView =  view.findViewById(R.id.avatar);
            TextView nameView =  view.findViewById(R.id.name);

            if (checkBox != null) {//TODO: checkBox == null

                checkBox.setButtonDrawable(R.drawable.checkbox_bg_selector);
                Log.e(TAG, "------166--------check box  ");

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Log.e(TAG, "------171--------ckeck box checked -> ");
                        // check the exist members
                        if (existMembers.contains(user.getUserId())) {
                            isChecked = true;
                            checkBox.setChecked(true);
                        }
                        Log.e(TAG, "------177--------ckeck box checked -> ");
                        isCheckedArray[position] = isChecked;

                    }
                });
//              checkBox.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        Log.e(TAG, "------185--------ckeck box checked -> ");
////                        isCheckedArray[position] = !isCheckedArray[position];
////                    }
////                });
                // keep exist members checked
                if (existMembers.contains(user.getUserId())) {
                    checkBox.setChecked(true);
                    isCheckedArray[position] = true;
                } else {
                    checkBox.setChecked(isCheckedArray[position]);
                }
            }

            return view;
        }
    }

    public void back(View view){
        finish();
    }
}
