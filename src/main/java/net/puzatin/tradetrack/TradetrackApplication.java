package net.puzatin.tradetrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TradetrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradetrackApplication.class, args);
	}

}
