package com.zwu.customs_broker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.header.DeliveryHeader;
import com.scwang.smartrefresh.header.FlyRefreshHeader;
import com.scwang.smartrefresh.header.WaveSwipeHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.impl.RefreshFooterWrapper;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zwu.customs_broker.R;
import com.zwu.customs_broker.activity.LoginActivity;
import com.zwu.customs_broker.adapter.AdminViewPagerAdapter;
import com.zwu.customs_broker.bean.CusDec;
import com.zwu.customs_broker.bean.Page;
import com.zwu.customs_broker.bean.Query;
import com.zwu.customs_broker.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdminViewFragment extends Fragment {
    private static final String TAG = "AdminViewFragment";
    private int i=0;

    public static Fragment newInstance() {
        return new AdminViewFragment();
    }

    final List<CusDec> items = new ArrayList<>();
    static final int ITEMS = 3;

    RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
                init(view);
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
        init(view);
    }

    private void init(View view) {
        String url = "http://129.211.77.80:8080/api/index";
        RequestBody requestBody = new FormBody.Builder()
                .add("page", String.valueOf(i+1))
                .add("rows", "1")
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
            page.getList().get(0).setID(i+1);
            items.add(page.getList().get(0));
            i++;
            Log.d(TAG, "i的下标值发生了变化:"+i);
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    mRecyclerView.setAdapter(new AdminViewPagerAdapter(items));
                }
            });


            Log.d(TAG, "Total:" + page.getTotal());
        } else {
            Log.d(TAG, "Page解析失败");
        }

    }
}