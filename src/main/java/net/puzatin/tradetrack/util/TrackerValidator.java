package net.puzatin.tradetrack.util;


import net.puzatin.tradetrack.model.Tracker;
import net.puzatin.tradetrack.service.TrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.HashMap;


@Component
public class TrackerValidator implements Validator {

    @Autowired
    private TrackerService trackerService;


    @Override
    public boolean supports(Class<?> aClass) {
        return Tracker.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        HashMap<String, String> prices = BinanceUtil.getPrices();
        double BTCprice = BinanceUtil.getBTCprice();

        Tracker tracker = (Tracker) o;

        if(trackerService.findByName(tracker.getName()) != null){
            errors.rejectValue("name","","name already in use!");
        }

        if(trackerService.findByPubKey(tracker.getPubKey()) != null){
            errors.rejectValue("pubKey","","tracker already exists");
        }


            double balanceInBTC = BinanceUtil.getTotalAccountBalanceInBTC(tracker.getPubKey(), tracker.getSecKey(), prices, BTCprice, true);
            double balanceInUSDT = BinanceUtil.getTotalAccountBalanceInUSDT(BTCprice, balanceInBTC);
            if (balanceInBTC == -1){
                errors.rejectValue("pubKey", "", "invalid API-key or permission");
            } else if(balanceInUSDT < 10) {
                errors.rejectValue("public", "","you must have a total of at least 10USDT on your account");
            }


    }

    public void nameValidate(Object o, Errors errors){
        Tracker tracker = (Tracker) o;

        if(trackerService.findByName(tracker.getName()) != null){
            errors.rejectValue("name","","name already in use!");
        }
    }
}
