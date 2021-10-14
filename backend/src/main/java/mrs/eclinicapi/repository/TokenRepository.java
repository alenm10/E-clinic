package mrs.eclinicapi.repository;

import mrs.eclinicapi.model.UnregisteredUser;
import mrs.eclinicapi.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<VerificationToken, String> {
    boolean existsVerificationTokenByUser(UnregisteredUser user);

    void deleteVerificationTokenByUser(UnregisteredUser user);

    VerificationToken findVerificationTokenByToken(String token);
}
