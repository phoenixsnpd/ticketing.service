package ticketmanagement.system.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ticketmanagement.system.entity.Route;
import ticketmanagement.system.entity.Ticket;
import ticketmanagement.system.repository.RouteRepository;
import ticketmanagement.system.services.TicketManagementService;

import java.util.Optional;


@ExtendWith(SpringExtension.class)
class TicketManagementServiceTest {

    @MockBean
    private TicketManagementService ticketManagementService;

    @MockBean
    private RouteRepository routeRepository;

    @Test
    void shouldSaveTicketAndGetId() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        Mockito.doReturn(1L)
                .when(ticketManagementService)
                .buyTicket(ticket);
        long checkId = ticketManagementService.buyTicket(ticket);
        Mockito.verify(ticketManagementService, Mockito.times(1)).buyTicket(ticket);
        Assertions.assertEquals(ticket.getId(), checkId);
    }

    @Test
    void shouldReturnTicketById() {
        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setName("Name");
        ticket.setSurname("Surname");
        Mockito.doReturn(ticket)
                .when(ticketManagementService)
                .ticketInformation(1L);
        Ticket checkTicket = ticketManagementService.ticketInformation(1L);
        Assertions.assertEquals(ticket.getId(), checkTicket.getId());
        Assertions.assertEquals(ticket.getName(), checkTicket.getName());
        Assertions.assertEquals(ticket.getSurname(), checkTicket.getSurname());
    }

    @Test
    void shouldReturnRouteById() {
        Route route = new Route();
        route.setId(1L);
        Mockito.doReturn(Optional.of(route))
                .when(routeRepository)
                .findById(1L);
        Route checkroute = routeRepository.findById(1L).get();
        Assertions.assertEquals(route.getId(), checkroute.getId());
    }
}