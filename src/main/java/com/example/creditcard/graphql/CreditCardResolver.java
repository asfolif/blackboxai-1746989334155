package com.example.creditcard.graphql;

import com.example.creditcard.model.CreditCard;
import com.example.creditcard.repository.CreditCardRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class CreditCardResolver {
    private final CreditCardRepository creditCardRepository;

    public CreditCardResolver(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }

    @QueryMapping
    public CreditCard getCreditCard(@Argument Long id) {
        return creditCardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Credit card not found"));
    }

    @QueryMapping
    public CreditCard getCreditCardByNumber(@Argument String cardNumber) {
        return creditCardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new RuntimeException("Credit card not found"));
    }

    @QueryMapping
    public List<CreditCard> getAllCreditCards() {
        return creditCardRepository.findAll();
    }

    @MutationMapping
    public CreditCard createCreditCard(
            @Argument String cardholderName,
            @Argument String cardNumber,
            @Argument String expiryDate,
            @Argument String cvv) {
        
        CreditCard creditCard = new CreditCard();
        creditCard.setCardholderName(cardholderName);
        creditCard.setCardNumber(cardNumber);
        creditCard.setExpiryDate(LocalDate.parse(expiryDate, DateTimeFormatter.ISO_DATE));
        creditCard.setCvv(cvv);
        
        return creditCardRepository.save(creditCard);
    }
}