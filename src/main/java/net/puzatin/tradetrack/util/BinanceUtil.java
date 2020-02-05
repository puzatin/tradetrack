package net.puzatin.tradetrack.util;

import com.binance.api.client.*;
import com.binance.api.client.constant.Util;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.AssetBalance;
import com.binance.api.client.domain.market.TickerPrice;
import com.binance.api.client.exception.BinanceApiException;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

public final class BinanceUtil {

    private BinanceUtil(){
    }

    private static HashMap<String, String> prices;
    private static double BTCprice;
    private static BinanceApiRestClient client;

   static  {
       try {
           client = BinanceApiClientFactory.newInstance().newRestClient();
           prices = getPrices(client);
           BTCprice = Double.parseDouble(prices.get("BTCUSDT"));
       } catch (BinanceApiException e) {
           e.printStackTrace();
       }

    }

    public static boolean ping(){
       try {
           client.ping();
           return true;
       } catch (BinanceApiException e) {
           return false;
       }

    }


    public static double getBTCprice() {
        return BTCprice;
    }

    public static HashMap<String, String> getPrices() {
        return prices;
    }

    public static double getTotalAccountBalanceInBTC(String pubKey, String secKey){
        double spotBalance = getTotalSpotBalance(pubKey, secKey, prices);
       if(spotBalance != -1) {
           return  spotBalance +
                   getTotalMarginBalance(pubKey, secKey) +
                   getTotalFuturesBalance(pubKey, secKey, BTCprice) +
                   getTotalLendingBalance(pubKey, secKey);
       } else return -1;


    }

    public static double getTotalAccountBalanceInUSDT(double BTCprice, double totalAmountBTC){
        return BTCprice * totalAmountBTC;
    }

    // Get total account balance in BTC (spot only)
    public static double getTotalSpotBalance(String pubKey, String secKey, HashMap<String, String> prices) {
       try {
           BinanceApiRestClient client = BinanceApiClientFactory.newInstance(pubKey, secKey).newRestClient();
           List<AssetBalance> balances = client.getAccount().getBalances();
           balances.removeIf(asset -> (Double.parseDouble(asset.getFree()) + Double.parseDouble(asset.getLocked()) == 0 ||
                   (asset.getAsset().charAt(0) == 'L' && asset.getAsset().charAt(1) == 'D')));

           double totalAccountBalance = 0;
           for (AssetBalance asset : balances) {
               double amount = Double.parseDouble(asset.getFree()) + Double.parseDouble(asset.getLocked());
               if (!asset.getAsset().equals(Util.BTC_TICKER)) {
                   if (Util.isFiatCurrency(asset.getAsset())) {
                       double price = Double.parseDouble(prices.get(Util.BTC_TICKER + asset.getAsset()));
                       totalAccountBalance += amount / price;
                   } else {
                       double price = Double.parseDouble(prices.get(asset.getAsset() + Util.BTC_TICKER));
                       totalAccountBalance += amount * price;
                   }

               } else {
                   totalAccountBalance += amount;
               }
           }
           return totalAccountBalance;
       } catch (BinanceApiException e) {
           return -1;
       }

       }


    public static HashMap<String, String> getPrices(BinanceApiRestClient client) {
        HashMap<String, String> prices = new HashMap<>();
        for (TickerPrice tickerPrice : client.getAllPrices()) {
            prices.put(tickerPrice.getSymbol(), tickerPrice.getPrice());
        }
        return prices;
    }

    public static double getTotalMarginBalance(String pubKey, String secKey){
        try {
            BinanceApiMarginRestClient client = BinanceApiClientFactory.newInstance(pubKey, secKey).newMarginRestClient();
            return Double.parseDouble(client.getAccount().getTotalNetAssetOfBtc());
        } catch (BinanceApiException e) {
            return 0;
        }
    }

    public static double getTotalFuturesBalance(String pubKey, String secKey, double BTCprice) {
        try {
            BinanceApiFuturesRestClient client = BinanceApiClientFactory.newInstance(pubKey, secKey).newFuturesRestClient();
            return Double.parseDouble(client.getBalance().getBalance()) / BTCprice;
        } catch (BinanceApiException e) {
            return 0;
        }
    }

    public static double getTotalLendingBalance(String pubKey, String secKey) {
        BinanceApiLendingClient client = BinanceApiClientFactory.newInstance(pubKey, secKey).newLendingRestClient();
        return Double.parseDouble(client.getLendingAccount().getTotalAmountInBTC());
    }


}
