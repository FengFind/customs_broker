package com.zwu.customs_broker.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zwu.customs_broker.R;
import com.zwu.customs_broker.bean.FiltrateBean;
import com.zwu.customs_broker.bean.Select;
import com.zwu.customs_broker.fragment.AdminViewFragment;
import com.zwu.customs_broker.util.HttpUtil;
import com.zwu.customs_broker.view.FlowPopWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MaterialViewPager mViewPager;
    private FloatingActionButton fb_select;
    private FloatingActionButton fb_query;
    private FloatingActionButton fb_password;
    private FlowPopWindow flowPopWindow;
    private List<FiltrateBean> dictList = new ArrayList<>();
    EditText edit_billNo;
    EditText edit_entryId;
    EditText edit_consignorCname;
    EditText edit_oldpassword;
    EditText edit_newpassword;
    private String authorization;
    private String username;
    static final int TAPS = 1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        SharedPreferences sp = getSharedPreferences("context", MODE_PRIVATE);
        authorization = sp.getString("authorization", "");
        Log.d(TAG, "当前用户类型:" + authorization);
        username = intent.getStringExtra("username");
        mViewPager = findViewById(R.id.materialViewPager);
        fb_password = findViewById(R.id.reset_password);

        initParam();
        fb_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reset(v);
            }
        });
        fb_select = findViewById(R.id.floatbutton_select);
        fb_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Select(v);
                //Toast.makeText(getApplicationContext(), "测试", Toast.LENGTH_SHORT).show();
            }
        });
        fb_query = findViewById(R.id.floatbutton_query);
        fb_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query(v);
            }
        });
        //添加监听
        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (authorization) {
                    case "0":
                        //Toast.makeText(getApplicationContext(), "这是管理员", Toast.LENGTH_SHORT).show();
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue, "");
                    case "1":
                        //Toast.makeText(getApplicationContext(), "这是报关行", Toast.LENGTH_SHORT).show();
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green, "");
                    case "2":
                        //Toast.makeText(getApplicationContext(), "这是客户", Toast.LENGTH_SHORT).show();
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan, "");
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });
        //设置Toolbar
        Toolbar toolbar = mViewPager.getToolbar();

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }

        //设置Adapter
        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (authorization) {
                    case "0":
                        return AdminViewFragment.newInstance();
                    case "1":
                        return AdminViewFragment.newInstance();

                    default:
                        return AdminViewFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return TAPS;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (authorization) {
                    case "0":
                        return "欢迎您，尊敬的管理员：" + username;

                    case "1":
                        return "欢迎您，尊敬的报关行：" + username;

                    default:
                        return "欢迎您，尊敬的客户：" + username;
                }
            }
        });
        //设置setViewPager
        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    private void Query(final View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("查询选项设置");
        builder.setCancelable(true);
        final View view = View.inflate(v.getContext(), R.layout.query, null);
        builder.setView(view);
        SharedPreferences sp = getSharedPreferences("context", MODE_PRIVATE);
        edit_consignorCname = view.findViewById(R.id.edit_consignorCname);
        edit_billNo = view.findViewById(R.id.edit_billNo);
        edit_entryId = view.findViewById(R.id.edit_entryId);
        edit_consignorCname.setText(sp.getString("consignorCname", ""));
        edit_billNo.setText(sp.getString("billNo", ""));
        edit_entryId.setText(sp.getString("entryId", ""));


        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String consignorCname = edit_consignorCname.getText().toString();
                String billNo = edit_billNo.getText().toString();
                String entryId = edit_entryId.getText().toString();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("consignorCname", consignorCname);
                editor.putString("billNo", billNo);
                editor.putString("entryId", entryId);
                editor.putBoolean("switch",true);
                editor.commit();
                //recreate();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    private void Select(View v) {

        flowPopWindow = new FlowPopWindow(this, dictList);
        flowPopWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        //flowPopWindow.showAsDropDown(v,0,-910);
        flowPopWindow.setOnConfirmClickListener(new FlowPopWindow.OnConfirmClickListener() {
            @Override
            public void onConfirmClick() {
                SharedPreferences sp = getSharedPreferences("context", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                for (FiltrateBean fb : dictList) {
                    List<FiltrateBean.Children> cdList = fb.getChildren();
                    boolean is = false;
                    for (int x = 0; x < cdList.size(); x++) {
                        FiltrateBean.Children children = cdList.get(x);
                        if (children.isSelected()) {
                            System.out.println("遍历" + x);

                            editor.putString(fb.getTypeName(), children.getValue());

                            is = true;
                            //sb.append(fb.getTypeName() + ":" + children.getValue() + "；");
                        }
                    }
                    if (is == false) {
                        editor.putString(fb.getTypeName(), "");
                    }
                }
                editor.putBoolean("switch", true);
                editor.commit();
                //recreate();
            }
        });
    }


    private void initParam() {

        String url = "http://129.211.77.80:8080/api/select";
        RequestBody requestBody = new FormBody.Builder()
                .build();
        HttpUtil.sendOkHttpRequest(url, requestBody, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "查询异常:" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "查询结果:" + responseData);
                Gson gson = new Gson();
                Select select = gson.fromJson(responseData, new TypeToken<Select>() {
                }.getType());
                list(select.getContactsCompanyType(), select.getUserCompanyType(), select.getContactsNameType());
            }
        });


    }

    private void list(String[] contactsCompanyType, String[] userCompanyType, String[] contactsNameType) {
        String[] updateTime = {"最近一天", "最近一周", "最近一月", "最近半年", "最近一年"};
        String[] cusDecStatus = {"查验通知", "保存", "已申报", "海关入库成功", "退单", "审结", "放行", "结关"};

        FiltrateBean fb1 = new FiltrateBean();
        fb1.setTypeName("报关状态");
        List<FiltrateBean.Children> childrenList = new ArrayList<>();
        for (int x = 0; x < cusDecStatus.length; x++) {
            FiltrateBean.Children cd = new FiltrateBean.Children();
            cd.setValue(cusDecStatus[x]);
            childrenList.add(cd);
        }
        fb1.setChildren(childrenList);

        if ((!authorization.equals("1") && !authorization.equals("2"))) {
            FiltrateBean fb2 = new FiltrateBean();
            fb2.setTypeName("本公司操作员");
            List<FiltrateBean.Children> childrenList2 = new ArrayList<>();
            for (int x = 0; x < userCompanyType.length; x++) {
                FiltrateBean.Children cd = new FiltrateBean.Children();
                cd.setValue(userCompanyType[x]);
                childrenList2.add(cd);
            }
            fb2.setChildren(childrenList2);
            dictList.add(fb2);
        }
        FiltrateBean fb3 = new FiltrateBean();
        fb3.setTypeName("最后更新时间");
        List<FiltrateBean.Children> childrenList3 = new ArrayList<>();
        for (int x = 0; x < updateTime.length; x++) {
            FiltrateBean.Children cd = new FiltrateBean.Children();
            cd.setValue(updateTime[x]);
            childrenList3.add(cd);
        }
        fb3.setChildren(childrenList3);

        if (!authorization.equals("2")) {
            FiltrateBean fb4 = new FiltrateBean();

            fb4.setTypeName("客户公司名称");


            List<FiltrateBean.Children> childrenList4 = new ArrayList<>();
            for (int x = 0; x < contactsCompanyType.length; x++) {
                FiltrateBean.Children cd = new FiltrateBean.Children();
                cd.setValue(contactsCompanyType[x]);
                childrenList4.add(cd);
            }
            fb4.setChildren(childrenList4);
            dictList.add(fb4);
        }


        FiltrateBean fb5 = new FiltrateBean();
        //当当前登录的用户为客户身份的时候，其对应操作员为本公司操作员
        if (authorization.equals("2")) {
            fb5.setTypeName("本公司操作员");
        } else {
            fb5.setTypeName("对应操作员");
        }

        List<FiltrateBean.Children> childrenList5 = new ArrayList<>();
        for (int x = 0; x < contactsNameType.length; x++) {
            FiltrateBean.Children cd = new FiltrateBean.Children();
            cd.setValue(contactsNameType[x]);
            childrenList5.add(cd);
        }
        fb5.setChildren(childrenList5);
        dictList.add(fb5);


        dictList.add(fb1);

        dictList.add(fb3);


    }

    private void Reset(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("修改密码");
        builder.setCancelable(true);
        final View view = View.inflate(v.getContext(), R.layout.reset, null);
        builder.setView(view);
        edit_oldpassword = view.findViewById(R.id.old_password);
        edit_newpassword = view.findViewById(R.id.new_password);

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String oldPwd = edit_oldpassword.getText().toString();
                String newPwd = edit_newpassword.getText().toString();
                Log.d(TAG, "旧密码:" + oldPwd + "，新密码:" + newPwd);
                String url = "http://129.211.77.80:8080/modifyPwd";
                RequestBody requestBody = new FormBody.Builder()
                        .add("oldPwd", oldPwd)
                        .add("newPwd", newPwd)
                        .build();
                HttpUtil.sendOkHttpRequest(url, requestBody, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "修改密码异常:" + e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d(TAG, "修改密码返回结果:" + responseData);
                        switch (responseData) {
                            case "OK":
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        intent.putExtra("setpassword", "success");
                                        startActivity(intent);
                                        finish();

                                    }
                                });
                                break;
                            case "FAIL":
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Snackbar.make(v, "修改密码失败", Snackbar.LENGTH_LONG).show();
                                    }
                                });
                                break;
                            case "Error":
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Snackbar.make(v, "原密码输入错误", Snackbar.LENGTH_LONG).show();
                                    }
                                });
                                break;
                            default:
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Snackbar.make(v, "修改密码异常", Snackbar.LENGTH_LONG).show();
                                    }
                                });

                        }

                    }
                });
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();


    }

}