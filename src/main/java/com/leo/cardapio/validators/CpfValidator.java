package com.leo.cardapio.validators;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CpfValidator {

     public boolean isValid(String cpf) {
            cpf = cpf.replaceAll("\\D", ""); // só números

            if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
                return false;
            }

            try {
                int sum = 0, weight = 10;
                for (int i = 0; i < 9; i++) {
                    sum += (cpf.charAt(i) - '0') * weight--;
                }
                int mod = 11 - (sum % 11);
                char dig10 = (mod == 10 || mod == 11) ? '0' : (char) (mod + '0');

                sum = 0;
                weight = 11;
                for (int i = 0; i < 10; i++) {
                    sum += (cpf.charAt(i) - '0') * weight--;
                }
                mod = 11 - (sum % 11);
                char dig11 = (mod == 10 || mod == 11) ? '0' : (char) (mod + '0');

                return dig10 == cpf.charAt(9) && dig11 == cpf.charAt(10);
            } catch (Exception e) {
                return false;
            }
        }
}
