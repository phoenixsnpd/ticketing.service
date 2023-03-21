package payment.system.controlers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import payment.system.Exceptions.PaymentNotCreatedException;
import payment.system.Exceptions.PaymentNotFoundException;
import payment.system.PaymentStatus;
import payment.system.dto.PaymentDTO;
import payment.system.entity.Payment;
import payment.system.services.PaymentService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/")
public class PaymentsControler {

    private final PaymentService paymentService;

    public PaymentsControler(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payments")
    public UUID pay(@RequestBody @Valid PaymentDTO paymentDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessage.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append("; ");
            }
            log.error("Incorrect payment data {}", paymentDTO);
            throw new PaymentNotCreatedException(errorMessage.toString());
        }
        log.info("Data received from client {}", paymentDTO);
        Payment payment = convertPaymentDTO(paymentDTO);
        paymentService.createPayment(payment);

        return payment.getIdentifier();
    }

    @GetMapping("/status/{identifier}")
    public PaymentStatus getPaymentStatus(@PathVariable UUID identifier) {
        PaymentStatus paymentStatus = paymentService.getPaymentStatus(identifier);
        if (paymentStatus == null) {
            log.error("Payment with identifier: {} doesn't exist", identifier);
            throw new PaymentNotFoundException();
        }
        return paymentService.getPaymentStatus(identifier);
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    private ResponseEntity<String> handlerException() {
        return new ResponseEntity<>("Identifier wasn't find", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PaymentNotCreatedException.class)
    private ResponseEntity<String> handlerException(PaymentNotCreatedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private Payment convertPaymentDTO(PaymentDTO paymentDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Payment payment = modelMapper.map(paymentDTO, Payment.class);
        payment.setIdentifier(UUID.randomUUID());
        payment.setStatus(PaymentStatus.getRandomStatus());
        return payment;
    }
}
