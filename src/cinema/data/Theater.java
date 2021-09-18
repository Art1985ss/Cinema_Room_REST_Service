package cinema.data;

import cinema.CinemaException;
import cinema.CinemaPasswordException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class Theater {
    private static final int TOTAL_ROWS = 9;
    private static final int TOTAL_COLUMNS = 9;
    private final List<Seat> availableSeats;
    @JsonIgnore
    private final List<SoldTicket> soldTickets = new ArrayList<>();


    public Theater() {
        availableSeats = new ArrayList<>();
        for (int row = 1; row <= TOTAL_ROWS; row++) {
            for (int col = 1; col <= TOTAL_COLUMNS; col++) {
                Seat seat = new Seat(row, col);
                seat.setPrice(row <= 4 ? 10 : 8);
                availableSeats.add(seat);

            }
        }
    }

    public int getTotalRows() {
        return TOTAL_ROWS;
    }

    public int getTotalColumns() {
        return TOTAL_COLUMNS;
    }

    public List<Seat> getAvailableSeats() {
        return availableSeats;
    }

    public SoldTicket purchase(Seat seat) throws CinemaException {
        Seat chosenSeat = availableSeats.stream()
                .filter(s -> s.equals(seat))
                .findAny()
                .orElseThrow(()-> new CinemaException("The number of a row or a column is out of bounds!"));
        if (!chosenSeat.isAvailable()) {
            throw new CinemaException("The ticket has been already purchased!");
        }
        chosenSeat.setAvailable(false);
        SoldTicket soldTicket = new SoldTicket(chosenSeat);
        soldTickets.add(soldTicket);
        return soldTicket;
    }

    public Map<String, Seat> returnTicket(UUID token) throws CinemaException {
        SoldTicket soldTicket = soldTickets.stream()
                .filter(st -> st.getToken().equals(token))
                .findAny()
                .orElseThrow(() -> new CinemaException("Wrong token!"));
        soldTickets.remove(soldTicket);
        soldTicket.getTicket().setAvailable(true);
        return Map.of("returned_ticket", soldTicket.getTicket());
    }

    public Stats getStats(Optional<String> password) throws CinemaPasswordException {
        if (password.isEmpty() || !"super_secret".equals(password.get())) {
            throw new CinemaPasswordException("The password is wrong!");
        }
        int income = soldTickets.stream()
                .map(SoldTicket::getTicket)
                .mapToInt(Seat::getPrice)
                .sum();
        long available = availableSeats.stream()
                .filter(Seat::isAvailable)
                .count();
        int sold = soldTickets.size();
        return new Stats(
                income,
                available,
                sold);
    }
}
