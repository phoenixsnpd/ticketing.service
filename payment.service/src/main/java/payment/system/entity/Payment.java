package payment.system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import payment.system.PaymentStatus;

import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    long id;

    @Column(name = "identifier")
    UUID identifier;

    @Column(name = "name")
    @NotEmpty(message = "Name should not be empty.")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    String name;

    @Column(name = "surname")
    @NotEmpty(message = "Surame should not be empty.")
    @Size(min = 2, max = 30, message = "Surame should be between 2 and 30 characters")
    String surname;

    @Column(name = "paymentSum")
    @Min(1)
    int paymentSum;

    @Column(name = "status")
    PaymentStatus status;

    public Payment() {
    }
}
