package com.mengxun.base.enums;

public enum Platform {

    EDGE("Microsoft Edge", "MicrosoftWebDriver", "webdriver.chrome.driver", false),
    CHROME("Google Chrome", "chromedriver", "webdriver.chrome.driver", false),
    FIREFOX("Mozilla Firefox", "geckodriver", "webdriver.chrome.driver", false),
    SAFARI("Apple Safari", "", "", false),
    IOS("IOS", "", "", true),
    ANDROID("ANDROID", "", "", true)
    ;

    private String name;
    private String driverName;
    private String propertyName;
    private Boolean isMobileApp;

    Platform(String name, String driverName, String propertyName, Boolean isMobileApp){
        this.name = name;
        this.driverName = driverName;
        this.propertyName = propertyName;
        this.isMobileApp = isMobileApp;
    }

    public String getName(){ return this.name; }
    public String getDriverName(){ return this.driverName; }
    public String getPropertyName(){ return this.propertyName; }
    public Boolean getIsMobileApp(){ return this.isMobileApp; }
}
