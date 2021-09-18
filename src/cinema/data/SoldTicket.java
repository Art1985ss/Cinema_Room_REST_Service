package cinema.data;

import java.util.UUID;

public class SoldTicket {
    private final UUID token;
    private final Seat ticket;

    public SoldTicket(Seat ticket) {
        this.ticket = ticket;
        token = UUID.randomUUID();
    }

    public UUID getToken() {
        return token;
    }

    public Seat getTicket() {
        return ticket;
    }
}
