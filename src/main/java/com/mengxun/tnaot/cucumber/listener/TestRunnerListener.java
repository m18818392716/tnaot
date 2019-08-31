package com.mengxun.tnaot.cucumber.listener;

import com.mengxun.base.AppiumUtil;
import com.mengxun.base.config.SelectDriver;
import com.mengxun.tnaot.utils.ScreenScr;
import org.apache.log4j.Logger;
import org.testng.*;

import java.util.Iterator;

public class TestRunnerListener extends TestListenerAdapter {

    private Logger logger = Logger.getLogger(TestRunnerListener.class);

    private int m_count = 0;
    private String filePath=System.getProperty("user.dir");

//    protected ExtentReports extent;
//    protected ExtentTest test;

    @Override
    public void onTestSuccess(ITestResult tr) {
        super.onTestSuccess(tr);
//        setExcelResult(tr, ExcelUtil.RESULT_PASS);
        logger.info("【" + tr.getName() + " Success】");

//        TestNGRetry retryAnalyzer = (TestNGRetry) tr.getMethod().getRetryAnalyzer();
//        retryAnalyzer.reSetCount();
//        finish(tr);由于版本问题，报错
    }

    private void setExcelResult(ITestResult tr, String resultPass) {
        // 如果testName包含"(", 那么就截取"("前的字符, 作为caseId
        if(tr.getName().contains("(")){
            String caseId = tr.getName().substring(0, tr.getName().indexOf("("));
//            ExcelUtil.getResults().get(caseId).setResult(resultPass);
        }
    }


    @Override
    public void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr);
//        setExcelResult(tr, ExcelUtil.RESULT_FAIL);
        //String picName =tr.getStartMillis()+""+tr.getTestName();
        String picName = tr.getTestName();
        ScreenScr.getScreen(SelectDriver.getAppiumDriver(),picName);
        logger.info("【" + tr.getName() + " Failure】");
        Reporter.log("["+tr.getName()+"失败]");
//        takeScreenShot(tr);


//        TestNGRetry retryAnalyzer = (TestNGRetry) tr.getMethod().getRetryAnalyzer();
//        retryAnalyzer.reSetCount();
//        finish(tr);由于版本问题，报错
    }

    @Override
    public void onTestStart(ITestResult tr) {
        super.onTestStart(tr);
        logger.info("【" + tr.getName() + " Start】");
        //    extent=InitDriverCase.getextent();
        //    test= extent.startTest(tr.getName());
    }


    public void takeScreenShot(ITestResult tr){
        AppiumUtil baseTestcase=(AppiumUtil)tr.getInstance();
        baseTestcase.takescreen(tr.getName());

    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        super.onTestSkipped(tr);
//        setExcelResult(tr, ExcelUtil.RESULT_SKIP);
        //String picName =tr.getStartMillis()+""+tr.getTestName();
        String picName = tr.getTestName();
        ScreenScr.getScreen(SelectDriver.getAppiumDriver(),picName);
        logger.info("【" + tr.getName() + " Skipped】");
        Reporter.log("["+tr.getName()+"跳过]");
        //takeScreenShot(tr);
        //logger.info("【" + tr.getName() + " Skipped】");




        //    test.log(LogStatus.SKIP, "SKIP");
        //    extent.endTest(test);
    }

    @Override
    public void onFinish(ITestContext testContext) {
        super.onFinish(testContext);
        Iterator<ITestResult> listOfFailedTests=testContext.getFailedTests().getAllResults().iterator();
        while(listOfFailedTests.hasNext()){
            ITestResult failedTest = listOfFailedTests.next();
            ITestNGMethod method = failedTest.getMethod();
            if(testContext.getFailedTests().getResults(method).size()>1){
                listOfFailedTests.remove();
            }else{
                if(testContext.getPassedTests().getResults(method).size()>0){
                    listOfFailedTests.remove();
                }
            }
        }
    }
}
