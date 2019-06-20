package com.zwu.customs_broker.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zwu.customs_broker.R;
import com.zwu.customs_broker.bean.Login;
import com.zwu.customs_broker.bean.User;
import com.zwu.customs_broker.util.HttpUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private EditText _usernameText;
    private EditText _passwordText;
    private RaiflatButton _loginButton;
    private TextView _signupLink;
    private ProgressDialog progressDialog;


    private String username;
    private String password;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ScrollView scrollview = (ScrollView) findViewById(R.id.scrollview);


        _usernameText = findViewById(R.id.input_username);
        _passwordText = findViewById(R.id.input_password);
        _loginButton = findViewById(R.id.btn_login);
        _signupLink = findViewById(R.id.link_signup);


        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login(v);
            }
        });

        //注册文本监听器
        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        Intent intent = getIntent();



        if ( intent.getStringExtra("setpassword")!=null) {
            Snackbar.make(scrollview, "修改密码成功", Snackbar.LENGTH_LONG).show();
        }
    }

    public void login(final View view) {
        Log.d(TAG, "执行了登录操作");

        if (!validate()) {
            onLoginFailed(view, "input validata");
            return;
        }

        _loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("正在为您登录...");
        progressDialog.show();

        password = _passwordText.getText().toString();
        username = _usernameText.getText().toString();


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        String url = "http://129.211.77.80:8080/api/loginCheck";
                        RequestBody requestBody = new FormBody.Builder()
                                .add("userName", username)
                                .add("passWord", password)
                                .build();
                        HttpUtil.sendOkHttpRequest(url, requestBody, new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d(TAG, "登录异常:" + e);
                                onLoginFailed(view, e.toString());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseData = response.body().string();
                                Log.d(TAG, "输出:" + responseData);
                                parseJSONWithGSON(responseData, view);
                            }
                        });
                    }
                }, 1000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                //Toast.makeText(getBaseContext(), "注册成功", Toast.LENGTH_LONG).show();
                Log.d(TAG, "注册成功返回了登录界面");
                //this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String authorization) {
        LoginActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                progressDialog.dismiss();
                _loginButton.setEnabled(true);
            }
        });

        username = _usernameText.getText().toString();
        SharedPreferences sp = getSharedPreferences("context", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("authorization", authorization);
        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();

    }

    public void onLoginFailed(View view, String error) {
        Log.d(TAG, "登录失败:" + error);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        LoginActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                Snackbar.make(view, "用户名不存在或密码错误", Snackbar.LENGTH_LONG).show();
                _loginButton.setEnabled(true);
            }
        });
    }

    public boolean validate() {
        boolean valid = true;
        password = _passwordText.getText().toString();
        username = _usernameText.getText().toString();
        if (username.isEmpty()) {
            _usernameText.setError("用户名不能为空");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 3 || password.length() > 10) {
            _passwordText.setError("请输入合法的密码");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }


    private void parseJSONWithGSON(String jsonData, View view) {
        //使用轻量级的Gson解析得到的json
        Gson gson = new Gson();
        Login loginList = gson.fromJson(jsonData, new TypeToken<Login>() {
        }.getType());
        Log.d(TAG, "code:" + loginList.getCode());
        Log.d(TAG, "info:" + loginList.getUser());

        if (loginList.getCode().equals("success")) {
            User user = gson.fromJson(gson.toJson(loginList.getUser()), new TypeToken<User>() {
            }.getType());
            Log.d(TAG, "登录验证通过");
            Log.d(TAG, "username:" + user.getUserName());
            Log.d(TAG, "password:" + user.getPassWord());
            Log.d(TAG, "agentcode:" + user.getAgentCode());
            Log.d(TAG, "regcode:" + user.getRegCode());
            Log.d(TAG, "fromAgentCode:" + user.getFromAgentCode());
            Log.d(TAG, "authorization:" + user.getAuthorization());
            Log.d(TAG, "qqgroup:" + user.getQQgroup());
            Log.d(TAG, "qq:" + user.getQQ());
            Log.d(TAG, "name:" + user.getName());
            onLoginSuccess(user.getAuthorization().toString());
        } else {
            Log.d(TAG, "登录验证未通过");
            onLoginFailed(view, loginList.getCode());
        }

    }

}