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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familyapplication.R;
import com.example.familyapplication.db.Users;
import com.example.familyapplication.db.UsersBaseDao;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;


public class MeFragment extends Fragment {
    private String TAG = "zzzzzjz-Me";
    private Users user;
    private ImageView head;
    private TextView id,nick;
    private Button modifyHead,modifyNick;
    private Button logout;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "MeFragment....onCreate...." );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //meTitle = getActivity().findViewById(R.id.me_title_bar);
        //meTitle.setBackgroundColor(R.color.colorPrimary);

        logout = getActivity().findViewById(R.id.me_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EMClient.getInstance().logout(true, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        Log.e(TAG, "退出登录成功···"+EMClient.getInstance().getCurrentUser() );
                        startActivity(new Intent(getActivity(),LoginActivity.class));
                        getActivity().finish();
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onError(int code, String message) {
                        // TODO Auto-generated method stub
                        Log.e(TAG, "退出登录失败···"+EMClient.getInstance().getCurrentUser() );
                        Log.e(TAG, "退出登录失败···"+message );
                    }
                });
            }
        });

        user = UsersBaseDao.searchByUserId(EMClient.getInstance().getCurrentUser());

        head = getActivity().findViewById(R.id.me_iv_head);
        head.setImageResource(user.getHead());

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ModifyHeadActivity.class));
            }
        });

        nick = getActivity().findViewById(R.id.me_tv_nick);
        if(!TextUtils.isEmpty(user.getNickname())){
            nick.setText(user.getNickname());
        }else {
            nick.setText("");
        }

        nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ModifyNickActivity.class));
            }
        });

        id = getActivity().findViewById(R.id.me_tv_id);
        id.setText(user.getUserId());

//        modifyHead = getActivity().findViewById(R.id.me_btn_modify_head);
//        modifyHead.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), ModifyHeadActivity.class));
//            }
//        });

//        modifyNick = getActivity().findViewById(R.id.me_btn_modify_nick);
//        modifyNick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), ModifyNickActivity.class));
//            }
//        });
    }
}
