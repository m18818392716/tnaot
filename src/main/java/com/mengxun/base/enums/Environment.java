package com.mengxun.base.enums;

public enum Environment {

    TEST("test"), PRODUCE("produce");

    private String env;

    Environment(String orientation){
        this.env = orientation;
    }

    public String getEnv(){ return env; }
}
