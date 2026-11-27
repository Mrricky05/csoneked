package hu.unideb.szfm.csoneked.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Regisztrációs kérés kötelező jelszó-megerősítéssel (jelszó, jelszó újra)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "A név megadása kötelező!")
    private String name;

    @NotBlank(message = "Az e-mail cím megadása kötelező!")
    @Email(message = "Érvénytelen e-mail formátum!")
    private String email;

    @NotBlank(message = "A jelszó megadása kötelező!")
    @Size(min = 6, message = "A jelszónak legalább 6 karakter hosszúnak kell lennie!")
    private String password;

    @NotBlank(message = "A jelszó megerősítése kötelező!")
    private String passwordConfirm;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPasswordConfirm() { return passwordConfirm; }
    public void setPasswordConfirm(String passwordConfirm) { this.passwordConfirm = passwordConfirm; }
}
