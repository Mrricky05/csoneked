package hu.unideb.szfm.csoneked.config;

import hu.unideb.szfm.csoneked.model.*;
import hu.unideb.szfm.csoneked.repository.BookingRepository;
import hu.unideb.szfm.csoneked.repository.EventRepository;
import hu.unideb.szfm.csoneked.repository.TransactionRepository;
import hu.unideb.szfm.csoneked.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Kezdő mintaadatok betöltése az SQLite adatbázisba (cso_event.db), ha az még üres.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final BookingRepository bookingRepository;
    private final TransactionRepository transactionRepository;

    @Value("${cso.data.init:true}")
    private boolean initEnabled;

    public DataInitializer(UserRepository userRepository,
                           EventRepository eventRepository,
                           BookingRepository bookingRepository,
                           TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.bookingRepository = bookingRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... args) {
        if (!initEnabled) {
            log.info("Adatbázis inicializálás letiltva a konfigurációban.");
            return;
        }

        if (userRepository.count() == 0) {
            log.info("SQLite adatbázis üres, kezdő mintaadatok betöltése indul...");
            seedData();
            log.info("Kezdő mintaadatok sikeresen betöltve az SQLite adatbázisba (cso_event.db)!");
        } else {
            log.info("SQLite adatbázis már tartalmaz adatokat (felhasználók: {}), inicializálás kihagyva.", userRepository.count());
        }
    }

    public void seedData() {
        // 1. Felhasználók létrehozása
        User admin = User.builder()
                .name("C.S.Ő Főadmin")
                .email("admin@csoneked.hu")
                .password("admin")
                .role(Role.ADMIN)
                .balance(150000L)
                .createdAt(LocalDateTime.now().minusDays(30))
                .build();
        admin = userRepository.save(admin);

        User janos = User.builder()
                .name("Kiss János")
                .email("janos@csoneked.hu")
                .password("password123")
                .role(Role.USER)
                .balance(45000L)
                .createdAt(LocalDateTime.now().minusDays(15))
                .build();
        janos = userRepository.save(janos);

        // 2. Rendezvények létrehozása
        Event e1 = Event.builder()
                .title("Campus Fesztivál 2026")
                .category("fesztival")
                .date(LocalDate.of(2026, 7, 22))
                .time("16:00")
                .location("Debrecen, Nagyerdő")
                .price(28900)
                .totalCapacity(500)
                .availableSeats(142)
                .organizer("Debreceni Campus Nonprofit Kft.")
                .imageUrl("https://images.unsplash.com/photo-1514525253161-7a46d19cd819?auto=format&fit=crop&w=1200&q=80")
                .description("Kelet-Magyarország legnagyobb nyári könnyűzenei és kulturális fesztiválja a debreceni Nagyerdő szívében, a Víztorony tövében. Hazai és nemzetközi sztárfellépők 4 napon át!")
                .createdAt(LocalDateTime.now().minusDays(20))
                .build();

        Event e2 = Event.builder()
                .title("Halott Pénz - Jubileumi Aréna Koncert")
                .category("koncert")
                .date(LocalDate.of(2026, 4, 18))
                .time("20:00")
                .location("Budapest, MVM Dome")
                .price(14900)
                .totalCapacity(350)
                .availableSeats(28)
                .organizer("Csőneked Booking & Live")
                .imageUrl("https://images.unsplash.com/photo-1470225620780-dba8ba36b745?auto=format&fit=crop&w=1200&q=80")
                .description("A Halott Pénz zenekar grandiózus tavaszi nagykoncertje a legújabb slágerekkel, lélegzetelállító látványtechnikával és meglepetés vendégművészekkel.")
                .createdAt(LocalDateTime.now().minusDays(18))
                .build();

        Event e3 = Event.builder()
                .title("A Pál utcai fiúk - Musical")
                .category("szinhaz")
                .date(LocalDate.of(2026, 5, 10))
                .time("19:00")
                .location("Budapest, Vígszínház")
                .price(8900)
                .totalCapacity(200)
                .availableSeats(0) // Betelt rendezvény teszteléshez
                .organizer("Vígszínház Nonprofit Kft.")
                .imageUrl("https://images.unsplash.com/photo-1507676184212-d03ab07a01bf?auto=format&fit=crop&w=1200&q=80")
                .description("Molnár Ferenc halhatatlan regényének színpadi zenés adaptációja Dés László és Geszti Péter fülbemászó dalaival. Egy szívszorító történet a hűségről és a grundról.")
                .createdAt(LocalDateTime.now().minusDays(15))
                .build();

        Event e4 = Event.builder()
                .title("TechInnováció 2026 Konferencia")
                .category("konferencia")
                .date(LocalDate.of(2026, 6, 5))
                .time("09:00")
                .location("Debrecen, Kölcsey Központ")
                .price(19900)
                .totalCapacity(180)
                .availableSeats(95)
                .organizer("Debreceni Informatikai Klaszter")
                .imageUrl("https://images.unsplash.com/photo-1540575467063-178a50c2df87?auto=format&fit=crop&w=1200&q=80")
                .description("Mesterséges intelligencia, felhőtechnológiák és modern szoftverarchitektúrák a régió legfontosabb szakmai konferenciáján. Workshopok és networking lehetőségek.")
                .createdAt(LocalDateTime.now().minusDays(12))
                .build();

        Event e5 = Event.builder()
                .title("Csík Zenekar & Presser Gábor Koncert")
                .category("koncert")
                .date(LocalDate.of(2026, 6, 20))
                .time("19:30")
                .location("Debrecen, Kölcsey Központ")
                .price(11900)
                .totalCapacity(250)
                .availableSeats(110)
                .organizer("Kölcsey Művelődési Központ")
                .imageUrl("https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?auto=format&fit=crop&w=1200&q=80")
                .description("A tradicionális magyar népzene és a modern rock zseniális fúziója egy felejthetetlen közös gálaesten a Nagyerdei Szabadtéri Színpadon.")
                .createdAt(LocalDateTime.now().minusDays(10))
                .build();

        Event e6 = Event.builder()
                .title("Magyar Kupa Döntő 2026")
                .category("sport")
                .date(LocalDate.of(2026, 5, 24))
                .time("18:00")
                .location("Budapest, Puskás Aréna")
                .price(6500)
                .totalCapacity(400)
                .availableSeats(210)
                .organizer("Magyar Labdarúgó Szövetség (MLSZ)")
                .imageUrl("https://images.unsplash.com/photo-1508098682722-e99c43a406b2?auto=format&fit=crop&w=1200&q=80")
                .description("A labdarúgó Magyar Kupa döntő összecsapása az ország legmodernebb stadionjában. Hatalmas szurkolótábor és vérbeli kupahangulat.")
                .createdAt(LocalDateTime.now().minusDays(8))
                .build();

        e1 = eventRepository.save(e1);
        e2 = eventRepository.save(e2);
        e3 = eventRepository.save(e3);
        e4 = eventRepository.save(e4);
        e5 = eventRepository.save(e5);
        e6 = eventRepository.save(e6);

        // 3. Kiss János kezdeti foglalásai
        Booking b1 = Booking.builder()
                .bookingCode("CSO-2026-CAMPUS-771")
                .event(e1)
                .user(janos)
                .userName(janos.getName())
                .userEmail(janos.getEmail())
                .ticketCount(2)
                .unitPrice(e1.getPrice())
                .totalPrice(2 * e1.getPrice())
                .bookingDate(LocalDateTime.now().minusDays(4))
                .status(BookingStatus.CONFIRMED)
                .build();
        bookingRepository.save(b1);

        Booking b2 = Booking.builder()
                .bookingCode("CSO-2026-HALOTT-304")
                .event(e2)
                .user(janos)
                .userName(janos.getName())
                .userEmail(janos.getEmail())
                .ticketCount(1)
                .unitPrice(e2.getPrice())
                .totalPrice(e2.getPrice())
                .bookingDate(LocalDateTime.now().minusDays(2))
                .status(BookingStatus.CONFIRMED)
                .build();
        bookingRepository.save(b2);

        // 4. Tranzakciók
        Transaction t1 = Transaction.builder()
                .user(janos)
                .type(TransactionType.TOPUP)
                .amount(120000L)
                .description("Koncert & Fesztivál csomag feltöltés")
                .date(LocalDateTime.now().minusDays(5))
                .build();
        transactionRepository.save(t1);

        Transaction t2 = Transaction.builder()
                .user(janos)
                .type(TransactionType.PURCHASE)
                .amount(-57800L)
                .description("Jegyvásárlás: Campus Fesztivál 2026 (2 db)")
                .date(LocalDateTime.now().minusDays(4))
                .build();
        transactionRepository.save(t2);

        Transaction t3 = Transaction.builder()
                .user(janos)
                .type(TransactionType.PURCHASE)
                .amount(-14900L)
                .description("Jegyvásárlás: Halott Pénz Aréna Koncert (1 db)")
                .date(LocalDateTime.now().minusDays(2))
                .build();
        transactionRepository.save(t3);
    }
}
