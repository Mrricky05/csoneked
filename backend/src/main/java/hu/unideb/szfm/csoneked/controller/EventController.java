package hu.unideb.szfm.csoneked.controller;

import hu.unideb.szfm.csoneked.dto.ApiResponse;
import hu.unideb.szfm.csoneked.dto.EventDto;
import hu.unideb.szfm.csoneked.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EventDto>>> getAllEvents(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location,
            @RequestParam(required = false, defaultValue = "date-asc") String sort) {
        List<EventDto> events = eventService.getAllEvents(q, category, location, sort);
        return ResponseEntity.ok(ApiResponse.ok(events));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventDto>> getEventById(@PathVariable Long id) {
        EventDto event = eventService.getEventById(id);
        return ResponseEntity.ok(ApiResponse.ok(event));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EventDto>> createEvent(@Valid @RequestBody EventDto dto) {
        EventDto created = eventService.createEvent(dto);
        return ResponseEntity.ok(ApiResponse.ok("Rendezvény sikeresen létrehozva!", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EventDto>> updateEvent(@PathVariable Long id, @Valid @RequestBody EventDto dto) {
        EventDto updated = eventService.updateEvent(id, dto);
        return ResponseEntity.ok(ApiResponse.ok("Rendezvény sikeresen frissítve!", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok(ApiResponse.ok("Rendezvény sikeresen törölve!", null));
    }
}
