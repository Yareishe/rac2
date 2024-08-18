package com.example.rac.notelist.arb;

import java.util.List;

public class ApiResponse {
    private String code;
    private Data data;

    // Getters and Setters

    public static class Data {
        private String currency;
        private String name;
        private String fullName;
        private int precision;
        private Integer confirms;
        private String contractAddress;
        private boolean isMarginEnabled;
        private boolean isDebitEnabled;
        private List<Chain> chains;

        // Getters and Setters

        public static class Chain {
            private String chainName;
            private String withdrawalMinSize;
            private String depositMinSize;
            private String withdrawFeeRate;
            private String withdrawalMinFee;
            private boolean isWithdrawEnabled;
            private boolean isDepositEnabled;
            private int confirms;
            private int preConfirms;
            private String contractAddress;
            private String chainId;

            // Getters and Setters
        }
    }
}