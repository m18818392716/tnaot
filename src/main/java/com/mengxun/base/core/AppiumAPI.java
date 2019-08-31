package com.mengxun.base.core;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public interface AppiumAPI {

    public void click(MobileElement e);

    public void type(MobileElement e, String message);

    public String getText(MobileElement e);

    public void scrollToElement(MobileElement e);

    public boolean isElementPresent(MobileElement e);

    public void assertToast(AndroidDriver<AndroidElement> driver, String message);
}
