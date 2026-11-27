package hu.unideb.szfm.csoneked.service;

import hu.unideb.szfm.csoneked.dto.LoginRequest;
import hu.unideb.szfm.csoneked.dto.RegisterRequest;
import hu.unideb.szfm.csoneked.dto.UserDto;
import hu.unideb.szfm.csoneked.model.Role;
import hu.unideb.szfm.csoneked.model.Transaction;
import hu.unideb.szfm.csoneked.model.TransactionType;
import hu.unideb.szfm.csoneked.model.User;
import hu.unideb.szfm.csoneked.repository.TransactionRepository;
import hu.unideb.szfm.csoneked.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Felhasználókezelés és autentikációs üzleti logika
 */
@Service
public class AuthService {

    private static final long WELCOME_BONUS_HANCOIN = 15000L;

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public AuthService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Bejelentkezés e-mail és jelszó alapján
     */
    public UserDto login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Nem található fiók ezzel az e-mail címmel!"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Helytelen jelszó! Kérjük, próbáld újra.");
        }

        return UserDto.fromEntity(user);
    }

    /**
     * Új felhasználó regisztrációja kötelező jelszó-megerősítéssel és 15 000 Hc kezdő kredittel
     */
    @Transactional
    public UserDto register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Ezzel az e-mail címmel már regisztráltak fiókot!");
        }

        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new IllegalArgumentException("A megadott jelszó és a megerősítés nem egyezik!");
        }

        if (request.getPassword().length() < 6) {
            throw new IllegalArgumentException("A jelszónak legalább 6 karakter hosszúnak kell lennie!");
        }

        User newUser = User.builder()
                .name(request.getName().trim())
                .email(email)
                .password(request.getPassword())
                .role(Role.USER)
                .balance(WELCOME_BONUS_HANCOIN)
                .createdAt(LocalDateTime.now())
                .build();

        newUser = userRepository.save(newUser);

        // Kezdő jóváírási tranzakció naplózása
        Transaction welcomeTransaction = Transaction.builder()
                .user(newUser)
                .type(TransactionType.TOPUP)
                .amount(WELCOME_BONUS_HANCOIN)
                .description("Regisztrációs ajándék kezdő jóváírás (+15 000 Hc)")
                .date(LocalDateTime.now())
                .build();
        transactionRepository.save(welcomeTransaction);

        return UserDto.fromEntity(newUser);
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("A felhasználó nem található! ID: " + id));
        return UserDto.fromEntity(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }
}
