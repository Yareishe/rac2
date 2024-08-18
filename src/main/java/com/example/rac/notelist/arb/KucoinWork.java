package com.example.rac.notelist.arb;

import com.example.rac.notelist.entity.ChainInfo;
import com.google.gson.*;
import com.kucoin.sdk.KucoinClientBuilder;
import com.kucoin.sdk.KucoinRestClient;
import com.kucoin.sdk.rest.request.AccountTransferV2Request;
import com.kucoin.sdk.rest.request.OrderCreateApiRequest;
import com.kucoin.sdk.rest.request.WithdrawApplyRequest;
import com.kucoin.sdk.rest.response.OrderCreateResponse;
import com.kucoin.sdk.rest.response.SubApiKeyResponse;
import com.kucoin.sdk.rest.response.TickerResponse;
import com.kucoin.sdk.rest.response.WithdrawApplyResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class KucoinWork {


    private static KucoinWork kucoinWork = new KucoinWork();
    public KucoinRestClient buildClientKucoin (String YOUR_API_KEY,String YOUR_SECRET,String YOUR_PASS_PHRASE){
        KucoinClientBuilder builder = new KucoinClientBuilder().withBaseUrl("https://openapi-v2.kucoin.com")
                .withApiKey(YOUR_API_KEY, YOUR_SECRET, YOUR_PASS_PHRASE);
        KucoinRestClient kucoinRestClient = builder.buildRestClient();
        return kucoinRestClient;
    }



    //66329055411f5a0001689b59
    //84bdb4ec-2a30-4aee-bc21-b36a8716296f
    //2
    //2222222

    //3
    //3333333
    //66329db244736d00013527c6
    //d31154b6-d3bd-45b8-98c8-e64c4f46e696
    //
    public SubApiKeyResponse modifySubAccountAPIKucoin (KucoinClientBuilder builder, String subName, String apiKey
            , String passphrase, String permission, String ipWhitelist, String expire) throws IOException {
        KucoinRestClient kucoinRestClient = builder.buildRestClient();

        return kucoinRestClient.accountAPI().updateSubApiKey(subName,apiKey,passphrase,permission,ipWhitelist,expire);

    }

    public String getDepositAddressKucoin(KucoinRestClient kucoinRestClient, String name, String chain) throws IOException {
        String response = String.valueOf(kucoinRestClient.depositAPI().getDepositAddress(name,chain));
        String address = null;
        // Regular expression to match the address field
        String regex = "address=([^,]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(response);

        if (matcher.find()) {
            address = matcher.group(1);
        }

        return address;
    }
    public String createDepositAddressKucoin(KucoinRestClient kucoinRestClient, String name, String chain) throws IOException {
        String response = String.valueOf(kucoinRestClient.depositAPI().createDepositAddress(name,chain));
        String address = null;
        // Regular expression to match the address field
        String regex = "address=([^,]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(response);

        if (matcher.find()) {
            address = matcher.group(1);
        }
        return address;
    }

    public List<ChainInfo> getCurrencyKucoin(String currencies) throws IOException {


        // Устанавливаем URL для вызова API
        URL url = new URL("https://openapi-v2.kucoin.com/api/v3/currencies/" + currencies);

        // Создаем соединение
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");


        // Считываем ответ
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JsonObject jsonObject = JsonParser.parseString(String.valueOf(response)).getAsJsonObject();

        JsonArray chains = jsonObject.getAsJsonObject("data").getAsJsonArray("chains");

        List<ChainInfo> chainInfoList = new ArrayList<>();

        for (JsonElement chainElement : chains) {
            JsonObject chainObject = chainElement.getAsJsonObject();

                String chain = chainObject.has("chainName") && !chainObject.get("chainName").isJsonNull() ? chainObject.get("chainName").getAsString() : null;
                String chaiName = chainObject.has("chainId") && !chainObject.get("chainId").isJsonNull() ? chainObject.get("chainId").getAsString() : null;
                String withdrawFee = chainObject.has("withdrawalMinFee") && !chainObject.get("withdrawalMinFee").isJsonNull() ? chainObject.get("withdrawalMinFee").getAsString() : null;
                String withdrawMin = chainObject.has("withdrawalMinSize") && !chainObject.get("withdrawalMinSize").isJsonNull() ? chainObject.get("withdrawalMinSize").getAsString() : null;
                String minAccuracy = chainObject.has("depositMinSize") && !chainObject.get("depositMinSize").isJsonNull() ? chainObject.get("depositMinSize").getAsString() : null;
                boolean isWithdrawEnabled = chainObject.has("isWithdrawEnabled") && !chainObject.get("isWithdrawEnabled").isJsonNull() ? chainObject.get("isWithdrawEnabled").getAsBoolean() : null;
                boolean isDepositEnabled =chainObject.has("isDepositEnabled") && !chainObject.get("isDepositEnabled").isJsonNull() ? chainObject.get("isDepositEnabled").getAsBoolean() : null;
            ChainInfo chainInfo = new ChainInfo(chain,chaiName, withdrawFee, withdrawMin, null,isWithdrawEnabled,isDepositEnabled,null);
                chainInfoList.add(chainInfo);

        }
        System.out.println("Updated Chains:K" +currencies);
        for (ChainInfo info : chainInfoList) {
            System.out.println("Chain: " + info.getChain() +" " +info.getChaiName());


        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        // Вывод списка для проверки
        return chainInfoList;
    }

    public OrderCreateResponse createOrderKucoin(KucoinRestClient kucoinRestClient, String size, String side, String symbol) throws IOException {
        OrderCreateApiRequest request = OrderCreateApiRequest.builder()
                .size(BigDecimal.valueOf(Double.parseDouble(size))).side(side)
               .symbol(symbol).type("market").clientOid(UUID.randomUUID().toString()).build();

        return kucoinRestClient.orderAPI().createOrder(request);
    }

    public BigDecimal getTickerKukoin(KucoinRestClient kucoinRestClient, String symbol) throws IOException {
        TickerResponse a = kucoinRestClient.symbolAPI().getTicker(symbol);
        return a.getPrice();
    }

    public String innerTransferKucoin(KucoinRestClient kucoinRestClient,String amount,String to,String currency) throws IOException {
        BigDecimal bigDecimal = new BigDecimal(amount);
        AccountTransferV2Request accountTransferV2Request ;
        if (to.equals("main")) {
            accountTransferV2Request = new AccountTransferV2Request(UUID.randomUUID().toString(), currency
                    , "trade", "main", bigDecimal);
        }
        else {
            accountTransferV2Request = new AccountTransferV2Request(UUID.randomUUID().toString(), currency
                    , "main", "trade", bigDecimal);
        }
        return kucoinRestClient.accountAPI().innerTransfer2(accountTransferV2Request).toString();
    }

    public Map<String,String> listAccountsKucoin(KucoinRestClient kucoinRestClient,String currency,String type) throws IOException {


        String a = kucoinRestClient.accountAPI().listAccounts(currency, type).toString();


        String idRegex = "id=([a-zA-Z0-9]+)";
        String balanceRegex = "balance=([0-9]+\\.?[0-9]*)";

        // Компиляция шаблонов
        Pattern idPattern = Pattern.compile(idRegex);
        Pattern balancePattern = Pattern.compile(balanceRegex);

        // Поиск совпадений
        Matcher idMatcher = idPattern.matcher(a);
        Matcher balanceMatcher = balancePattern.matcher(a);

        String id = null;
        double balance = 0.0;

        if (idMatcher.find()) {
            id = idMatcher.group(1);
        }

        if (balanceMatcher.find()) {
            balance = Double.parseDouble(balanceMatcher.group(1));
        }

        Map <String,String> mybalance = new HashMap<>();

        mybalance.put("id",id);
        mybalance.put("balance", String.valueOf(balance));
        return mybalance;
    }

    public WithdrawApplyResponse withdrawKucoin(KucoinRestClient kucoinRestClient, double amount, String address, String currency,String chain) throws IOException {
        WithdrawApplyRequest withdrawApplyRequest = WithdrawApplyRequest.builder().address(address)
                .amount(BigDecimal.valueOf(amount)).currency(currency).chain(chain).build();

        return kucoinRestClient.withdrawalAPI().applyWithdraw(withdrawApplyRequest);
    }




    public static String replaceExceptOne(String numberStr, int positionToKeep) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < numberStr.length(); i++) {
            if (i == positionToKeep) {
                result.append(numberStr.charAt(i));
            } else if (numberStr.charAt(i) == '.') {
                result.append('.');
            } else {
                result.append('0');
            }
        }

        // Удаляем лишние нули в конце дробной части, но оставляем один, если это нужно
        int decimalIndex = result.indexOf(".");
        if (decimalIndex != -1) {
            int i = result.length() - 1;
            while (i > decimalIndex && result.charAt(i) == '0') {
                result.deleteCharAt(i);
                i--;
            }
            if (result.charAt(result.length() - 1) == '.') {
                result.append('0');
            }
        }

        return result.toString();
    }

    public String TrueSize(KucoinRestClient kucoinRestClient, String size, String symbol) throws IOException {
        for (int i = size.length();i >= 0 ;i--){

            double a = Double.parseDouble(kucoinWork.replaceExceptOne(size,i)) * Double.parseDouble(String
                    .valueOf(kucoinWork.getTickerKukoin(kucoinRestClient,symbol)));
            if (a >= 0.01){
                return size.substring(0,i+1);
            }


        }
        return "0";
    }




    public static void main(String[] args) throws IOException, InterruptedException {
        KucoinClientBuilder builder = new KucoinClientBuilder().withBaseUrl("https://openapi-v2.kucoin.com")
                .withApiKey("66329055411f5a0001689b59", "84bdb4ec-2a30-4aee-bc21-b36a8716296f", "2222222");
        KucoinRestClient kucoinRestClient = builder.buildRestClient();

        List<ChainInfo> x = kucoinWork.getCurrencyKucoin("BTC");
        for (ChainInfo info : x) {
            System.out.println("Chain: " + info.getChain());
            System.out.println("Withdraw Fee: " + info.getWithdrawFee());
            System.out.println("Withdraw Min: " + info.getWithdrawMin());
            System.out.println("Min Accuracy: " + info.getMinAccuracy());
            System.out.println();
        }

        /*
        String a = kucoinWork.listAccountsKucoin(kucoinRestClient,"ADA","trade").get("balance");

        //System.out.println(kucoinWork.createOrderKucoin(kucoinRestClient,kucoinWork.TrueSize(kucoinRestClient,a,"ADA-USDT"),"sell","ADA-USDT"));


        String b = String.valueOf(kucoinWork.getTickerKukoin(kucoinRestClient,"ADA-USDT"));
        System.out.println(kucoinWork.innerTransferKucoin(kucoinRestClient,"8","trade","USDT") + " 1");
        double prise = Double.parseDouble(kucoinWork.TrueSize(kucoinRestClient,String.valueOf(8 / Double.parseDouble(b) ),"ADA-USDT"));
        System.out.println(prise);
        String d = kucoinWork.listAccountsKucoin(kucoinRestClient,"ADA","trade").get("balance");
        System.out.println(kucoinWork.createOrderKucoin(kucoinRestClient,String.valueOf(prise),"buy","ADA-USDT") + " 2");
        double e = Double.parseDouble(kucoinWork.listAccountsKucoin(kucoinRestClient,"ADA","trade").get("balance"))
                - Double.parseDouble(d);
        String f = kucoinWork.TrueSize(kucoinRestClient,String.valueOf(e),"ADA-USDT");
        System.out.println(f);
        System.out.println(kucoinWork.innerTransferKucoin(kucoinRestClient,f,"main","ADA") + " 3");
        System.out.println(kucoinWork.withdrawKucoin(kucoinRestClient, Double.parseDouble(f),"addr1v9c6l79t77w0pnmdqkxvfvu3t8cpg6vac2wwtjqepxtjzpgpya8q0",
                "ADA","ada"));


         */














        

        //System.out.println(Double.parseDouble(b) + " " );
        //System.out.println(b + " " + a.length() +" " + a +" "+ c);
        //System.out.println(Double.parseDouble(a) * Double.parseDouble(b));
        //String b = kucoinWork.listAccountsKucoin(kucoinRestClient,"ADA","trade").get("balance");
        //System.out.println(b);
        //System.out.println(kucoinWork.createOrderKucoin(kucoinRestClient, b,"sell","ADA-USDT"));

        //System.out.println(5 / a.doubleValue());

        //Map<String, String> b = kucoinWork.listAccountsKucoin(kucoinRestClient,"ADA","trade");
        //System.out.println(b);
        //System.out.println(kucoinWork.innerTransferKucoin(kucoinRestClient,10,"trade","USDT"));
        //System.out.println(b.get("balance"));
        //System.out.println(kucoinWork.createOrderKucoin(kucoinRestClient,b.get("balance").substring(0,b.get("balance").length()-4),"sell","ADA-USDT"));
    }

}











