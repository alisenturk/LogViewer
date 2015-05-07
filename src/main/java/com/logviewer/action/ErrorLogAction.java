/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logviewer.action;

import com.logviewer.entity.ErrorClass;
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
@Named("errorLog")
@ViewScoped
public class ErrorLogAction implements Serializable {

  
    
    private ErrorClass instance = new ErrorClass();
    private ErrorClass selected = new ErrorClass();
    private List<ErrorClass> errorLogs = new ArrayList<ErrorClass>();
    
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
    
    public List<ErrorClass> getErrorLogs() {
        return errorLogs;
    }

    public void setErrorLogs(List<ErrorClass> errorLogs) {
        this.errorLogs = errorLogs;
    }
    
    
    public ErrorClass getInstance() {
        if (instance == null) {
            instance = new ErrorClass();
        }
        return instance;
    }

    public void setInstance(ErrorClass instance) {
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
    
    public void searchErrorLog(){
        errorLogs = new ArrayList<ErrorClass>();
        errorLogs = MongoDBUtil.queryAtlasjetLogs(getInstance().getProject(), Helper.date2String(beginDate,"dd/MM/yyyy HH:mm:ss") , Helper.date2String(endDate,"dd/MM/yyyy HH:mm:ss"),getInstance().getErrorMessage(),getInstance().getClassName(),getInstance().getFileName());
    }

    public ErrorClass getSelected() {
        if(selected==null){
            selected = new ErrorClass();
        }
        return selected;
    }

    public void setSelected(ErrorClass selected) {
        this.selected = selected;
    }
    
    
}
