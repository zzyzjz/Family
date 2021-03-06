package com.example.familyapplication.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.familyapplication.Contacted;
import com.example.familyapplication.adapter.FmlContactAdapter;
import com.example.familyapplication.R;
import com.example.familyapplication.db.Contacts;
import com.example.familyapplication.db.ContactsBaseDao;
import com.example.familyapplication.db.Users;
import com.example.familyapplication.db.UsersBaseDao;
import com.hyphenate.chat.EMClient;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class ContactFragment extends Fragment {
    private String TAG = "zzzzzjz-Contact";

    ListView listView;
    private List<Contacted> contactedList = new ArrayList<Contacted>() ;
    //没有初始化，所以会报错NullPointerException

    Button addUser,group,addContact,addFriend;
    ImageView iv;

    public ContactFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e(TAG, "ContactFragment  onCreate: " );
    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
//        return super.onCreateView(R.layout.fragment_contact, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_contact,container,false);
    }



    @SuppressLint("ResourceType")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //测试根据路径显示图片
//        iv = getActivity().findViewById(R.id.contact_image);
//        String path = "../res/mipmap=hdpi/harry_potter.png";
//        Bitmap bm = BitmapFactory.decodeFile(path);
//        iv.setImageBitmap(bm);
//        iv.setImageResource(R.drawable.harry_potter);
        //事实证明R.xxx.xxx 实质上是一个int
//        iv.setImageResource(R.drawable.head);
//        Log.e(TAG, "harry_potter id -> "+R.drawable.head);
//        iv.setImageResource(getPic("R.mipmap.Harry_Potter"));



        //从环信获取联系人，只能获取联系人name，还得从自己的表找头像和备注
