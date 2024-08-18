package com.example.rac.notelist.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChainInfo {
    private String chain;
    private String chaiName;
    private String withdrawFee;
    private String withdrawMin;
    private String minAccuracy;
    private boolean isWithdrawEnabled;
    private boolean isDepositEnabled;
    private String chaiName2;

    public ChainInfo(String chain,String chaiName, String withdrawFee, String withdrawMin, String minAccuracy,boolean isWithdrawEnabled,boolean isDepositEnabled,String chaiName2) {
        this.chaiName = chaiName;
        this.chain = chain;
        this.withdrawFee = withdrawFee;
        this.withdrawMin = withdrawMin;
        this.minAccuracy = minAccuracy;
        this.isWithdrawEnabled = isWithdrawEnabled;
        this.isDepositEnabled = isDepositEnabled;
        this.chaiName2 = chaiName2;
    }

    public ChainInfo() {

    }

    public String getChaiName() {
        return chaiName;
    }
    public String getChaiName2() {
        return chaiName2;
    }
    public String getChain() {
        return chain;
    }

    public String getWithdrawFee() {
        return withdrawFee;
    }

    public String getWithdrawMin() {
        return withdrawMin;
    }

    public String getMinAccuracy() {
        return minAccuracy;
    }

    public boolean getisWithdrawEnabled() {
        return isWithdrawEnabled;
    }

    public boolean getisDepositEnabled() {
        return isDepositEnabled;
    }

}