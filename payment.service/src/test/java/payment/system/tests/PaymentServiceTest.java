package payment.system.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import payment.system.PaymentStatus;
import payment.system.entity.Payment;
import payment.system.repository.PaymentRepository;
import payment.system.services.PaymentService;

import java.util.UUID;

@ExtendWith(SpringExtension.class)
class PaymentServiceTest {
    @MockBean
    PaymentService paymentService;

    @MockBean
    PaymentRepository paymentRepository;

    @Test
    void createPayment() {
        Payment payment = new Payment();
        paymentService.createPayment(payment);
        Mockito.verify(paymentService, Mockito.times(1)).createPayment(payment);
    }

    @Test
    void getPaymentStatus() {
        UUID identifier = UUID.randomUUID();
        Payment payment = new Payment();
        payment.setIdentifier(identifier);
        payment.setStatus(PaymentStatus.DONE);
        Mockito.doReturn(payment.getStatus())
                .when(paymentService)
                .getPaymentStatus(identifier);
        PaymentStatus paymentStatus = paymentService.getPaymentStatus(identifier);
        Assertions.assertEquals(payment.getStatus(), paymentStatus);
    }
}