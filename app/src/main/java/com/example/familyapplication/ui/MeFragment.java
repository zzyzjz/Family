package com.example.familyapplication.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("退出登录");
                builder.setMessage("是否退出登录？");
                builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getActivity(),"退出",Toast.LENGTH_SHORT).show();
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
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getActivity(),"取消",Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
                builder.show();


            }
        });

        if(UsersBaseDao.searchByUserId(EMClient.getInstance().getCurrentUser()) != null){
            user = UsersBaseDao.searchByUserId(EMClient.getInstance().getCurrentUser());
        }else {
            user = new Users(null,"id","pass","nick",R.drawable.head);
        }


        head = getActivity().findViewById(R.id.me_iv_head);
        if (user.getHead() == 0){
            head.setImageResource(R.drawable.head);
        }else {
            head.setImageResource(user.getHead());
        }


        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), ModifyHeadActivity.class),0);
            }
        });

        nick = getActivity().findViewById(R.id.me_tv_nick);
        if(user.getNickname() == null || TextUtils.isEmpty(user.getNickname())){
            nick.setText("点击设置昵称");
        }else {
            nick.setText(user.getNickname());
        }

        nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), ModifyNickActivity.class),1);
            }
        });

        id = getActivity().findViewById(R.id.me_tv_id);
        id.setText(EMClient.getInstance().getCurrentUser());

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

    public void update(){
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
                startActivityForResult(new Intent(getActivity(), ModifyHeadActivity.class),0);
            }
        });

        nick = getActivity().findViewById(R.id.me_tv_nick);
        if(user.getNickname() == null || TextUtils.isEmpty(user.getNickname())){
            nick.setText("点击设置昵称");
        }else {
            nick.setText(user.getNickname());
        }

        nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), ModifyNickActivity.class),1);
            }
        });

        id = getActivity().findViewById(R.id.me_tv_id);
        id.setText(user.getUserId());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        update();
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        update();
//    }
}
