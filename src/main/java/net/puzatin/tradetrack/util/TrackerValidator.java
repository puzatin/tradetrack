package net.puzatin.tradetrack.util;


import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.exception.BinanceApiException;
import net.puzatin.tradetrack.model.Tracker;
import net.puzatin.tradetrack.service.TrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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
        Tracker tracker = (Tracker) o;

        if(trackerService.findByName(tracker.getName()) != null){
            errors.rejectValue("name","","Name already in use");
        }

//        try {
//            BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(tracker.getPubKey(),tracker.getSecKey());
//            BinanceApiRestClient client = factory.newRestClient();
//            client.getAccount();
//        } catch (BinanceApiException e) {
//            errors.rejectValue("pubKey", "", "Invalid API key");
//        }

    }
}