//        try {
//            usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
//            Log.e(TAG, "Contact List" +usernames );
//        } catch (HyphenateException e) {
//            e.printStackTrace();
//        }
//
//        contactListLayout = (EaseContactList) getView().findViewById(R.id.contact_list);
//        //初始化时需要传入联系人list
//        contactListLayout.init(usernames);
//        //刷新列表
//        contactListLayout.refresh();

        if(contactedList != null || !contactedList.isEmpty()){
            contactedList.clear();
        }

        //处理联系人信息，传给adapter，显示在listView上
        initContacts();//初始化联系人数据
        FmlContactAdapter adapter = new FmlContactAdapter(getActivity(),R.layout.item_contacts,contactedList);
        final ListView listView = getActivity().findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contacted contacted = contactedList.get(position);
                Intent i = new Intent(getActivity(), ContactedInfoActivity.class);
                i.putExtra("contactedId",contacted.getContactedId());
                i.putExtra("name",contacted.getName());
                i.putExtra("head",contacted.getImage());
                //contactedId 为当前联系人的userId
                startActivityForResult(i,2);

            }
        });

        //跳转到群组页面
        group = getActivity().findViewById(R.id.contact_btn_group);
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), GroupActivity.class),1);
            }
        });

        addFriend = getActivity().findViewById(R.id.contact_btn_add_friend);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), AddContactsActivity.class),0);
            }
        });





        //测试数据库
        addUser = getActivity().findViewById(R.id.contact_btn_add_user);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAUser();
            }
        });
        addUser.setVisibility(View.GONE);
        addContact = getActivity().findViewById(R.id.contact_btn_add_contact);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAContact();
            }
        });
        addContact.setVisibility(View.GONE);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(contactedList != null || !contactedList.isEmpty()){
            contactedList.clear();
        }

        //处理联系人信息，传给adapter，显示在listView上
        initContacts();//初始化联系人数据
        FmlContactAdapter adapter = new FmlContactAdapter(getActivity(),R.layout.item_contacts,contactedList);
        final ListView listView = getActivity().findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contacted contacted = contactedList.get(position);
                Intent i = new Intent(getActivity(), ContactedInfoActivity.class);
                i.putExtra("contactedId",contacted.getContactedId());
                i.putExtra("name",contacted.getName());
                i.putExtra("head",contacted.getImage());
                //contactedId 为当前联系人的userId
                startActivityForResult(i,2);

            }
        });

    }

    public void contactedListInit(){
        //处理联系人信息，传给adapter，显示在listView上
        initContacts();//初始化联系人数据
        FmlContactAdapter adapter = new FmlContactAdapter(getActivity(),R.layout.item_contacts,contactedList);
        ListView listView = getActivity().findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }

    private void initContacts(){


        List<Users> users = UsersBaseDao.searchAll();
        Log.e(TAG, "------------------148" );
        for(Users use :users){
            Log.e(TAG, "id ---> "+use.getId() );
            Log.e(TAG, "userId ---> "+use.getUserId() );
            Log.e(TAG, "pass ---> "+use.getPassword() );
            Log.e(TAG, "nick ---> "+use.getNickname() );
            Log.e(TAG, "head ---> "+use.getHead() );
        }

        List<Contacts> contacts;
        List<String> contactedId = new ArrayList<String>();

        //在Contacts中查找userId=CurrentUser的list -> A-contacts
        //contacts是当前用户的所有联系人
        contacts = ContactsBaseDao.searchByUserId(EMClient.getInstance().getCurrentUser());
        //提出A中ContactedId的list -> B-contactedId
        //contactedId是当前用户的所有联系人的id
        for(Contacts c :contacts){
            contactedId.add(c.getContactedId());
        }
        //循环B >>> for(String b:B) <<<
        // 从User中查找userId=b的image 赋给 id
        // 从Contacts中查UserId==currentUser && ContactedId==b 的name值 赋给 name
        //没有则查User中 userId=b 的 nickname 赋给name
        //再没有就把 b赋给 name
        //用name和id实例化一个Contacted 然后add到ContactedList中
        for(String id :contactedId){
            //每一个id都是当前用户的联系人之一的id
            int image;
            String name = id;//默认name为当前联系人的userId
            Log.e(TAG, "------------------178`````contacted id --->"+id);
            image = UsersBaseDao.searchByUserId(id).getHead();
            Log.e(TAG, "---------180----------" );
            if(!id.equals(EMClient.getInstance().getCurrentUser())
                    && ContactsBaseDao.searchByUserIdAndContactedId
                    (EMClient.getInstance().getCurrentUser(),id).getName() != null
                    && !TextUtils.isEmpty(ContactsBaseDao.searchByUserIdAndContactedId
                    (EMClient.getInstance().getCurrentUser(),id).getName())  ){
                //当前用户给该联系人设置了name时
                name = ContactsBaseDao.searchByUserIdAndContactedId
                        (EMClient.getInstance().getCurrentUser(),id).getName();
            }else if(UsersBaseDao.searchByUserId(id).getNickname() != null){
                //该联系人给自己设置了昵称
                name = UsersBaseDao.searchByUserId(id).getNickname();
            }

            Contacted contacted = new Contacted(id,name,image);
            //id是该联系人的userId,name是这条item将要显示的名字，image是头像

            contactedList.add(contacted);

        }


        //从Contacts表获取当前用户的联系人
        //根据联系人的id找到联系人的头像（User表）和联系人要显示的name（name -> nick -> userId）
        //并整理成一个name和头像的list（String类型），保存在contactsList中
    }

    private void addAUser(){

        List<Users> users = UsersBaseDao.searchAll();
        Log.e(TAG, "-----------------209" );
        for(Users use :users){
            Log.e(TAG, "id ---> "+use.getId() );
            Log.e(TAG, "userId ---> "+use.getUserId() );
            Log.e(TAG, "pass ---> "+use.getPassword() );
            Log.e(TAG, "nick ---> "+use.getNickname() );
            Log.e(TAG, "head ---> "+use.getHead() );
        }
        Log.e(TAG, "---------------------217" );
        UsersBaseDao.deleteAll();
//        UsersBaseDao.deleteByKey(4);

        Users u1 = new Users(null,"user1","qwer","user01",R.drawable.h23);
        UsersBaseDao.insert(u1);
        Users u2 = new Users(null,"user2","qwer","user002",R.drawable.h13);
        UsersBaseDao.insert(u2);
        Users u3 = new Users(null,"user3","qwer",null,R.drawable.h29);
        UsersBaseDao.insert(u3);

//
//
//        users = UsersBaseDao.searchAll();
//        Log.e(TAG, "-------------------232" );
//        for(Users use :users){
//            Log.e(TAG, "id ---> "+use.getId() );
//            Log.e(TAG, "userId ---> "+use.getUserId() );
//            Log.e(TAG, "pass ---> "+use.getPassword() );
//            Log.e(TAG, "nick ---> "+use.getNickname() );
//            Log.e(TAG, "head ---> "+use.getHead() );
//        }

    }
    private void addAContact(){

        List<Contacts> contacts = ContactsBaseDao.searchAll();
        Log.e(TAG, "-------------------242" );
        for(Contacts con : contacts){
            Log.e(TAG, "id ---> "+con.getId() );
            Log.e(TAG, "user ---> "+con.getUserId() );
            Log.e(TAG, "contact ---> "+con.getContactedId() );
            Log.e(TAG, "name ---> "+con.getName() );
            Log.e(TAG, "birth ---> "+con.getBirthday() );
            Log.e(TAG, "tel ---> "+con.getTel() );
            Log.e(TAG, "remark ---> "+con.getRemarks() );
        }
        Log.e(TAG, "-------------------252" );
        ContactsBaseDao.deleteAll();
//
        Contacts c1 = new Contacts(null,"user1","user2","小1-2",null,
                null,null,null,null,null,null,"啦啦啦啦啦233333红红火火恍恍惚惚");

        ContactsBaseDao.insert(c1);

        Contacts c2 = new Contacts(null,"user3","user2","姐姐3-2",null,
                null,null,null,null,null,null,"啦啦啦啦啦233333红红火火恍恍惚惚");

        ContactsBaseDao.insert(c2);

        Contacts c3 = new Contacts(null,"user2","user1","奶奶",null,
                null,null,null,null,null,null,"啦啦啦啦啦233333红红火火恍恍惚惚");

        ContactsBaseDao.insert(c3);
//
        Contacts c4 = new Contacts(null,"user2","user3","妹妹2-3",null,
                null,null,null,null,null,null,"啦啦啦啦啦233333红红火火恍恍惚惚");

        ContactsBaseDao.insert(c4);
        Contacts c5 = new Contacts(null,"user1","user3",null,null,
                null,null,null,null,null,null,"啦啦啦啦啦233333红红火火恍恍惚惚");

        ContactsBaseDao.insert(c5);

        Contacts c6 = new Contacts(null,"user3","user1","奶奶",null,
                null,null,null,null,null,null,"啦啦啦啦啦233333红红火火恍恍惚惚");

        ContactsBaseDao.insert(c6);



//        contacts = ContactsBaseDao.searchAll();
//        Log.e(TAG, "``````254" );
//        for(Contacts con : contacts){
//            Log.e(TAG, "id ---> "+con.getId() );
//            Log.e(TAG, "user ---> "+con.getUserId() );
//            Log.e(TAG, "contact ---> "+con.getContactedId() );
//            Log.e(TAG, "name ---> "+con.getName() );
//            Log.e(TAG, "birth ---> "+con.getBirthday() );
//            Log.e(TAG, "tel ---> "+con.getTel() );
//            Log.e(TAG, "remark ---> "+con.getRemarks() );
//        }

    }

}
