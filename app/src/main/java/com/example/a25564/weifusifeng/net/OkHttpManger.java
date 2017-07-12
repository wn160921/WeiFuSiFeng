package com.example.a25564.weifusifeng.net;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by 25564 on 2017/7/12.
 */

public class OkHttpManger {
    private static OkHttpManger instance;
    private OkHttpClient okHttpClient;
    private Handler okhandler;

    private OkHttpManger(){     //单例模式，不让new
        okhandler=new Handler(Looper.getMainLooper());
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20,TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS);
        okHttpClient=builder.build();
    }
    public static OkHttpManger getInstance(){
        if(instance == null){
            synchronized (OkHttpManger.class){
                instance=new OkHttpManger();
            }
        }
        return instance;
    }

    public void postNet(String url,ResultCallback resultCallback,Param... params){
        if(params==null){
            params=new Param[0];    //校验
        }
        FormBody.Builder formbody= new FormBody.Builder();//模拟表单请求
        for(Param p:params){
            formbody.add(p.key,p.value);
        }
        RequestBody requestBody=formbody.build();
        Request request=new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        dealNet(request,resultCallback);
    }

    private void dealNet(final Request request, final ResultCallback resultCallback){
        okHttpClient.newCall(request).enqueue(new Callback() {//异步执行
            @Override
            public void onFailure(Call call, final IOException e) {
                okhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        resultCallback.onFailed(request,e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String str="";
                try{
                    str=response.body().string();
                }catch (IOException e){
                    e.printStackTrace();
                }
                final String finalstr=str;
                okhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        resultCallback.onSuccess(finalstr);

                    }
                });
            }
        });
    }

    public static class Param{
        String key;
        String value;
        public Param(String key,String value){
            this.key=key;
            this.value=value;
        }
    }

    public static abstract class ResultCallback{
        public abstract void onFailed(Request request,IOException e);
        public abstract void onSuccess(String s);
    }
}
