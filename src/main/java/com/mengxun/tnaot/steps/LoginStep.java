package com.mengxun.tnaot.steps;

import com.mengxun.base.BaseStep;
import com.mengxun.base.config.SelectDriver;
import com.mengxun.tnaot.flows.LoginFlow;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginStep extends BaseStep {

    @Autowired
    private LoginFlow loginFlow;

    @Autowired
    private SelectDriver selectDriver;

    @Given("^I navigated to lemfix site$")
    public void i_navigated_to_lemfix_site() throws Throwable {
//        driver = new FirefoxDriver();
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//        driver.manage().window().maximize();
//
//        driver.get("http://fm.lemfix.com");
    }

    @Given("^I want to write a step with precondition$")
    public void iWantToWriteAStepWithPrecondition() throws Throwable {

    }

    @And("^some other precondition$")
    public void someOtherPrecondition() throws Throwable {

    }

    @When("^I complete action$")
    public void iCompleteAction() throws Throwable {

    }

    @And("^some other action$")
    public void someOtherAction() throws Throwable {

    }

    @Then("^I validate the outcomes$")
    public void iValidateTheOutcomes() throws Throwable {

    }

    @And("^check more outcomes$")
    public void checkMoreOutcomes() throws Throwable {

    }

    @Given("^I want to write a step with \"([^\"]*)\"$")
    public void iWantToWriteAStepWithName(String aa) throws Throwable {

    }

    @When("^I check for the \"([^\"]*)\" in step$")
    public void iCheckForTheInStep(String arg0) throws Throwable {

    }

    @Then("^I verify the \"([^\"]*)\" in step$")
    public void iVerifyTheInStep(String arg0) throws Throwable {

    }
}
