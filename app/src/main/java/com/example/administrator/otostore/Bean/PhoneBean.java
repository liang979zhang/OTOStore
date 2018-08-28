package com.example.administrator.otostore.Bean;

/**
 * Created by Administrator on 2018/6/10.
 */

public class PhoneBean {
    private String phonename;
    private String phonenumber;
    private String pinyin;
    private String shoufirstletter;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getShoufirstletter() {
        return shoufirstletter;
    }

    public void setShoufirstletter(String shoufirstletter) {
        this.shoufirstletter = shoufirstletter;
    }

    public String getPhonename() {
        return phonename;
    }

    public void setPhonename(String phonename) {
        this.phonename = phonename;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
