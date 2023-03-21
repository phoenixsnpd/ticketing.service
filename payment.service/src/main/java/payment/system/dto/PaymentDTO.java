package payment.system.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PaymentDTO {
    @NotEmpty(message = "Name should not be empty.")
    @Size(min = 2, max = 30, message = "Name should be betwen 2 and 30 characters")
    String name;

    @NotEmpty(message = "Surame should not be empty.")
    @Size(min = 2, max = 30, message = "Surame should be betwen 2 and 30 characters")
    String surname;

    @Min(1)
    int paymentSum;
}
