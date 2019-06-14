package com.zwu.customs_broker.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.zwu.customs_broker.R;
import com.zwu.customs_broker.bean.FiltrateBean;
import com.zwu.customs_broker.fragment.AdminViewFragment;
import com.zwu.customs_broker.view.FlowPopWindow;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MaterialViewPager mViewPager;
    private FloatingActionButton select;
    private FloatingActionButton query;
    private FlowPopWindow flowPopWindow;
    private List<FiltrateBean> dictList = new ArrayList<>();
    EditText edit_billNo;
    EditText edit_entryId;
    EditText edit_consignorCname;
    private String authorization;
    private String username;
    static final int TAPS = 1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        initParam();
        authorization = intent.getStringExtra("authorization");
        username = intent.getStringExtra("username");
        mViewPager = findViewById(R.id.materialViewPager);
        select = findViewById(R.id.floatbutton_select);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Select(v);
                Toast.makeText(getApplicationContext(),"测试",Toast.LENGTH_SHORT).show();
            }
        });
        query = findViewById(R.id.floatbutton_query);
        query.setOnClickListener(new View.OnClickListener() {
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
                        Toast.makeText(getApplicationContext(),"这是0",Toast.LENGTH_SHORT).show();
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue, "");
                    case "1":
                        Toast.makeText(getApplicationContext(),"这是1",Toast.LENGTH_SHORT).show();
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green, "");
                    case "2":
                        Toast.makeText(getApplicationContext(),"这是2",Toast.LENGTH_SHORT).show();
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
                        return "欢迎您，尊敬的管理员："+username;

                    case "1":
                        return "欢迎您，尊敬的报关行："+username;

                    default:
                        return "欢迎您，尊敬的客户："+username;
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
            finish();
        }
        return true;
    }

    private void Query(final View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("查询选项设置");
        builder.setCancelable(true);
        final View view = View.inflate(v.getContext(),R.layout.query, null);
        builder.setView(view);
        SharedPreferences sp = getSharedPreferences("context", MODE_PRIVATE);
        edit_consignorCname =  view.findViewById(R.id.edit_consignorCname);
        edit_billNo =  view.findViewById(R.id.edit_billNo);
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
                editor.commit();
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
        flowPopWindow.showAtLocation(v, Gravity.CENTER,0,0);
        //flowPopWindow.showAsDropDown(v,0,-910);
        flowPopWindow.setOnConfirmClickListener(new FlowPopWindow.OnConfirmClickListener() {
            @Override
            public void onConfirmClick() {
                StringBuilder sb = new StringBuilder();
                for (FiltrateBean fb : dictList) {
                    List<FiltrateBean.Children> cdList = fb.getChildren();
                    for (int x = 0; x < cdList.size(); x++) {
                        FiltrateBean.Children children = cdList.get(x);
                        if (children.isSelected())
                            sb.append(fb.getTypeName() + ":" + children.getValue() + "；");
                    }
                }
                if (!TextUtils.isEmpty(sb.toString()))
                    Toast.makeText(MainActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //这些是假数据，真实项目中直接接口获取添加进来，FiltrateBean对象可根据自己需求更改
    private void initParam() {
        String[] sexs = {"查验通知", "保存", "已申报", "海关入库成功", "退单", "审结", "放行", "结关"};
        String[] colors = {"德俊", "百伦", "创发", "乾恩"};
        String[] company = {"最近一天", "最近一周", "最近一月", "最近半年", "最近一年"};

        FiltrateBean fb1 = new FiltrateBean();
        fb1.setTypeName("报关状态");
        List<FiltrateBean.Children> childrenList = new ArrayList<>();
        for (int x = 0; x < sexs.length; x++) {
            FiltrateBean.Children cd = new FiltrateBean.Children();
            cd.setValue(sexs[x]);
            childrenList.add(cd);
        }
        fb1.setChildren(childrenList);

        FiltrateBean fb2 = new FiltrateBean();
        fb2.setTypeName("本公司操作员");
        List<FiltrateBean.Children> childrenList2 = new ArrayList<>();
        for (int x = 0; x < colors.length; x++) {
            FiltrateBean.Children cd = new FiltrateBean.Children();
            cd.setValue(colors[x]);
            childrenList2.add(cd);
        }
        fb2.setChildren(childrenList2);

        FiltrateBean fb3 = new FiltrateBean();
        fb3.setTypeName("最后更新时间");
        List<FiltrateBean.Children> childrenList3 = new ArrayList<>();
        for (int x = 0; x < company.length; x++) {
            FiltrateBean.Children cd = new FiltrateBean.Children();
            cd.setValue(company[x]);
            childrenList3.add(cd);
        }
        fb3.setChildren(childrenList3);

        dictList.add(fb1);
        dictList.add(fb2);
        dictList.add(fb3);
    }
}
