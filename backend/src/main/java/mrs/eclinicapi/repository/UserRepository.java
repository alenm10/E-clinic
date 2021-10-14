package mrs.eclinicapi.repository;

import mrs.eclinicapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE User SET email = ?2 WHERE id = ?1")
    void updateEmail(String id, String newEmail);

    @Modifying
    @Query("UPDATE User SET password = ?2 WHERE id = ?1")
    void updatePassword(String id, String newPassword);

    @Modifying
    @Query("UPDATE User SET name = ?2 WHERE id = ?1")
    void updateName(String id, String newName);

}
