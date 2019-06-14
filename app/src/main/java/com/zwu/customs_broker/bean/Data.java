package com.zwu.customs_broker.bean;

import java.io.Serializable;
import java.util.HashMap;

public class Data implements Serializable {
    /**数据表**/
    private String table;
    /**数据**/
    private HashMap<String,String> params;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "Data{" +
                "table='" + table + '\'' +
                ", params=" + params +
                '}';
    }
}