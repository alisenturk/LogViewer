/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logviewer.enums;

/**
 *
 * @author asenturk
 */
public enum Project {
    ATLASONLINE("AtlasOnline","Atlasglobal Web Sitesi"),
    ADS("ADS","Acenta Sistemi"),
    JETMILWEB("JetmilWeb","Atlasmiles Web Sitesi"),
    ALYA("Alya","Atlasmiles Yönetim Paneli"),
    GENCTATLAS("GencatlasWeb","Genç Atlas Web Sitesi"),
    ADSDCS("ADSDCS","DCS Uygulaması");
    
    Project(String key,String value){
        this.key    = key;
        this.value  = value;
    }
    
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
