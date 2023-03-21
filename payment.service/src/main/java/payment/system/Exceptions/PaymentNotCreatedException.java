package payment.system.Exceptions;

public class PaymentNotCreatedException extends RuntimeException {
    public PaymentNotCreatedException(String msg) {
        super(msg);
    }
}
