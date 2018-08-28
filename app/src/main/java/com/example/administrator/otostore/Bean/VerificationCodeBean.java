package com.example.administrator.otostore.Bean;

/**
 * Created by Administrator on 2018/6/12.
 */

public class VerificationCodeBean {

    /**
     * Result : 1
     * VerifyCode : 522908
     * VerifyCodeID : 23
     * SMSorEmailID : 22
     */

    private String Result;
    private String VerifyCode;
    private String VerifyCodeID;
    private String SMSorEmailID;

    public String getResult() {
        return Result;
    }

    public void setResult(String Result) {
        this.Result = Result;
    }

    public String getVerifyCode() {
        return VerifyCode;
    }

    public void setVerifyCode(String VerifyCode) {
        this.VerifyCode = VerifyCode;
    }

    public String getVerifyCodeID() {
        return VerifyCodeID;
    }

    public void setVerifyCodeID(String VerifyCodeID) {
        this.VerifyCodeID = VerifyCodeID;
    }

    public String getSMSorEmailID() {
        return SMSorEmailID;
    }

    public void setSMSorEmailID(String SMSorEmailID) {
        this.SMSorEmailID = SMSorEmailID;
    }
}
