package net.puzatin.tradetrack.util;

import com.binance.api.client.*;
import com.binance.api.client.constant.Util;
import com.binance.api.client.domain.account.*;
import com.binance.api.client.domain.market.TickerPrice;
import com.binance.api.client.exception.BinanceApiException;


import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public final class BinanceUtil {

    private BinanceUtil(){
    }

    public static final List<String> CRYPTOFIAT_CURRENCY = Collections.unmodifiableList(Arrays.asList("USDT", "BUSD", "PAX", "TUSD", "USDC", "USDS"));

    private static BinanceApiRestClient client;

   static  {
       try {
           System.out.println("static block");
           client = BinanceApiClientFactory.newInstance().newRestClient();
       } catch (BinanceApiException e) {
           e.printStackTrace();
       }

    }

    public static boolean isCryptoFiatCurrency(String symbol) {
        for (String fiat : CRYPTOFIAT_CURRENCY) {
            if (symbol.equals(fiat)) return true;
        }
        return false;
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
        System.out.println("getBTCprice");
        return Double.parseDouble(client.getPrice("BTCUSDT").getPrice());
    }


    public static double getTotalAccountBalanceInBTC(String pubKey, String secKey, HashMap<String, String> prices, double BTCprice, boolean first){
       double spotBalance;
       if(first){
            spotBalance = getTotalSpotBalanceFirst(pubKey, secKey, prices);
       } else {
           spotBalance = getTotalSpotBalance(pubKey, secKey, prices);
       }
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

    // TODO rewrite after emergence api for fiat
    public static double getTotalSpotBalanceFirst(String pubKey, String secKey, HashMap<String, String> prices) {
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
            e.printStackTrace();
           return -1;
       }

       }


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
                    if (BinanceUtil.isCryptoFiatCurrency(asset.getAsset())) {
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
            e.printStackTrace();
            return -1;
        }

    }





    public static HashMap<String, String> getPrices() {
        System.out.println("getprices");
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
            return Double.parseDouble(client.getFuturesAccount().getTotalWalletBalance()) / BTCprice;
        } catch (BinanceApiException e) {
            return 0;
        }
    }

    public static double getTotalLendingBalance(String pubKey, String secKey) {
        BinanceApiLendingClient client = BinanceApiClientFactory.newInstance(pubKey, secKey).newLendingRestClient();
        return Double.parseDouble(client.getLendingAccount().getTotalAmountInBTC());
    }

    public static double getDeltaDepositInBTC(String pubKey, String secKey, Long startTime, HashMap<String, String> prices){
       try {
           BinanceApiRestClient client = BinanceApiClientFactory.newInstance(pubKey, secKey).newRestClient();
          return getDeposit(client, startTime, prices) + getWithdraw(client, startTime, prices);
       } catch (BinanceApiException e) {
           e.getMessage();
           return 0;
       }
    }

    public static double getDeltaDepositInUSDT(double BTCprice, double amount) {
       return BTCprice * amount;
    }



    public static double getDeposit(BinanceApiRestClient client, Long startTime, HashMap<String, String> prices){
       double totalDeposit = 0;
//    status   0:pending,6: credited but cannot withdraw, 1:success)0:pending,6: credited but cannot withdraw, 1:success)
       List<DepositHistory> deposits = client.getDepositHistory(startTime);
       for (DepositHistory deposit : deposits) {
           if (deposit.getStatus() == 6 || deposit.getStatus() == 1) {
               String asset = deposit.getCoin();
               double amount = Double.parseDouble(deposit.getAmount());
               if (!asset.equals(Util.BTC_TICKER)) {
                   if (Util.isFiatCurrency(asset)) {
                       double price = Double.parseDouble(prices.get(Util.BTC_TICKER + asset));
                       totalDeposit += amount / price;
                   } else {
                       double price = Double.parseDouble(prices.get(asset + Util.BTC_TICKER));
                       totalDeposit += amount * price;
                   }
               } else totalDeposit += amount;
           }
       }


       return totalDeposit;

    }

    // TODO fix bug if user cancels withdraw after one period
    public static double getWithdraw(BinanceApiRestClient client, Long startTime, HashMap<String, String> prices){
        double totalWithdraw = 0;
//      status  0:Email Sent,1:Cancelled 2:Awaiting Approval 3:Rejected 4:Processing 5:Failure 6:Completed
        List<WithdrawHistory> withdraws = client.getWithdrawHistory(startTime);
             for (WithdrawHistory withdraw : withdraws) {
                 int status = withdraw.getStatus();
                 String asset = withdraw.getCoin();
                 double amount = Double.parseDouble(withdraw.getAmount());
                 if (status == 6 ||
                         status == 4 ||
                         status == 2 ||
                         status == 0) {
                     if (!asset.equals(Util.BTC_TICKER)) {
                         if (Util.isFiatCurrency(asset)) {
                             double price = Double.parseDouble(prices.get(Util.BTC_TICKER + asset));
                             amount /= price;
                         } else {
                             double price = Double.parseDouble(prices.get(asset + Util.BTC_TICKER));
                             amount *= price;
                         }
                     }
                     totalWithdraw -= amount;
                 }

             }

        List<WithdrawHistory> prevWithdraws = client.getWithdrawHistory(startTime - 900000, startTime);
        for (WithdrawHistory prevWithdraw : prevWithdraws) {
            int status = prevWithdraw.getStatus();
            String asset = prevWithdraw.getCoin();
            double amount = Double.parseDouble(prevWithdraw.getAmount());
            if (status == 1 ||
                    status == 3 ||
                    status == 5) {
                System.out.println("cancl");
                if (!asset.equals(Util.BTC_TICKER)) {
                    if (Util.isFiatCurrency(asset)) {
                        double price = Double.parseDouble(prices.get(Util.BTC_TICKER + asset));
                        amount /= price;
                    } else {
                        double price = Double.parseDouble(prices.get(asset + Util.BTC_TICKER));
                        amount *= price;
                    }
                }
                totalWithdraw += amount;
            }

        }



        return totalWithdraw;
    }


}
