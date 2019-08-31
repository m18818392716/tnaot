package com.mengxun.base.enums;

public enum ScrollDirection {

    UP("VERTICAL"), DOWN("VERTICAL"), LEFT("HORIZONTAL"), RIGHT("HORIZONTAL");

    private String orientation;

    ScrollDirection(String orientation){
        this.orientation = orientation;
    }

    public String getOrientation(){ return orientation; }
}
