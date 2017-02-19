package com.panda.store;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.panda.store.entity.StoreVidwo;
import com.panda.store.utils.Config;
import com.panda.store.utils.ListDataSave;
import com.panda.store.utils.StringUtil;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_manageCode,et_storeCode ,et_storePwd;
    String manage,store,storePwd;
    AsyncHttpClient mClient = new AsyncHttpClient();
    int manageCode,storeCode,pwd;
    private Button login;
    private SharedPreferences spf;
    SharedPreferences.Editor et;
    ListDataSave listsave;
    ArrayList<String> infolist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        spf=this.getSharedPreferences(Config.LOFIN_INFO, Context.MODE_PRIVATE);
        infolist=new ArrayList<String>();
        initView();
        setListener();
    }
    private void initView() {
        et_manageCode=(EditText) findViewById(R.id.et_manageCode);
        et_storeCode=(EditText) findViewById(R.id.et_storeCode);
        et_storePwd=(EditText) findViewById(R.id.et_storePwd);
        login=(Button) findViewById(R.id.btn_login);
    }
    private void setListener(){

        login.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean flag=spf.getBoolean("flag", false);
        if(flag){
            manage=spf.getString("manageCode", "");
            store=spf.getString("storeCode", "");
            storePwd=spf.getString("storePwd", "");
            et_manageCode.setText(manage);
            et_storeCode.setText(store);
            et_storePwd.setText(storePwd);
        }else{
            et_manageCode.setText("");
            et_storeCode.setText("");
            et_storePwd.setText("");
        }
    }
    private boolean isNull(){
        if(StringUtil.isEmptyOrNull(et_manageCode.getText().toString().trim())){
            Toast.makeText(this, "管理公司代码不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if(StringUtil.isEmptyOrNull(et_storeCode.getText().toString().trim())){
            Toast.makeText(this, "门店不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if(StringUtil.isEmptyOrNull(et_storePwd.getText().toString().trim())){
            Toast.makeText(this, "门店密码不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                if(isNull()){
                    manageCode=Integer.valueOf(et_manageCode.getText().toString().trim());
                    storeCode=Integer.valueOf(et_storeCode.getText().toString().trim());
                    pwd=Integer.valueOf(et_storePwd.getText().toString().trim());
                    String  url=Config.ROOT;
                    RequestParams p = new RequestParams();
                    // String json = "{\"key01\":\"" + value01 + "\",\"key02\":" + value02+ "}";
                    String value01="3001";
                    String json= "{\"msgNo\":\"" + value01 + "\",\"managementCompany\":" + manageCode+ ",\"store\":"  + storeCode +",\"storePassWord\":"+ pwd +"}";
                    // Log.d("json",json+"");
                    p.put("jsonObject",json);
                    mClient.post(url, p, new AsyncHttpResponseHandler(){
                        @Override
                        public void onSuccess(String res) {
                            // TODO Auto-generated method stub
                            super.onSuccess(res);
                            //Log.d("info", res+"");
                            StoreVidwo r= JSON.parseObject(res, StoreVidwo.class);
                            if(r.ok()){
                                et=spf.edit();
                                et.putString("manageCode", et_manageCode.getText().toString().trim());
                                et.putString("storeCode", et_storeCode.getText().toString().trim());
                                et.putString("storePwd", et_storePwd.getText().toString().trim());
                                et.putBoolean("flag", true);
                                et.commit();
                                ArrayList<StoreVidwo.Data> list=(ArrayList<StoreVidwo.Data>) r.getData();
                                for(StoreVidwo.Data d :list){
                                    String s= JSONObject.toJSONString(d);
                                    infolist.add(s);
                                }
                                listsave=new ListDataSave(LoginActivity.this, Config.DOWNLIST);
                                listsave.setDataList(Config.TAG_DOWN, infolist);
                                Intent intent=new Intent(LoginActivity.this, MainActivity.class);

                                startActivity(intent);
                                LoginActivity.this.finish();
                            }
                            //Log.d("info", res+"");
                        }
                        @Override
                        public void onFailure(Throwable e, String res) {
                            // TODO Auto-generated method stub
                            super.onFailure(e, res);
                        }
                    });
                }
                break;
        }
    }
}
