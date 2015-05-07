/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logviewer.action;

import com.logviewer.entity.SirenaLog;
import com.logviewer.mongo.MongoDBUtil;
import com.logviewer.util.Helper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author asenturk
 */
@Named("sirenaLog")
@ViewScoped
public class SirenaLogAction implements Serializable {

  
    
    private SirenaLog instance = new SirenaLog();
    private SirenaLog selected = new SirenaLog();
    private List<SirenaLog> sirenaLogs = new ArrayList<SirenaLog>();
    
    private Date beginDate  = Helper.str2Time(Helper.date2String(new Date())+" 00:00:00");
    private Date endDate    = new Date();

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }   
    
    public List<SirenaLog> getSirenaLogs() {
        return sirenaLogs;
    }

    public void setErrorLogs(List<SirenaLog> sirenaLogs) {
        this.sirenaLogs = sirenaLogs;
    }
    
    
    public SirenaLog getInstance() {
        if (instance == null) {
            instance = new SirenaLog();
        }
        return instance;
    }

    public void setInstance(SirenaLog instance) {
        this.instance = instance;
    }

    public void clickErrorMessage() {
        RequestContext requestContext = RequestContext.getCurrentInstance();

        requestContext.update("form:searchForm");
        requestContext.execute("PF('dlgErrorMsg').show()");
    }

    public void clickExtraInfo() {
        RequestContext requestContext = RequestContext.getCurrentInstance();

        requestContext.update("form:searchForm");
        requestContext.execute("PF('dlgErrorInfo').show()");
    }
    
    public void searchSirenaLog(){
        sirenaLogs = new ArrayList<SirenaLog>();
        sirenaLogs = MongoDBUtil.querySirenaLogs(getInstance().getProject(),  Helper.date2String(beginDate,"dd/MM/yyyy HH:mm:ss") , Helper.date2String(endDate,"dd/MM/yyyy HH:mm:ss"), getInstance().getOfficeId(),
                                                getInstance().getAgentId(),getInstance().getMethodName(),getInstance().getWebServerUrl(),getInstance().getRequestXml(),getInstance().getResponseXml());
    }

    public SirenaLog getSelected() {
        if(selected==null){
            selected = new SirenaLog();
        }
        return selected;
    }

    public void setSelected(SirenaLog selected) {
        this.selected = selected;
    }
    
    
}
