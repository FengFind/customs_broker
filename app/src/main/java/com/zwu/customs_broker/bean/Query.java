package com.zwu.customs_broker.bean;

import java.util.Map;

public class Query {
    private Page page;
    private Map<String,String> map;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
