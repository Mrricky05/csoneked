package hu.unideb.szfm.csoneked.dto;

import hu.unideb.szfm.csoneked.model.Event;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {
    private Long id;

    @NotBlank(message = "A cím kötelező!")
    private String title;

    @NotBlank(message = "A kategória kötelező!")
    private String category;

    @NotNull(message = "A dátum kötelező!")
    private LocalDate date;

    @NotBlank(message = "Az időpont kötelező!")
    private String time;

    @NotBlank(message = "A helyszín kötelező!")
    private String location;

    @NotNull(message = "A jegyár kötelező!")
    @Min(value = 0, message = "A jegyár nem lehet negatív!")
    private Integer price; // Hancoin (Hc)

    @NotNull(message = "A férőhely kapacitás kötelező!")
    @Min(value = 1, message = "Legalább 1 férőhely szükséges!")
    private Integer totalCapacity;

    private Integer availableSeats;

    private String organizer;
    private String imageUrl;
    private String description;

    public static EventDto fromEntity(Event e) {
        if (e == null) return null;
        return EventDto.builder()
                .id(e.getId())
                .title(e.getTitle())
                .category(e.getCategory())
                .date(e.getDate())
                .time(e.getTime())
                .location(e.getLocation())
                .price(e.getPrice())
                .totalCapacity(e.getTotalCapacity())
                .availableSeats(e.getAvailableSeats())
                .organizer(e.getOrganizer())
                .imageUrl(e.getImageUrl())
                .description(e.getDescription())
                .build();
    }

    // Explicit Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }

    public Integer getTotalCapacity() { return totalCapacity; }
    public void setTotalCapacity(Integer totalCapacity) { this.totalCapacity = totalCapacity; }

    public Integer getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }

    public String getOrganizer() { return organizer; }
    public void setOrganizer(String organizer) { this.organizer = organizer; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
