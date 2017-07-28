package com.example.a25564.weifusifeng.activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.a25564.weifusifeng.MainActivity;
import com.example.a25564.weifusifeng.R;
import com.example.a25564.weifusifeng.bean.LoginBeanResult;
import com.example.a25564.weifusifeng.bean.user;
import com.example.a25564.weifusifeng.net.OkHttpManger;
import com.example.a25564.weifusifeng.utils.Constant;
import com.example.a25564.weifusifeng.utils.SharePreUtil;
import com.google.gson.Gson;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;

public class LoginActivity extends AppCompatActivity {
    private android.support.design.widget.TextInputLayout mUsername_layout;
    private android.support.design.widget.TextInputEditText mUsername_edit;
    private android.support.design.widget.TextInputLayout mMima_layout;
    private android.support.design.widget.TextInputEditText mMima_edit;
    private Button mLoginBtn;
    private RelativeLayout loaging;

    private String mUserName;
    private String mPassWord;

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        SQLiteDatabase db= LitePal.getDatabase();
        //SQLiteDatabase db= Connector.getDatabase();
        if(chechLogin()){
            startActivity(new Intent(this, MainActivity.class));
            finish();

        }else {
            bindViews();
            initStatusBarColor();
            initListener();
        }


    }

    private void bindViews() {
        mUsername_layout = (android.support.design.widget.TextInputLayout) findViewById(R.id.username_layout);
        mUsername_edit = (android.support.design.widget.TextInputEditText) findViewById(R.id.username_edit);
        mMima_layout = (android.support.design.widget.TextInputLayout) findViewById(R.id.mima_layout);
        mMima_edit = (android.support.design.widget.TextInputEditText) findViewById(R.id.mima_edit);
        mLoginBtn = (Button) findViewById(R.id.loginBtn);
        loaging= (RelativeLayout) findViewById(R.id.loaging);
        addChangeL();
    }

    private void addChangeL(){
        mUsername_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mUsername_layout.setErrorEnabled(false);
            }
        });
        mMima_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mMima_layout.setErrorEnabled(false);
            }
        });
    }

    private void initListener(){
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login(){
        if(chechData()){
            loaging.setVisibility(View.VISIBLE);
            OkHttpManger.Param param1=new OkHttpManger.Param("userid",mUserName);
            OkHttpManger.Param param2=new OkHttpManger.Param("password",mPassWord);
            OkHttpManger.getInstance().postNet(Constant.Login, new OkHttpManger.ResultCallback() {
                @Override
                public void onFailed(Request request, IOException e) {
                    loaging.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"服务器异常，登陆失败！",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(String s) {
                    loaging.setVisibility(View.GONE);
                    LoginBeanResult loginBeanResult=getDataFromGson(s);
                    if(loginBeanResult.getCode()==0){
                        //用户存在，密码真确，先保持用户信息，再跳转主界面
                        SharePreUtil.SetShareString(getApplicationContext(),"userid",loginBeanResult.getBody().getUserid());
                        DataSupport.deleteAll(user.class);
                        user user = new user();
                        user.setUserId(loginBeanResult.getBody().getUserid());
                        user.setNickName(loginBeanResult.getBody().getNickname());
                        user.setSex(loginBeanResult.getBody().getSex());
                        user.setJob(loginBeanResult.getBody().getJob());
                        user.setArea(loginBeanResult.getBody().getArea());
                        user.setPhoneNum(loginBeanResult.getBody().getPhonenum());
                        user.setImg(loginBeanResult.getBody().getImg());
                        //保存数据到数据库中
                        user.save();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "用户名或密码错误，登录失败", Toast.LENGTH_SHORT).show();
                    }
                }
            },param1,param2);
            //startActivity(new Intent(this,MainActivity.class));
            //finish();
        }
    }

    private boolean chechData(){
        mUserName = mUsername_edit.getText().toString().trim();
        mPassWord = mMima_edit.getText().toString().trim();
        if (TextUtils.isEmpty(mUserName.trim())) {
            mUsername_layout.setErrorEnabled(true);
            mUsername_layout.setError("用户名不能为空");
            return false;
        }
        if (mUserName.trim().length() < 0 || mUserName.trim().length() > 6) {
            mUsername_layout.setErrorEnabled(true);
            mUsername_layout.setError("请输入6位数以内的用户名");
            return false;
        }
        if (TextUtils.isEmpty(mPassWord)) {
            mMima_layout.setErrorEnabled(true);
            mMima_layout.setError("密码不能为空");
            return false;
        }
        return true;
    }

    private boolean chechLogin(){
        List<user> userList=DataSupport.findAll(user.class);
        if(userList!=null&&userList.size()>0){
            return true;
        }else return false;
    }
    private void initStatusBarColor(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                   | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private LoginBeanResult getDataFromGson(String response){
        Gson gson = new Gson();
        LoginBeanResult result=gson.fromJson(response,LoginBeanResult.class);
        return result;
    }
}

