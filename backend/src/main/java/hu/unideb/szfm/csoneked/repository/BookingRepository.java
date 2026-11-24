package hu.unideb.szfm.csoneked.repository;

import hu.unideb.szfm.csoneked.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserIdOrderByBookingDateDesc(Long userId);
    Optional<Booking> findByBookingCode(String bookingCode);
    List<Booking> findAllByOrderByBookingDateDesc();
}
