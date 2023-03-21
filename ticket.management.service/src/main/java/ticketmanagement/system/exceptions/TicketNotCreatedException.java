package ticketmanagement.system.exceptions;

public class TicketNotCreatedException extends RuntimeException {
    public TicketNotCreatedException(String msg) {
        super(msg);
    }
}
