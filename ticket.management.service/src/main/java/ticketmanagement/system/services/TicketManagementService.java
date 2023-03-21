package ticketmanagement.system.services;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ticketmanagement.system.exceptions.RouteNotFoundException;
import ticketmanagement.system.exceptions.TicketNotFoundException;
import ticketmanagement.system.repository.TicketRepository;
import ticketmanagement.system.dto.TicketDTO;
import ticketmanagement.system.entity.Route;
import ticketmanagement.system.entity.Ticket;
import ticketmanagement.system.repository.RouteRepository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class TicketManagementService {
    private final RouteRepository routeRepository;
    private final TicketRepository ticketRepository;
    private final WebClient webClient;

    public TicketManagementService(RouteRepository routeRep, TicketRepository ticketRep, WebClient webClient) {
        this.routeRepository = routeRep;
        this.ticketRepository = ticketRep;
        this.webClient = webClient;
    }

    @PostConstruct
    private void fillInTheRoutes() {
        routeRepository.save(new Route("A -> B", new Date(), 100, 1));
        routeRepository.save(new Route("B -> C", new Date(), 200, 10));
        routeRepository.save(new Route("C -> A", new Date(), 300, 10));
    }

    @Transactional
    public long buyTicket(Ticket ticket) {
        ticketRepository.save(ticket);
        return ticket.getId();
    }

    public Ticket ticketInformation(Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isEmpty()) {
            log.error("Not found ticket with id: " + id);
            throw new TicketNotFoundException();
        }
        return ticket.get();
    }

    public UUID paymentRequest(TicketDTO ticketDTO) {
        return webClient.post()
                .uri("/payments")
                .body(Mono.just(ticketDTO), TicketDTO.class)
                .retrieve()
                .bodyToMono(UUID.class).block();
    }

    public String paymentStatusRequest(UUID identifier) {
        return webClient.get()
                .uri("/status/" + identifier.toString())
                .retrieve()
                .bodyToMono(String.class).block();
    }

    public Route findRoute(long id) {
        Optional<Route> route = routeRepository.findById(id);
        if (route.isEmpty()) {
            log.error("Not found route with id: {}", id);
            throw new RouteNotFoundException();
        }
        return route.get();
    }
}
