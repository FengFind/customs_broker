


package com.zwu.customs_broker.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.header.DeliveryHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zwu.customs_broker.R;
import com.zwu.customs_broker.adapter.AdminViewPagerAdapter;
import com.zwu.customs_broker.bean.CusDec;
import com.zwu.customs_broker.bean.Page;
import com.zwu.customs_broker.bean.Query;
import com.zwu.customs_broker.util.HttpUtil;
import com.zwu.customs_broker.util.TimeSwitch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class AdminViewFragment extends Fragment {
    private static final String TAG = "AdminViewFragment";
    private int i = 0;
    private SharedPreferences sp;
    private long lastonclickTime = 0;
    private String authorization;
    ExecutorService singleThreadExecutor;

    public static Fragment newInstance() {
        return new AdminViewFragment();
    }

    final List<CusDec> items = new ArrayList<>();
    static final int ITEMS = 3;

    RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sp = getActivity().getSharedPreferences("context", MODE_PRIVATE);
        authorization = sp.getString("authorization", "");
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RefreshLayout refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new DeliveryHeader(getContext()));
        //refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        refreshLayout.setEnableAutoLoadMore(false);
        //refreshLayout.setEnableFooterTranslationContent(false);//是否上拉Footer的时候向上平移列表或者内容
        refreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                init(view, false);
                refreshlayout.finishRefresh(1000);

            }
        });
/*        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                init(view);
                mRecyclerView.setAdapter(new AdminViewPagerAdapter(items));
                refreshlayout.finishLoadMore(2000);//传入false表示加载失败
            }
        });*/

        //线程池
        singleThreadExecutor = Executors.newSingleThreadExecutor();
        mRecyclerView = view.findViewById(R.id.recyclerView);
/*        for (int i = 0; i < ITEMS; i++) {
            items.add(new CusDec());
        }*/


        //CusDec cusDec = new CusDec(1, "2", new Date().toString(), "4", "5", "6", "7", "8", "9", "10");
        //items.add(cusDec);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        mRecyclerView.setAdapter(new AdminViewPagerAdapter(items));
        init(view, false);
    }


    private void init(View view, boolean flag) {
        if (flag) {
            i = 0;
            items.clear();
            Log.d(TAG, "进行了一次重置");
        }
        String cusDecStatus;
        switch (sp.getString("报关状态", "")) {
            case "保存":
                cusDecStatus = "1";
                break;
            case "已申报":
                cusDecStatus = "2";
                break;
            case "入库成功":
                cusDecStatus = "4";
                break;
            case "退单":
                cusDecStatus = "6";
                break;
            case "审结":
                cusDecStatus = "7";
                break;
            case "放行":
                cusDecStatus = "9";
                break;
            case "结关":
                cusDecStatus = "10";
                break;
            case "查验通知":
                cusDecStatus = "11";
                break;
            default:
                cusDecStatus = "";
        }

        String toTime = TimeSwitch.timeTips("今天");
        String fromTime = TimeSwitch.timeTips(sp.getString("最后更新时间", ""));

        String contactsName;
        String company;

        if (authorization.equals("2")) {
            contactsName = sp.getString("本公司操作员", "");
            company = "";
            Log.d(TAG, "当前登录的为用户类型，需要特殊处理，处理后的结果:" + contactsName);
        } else {
            contactsName = sp.getString("对应操作员", "");
            company = sp.getString("本公司操作员", "");
        }


        String url = "http://129.211.77.80:8080/api/index";
        RequestBody requestBody = new FormBody.Builder()
                .add("page", String.valueOf(i + 1))
                .add("rows", "1")
                .add("company", company)
                .add("contactsCompany", sp.getString("客户公司名称", ""))
                .add("contactsName", contactsName)
                .add("consignorCname", sp.getString("consignorCname", ""))
                .add("billNo", sp.getString("billNo", ""))
                .add("entryId", sp.getString("entryId", ""))
                .add("cusDecStatus", cusDecStatus)
                .add("toTime", toTime)
                .add("fromTime", fromTime)
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
                parseJSONWithGSON(responseData, view);
            }
        });
    }

    private void parseJSONWithGSON(String jsonData, View view) {
/*       Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();*/

/*
        GsonBuilder builder = new GsonBuilder();
        // Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });
        Gson gson = builder.create();
*/

        Gson gson = new Gson();
        Query query = gson.fromJson(jsonData, new TypeToken<Query>() {
        }.getType());
        Log.d(TAG, "map:" + query.getMap());
        Log.d(TAG, "page:" + query.getPage());

        if (query.getPage() != null) {
            Page page = gson.fromJson(gson.toJson(query.getPage()), new TypeToken<Page>() {
            }.getType());
            Log.d(TAG, "Page解析成功");
            Log.d(TAG, "List:" + page.getList());
            if (page.getList().size() != 0) {
                page.getList().get(0).setID(i + 1);
                items.add(page.getList().get(0));
                i++;
                Log.d(TAG, "i的下标值发生了变化:" + i);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        mRecyclerView.setAdapter(new AdminViewPagerAdapter(items));
                        Snackbar.make(view, "成功加载一条新数据，总数据:" + page.getTotal() +
                                ",剩余数据:" + ((int) page.getTotal() - i), Snackbar.LENGTH_LONG).show();
                    }
                });
                Log.d(TAG, "Total:" + page.getTotal());
            } else {
                Log.d(TAG, "无数据");
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        mRecyclerView.setAdapter(new AdminViewPagerAdapter(items));//Debug
                        Snackbar.make(view, "来到了没有数据的荒原", Snackbar.LENGTH_LONG).show();
                    }
                });
            }

        } else {
            Log.d(TAG, "Page解析失败");
        }

    }

    private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            //Toast.makeText(getActivity(),"onSharedPreferenceChanged",Toast.LENGTH_LONG).show();
            if(key.equals("switch")){
                if (sharedPreferences.getBoolean("switch", false)){
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("switch", false);
                    editor.commit();
                    //Toast.makeText(getActivity(),"if语句",Toast.LENGTH_LONG).show();
                    singleThreadExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                init(getView(), true);
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }

/*            long time= SystemClock.uptimeMillis();//局部变量
            if (time-lastonclickTime<=1000) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Snackbar.make(getView(), "正在加载数据，温馨提示:考虑到性能问题，1秒内仅一次有效请求", Snackbar.LENGTH_LONG).show();
                    }
                });

            }else {
                lastonclickTime=time;
                init(getView(), true);
            }*/
        }
    };

    @Override
    public void onResume() {
        Log.d(TAG, "——————onResume()被调用——————");
        super.onResume();
        sp.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "——————onPause()被调用——————");
        super.onPause();
        sp.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "——————onStop()被调用——————");
        //清楚数据
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("最后更新时间");
        editor.remove("客户公司名称");
        editor.remove("对应操作员");
        editor.remove("本公司操作员");
        editor.remove("报关状态");
        editor.commit();
    }
}