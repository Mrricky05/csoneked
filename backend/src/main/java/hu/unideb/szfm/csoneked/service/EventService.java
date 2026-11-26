package hu.unideb.szfm.csoneked.service;

import hu.unideb.szfm.csoneked.dto.EventDto;
import hu.unideb.szfm.csoneked.exception.ResourceNotFoundException;
import hu.unideb.szfm.csoneked.model.Event;
import hu.unideb.szfm.csoneked.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Rendezvények kezelése és lekérdezése
 */
@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Rendezvények listázása szűréssel és rendezéssel
     */
    public List<EventDto> getAllEvents(String query, String category, String location, String sort) {
        List<Event> events = eventRepository.findAll();

        return events.stream()
                // Szűrés kulcsszóra (cím, leírás, helyszín, szervező)
                .filter(e -> {
                    if (query == null || query.isBlank()) return true;
                    String q = query.toLowerCase().trim();
                    return (e.getTitle() != null && e.getTitle().toLowerCase().contains(q))
                            || (e.getDescription() != null && e.getDescription().toLowerCase().contains(q))
                            || (e.getLocation() != null && e.getLocation().toLowerCase().contains(q))
                            || (e.getOrganizer() != null && e.getOrganizer().toLowerCase().contains(q));
                })
                // Szűrés kategóriára
                .filter(e -> {
                    if (category == null || category.isBlank() || "all".equalsIgnoreCase(category)) return true;
                    return category.equalsIgnoreCase(e.getCategory());
                })
                // Szűrés helyszínre / városra
                .filter(e -> {
                    if (location == null || location.isBlank() || "all".equalsIgnoreCase(location)) return true;
                    return e.getLocation() != null && e.getLocation().toLowerCase().contains(location.toLowerCase().trim());
                })
                // Rendezés
                .sorted((a, b) -> {
                    if ("date-desc".equalsIgnoreCase(sort)) {
                        return b.getDate().compareTo(a.getDate());
                    } else if ("price-asc".equalsIgnoreCase(sort)) {
                        return Integer.compare(a.getPrice(), b.getPrice());
                    } else if ("price-desc".equalsIgnoreCase(sort)) {
                        return Integer.compare(b.getPrice(), a.getPrice());
                    } else {
                        // Alapértelmezett: date-asc
                        return a.getDate().compareTo(b.getDate());
                    }
                })
                .map(EventDto::fromEntity)
                .collect(Collectors.toList());
    }

    public EventDto getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("A keresett rendezvény nem található! ID: " + id));
        return EventDto.fromEntity(event);
    }

    @Transactional
    public EventDto createEvent(EventDto dto) {
        int capacity = dto.getTotalCapacity() != null ? dto.getTotalCapacity() : 100;
        int seats = dto.getAvailableSeats() != null ? dto.getAvailableSeats() : capacity;

        Event newEvent = Event.builder()
                .title(dto.getTitle().trim())
                .category(dto.getCategory().toLowerCase().trim())
                .date(dto.getDate())
                .time(dto.getTime().trim())
                .location(dto.getLocation().trim())
                .price(dto.getPrice())
                .totalCapacity(capacity)
                .availableSeats(seats)
                .organizer(dto.getOrganizer() != null ? dto.getOrganizer().trim() : "Hivatalos Szervező")
                .imageUrl(dto.getImageUrl() != null && !dto.getImageUrl().isBlank() 
                        ? dto.getImageUrl().trim() 
                        : "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?auto=format&fit=crop&w=1200&q=80")
                .description(dto.getDescription() != null ? dto.getDescription().trim() : "")
                .createdAt(LocalDateTime.now())
                .build();

        newEvent = eventRepository.save(newEvent);
        return EventDto.fromEntity(newEvent);
    }

    @Transactional
    public EventDto updateEvent(Long id, EventDto dto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("A rendezvény nem található! ID: " + id));

        event.setTitle(dto.getTitle().trim());
        event.setCategory(dto.getCategory().toLowerCase().trim());
        event.setDate(dto.getDate());
        event.setTime(dto.getTime().trim());
        event.setLocation(dto.getLocation().trim());
        event.setPrice(dto.getPrice());
        event.setOrganizer(dto.getOrganizer());
        event.setImageUrl(dto.getImageUrl());
        event.setDescription(dto.getDescription());

        if (dto.getTotalCapacity() != null && !dto.getTotalCapacity().equals(event.getTotalCapacity())) {
            int diff = dto.getTotalCapacity() - event.getTotalCapacity();
            event.setTotalCapacity(dto.getTotalCapacity());
            event.setAvailableSeats(Math.max(0, event.getAvailableSeats() + diff));
        }

        event = eventRepository.save(event);
        return EventDto.fromEntity(event);
    }

    @Transactional
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("A rendezvény nem található! ID: " + id);
        }
        eventRepository.deleteById(id);
    }
}
