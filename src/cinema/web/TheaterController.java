package cinema.web;

import cinema.CinemaException;
import cinema.CinemaPasswordException;
import cinema.data.Seat;
import cinema.data.Stats;
import cinema.data.Theater;
import cinema.data.SoldTicket;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
public class TheaterController {
    private final Theater theater = new Theater();

    @GetMapping("/seats")
    public ResponseEntity<Theater> getTheater() {
        return ResponseEntity.ok(theater);
    }

    @PostMapping("/purchase")
    public ResponseEntity<SoldTicket> purchase(@RequestBody Seat seat) {
        return ResponseEntity.ok(theater.purchase(seat));
    }

    @PostMapping("/return")
    public ResponseEntity<Map<String, Seat>> returnTicket(@RequestBody Map<String, UUID> token) {
        return ResponseEntity.ok(theater.returnTicket(token.get("token")));
    }

    @PostMapping("/stats")
    public ResponseEntity<Stats> getStats(@RequestParam Optional<String> password) {
        return ResponseEntity.ok(theater.getStats(password));
    }

    @ExceptionHandler({CinemaException.class, CinemaPasswordException.class})
    public ResponseEntity<Map<String, String>> handler(RuntimeException exception) {
        Map<String, String> exceptionMap = new HashMap<>();
        exceptionMap.put("error", exception.getMessage());
        ResponseEntity.BodyBuilder bodyBuilder;
        if (exception instanceof CinemaException) {
            bodyBuilder = ResponseEntity.status(HttpStatus.BAD_REQUEST);
        } else {
            bodyBuilder = ResponseEntity.status(HttpStatus.UNAUTHORIZED);
        }
        return bodyBuilder.body(exceptionMap);
    }
}
