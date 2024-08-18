package com.example.rac.notelist.arb;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.account.AccountType;
import com.bybit.api.client.domain.asset.request.AssetDataRequest;
import com.bybit.api.client.domain.market.request.MarketDataRequest;
import com.bybit.api.client.domain.user.UserDataRequest;
import com.bybit.api.client.restApi.BybitApiAssetRestClient;

import com.bybit.api.client.restApi.BybitApiAsyncUserRestClient;
import com.bybit.api.client.restApi.BybitApiTradeRestClient;
import com.bybit.api.client.service.BybitApiClientFactory;

import com.example.rac.notelist.adress.IpAdress;
import com.example.rac.notelist.entity.ChainInfo;
import com.example.rac.notelist.entity.CoinInfoResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class BybitWork {

    //"WyDdqWLVn0QzvokWAr","iGFGw7PBlFxVwYqWPJ1UWsGC5x99CYSi5yVk
    public String chekapi_Bybit(String apikey,String secret,String ip) throws ExecutionException, InterruptedException {
        BybitApiClientFactory factory = BybitApiClientFactory.newInstance(apikey,secret);

        BybitApiAsyncUserRestClient client = factory.newAsyncUserRestClient();

        UserDataRequest userDataRequest;

        if (ip.equals("on")){
            IpAdress ipAdress = new IpAdress();
            List<String> a = new LinkedList<>();
            a.add(ipAdress.myIp());
            userDataRequest = UserDataRequest.builder().ips(a).build();
        }
        else {
            userDataRequest = UserDataRequest.builder().build();
        }

        CompletableFuture<String> future = new CompletableFuture<>();

        client.modifyMasterApiKey(userDataRequest, response -> {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonResponse = gson.toJson(response);
            future.complete(jsonResponse);
        });
        String jsonResponse = future.get();
        return jsonResponse;
    }

    public String side_crypto_Bybit(String keyApi, String apisecret, String crypto, String amount, String side) {
        BybitApiClientFactory factory = BybitApiClientFactory.newInstance(keyApi, apisecret);
        BybitApiTradeRestClient client = factory.newTradeRestClient();

        Map<String, Object> order = Map.of(
                "category", "spot",
                "symbol", crypto,
                "side", side,
                "orderType", "Market",
                "qty", amount
        );



        Gson gson = new Gson();
        String jsonResponse = gson.toJson(client.createOrder(order));
        Map<String, Object> responseMap = gson.fromJson(jsonResponse, Map.class);

        if (responseMap.containsKey("retCode")) {
            int retCode = ((Double) responseMap.get("retCode")).intValue(); // Правильное преобразование в int
            return String.valueOf(retCode);
        } else {
            return "Неожиданный формат ответа"; // Обработка неожиданного ответа
        }
    }

    public String createInternalTransfer_Bybit(String keyApi,String apisecret,String crypto, String amount ,String accountType) {
        BybitApiClientFactory factory = BybitApiClientFactory.newInstance(keyApi, apisecret);

        BybitApiAssetRestClient client = factory.newAssetRestClient();

        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();

        AssetDataRequest assetDataRequest = null;

        if (accountType.equals("FUND")) {
            assetDataRequest = AssetDataRequest.builder()

                    .transferId(uuidAsString)
                    .coin(crypto)
                    .amount(amount)
                    .fromAccountType(AccountType.FUND)
                    .toAccountType(AccountType.UNIFIED)
                    .build();

        }
        else {
             assetDataRequest = AssetDataRequest.builder()
                    .transferId(uuidAsString)
                    .coin(crypto)
                    .amount(amount)
                    .fromAccountType(AccountType.UNIFIED)
                    .toAccountType(AccountType.FUND)
                    .build();

        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(client.createAssetInternalTransfer(assetDataRequest));
        if (jsonResponse != null) {
            Map<String, Object> responseMap = gson.fromJson(jsonResponse, Map.class);
            if (responseMap != null && responseMap.containsKey("retCode")) {
                int retCode = ((Double) responseMap.get("retCode")).intValue();
                return String.valueOf(retCode);
            } else {
                return "Неожиданный формат ответа";
            }
        } else {
            return "Ошибка при выполнении запроса";
        }
    }


    public long marketTime() throws InterruptedException, ExecutionException {
        var client = BybitApiClientFactory.newInstance().newAsyncMarketDataRestClient();
        CompletableFuture<String> timeFuture = new CompletableFuture<>();

        client.getServerTime(response -> {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonResponse = gson.toJson(response);
            timeFuture.complete(jsonResponse);
        });
        String jsonResponse = timeFuture.get();
        long timenew = 0;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            timenew = jsonNode.get("time").asLong();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timenew;
    }

    public static List<ChainInfo> coin_info_Bybit(String apiKey, String secret, String coin) throws ExecutionException, InterruptedException {
        BybitApiClientFactory factory = BybitApiClientFactory.newInstance(apiKey, secret);
        BybitApiAssetRestClient client = factory.newAssetRestClient();
        AssetDataRequest assetDataRequest = AssetDataRequest.builder().coin(coin).build();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResponse = gson.toJson(client.getAssetCoinInfo(assetDataRequest));

        // Парсинг JSON и извлечение нужных данных
        CoinInfoResponse coinInfoResponse = gson.fromJson(jsonResponse, CoinInfoResponse.class);
        List<ChainInfo> chainInfoList = new ArrayList<>();

        if (coinInfoResponse != null && coinInfoResponse.getResult() != null) {
            List<CoinInfoResponse.Row> rows = coinInfoResponse.getResult().getRows();
            if (rows != null) {
                for (CoinInfoResponse.Row row : rows) {
                    List<CoinInfoResponse.Chain> chains = row.getChains();
                    if (chains != null) {
                        for (CoinInfoResponse.Chain chain : chains) {
                            ChainInfo chainInfo = new ChainInfo(
                                    chain.getChainType(), // Используем chainType для chain
                                    chain.getChain(), // Используем chain для chaiName
                                    chain.getWithdrawFee(),
                                    chain.getWithdrawMin(),
                                    chain.getMinAccuracy(),
                                    true,
                                    true,
                                    null
                            );
                            chainInfoList.add(chainInfo);
                        }
                    }
                }
            }
        }
        System.out.println("Updated Chains:B"+ coin);
        for (ChainInfo info :  chainInfoList) {
            System.out.println("Chain: " + info.getChain() +" " +info.getChaiName());

        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        return chainInfoList;
    }




    public String deposit_Address_Bybit(String apiKey, String secret, String coin, String chainType) throws ExecutionException, InterruptedException {
        BybitApiClientFactory factory = BybitApiClientFactory.newInstance(apiKey, secret);
        BybitApiAssetRestClient client = factory.newAssetRestClient();
        AssetDataRequest assetDataRequest = AssetDataRequest.builder().coin(coin).chainType(chainType).build();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResponse = gson.toJson(client.getAssetMasterDepositAddress(assetDataRequest));

        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
        JsonObject resultObject = jsonObject.getAsJsonObject("result");

        // Проверка на наличие ключа "chains" и непустой массив
        if (resultObject != null && resultObject.has("chains")) {
            JsonArray chainsArray = resultObject.getAsJsonArray("chains");
            if (chainsArray != null && chainsArray.size() > 0) {
                JsonObject firstChain = chainsArray.get(0).getAsJsonObject();
                if (firstChain.has("addressDeposit")) {
                    return firstChain.get("addressDeposit").getAsString();
                }
            }
        }

        // Возвращение null или сообщения об ошибке, если ключ "chains" отсутствует или массив пустой
        return null;  // Или можно вернуть строку "Адрес депозита не найден"
    }

    public double coin_price_Bybit(String coin) throws ExecutionException, InterruptedException, JsonProcessingException {
        var client = BybitApiClientFactory.newInstance().newAsyncMarketDataRestClient();
        var tickerReueqt = MarketDataRequest.builder().category(CategoryType.SPOT).symbol(coin).build();

        CompletableFuture<String> future = new CompletableFuture<>();
        client.getMarketTickers(tickerReueqt, response -> {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonResponse = gson.toJson(response);
            future.complete(jsonResponse);
        });
        String jsonResponse = future.get();
        Gson gson = new Gson();
        JsonObject jsonResponseObject = gson.fromJson(jsonResponse, JsonObject.class);
        JsonObject resultObject = jsonResponseObject.getAsJsonObject("result");
        JsonArray listArray = resultObject.getAsJsonArray("list");
        JsonObject firstItem = listArray.get(0).getAsJsonObject();
        String lastPrice = firstItem.get("lastPrice").getAsString();

        return Double.parseDouble(lastPrice);
    }
    public String get_All_Coin_Balanc(String apikey, String secret, String coin, AccountType accountType) {
        BybitApiClientFactory factory = BybitApiClientFactory.newInstance(apikey, secret);
        BybitApiAssetRestClient client = factory.newAssetRestClient();
        AssetDataRequest assetDataRequest = AssetDataRequest.builder()
                .accountType(accountType)
                .coin(coin).build();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResponse = gson.toJson(client.getAssetAllCoinsBalance(assetDataRequest));
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
        JsonObject resultObject = jsonObject.getAsJsonObject("result");
        if (resultObject != null) {
            JsonArray balanceArray = resultObject.getAsJsonArray("balance");
            if (balanceArray != null && balanceArray.size() > 0) {
                for (int i = 0; i < balanceArray.size(); i++) {
                    JsonObject balanceObject = balanceArray.get(i).getAsJsonObject();
                    String coinName = balanceObject.get("coin").getAsString();
                    if (coinName.equals(coin)) {
                        String walletBalance = balanceObject.get("walletBalance").getAsString();
                        return walletBalance;
                    }
                }
            }
        }
        // Возвращаем пустую строку, если баланс не найден
        return null;
    }
    public String withdraw_Bybit(String apikey,String secret,String coin,String chain, String amount, String address) throws ExecutionException, InterruptedException {
        BybitApiClientFactory factory = BybitApiClientFactory.newInstance(apikey, secret);
        BybitApiAssetRestClient client = factory.newAssetRestClient();
        BybitWork buyToken = new BybitWork();
        AssetDataRequest assetDataRequest = AssetDataRequest.builder()
                .coin(coin)
                .address(address)
                .chain(chain)
                .amount(amount)
                .timestamp(buyToken.marketTime())
                .build();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(client.createAssetWithdraw(assetDataRequest));
    }
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        BybitWork buyToken = new BybitWork();
        buyToken.chekapi_Bybit("rjaljT652oJYGdALnE","Mt4NZ0jK1rsJs1ET36W7dB2WQ2cAEbfjuIDH","on");
        List<ChainInfo> chainInfoList =buyToken.coin_info_Bybit("rjaljT652oJYGdALnE","Mt4NZ0jK1rsJs1ET36W7dB2WQ2cAEbfjuIDH","BTC");
        for (ChainInfo info : chainInfoList) {
            System.out.println("Chain: " + info.getChain());
            System.out.println("Withdraw Fee: " + info.getWithdrawFee());
            System.out.println("Withdraw Min: " + info.getWithdrawMin());
            System.out.println("Min Accuracy: " + info.getMinAccuracy());
            System.out.println();
        }
        buyToken.chekapi_Bybit("rjaljT652oJYGdALnE","Mt4NZ0jK1rsJs1ET36W7dB2WQ2cAEbfjuIDH","");

        /*
        String a = "";
        a = buyToken.get_All_Coin_Balanc("WyDdqWLVn0QzvokWAr","iGFGw7PBlFxVwYqWPJ1UWsGC5x99CYSi5yVk",
                "ADA",AccountType.UNIFIED);
        System.out.println(buyToken.createInternalTransfer_Bybit("WyDdqWLVn0QzvokWAr",
                "iGFGw7PBlFxVwYqWPJ1UWsGC5x99CYSi5yVk","USDT","6","FUND") + 2 );
        System.out.println(buyToken.side_crypto_Bybit("WyDdqWLVn0QzvokWAr","iGFGw7PBlFxVwYqWPJ1UWsGC5x99CYSi5yVk"
                ,"ADAUSDT","6","Buy"));
        String b = buyToken.get_All_Coin_Balanc("WyDdqWLVn0QzvokWAr","iGFGw7PBlFxVwYqWPJ1UWsGC5x99CYSi5yVk",
                "ADA",AccountType.UNIFIED);
        System.out.println(Double.parseDouble(b) - Double.parseDouble(a));
        double c = Double.parseDouble(b) - Double.parseDouble(a);
        System.out.println(buyToken.createInternalTransfer_Bybit("WyDdqWLVn0QzvokWAr",
                "iGFGw7PBlFxVwYqWPJ1UWsGC5x99CYSi5yVk","ADAUSDT",Double.toString(c),"FUND") + 2 );

         */

        /*
        System.out.println(buyToken.get_All_Coin_Balanc("WyDdqWLVn0QzvokWAr","iGFGw7PBlFxVwYqWPJ1UWsGC5x99CYSi5yVk",
                "ADA",AccountType.UNIFIED));
        System.out.println(buyToken.coin_info_Bybit("WyDdqWLVn0QzvokWAr","iGFGw7PBlFxVwYqWPJ1UWsGC5x99CYSi5yVk",
                "ADA"));

        System.out.println(buyToken.deposit_Address_Bybit("WyDdqWLVn0QzvokWAr","iGFGw7PBlFxVwYqWPJ1UWsGC5x99CYSi5yVk",
                "ADA","ADA"));
        System.out.println(buyToken.get_All_Coin_Balanc("WyDdqWLVn0QzvokWAr","iGFGw7PBlFxVwYqWPJ1UWsGC5x99CYSi5yVk"
                ,"ADA",AccountType.UNIFIED));
        System.out.println(buyToken.createInternalTransfer_Bybit("WyDdqWLVn0QzvokWAr","iGFGw7PBlFxVwYqWPJ1UWsGC5x99CYSi5yVk",
                "USDT","6","FUND"));
        System.out.println(buyToken.side_crypto_Bybit("WyDdqWLVn0QzvokWAr","iGFGw7PBlFxVwYqWPJ1UWsGC5x99CYSi5yVk"
                ,"ADAUSDT","6","Buy"));

         */
        /*
        System.out.println(buyToken.chekapi_Bybit("WyDdqWLVn0QzvokWAr","iGFGw7PBlFxVwYqWPJ1UWsGC5x99CYSi5yVk","")+1);
        System.out.println(buyToken.createInternalTransfer_Bybit("WyDdqWLVn0QzvokWAr",
                "iGFGw7PBlFxVwYqWPJ1UWsGC5x99CYSi5yVk","USDT","2.8","FUND") + 2 );
        System.out.println(buyToken.side_crypto_Bybit("WyDdqWLVn0QzvokWAr",
                "iGFGw7PBlFxVwYqWPJ1UWsGC5x99CYSi5yVk", "ACMUSDT","2.8","Buy")+3);
        System.out.println(buyToken.createInternalTransfer_Bybit("WyDdqWLVn0QzvokWAr",
                "iGFGw7PBlFxVwYqWPJ1UWsGC5x99CYSi5yVk","ACMUSDT","1",""));
        System.out.println(buyToken.withdraw_Bybit("WyDdqWLVn0QzvokWAr",
                "iGFGw7PBlFxVwYqWPJ1UWsGC5x99CYSi5yVk","ACM","","1",""));
        System.out.println(buyToken.chekapi_Bybit("WyDdqWLVn0QzvokWAr","iGFGw7PBlFxVwYqWPJ1UWsGC5x99CYSi5yVk","on"));

         */
    }
}
