package mrs.eclinicapi.service;

import mrs.eclinicapi.dto.UserDTO;
import mrs.eclinicapi.model.User;
import mrs.eclinicapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;


    public User addUser(User newUser) {
        return repository.save(newUser);
    }

    public User findOne(String id) {
        if (id == null) return null;
        return repository.findById(id).orElse(null);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public void deleteById(String id) {
        try {
            repository.deleteById(id);
        } catch (Exception NullPointerException) {
            // pa metni mu nesto odje
        }
    }

    @Transactional
    public User updateUser(String id, UserDTO u) {
        User toModify = repository.findById(id).orElse(null);
        if (toModify == null) return null;
        toModify.setAddress(u.getAddress());
        toModify.setCity(u.getCity());
        toModify.setCountry(u.getCountry());
        toModify.setName(u.getName());
        toModify.setSurname(u.getSurname());
        toModify.setPhoneNumber(u.getPhoneNumber());
        return repository.save(toModify);
    }

    @Transactional
    public User changePassword(String id, String oldPassword, String newPassword, boolean personal) throws UserNotFoundException, BadCredentialsException {
        User found = repository.findById(id).orElseThrow(UserNotFoundException::new);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(found.getEmail(), oldPassword));

        found.setPassword(passwordEncoder.encode(newPassword));

        User saved = repository.save(found);
        if (personal) {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(saved.getEmail(),
                            newPassword));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return saved;
    }

    public static class UserNotFoundException extends Exception {

    }

}
