package com.example.creditcard;

import com.example.creditcard.model.CreditCard;
import com.example.creditcard.repository.CreditCardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureGraphQlTester
class CreditCardApplicationTests {

    @Autowired
    private GraphQlTester graphQlTester;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldCreateCreditCard() {
        String document = """
            mutation {
                createCreditCard(
                    cardholderName: "John Doe",
                    cardNumber: "4111111111111111",
                    expiryDate: "2025-12-31",
                    cvv: "123"
                ) {
                    id
                    cardholderName
                    cardNumber
                    expiryDate
                }
            }
        """;

        graphQlTester.document(document)
                .execute()
                .path("data.createCreditCard")
                .entity(CreditCard.class)
                .satisfies(creditCard -> {
                    assertNotNull(creditCard.getId());
                });
    }

    @Test
    void shouldGetCreditCardById() {
        CreditCard card = new CreditCard();
        card.setCardholderName("Test User");
        card.setCardNumber("5555555555554444");
        card.setExpiryDate(LocalDate.now().plusYears(2));
        card.setCvv("456");
        CreditCard savedCard = creditCardRepository.save(card);

        String document = """
            query {
                getCreditCard(id: "%s") {
                    id
                    cardholderName
                    cardNumber
                }
            }
        """.formatted(savedCard.getId());

        graphQlTester.document(document)
                .execute()
                .path("data.getCreditCard")
                .entity(CreditCard.class)
                .satisfies(foundCard -> {
                    assertNotNull(foundCard.getId());
                });
    }
}