package com.yado.pryado.pryadonew.bean;

/**
 * Created by eado on 2017/2/6.
 */

public abstract class Entity {

    protected int id;

    protected String cacheKey;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }
}
