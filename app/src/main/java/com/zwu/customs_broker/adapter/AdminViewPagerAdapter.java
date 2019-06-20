package com.zwu.customs_broker.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.github.czy1121.view.CornerLabelView;
import com.zwu.customs_broker.R;
import com.zwu.customs_broker.bean.CusDec;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class AdminViewPagerAdapter extends RecyclerView.Adapter<AdminViewPagerAdapter.ViewHolder> {
    List<CusDec> contents;
    private String authorization;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView trafName;
        TextView billNo;
        TextView entryId;
        TextView consignorCname;
        TextView contacts;
        TextView updateTime;
        TextView company;
        CornerLabelView cusDecStatus;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trafName = itemView.findViewById(R.id.trafName);//船名航次
            billNo = itemView.findViewById(R.id.billNo);//提单号
            entryId = itemView.findViewById(R.id.entryId);//报关单号
            consignorCname = itemView.findViewById(R.id.consignorCname);//收发货人
            contacts = itemView.findViewById(R.id.contacts);//客户+操作员
            cusDecStatus = itemView.findViewById(R.id.cusDecStatus);//状态
            updateTime = itemView.findViewById(R.id.updateTime);//更新时间
            company = itemView.findViewById(R.id.company);//操作员
        }
    }

    public AdminViewPagerAdapter(List<CusDec> contents) {
        this.contents = contents;
    }

    public int getItemCount() {
        return contents.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_cardview, parent, false);
        ViewHolder holder = new ViewHolder(view);
        SharedPreferences sp = parent.getContext().getSharedPreferences("context", MODE_PRIVATE);
        authorization = sp.getString("authorization", "");
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CusDec cusDec = contents.get(position);
        holder.trafName.setText(cusDec.getTrafName());
        holder.billNo.setText(cusDec.getBillNo());
        holder.entryId.setText(cusDec.getEntryId());
        holder.consignorCname.setText(cusDec.getConsignorCname());
        String CusDecStatus = "暂无数据";
        switch (cusDec.getCusDecStatus()) {
            case "1":
                CusDecStatus = "保存";
                break;
            case "2":
                CusDecStatus = "已申报";
                break;
            case "4":
                CusDecStatus = "入库成功";
                break;
            case "6":
                CusDecStatus = "退单";
                break;
            case "7":
                CusDecStatus = "审结";
                break;
            case "9":
                CusDecStatus = "放行";
                break;
            case "10":
                CusDecStatus = "结关";
                break;
            case "11":
                CusDecStatus = "查验通知";
                break;
            default:
                CusDecStatus = "获取异常";
        }

        if (CusDecStatus == "退单") {
            holder.cusDecStatus.setFillColorResource(R.color.red_f4);
        } else if (CusDecStatus == "查验通知") {
            holder.cusDecStatus.setFillColorResource(R.color.orange_f7);
        }
        holder.cusDecStatus.setText1(CusDecStatus);
        holder.cusDecStatus.setText2(cusDec.getID().toString());
        if (cusDec.getContactsCompany() == null && cusDec.getContactsName() == null) {
            holder.contacts.setText("无客户信息");
        } else {
            //客户+操作员
            switch (authorization) {
                case "1":
                    //报关行（好吧...其实没关系...留着日后用吧
                    holder.contacts.setText(cusDec.getContactsCompany() + "——" + cusDec.getContactsName());
                    break;
                case "2":
                    //客户
                    holder.contacts.setText(cusDec.getContactsName());
                    break;
                default:
                    holder.contacts.setText(cusDec.getContactsCompany() + "——" + cusDec.getContactsName());
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(Double.parseDouble(cusDec.getUpdateTime()));
        holder.updateTime.setText(time);
        //操作员
        switch (authorization) {
            case "1":
                holder.company.setText("无本公司操作员");
                break;
            case "2":
                //客户
                holder.company.setText(cusDec.getContactsName());
                break;
            default:
                holder.company.setText(cusDec.getCompany());
        }

    }

}
