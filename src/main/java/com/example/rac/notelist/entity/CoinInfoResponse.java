package com.example.rac.notelist.entity;

import java.util.List;

public class CoinInfoResponse {
    private int retCode;
    private String retMsg;
    private Result result;

    // Getters и setters
    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    // Вложенный класс для десериализации части JSON ответа
    public static class Result {
        private List<Row> rows;

        // Getters и setters
        public List<Row> getRows() {
            return rows;
        }

        public void setRows(List<Row> rows) {
            this.rows = rows;
        }
    }

    // Вложенный класс для десериализации части JSON ответа
    public static class Row {
        private List<Chain> chains;

        // Getters и setters
        public List<Chain> getChains() {
            return chains;
        }

        public void setChains(List<Chain> chains) {
            this.chains = chains;
        }
    }

    // Вложенный класс для десериализации части JSON ответа
    public static class Chain {
        private String chain;
        private String chainType;
        private String withdrawFee;
        private String withdrawMin;
        private String minAccuracy;

        // Getters и setters
        public String getChain() {
            return chain;
        }

        public void setChain(String chain) {
            this.chain = chain;
        }

        public String getChainType() {
            return chainType;
        }

        public void setChainType(String chainType) {
            this.chainType = chainType;
        }

        public String getWithdrawFee() {
            return withdrawFee;
        }

        public void setWithdrawFee(String withdrawFee) {
            this.withdrawFee = withdrawFee;
        }

        public String getWithdrawMin() {
            return withdrawMin;
        }

        public void setWithdrawMin(String withdrawMin) {
            this.withdrawMin = withdrawMin;
        }

        public String getMinAccuracy() {
            return minAccuracy;
        }

        public void setMinAccuracy(String minAccuracy) {
            this.minAccuracy = minAccuracy;
        }
    }
}


