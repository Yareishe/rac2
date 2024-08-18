package com.example.rac.notelist.arb;

import com.bybit.api.client.domain.account.AccountType;


import com.example.rac.notelist.adress.Commission;
import com.example.rac.notelist.entity.ChainInfo;
import com.example.rac.notelist.entity.ExchangeInfo;
import com.kucoin.sdk.KucoinRestClient;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Arbitrshe {

    private static BybitWork bybitWork = new BybitWork();
    private static KucoinWork kucoinWork = new KucoinWork();
    private static Arbitrshe arbitrshe = new Arbitrshe();


    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {


        /*
        Map<String,String> api = new HashMap<>();
        api.put("Bybitkey","rjaljT652oJYGdALnE");
        api.put("Bybitsecret","Mt4NZ0jK1rsJs1ET36W7dB2WQ2cAEbfjuIDH");
        api.put("Kucoinkey","66329055411f5a0001689b59");
        api.put("Kucoinsecret","84bdb4ec-2a30-4aee-bc21-b36a8716296f");
        api.put("KucoinName","2222222");
        Arbitrshe arbitrshe = new Arbitrshe();
        BybitWork bybitWork = new BybitWork();
        Commission[] commissions = new Commission[10]; // Например, создаем массив размером 10


        commissions[0] = new Commission("ada", 0.99);
        arbitrshe.avto("KUCOIN","BYBIT",api,commissions,"ADAUSDT","8");

         */
    }

    public List<ChainInfo> getMatchingChainsmain(ExchangeInfo exchangeInfoList,String coin) throws IOException, ExecutionException, InterruptedException {
        List<ChainInfo> chainInfoList1 = new ArrayList<>();

        switch (exchangeInfoList.getExchangeName()) {
            case "Bybit":
                chainInfoList1.addAll(bybitWork.coin_info_Bybit(exchangeInfoList.getApikey(), exchangeInfoList.getSecretapi(), coin));
                break;
            case "Kucoin":
                chainInfoList1.addAll(kucoinWork.getCurrencyKucoin(coin));
                break;
        }
        return chainInfoList1;
    }


    public List<ChainInfo> getMatchingChains(ExchangeInfo exchangeInfoList1, ExchangeInfo exchangeInfoList2, String coin) throws ExecutionException, InterruptedException, IOException {
        List<ChainInfo> chainInfoList1 = new ArrayList<>();
        List<ChainInfo> chainInfoList2 = new ArrayList<>();

        // Получение данных из первого источника
        switch (exchangeInfoList1.getExchangeName()) {
            case "Bybit":
                chainInfoList1.addAll(bybitWork.coin_info_Bybit(exchangeInfoList1.getApikey(), exchangeInfoList1.getSecretapi(), coin));
                break;
            case "Kucoin":
                chainInfoList1.addAll(kucoinWork.getCurrencyKucoin(coin));
                break;
        }

        // Получение данных из второго источника
        switch (exchangeInfoList2.getExchangeName()) {
            case "Bybit":
                chainInfoList2.addAll(bybitWork.coin_info_Bybit(exchangeInfoList2.getApikey(), exchangeInfoList2.getSecretapi(), coin));
                break;
            case "Kucoin":
                chainInfoList2.addAll(kucoinWork.getCurrencyKucoin(coin));
                break;
        }

        // Создание мапы для хранения chaiName по chaiName из chainInfoList2 в верхнем регистре
        Map<String, String> chaiNameMap = new HashMap<>();
        for (ChainInfo chainInfo : chainInfoList2) {
            String chaiName = chainInfo.getChaiName().toUpperCase();
            chaiNameMap.put(chaiName, chainInfo.getChaiName());
        }

        // Создание нового списка для хранения элементов с соответствием по chaiName в верхнем регистре
        List<ChainInfo> matchingChainInfoList = new ArrayList<>();
        for (ChainInfo chainInfo : chainInfoList1) {
            String chaiName = chainInfo.getChaiName().toUpperCase();
            String matchedChaiName = chaiNameMap.get(chaiName);
            if (matchedChaiName != null) {
                ChainInfo updatedChainInfo = new ChainInfo();
                updatedChainInfo.setChain(chainInfo.getChain());
                updatedChainInfo.setChaiName(chainInfo.getChaiName());
                updatedChainInfo.setWithdrawFee(chainInfo.getWithdrawFee());
                updatedChainInfo.setWithdrawMin(chainInfo.getWithdrawMin());
                updatedChainInfo.setMinAccuracy(chainInfo.getMinAccuracy());
                updatedChainInfo.setDepositEnabled(chainInfo.getisDepositEnabled());
                updatedChainInfo.setWithdrawEnabled(chainInfo.getisWithdrawEnabled());
                updatedChainInfo.setChaiName2(matchedChaiName); // Устанавливаем chaiName2 из chaiNameMap
                matchingChainInfoList.add(updatedChainInfo);
            }
        }

        // Вывод обновленных данных
        return matchingChainInfoList;
    }

    public static String normalizeChainName(String chainName) {
        // Приведение к нижнему регистру и удаление пробелов
        String normalized = chainName.trim().toLowerCase();


        normalized = normalized.replaceAll("co-chain", "")
                .replaceAll(" ", "")
                .replaceAll("[-]", "")
                .replaceAll("[^a-z0-9]", "");

        if (normalized.equals("kavaevm") || normalized.equals("kavaevmcochain")) {
            return "kavaevm";
        }

        return normalized;
    }

    public double transver(String mybalanc,String coin,String apikey,String secret,String accountType){
        BybitWork bybitWork = new BybitWork();

        for (int a = mybalanc.length();0 <= a;a--){
            String b = bybitWork.createInternalTransfer_Bybit(apikey,secret,coin,mybalanc.substring(0,a),accountType);

            if (b.equals("0")){
                return Double.parseDouble(mybalanc.substring(0,a));
            }
        }
        return 0;
    }


    public int sell(String mybalanc,String coin,String apikey,String secret){
        BybitWork bybitWork = new BybitWork();
        for (int a = mybalanc.length();0 <= a;a--){
            String b = bybitWork.side_crypto_Bybit(apikey,secret,coin,mybalanc.substring(0,a),"Sell");
            if (b.equals("0")){
                return a;
            }
        }
        return 0;
    }

    public String avto(String stock_exchange1, String stock_exchange2, Map<String,String> api, Commission[] commissions, String crypto, String amount) throws ExecutionException, InterruptedException, IOException {
        BybitWork bybitWork = new BybitWork();
        KucoinWork kucoinWork = new KucoinWork();
        Arbitrshe arbitrshe = new Arbitrshe();
        String adress = "";

        KucoinRestClient kucoinRestClient = kucoinWork.buildClientKucoin(api.get("Kucoinkey"),api.get("Kucoinsecret"),api.get("KucoinName"));
        switch (stock_exchange2){
            case "BYBIT"->{
                adress =  bybitWork.deposit_Address_Bybit(api.get("Bybitkey"),api.get("Bybitsecret"),crypto,commissions[0].getAddress());
            }
            case "KUCOIN"->{
                adress = kucoinWork.createDepositAddressKucoin(kucoinRestClient,crypto.replace("USDT",""),commissions[0].getAddress());
                adress = kucoinWork.getDepositAddressKucoin(kucoinRestClient,crypto.replace("USDT",""),commissions[0].getAddress());

            }
        }

        switch (stock_exchange1){
            case "BYBIT"->{
                System.out.println(bybitWork.chekapi_Bybit(api.get("Bybitkey"),api.get("Bybitsecret"),"on")+ 0);
                System.out.println(bybitWork.createInternalTransfer_Bybit(api.get("Bybitkey"),api.get("Bybitsecret"),"USDT",amount,"FUND") + " 1");
                String myamount = bybitWork.get_All_Coin_Balanc(api.get("Bybitkey"),api.get("Bybitsecret"),crypto.replace("USDT",""),AccountType.UNIFIED);
                System.out.println(bybitWork.side_crypto_Bybit(api.get("Bybitkey"),api.get("Bybitsecret"),crypto,amount,"Buy") +" 2 " + myamount);
                double myamount2  = Double.parseDouble(bybitWork.get_All_Coin_Balanc(api.get("Bybitkey"),api.get("Bybitsecret")
                        ,crypto.replace("USDT",""),AccountType.UNIFIED)) - Double.parseDouble(myamount) ;
                double muamountf = arbitrshe.transver(Double.toString(myamount2),crypto.replace("USDT","")
                        ,api.get("Bybitkey"),api.get("Bybitsecret"),"U");
                System.out.println(muamountf + "3");
                System.out.println(bybitWork.withdraw_Bybit(api.get("Bybitkey"),api.get("Bybitsecret"),crypto.replace("USDT",""),commissions[0].getAddress()
                        ,Double.toString(muamountf - commissions[0].getCommission1()),adress) + "4");
                System.out.println(bybitWork.chekapi_Bybit(api.get("Bybitkey"),api.get("Bybitsecret"),"off"));
            }
            case "KUCOIN"->{
                String trueCrupto = crypto.replace("USDT","")+"-USDT";
                String b = String.valueOf(kucoinWork.getTickerKukoin(kucoinRestClient,trueCrupto));
                System.out.println(kucoinWork.innerTransferKucoin(kucoinRestClient,amount,"trade","USDT") + " 1");
                double prise = Double.parseDouble(kucoinWork.TrueSize(kucoinRestClient,String.valueOf(Double.parseDouble(amount)/Double.parseDouble(b))
                        ,trueCrupto));
                System.out.println(prise);
                String d = kucoinWork.listAccountsKucoin(kucoinRestClient,crypto.replace("USDT",""),"trade").get("balance");
                System.out.println(kucoinWork.createOrderKucoin(kucoinRestClient,String.valueOf(prise),"buy",trueCrupto) + " 2");
                double e = Double.parseDouble(kucoinWork.listAccountsKucoin
                        (kucoinRestClient,crypto.replace("USDT",""),"trade").get("balance")) - Double.parseDouble(d);
                String f = kucoinWork.TrueSize(kucoinRestClient,String.valueOf(e),trueCrupto);
                System.out.println(f);
                System.out.println(kucoinWork.innerTransferKucoin(kucoinRestClient,f,"main",crypto.replace("USDT","")) + " 3");
                System.out.println(kucoinWork.withdrawKucoin(kucoinRestClient, Double.parseDouble(f),adress,
                        crypto.replace("USDT",""),adress));
                
            }
        }
        return null;
    }

}
