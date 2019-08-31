package com.mengxun.base;

import com.mengxun.base.annotation.FindElementBy;
import com.mengxun.base.config.SelectDriver;
import com.mengxun.base.core.AndroidDriverWait;
import com.mengxun.base.core.Locator;
import com.mengxun.tnaot.utils.ScreenScr;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.NoSuchContextException;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.functions.ExpectedCondition;
import io.appium.java_client.ios.IOSDriver;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.ITestResult;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class AppiumUtil {

    public AppiumDriver<WebElement> driver;
    public ITestResult it;
    public static final String PAGE_PACKAGE_PATH = "com.tnaot.page";

    @Getter
    private WebElement webElement;
    /**
     * 定义日志输出对象
     */
    public static Logger logger = Logger.getLogger(AppiumUtil.class);

    /**
     * 获取driver
     *
     * @throws
     */
    public AppiumDriver<WebElement> getDriver(String url, DesiredCapabilities capabilities, String platform) {

        if (platform.equalsIgnoreCase("android")) {
            try {
                driver = new AndroidDriver<WebElement>(new URL(url), capabilities);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else if (platform.equalsIgnoreCase("ios")) {
            try {
                driver = new IOSDriver<WebElement>(new URL(url), capabilities);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {

        }
        return driver;

    }

    /**Appium获取安卓页面的toast  message为toast提示信息*/
    /**
     * ps：不要去定位toast，尽量不要用element去接收这个元素。我们取这个提示一般来说都是来做判断比对的，所以这里直接用assert
     */
    public static void assertToast(AndroidDriver<AndroidElement> driver, String message) {
//        final WebDriverWait wait = new AndroidDriverWait(driver, 10);
//        AndroidDriverWait wait = new AndroidDriverWait(driver, 60);
        WebElement showClose = new AndroidDriverWait(driver, 60).until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(@text,'" + message + "')]")));
        Assert.assertNotNull(showClose);
    }

    // 根据elementPath获取MobileElement
    public static MobileElement getMobileElement(String elementPath) {
        MobileElement mobileElement = null;
        String[] pathArray = elementPath.split("\\.");
        String pageClassName = pathArray[0];
        String pageElementIndex = "";
        String pageElement = "";
        int index = -1; // 控件索引
        // 解析elementPath获取属性
        if (elementPath.contains("[")) {
            pageElementIndex = pathArray[1];
            pageElement = pageElementIndex.substring(0,pageElementIndex.indexOf("["));
            index = Integer.valueOf(pageElementIndex.substring(pageElementIndex.indexOf("[") + 1,pageElementIndex.indexOf("]")));
        } else {
            pageElement = pathArray[1];
        }
        try {
            Class targetPage = Class.forName(PAGE_PACKAGE_PATH + "." + pageClassName);
            Field elementField = targetPage.getDeclaredField(pageElement);
            FindElementBy findElementBy = elementField.getAnnotation(FindElementBy.class);
            // 如果有FindElementBy注解，则进行解析，否则直接调用get方法
            if(findElementBy != null){
//                System.out.println("解析FindElementBy注解, 属性：["+ elementPath +"]");
                if (index != -1) {
                    mobileElement = AppiumUtil.getMobileElement(new Locator(findElementBy.value(), Locator.ByType.valueOf(findElementBy.type())),index);
                } else {
                    mobileElement = AppiumUtil.getMobileElement(new Locator(findElementBy.value(), Locator.ByType.valueOf(findElementBy.type())));
                }

            } else {
                Method getMethod = getGetMethod(targetPage, pageElement);
                Constructor constructor = targetPage.getConstructor(AppiumDriver.class);
                Object pageObject = constructor.newInstance(SelectDriver.getAppiumDriver());
                mobileElement = (MobileElement) getMethod.invoke(pageObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mobileElement;
    }

    // 获取属性的get方法
    public static Method getGetMethod(Class targetClass, String fieldName) {
        String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            return targetClass.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Assert.fail("未找到改属性对应的get方法：[" + fieldName + "]");
        }
        return null;
    }

    /** 等待 */
    public static void sleep(Long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex);
        }
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex);
        }
    }

    /** 判断元素是否存在 */
    public static boolean isExist(WebElement webElement) {
        try {
            webElement.isDisplayed();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }

    }

    /** 判断元素是否可见 */
    public static boolean isVisible(WebElement webElement) {
        try {
            return webElement.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }

    }

    public static void clickElementIfExist(MobileElement mobileElement) {
        try {
            mobileElement.click();
        } catch (NoSuchElementException e){
            logger.info("该元素不存在："+mobileElement);
        }
    }

    /** 截屏操作 */
    public static void takescreen(String filename){
        ScreenScr.getScreen(SelectDriver.getAppiumDriver(), filename);
    }

    //    /**
//     * 获取toast
//     * @param driver
//     * @param key
//     * @param times
//     * @return
//     */
//    public static String getToast(AndroidDriver driver, String key,int times) {
//        String target2 = "";
//        try {
//            WebDriverWait wait = new WebDriverWait(driver, times);
//            WebElement target = wait.until(ExpectedConditions
//                    .presenceOfElementLocated(By.xpath("//*[contains(@text,'"
//                            + key + "')]")));
//            target2 = target.getAttribute("text");
//            logger.info("toast值输出：" + target2);
//        } catch (Exception e) {
//            logger.log(Level.INFO, "<Toast>toast not found!!!");
//        }
//        return target2;
//    }
//
//    /**
//     * 隐藏键盘
//     * @param driver
//     */
//    public static void closeKeyBoard(AndroidDriver driver) {
//        try {
//            driver.hideKeyboard();// 隐藏键盘
//        } catch (WebDriverException ex) {
//            // logger.log(Level.INFO,"<Keyboard>Soft keyboard not present, cannot hide keyboard!!!");
//            logger.log(Level.INFO, "<Keyboard>键盘没有呼出，不需要隐藏!!!");
//        }
//    }
//
//
    public static MobileElement getMobileElement(Locator locator) {
        return getMobileElement(SelectDriver.getAppiumDriver(), locator);
    }
    public static MobileElement getMobileElement(AppiumDriver<WebElement> driver, Locator locator) {
        //System.out.println(locatorMap+"+++++++++++++++++++++++++++++++++++++++++");
        //locator = getLocator(locatorName);
        WebElement webElement = null;
        if (locator.getBy() == Locator.ByType.accessibilityId) {
            webElement = driver.findElementByAccessibilityId(locator.getElement());
        } else {
            webElement = driver.findElement(getBy(locator));
        }
        return (MobileElement)webElement;
    }

    public static MobileElement getMobileElement(Locator locator,int index) {
        return getMobileElement(SelectDriver.getAppiumDriver(), locator,index);
    }
    public static MobileElement getMobileElement(AppiumDriver<WebElement> driver, Locator locator, int index) {
        WebElement webElement = null;
        if (locator.getBy() == Locator.ByType.accessibilityId) {
            webElement = driver.findElementsByAccessibilityId(locator.getElement()).get(index);
        } else {
            webElement = driver.findElements(getBy(locator)).get(index);
        }
        return (MobileElement)webElement;
    }

    public static By getBy(Locator locator) {
        By by = null;
        switch (locator.getBy()) {
            case xpath:
                logger.debug("find element By xpath");
                by = By.xpath(locator.getElement());
                break;
            case id:
                logger.debug("find element By id");
                by = By.id(locator.getElement());
                break;
            case name:
                logger.debug("find element By name");
                by = By.name(locator.getElement());
                break;
            case accessibilityId:
                logger.debug("find element By accessibilityId");
                break;
            case cssSelector:
                logger.debug("find element By cssSelector");
                by = By.cssSelector(locator.getElement());
                break;
            case className:
                logger.debug("find element By className");
                by = By.className(locator.getElement());
                break;
            case tagName:
                logger.debug("find element By tagName");
                by = By.tagName(locator.getElement());
                break;
            case linkText:
                logger.debug("find element By linkText");
                by = By.linkText(locator.getElement());;
                break;
            case partialLinkText:
                logger.debug("find element By partialLinkText");
                by = By.partialLinkText(locator.getElement());
                break;
            case androidUIAutomator:
                //e = ((AndroidDriver) driver).findElementByAndroidUIAutomator(locator.getElement());
                break;
            case iOSUIAutomation:
                break;
            case by:
                break;
            default:
                by = By.id(locator.getElement());
                System.out.println("找不到对应的定位方法！");
        }
        return by;
    }
//
//
//    public boolean isElementPresent(AndroidDriver<AndroidElement> driver, final Locator myLocator, int timeOut) throws IOException {
//
//        final Locator locator = getLocator(myLocator.getElement());
//        boolean isPresent = false;
//        AndroidDriverWait wait = new AndroidDriverWait(driver, 60);
//        isPresent = wait.until(new ExpectedCondition<WebElement>() {
//            public WebElement apply(AndroidDriver d) {
//                return findElement(d, locator);
//            }
//        }).isDisplayed();
//        return isPresent;
//    }
//
//
//    public boolean isElementPresent(Locator locator, int timeOut)
//            throws IOException {
//        return isElementPresent(appiumDriver, locator, timeOut);
//    }
//
//    public WebElement findElement(AndroidDriver<AndroidElement> driver, final Locator locator) {
//        //System.out.println("driver是否为空："+ driver);
//        WebElement element = (new AndroidDriverWait(driver, locator.getWaitSec()))
//                .until(new ExpectedCondition<WebElement>() {
//
//                    public WebElement apply(AndroidDriver driver) {
//                        try {
//                            return getElement(driver, locator);
//                        } catch (IOException e) {
//                            // TODO Auto-generated catch block
//                            log.error("can't find element "
//                                    + locator.getElement());
//                            return null;
//                        }
//
//                    }
//
//                });
//        return element;
//
//    }


    /**
     * 退出app
     */
    public void closeApp(String appName) {
        driver.closeApp();
        logger.info(appName + "已经关闭");
    }

    /**
     * 退出移动浏览器
     */
    public void quit() {
        driver.quit();
        logger.info("driver已被清理");
    }

    /**
     * 通过By对象 去查找某个元素
     */
    public WebElement findElement(By by) {
        return driver.findElement(by);
    }

    /**
     * 通过By对象 去查找一组元素
     */
    public List<WebElement> findElements(By by) {
        return driver.findElements(by);
    }

    /**
     * 清空元素内容
     */
    public void clear(By byElement) {
        WebElement element = findElement(byElement);
        element.clear();
        logger.info("清空元素：" + getLocatorByElement(element, ">") + "上的内容");
    }

    /**
     * 输入内容
     */
    public void typeContent(By byElement, String str) {
        WebElement element = findElement(byElement);
        element.sendKeys(str);
        logger.info("在元素：" + getLocatorByElement(element, ">") + "输入内容：" + str);
    }

    /**
     * 点击
     */
    public void click(By byElement) {
        WebElement element = findElement(byElement);
        try {
            element.click();
            logger.info("点击元素：" + getLocatorByElement(element, ">"));
        } catch (Exception e) {
            logger.error("点击元素:" + getLocatorByElement(element, ">") + "失败", e);
            Assert.fail("点击元素:" + getLocatorByElement(element, ">") + "失败", e);
        }

    }

    /**
     * 查找一个元素 - appium新增的查找元素方法
     */
    public WebElement findElement(String locateWay, String locateValue) {
        WebElement element = null;
        switch (locateWay) {

            case "AccessibilityId":
                element = driver.findElementByAccessibilityId(locateValue);
                break;
//    case "AndroidUIAutomator":
//        element = driver.findElementByAndroidUIAutomator(locateValue);
//            break;
            case "ClassName":
                element = driver.findElementByClassName(locateValue);
                break;
            case "CSS":
                element = driver.findElementByCssSelector(locateValue);
                break;
            case "ID":
                element = driver.findElementById(locateValue);
                break;
            case "LinkText":
                element = driver.findElementByLinkText(locateValue);
                break;
            case "Name":
                element = driver.findElementByName(locateValue);
                break;
            case "PartialLinkText":
                element = driver.findElementByPartialLinkText(locateValue);
                break;
            case "TagName":
                element = driver.findElementByTagName(locateValue);
                break;
            case "Xpath":
                element = driver.findElementByXPath(locateValue);
                break;
            default:
                logger.error("定位方式：" + locateWay + "不被支持");
                Assert.fail("定位方式：" + locateWay + "不被支持");

        }
        return element;

    }

    /**
     * 查找一组元素 - appium新增的查找元素方法
     */
    public List<?> findElements(String locateWay, String locateValue) {
        List<?> element = null;
        switch (locateWay) {

            case "AccessibilityId":
                element = driver.findElementsByAccessibilityId(locateValue);
                break;
//    case "AndroidUIAutomator":
//        element = driver.findElementsByAndroidUIAutomator(locateValue);
//            break;
            case "ClassName":
                element = driver.findElementsByClassName(locateValue);
                break;
            case "CSS":
                element = driver.findElementsByCssSelector(locateValue);
                break;
            case "ID":
                element = driver.findElementsById(locateValue);
                break;
            case "LinkText":
                element = driver.findElementsByLinkText(locateValue);
                break;
            case "Name":
                element = driver.findElementsByName(locateValue);
                break;
            case "PartialLinkText":
                element = driver.findElementsByPartialLinkText(locateValue);
                break;
            case "TagName":
                element = driver.findElementsByTagName(locateValue);
                break;
            case "Xpath":
                element = driver.findElementsByXPath(locateValue);
                break;
            default:
                logger.error("定位方式：" + locateWay + "不被支持");
                Assert.fail("定位方式：" + locateWay + "不被支持");

        }
        return element;

    }

    /**
     * 获取文本1
     */
    public String getText(By by) {
        return findElement(by).getText().trim();
    }


    /**
     * 获取文本2
     */
    public String getText(String locateWay, String locateValue) {
        String str = "";
        switch (locateWay) {

            case "AccessibilityId":
                str = driver.findElementByAccessibilityId(locateValue).getText().trim();
                break;
//    case "AndroidUIAutomator":
//        str = driver.findElementByAndroidUIAutomator(locateValue).getText().trim();
//            break;
            case "ClassName":
                str = driver.findElementByClassName(locateValue).getText().trim();
                break;
            case "CSS":
                str = driver.findElementByCssSelector(locateValue).getText().trim();
                break;
            case "ID":
                str = driver.findElementById(locateValue).getText().trim();
                break;
            case "LinkText":
                str = driver.findElementByLinkText(locateValue).getText().trim();
                break;
            case "Name":
                str = driver.findElementByName(locateValue).getText().trim();
                break;
            case "PartialLinkText":
                str = driver.findElementByPartialLinkText(locateValue).getText().trim();
                break;
            case "TagName":
                str = driver.findElementByTagName(locateValue).getText().trim();
                break;
            case "Xpath":
                str = driver.findElementByXPath(locateValue).getText().trim();
                break;
            default:
                logger.error("定位方式：" + locateWay + "不被支持");
                Assert.fail("定位方式：" + locateWay + "不被支持");

        }
        return str;

    }

    /**
     * 提交
     */
    public void submit(By by) {
        WebElement element = findElement(by);
        try {
            element.submit();
        } catch (Exception e) {
            logger.error("在元素：" + getLocatorByElement(element, ">") + "做的提交操作失败", e);
            Assert.fail("在元素：" + getLocatorByElement(element, ">") + "做的提交操作失败", e);
        }
        logger.info("在元素：" + getLocatorByElement(element, ">") + "做了提交操作");
    }

    /**
     * 获得webview页面的标题
     */
    public String getTitle() {
        return driver.getTitle();
    }

    /**
     * 获得元素 属性的文本
     */
    public String getAttributeText(By elementLocator, String attribute) {
        return findElement(elementLocator).getAttribute(attribute).trim();
    }

    /**
     * 在给定的时间内去查找元素，如果没找到则超时，抛出异常
     */
    public WebElement waitForElementToLoad(AndroidDriver<AndroidElement> driver, int elementTimeOut, final By By) {
        logger.info("开始查找元素[" + By + "]");
        try {

            WebElement element = new AndroidDriverWait(driver, 60)
                    .until((ExpectedCondition<WebElement>) androidDriver -> androidDriver.findElement(By));
//                    .until(new ExpectedCondition<WebElement>() {
//                        public WebElement apply(AndroidDriver driver) {
//                            WebElement element = driver.findElement(By);
//                            return element;
//                        }
//                    });
            logger.info("找到了元素 [" + By + "]");
            return element;
        } catch (TimeoutException e) {
            logger.error("超时!! " + elementTimeOut + " 秒之后还没找到元素 [" + By + "]");
            Assert.fail("超时!! " + elementTimeOut + " 秒之后还没找到元素 [" + By + "]");
        }
        return null;
    }

    /**
     * 判断文本是不是和需求要求的文本一致
     **/
    public void isTextCorrect(String actual, String expected) {
        try {
            Assert.assertEquals(actual, expected);
        } catch (AssertionError e) {
            logger.error("期望的文字是 [" + expected + "] 但是找到了 [" + actual + "]");
            Assert.fail("期望的文字是 [" + expected + "] 但是找到了 [" + actual + "]");

        }
        logger.info("找到了期望的文字: [" + expected + "]");

    }

    /**
     * 暂停当前用例的执行，暂停的时间为：sleepTime
     */
    public void pause(int sleepTime) {
        if (sleepTime <= 0) {
            return;
        }
        try {
            TimeUnit.SECONDS.sleep(sleepTime);
            logger.info("暂停:" + sleepTime + "秒");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    /**
     * 根据元素来获取此元素的定位值
     */
    public String getLocatorByElement(WebElement element, String expectText) {
        String text = element.toString();
        String expect = null;
        try {
            expect = text.substring(text.indexOf(expectText) + 1, text.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("failed to find the string [" + expectText + "]");

        }

        return expect;

    }


    /**
     * 判断实际文本时候包含期望文本
     *
     * @param actual 实际文本
     * @param expect 期望文本
     */
    public void isContains(String actual, String expect) {
        try {
            Assert.assertTrue(actual.contains(expect));
        } catch (AssertionError e) {
            logger.error("The [" + actual + "] is not contains [" + expect + "]");
            Assert.fail("The [" + actual + "] is not contains [" + expect + "]");
        }
        logger.info("The [" + actual + "] is contains [" + expect + "]");
    }

    /**
     * 跳转到webview页面
     */
    public void switchWebview(int index) {
        Set<String> contexts = driver.getContextHandles();
        for (String context : contexts) {
            System.out.println(context);
            //打印出来看看有哪些context
        }
        driver.context((String) contexts.toArray()[index]);

    }


    /**
     * 跳转到webview页面
     */
    public void switchWebview(String contextName) {
        try {
            Set<String> contexts = driver.getContextHandles();
            for (String context : contexts) {
                System.out.println(context);
                //打印出来看看有哪些context
            }
            driver.context(contextName);
        } catch (NoSuchContextException nce) {
            logger.error("没有这个context:" + contextName, nce);
            Assert.fail("没有这个context:" + contextName, nce);
        }

    }


    /**
     * 执行JavaScript 方法
     */
    public void executeJS(String js) {
        ((JavascriptExecutor) driver).executeScript(js);
        logger.info("执行JavaScript语句：[" + js + "]");
    }

    /**
     * 执行JavaScript 方法和对象
     * 用法：seleniumUtil.executeJS("arguments[0].click();", seleniumUtil.findElementBy(MyOrdersPage.MOP_TAB_ORDERCLOSE));
     */
    public void executeJS(String js, Object... args) {
        ((JavascriptExecutor) driver).executeScript(js, args);
        logger.info("执行JavaScript语句：[" + js + "]");
    }

    /**
     * 检查元素是不是存在
     */
    public boolean doesElementsExist(By byElement) {
        try {
            findElement(byElement);
            return true;
        } catch (NoSuchElementException nee) {

            return false;
        }


    }

//    /**长按操作*/
//    public void longPress(){
//        //初始化
//        TouchAction action = new TouchAction(driver);
//        //按住等待5秒后释放
//        action.press(driver.findElement(By.name("按住说话"))).waitAction(5000);
//        action.perform();
//    }
//
//    /**硬件返回*/
//    public void goBack(AndroidDriver driver) {
//        //driver.press_keycode(4);
//        driver.sendKeyEvent(AndroidKeyCode.BACK);
//        driver.
//    }
//
//    public static void Clip(WebElement el) {
//        //获取焦点
//        el.click();
//        //ctrl+v的组合操作
//        driver.sendKeyEvent(50,AndroidKeyMetastate.META_CTRL_ON);
//
//    }

    /**滑动*/
    /*public void swipe(int beginX,int beginY,int endX,int endY){
        TouchAction tAction=new TouchAction(driver);
        try{
            tAction.press(beginX,beginY).moveTo(endX,endY).release().perform();
        }catch(Exception e){
            e.printStackTrace();
        }
    }*/

    /**滚动 - 根据文本模糊匹配*/
//    public void scroll(String text){
//        driver.scrollTo(text);
//    }

    /**滚动 - 根据文本精准匹配*/
//    public WebElement scrollExact(String text){
//        return driver.scrollToExact(text);
//    }

    /**拖拽操作*/
//    public void DragAndDrop(By dragElement,By dropElement){
//        TouchAction act=new TouchAction(driver);
//        act.press(findElement(dragElement)).perform();
//        act.moveTo(findElement(dropElement)).release().perform();
//    }

    /**放大和缩小*/
//    public void zoomAndPinch(int beginX,int beginY,int endX,int endY){
//        int scrHeight = driver.manage().window().getSize().getHeight();
//        int scrWidth = driver.manage().window().getSize().getWidth();
//        MultiTouchAction multiTouch = new MultiTouchAction(driver);
//        TouchAction tAction0 = new TouchAction(driver);
//        TouchAction tAction1 = new TouchAction(driver);
//        tAction0.press(scrWidth/2,scrHeight/2).waitAction(1000).moveTo(beginX,beginY).release();
//        tAction1.press(scrWidth/2,scrHeight/2+40).waitAction(1000).moveTo(endX,endY).release();
//        multiTouch.add(tAction0).add(tAction1);
//        multiTouch.perform();
//
//    }

    /**app置于后台运行*/
//    public void runBackgound(int runTimes){
//        driver.runAppInBackground(runTimes);
//
//    }

    /**
     * 收起键盘
     */
    public void hideKeyboard() {
        driver.hideKeyboard();
        logger.info("虚拟键盘已经收起");

    }

    /**
     * 安装app
     */
    public void instalApp(String appPath) {
        try {
            driver.installApp(appPath);
        } catch (Exception e) {
            logger.error("app安装失败", e);
            Assert.fail("app安装失败", e);
        }
    }

    /**
     * app是否安装
     */
    public boolean isAppInstalled(String appPackage) {

        if (driver.isAppInstalled(appPackage)) {
            logger.info(appPackage + ":已经安装");
            return true;
        } else {
            logger.info(appPackage + ":未安装");
            return false;
        }
    }

    /**
     * 页面过长时候滑动页面 window.scrollTo(左边距,上边距);
     */
    public void scrollPage(int x, int y) {
        String js = "window.scrollTo(" + x + "," + y + ");";
        ((JavascriptExecutor) driver).executeScript(js);
    }
}
