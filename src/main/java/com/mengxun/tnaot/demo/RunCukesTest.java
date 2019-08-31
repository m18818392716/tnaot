package com.mengxun.tnaot.demo;


import com.aventstack.extentreports.ResourceCDN;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.cucumber.listener.Reporter;
import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import lombok.Data;
//import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
//加入注释语句位置，不能运行所有用例集合
//@RunWith(Cucumber.class)
//@ContextConfiguration("classpath:cucumber.xml")
@Test
@CucumberOptions(
        //plugin = {"com.cucumber.listener.ExtentCucumberFormatter:target/extent-report/report.html"},
//        plugin = {"com.cucumber.listener.ExtentCucumberFormatter:target/cucumber/index.html"},
        format = {"pretty", "html:target/cucumber", "json:target/cucumber.json"},
        features = {"src/test/resources/features/"},
        //glue = {"com.mengxun.tnaot.demo","com.po.demo"},
        glue = {"com.mengxun.tnaot.steps"},
        tags = {
                "~@performance","~@skip"
        },
        monochrome = true)

@Data
public class RunCukesTest extends AbstractTestNGCucumberTests {

    @BeforeClass
    public static void setup() {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("target/extent-report/report.html");
        htmlReporter.config().setResourceCDN(ResourceCDN.EXTENTREPORTS);

    }

    @AfterClass
    public static void tearDown() {
        Reporter.loadXMLConfig(new File("src/test/resources/extent-config.xml"));//1
        Reporter.setSystemInfo("user", System.getProperty("user.name"));
        Reporter.setSystemInfo("os", "Windows");
        Reporter.setTestRunnerOutput("Sample test runner output message");
    }

}
