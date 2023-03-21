package payment.system;

import java.util.Random;

public enum PaymentStatus {
    NEW, FAILED, DONE;
    private static final Random RANDOM_STATUS = new Random();
    private static final PaymentStatus[] statuses = values();

    public static PaymentStatus getRandomStatus() {
        return statuses[RANDOM_STATUS.nextInt(statuses.length)];
    }
}
