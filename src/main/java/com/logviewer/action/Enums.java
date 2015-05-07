/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logviewer.action;


import com.logviewer.enums.Project;
import java.io.Serializable;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.inject.Named;

/**
 *
 * @author asenturk
 */
@Named(value = "enums")
@Stateless
public class Enums implements Serializable{
        
    public SelectItem[] getProjectSelect() {
        SelectItem[] items = new SelectItem[Project.values().length];
        int i = 0;
        for (Project s : Project.values()) {
            items[i++] = new SelectItem(s.getKey(), s.getValue());
        }     
        return items;
    }
     
}
