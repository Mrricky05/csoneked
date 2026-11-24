package hu.unideb.szfm.csoneked.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Rendezvény entitás az SQLite adatbázisban (events tábla)
 */
@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category; // fesztival, koncert, szinhaz, konferencia, sport

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String time; // Pl. "20:00"

    @Column(nullable = false)
    private String location;

    /**
     * Jegyár Hancoin (Hc) egységben
     */
    @Column(nullable = false)
    private Integer price;

    /**
     * Összes elérhető férőhely (csak az adminisztrátornak jelenik meg részletesen)
     */
    @Column(name = "total_capacity", nullable = false)
    private Integer totalCapacity;

    /**
     * Jelenleg még szabadon foglalható helyek száma
     */
    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;

    private String organizer;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @Column(length = 4000)
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.availableSeats == null && this.totalCapacity != null) {
            this.availableSeats = this.totalCapacity;
        }
    }

    // Explicit Getters & Setters
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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
