package com.mengxun.tnaot.flows;

import com.mengxun.base.BaseFlow;
import com.mengxun.base.config.SelectDriver;
import com.mengxun.tnaot.pages.LoginPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("thread")
public class LoginFlow extends BaseFlow<LoginPage> {

    @Autowired
    private SelectDriver selectDriver;

    @Autowired
    private LoginPage loginPage;

    protected LoginFlow(LoginPage currentPage) {
        super(currentPage);
    }

}
