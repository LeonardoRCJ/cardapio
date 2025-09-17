package com.leo.cardapio;

import com.mercadopago.MercadoPagoConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CardapioApplication {
	@Value("${mercadopago.access_token}")
	private String mpAccessToken;

	public static void main(String[] args) {
		SpringApplication.run(CardapioApplication.class, args);
	}

	@PostConstruct
	public void init() {
		MercadoPagoConfig.setAccessToken(mpAccessToken);
	}
}
