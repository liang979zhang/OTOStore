package com.example.administrator.otostore.Bean;

/**
 * Created by Administrator on 2018/7/12.
 */

public class ShopRefundBillsBean {
    private String BuyerName;
    private String MobileNum;
    private String DeliveryCode;
    private String RefundBonus;
    private String RefundAmt;
    private String MaxRefundAmt;
    private String RecStatus;
    private String RefundBillID;

    public String getBuyerName() {
        return BuyerName;
    }

    public void setBuyerName(String buyerName) {
        BuyerName = buyerName;
    }

    public String getMobileNum() {
        return MobileNum;
    }

    public void setMobileNum(String mobileNum) {
        MobileNum = mobileNum;
    }

    public String getDeliveryCode() {
        return DeliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        DeliveryCode = deliveryCode;
    }

    public String getRefundBonus() {
        return RefundBonus;
    }

    public void setRefundBonus(String refundBonus) {
        RefundBonus = refundBonus;
    }

    public String getRefundAmt() {
        return RefundAmt;
    }

    public void setRefundAmt(String refundAmt) {
        RefundAmt = refundAmt;
    }

    public String getMaxRefundAmt() {
        return MaxRefundAmt;
    }

    public void setMaxRefundAmt(String maxRefundAmt) {
        MaxRefundAmt = maxRefundAmt;
    }

    public String getRecStatus() {
        return RecStatus;
    }

    public void setRecStatus(String recStatus) {
        RecStatus = recStatus;
    }

    public String getRefundBillID() {
        return RefundBillID;
    }

    public void setRefundBillID(String refundBillID) {
        RefundBillID = refundBillID;
    }
}
