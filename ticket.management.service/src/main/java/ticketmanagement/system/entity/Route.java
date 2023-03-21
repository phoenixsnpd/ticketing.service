package ticketmanagement.system.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name = "routes")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    long id;

    @Column(name = "destinations")
    String destination;

    @Column(name = "departureTime")
    Date departureTime;

    @Column(name = "ticketPrices")
    int ticketPrice;

    @Column(name = "numberOfTickets")
    int numberOfTickets;

    public Route(String destination, Date departureTime, int ticketPrice, int numberOfTickets) {
        this.destination = destination;
        this.departureTime = departureTime;
        this.ticketPrice = ticketPrice;
        this.numberOfTickets = numberOfTickets;
    }
}
