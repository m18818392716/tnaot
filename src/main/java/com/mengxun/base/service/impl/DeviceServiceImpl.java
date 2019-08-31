package com.mengxun.base.service.impl;

import com.mengxun.base.config.SelectDriver;
import com.mengxun.base.service.DeviceService;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DeviceServiceImpl implements DeviceService {

    private static final String ERR_CODE = "ERR_CODE";

    protected Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private SelectDriver selectDriver;


    public void resetApp() {
//        if(selectDriver.getPlatformName().getIsMobileApp()){
//            AppiumDriver appiumDriver = selectDriver.getAppiumDriver();
//            logger.info("Reset App");
//            appiumDriver.resetApp();
//        } else {
//            //TODO
//        }
        AppiumDriver appiumDriver = selectDriver.getAppiumDriver();
        logger.info("Reset App");
        appiumDriver.resetApp();

    }

    public File takeScreenShot(String filename) {
        if(StringUtils.isEmpty(filename)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            filename = sdf.format(new Date()) + RandomStringUtils.randomNumeric(16) + ".png";
        }

        File tmpFile = selectDriver.getAppiumDriver().getScreenshotAs(OutputType.FILE);
        File file = new File("test-output/screen-capture-record/" + filename);

        try {
            FileUtils.copyFile(tmpFile, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
