package hu.unideb.szfm.csoneked.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "Az e-mail cím megadása kötelező!")
    @Email(message = "Érvénytelen e-mail formátum!")
    private String email;

    @NotBlank(message = "A jelszó megadása kötelező!")
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
