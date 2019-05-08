package com.example.familyapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.familyapplication.R;
import com.example.familyapplication.adapter.FmlRemindAdapter;
import com.example.familyapplication.db.Contacts;
import com.example.familyapplication.db.ContactsBaseDao;
import com.example.familyapplication.list.ListViewInScroll;
import com.hyphenate.chat.EMClient;

import java.util.List;


public class RemindFragment extends Fragment {

    private String TAG = "zzzzzjz-Remind fragment";

    private ListViewInScroll listView;
    private List<Contacts> contactsList;

    public RemindFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_remind, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        contactsList = ContactsBaseDao.searchByUserId(EMClient.getInstance().getCurrentUser());

        FmlRemindAdapter adapter = new FmlRemindAdapter(getActivity(),R.layout.item_remind,contactsList);
        listView = getActivity().findViewById(R.id.remind_fragment_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contacts c = contactsList.get(position);

                //跳转到remindActivity,传当前用户id和联系人id
                Intent i = new Intent(getActivity(),RemindActivity.class);
                i.putExtra("userId",c.getUserId());
                i.putExtra("contactedId",c.getContactedId());
                startActivityForResult(i,0);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //返回时更新页面
        contactsList = ContactsBaseDao.searchByUserId(EMClient.getInstance().getCurrentUser());

        FmlRemindAdapter adapter = new FmlRemindAdapter(getActivity(),R.layout.item_remind,contactsList);
        listView = getActivity().findViewById(R.id.remind_fragment_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contacts c = contactsList.get(position);

                //跳转到remindActivity,传当前用户id和联系人id
                Intent i = new Intent(getActivity(),RemindActivity.class);
                i.putExtra("userId",c.getUserId());
                i.putExtra("contactedId",c.getContactedId());
                startActivityForResult(i,0);
            }
        });
    }
}
