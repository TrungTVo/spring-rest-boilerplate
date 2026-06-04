package com.example.boilerplate.commons.dtos;

import java.math.BigDecimal;

public record AnimalRequest(
        String name,
        int age,
        BigDecimal balance,
        String password) {

}
