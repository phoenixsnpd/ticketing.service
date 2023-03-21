package payment.system.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import payment.system.Exceptions.PaymentNotFoundException;
import payment.system.PaymentStatus;
import payment.system.entity.Payment;
import payment.system.repository.PaymentRepository;

import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public void createPayment(Payment payment) {
        paymentRepository.save(payment);
    }

    public PaymentStatus getPaymentStatus(UUID identifier) {
        Payment payment = paymentRepository.findByIdentifier(identifier);
        if (payment == null) {
            throw new PaymentNotFoundException();
        }
        return payment.getStatus();
    }
}