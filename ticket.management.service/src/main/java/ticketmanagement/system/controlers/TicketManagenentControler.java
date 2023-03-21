package ticketmanagement.system.controlers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ticketmanagement.system.dto.TicketDTO;
import ticketmanagement.system.entity.Ticket;
import ticketmanagement.system.exceptions.RouteNotFoundException;
import ticketmanagement.system.exceptions.TicketNotCreatedException;
import ticketmanagement.system.exceptions.TicketNotFoundException;
import ticketmanagement.system.exceptions.TicketsRanOutException;
import ticketmanagement.system.services.TicketManagementService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
public class TicketManagenentControler {
    private final TicketManagementService ticketManagementService;

    public TicketManagenentControler(TicketManagementService ticketManagementService) {
        this.ticketManagementService = ticketManagementService;
    }

    @PostMapping("/buy")
    public long buyTicket(@RequestBody @Valid TicketDTO ticketDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessage.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append("; ");
            }
            log.error("User set incorrect data name: {}, surname: {}, routeId: {}",
                    ticketDTO.getName(), ticketDTO.getSurname(), ticketDTO.getRouteId());
            throw new TicketNotCreatedException(errorMessage.toString());
        }
        log.info("Data received from client {}", ticketDTO);
        Ticket ticket = TicketDTOConverter(ticketDTO);
        if (ticket.getRoute().getNumberOfTickets() <= 0) {
            log.error("Impossible to create ticket. Number of tickets on the route {}",
                    ticket.getRoute().getNumberOfTickets());
            throw new TicketsRanOutException();
        }
        ticketDTO.setPaymentSum(ticket.getRoute().getTicketPrice());
        ticket.setPaymentIdentifier(ticketManagementService.paymentRequest(ticketDTO));
        ticket.getRoute().setNumberOfTickets(ticket.getRoute().getNumberOfTickets() - 1);
        ticket.setPaymentStatus(ticketManagementService.paymentStatusRequest(ticket.getPaymentIdentifier()));

        return ticketManagementService.buyTicket(ticket);
    }

    @GetMapping("/information/{id}")
    public String getTicketInformation(@PathVariable long id) {
        log.info("Ticket id received from client. Id: {}", id);
        Ticket ticket = ticketManagementService.ticketInformation(id);
        return String.format("Payment status: %s Route information: %s",
                ticket.getPaymentStatus(), ticket.getRoute());
    }

    @ExceptionHandler(TicketNotFoundException.class)
    private ResponseEntity<String> handlerTicketNotFoundException() {
        return new ResponseEntity<>("A Ticket with this id doesn't exist", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RouteNotFoundException.class)
    private ResponseEntity<String> handlerRouteNotFoundException() {
        return new ResponseEntity<>("A Route with this id doesn't exist", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TicketsRanOutException.class)
    private ResponseEntity<String> handlerTicketsRanOutException() {
        return new ResponseEntity<>("Tickets on this route have run out", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TicketNotCreatedException.class)
    private ResponseEntity<String> handlerTicketNotCreatedException(TicketNotCreatedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private Ticket TicketDTOConverter(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        ticket.setName(ticketDTO.getName());
        ticket.setSurname(ticketDTO.getSurname());
        ticket.setRoute(ticketManagementService.findRoute(ticketDTO.getRouteId()));

        return ticket;
    }
}
