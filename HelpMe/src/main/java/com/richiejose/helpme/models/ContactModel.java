package com.richiejose.helpme.models;

/**
 * Created by richie on 8/19/13.
 */
public class ContactModel {

    public ContactModel() {
        this.phone = "";
        this.name="";
        this.priority=0;
    }

    public ContactModel(String phone, String name, int priority) {
        this.phone = phone;
        this.name = name;
        this.priority = priority;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        if(name==null){
            return "unnamed";
        }else{
            return name;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    String phone;
    String name;
    int priority;
}