package mrs.eclinicapi.service;

import mrs.eclinicapi.model.UnregisteredUser;
import mrs.eclinicapi.model.VerificationToken;
import mrs.eclinicapi.repository.TokenRepository;
import mrs.eclinicapi.repository.UnregisteredUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UnregisteredUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UnregisteredUserRepository repository;
    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    public UnregisteredUserService(UnregisteredUserRepository repository) {
        this.repository = repository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UnregisteredUser addUnregisteredUser(UnregisteredUser uu) {
        if(this.repository.existsByUserEmail(uu.getUser().getEmail())) return null;
        uu.getUser().setPassword(passwordEncoder.encode(uu.getUser().getPassword()));
        return repository.save(uu);
    }

    public List<UnregisteredUser> findAll() {
        return repository.findAll();
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public UnregisteredUser findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public VerificationToken createVerificationToken(UnregisteredUser user, String tokenString) {
        VerificationToken token = new VerificationToken(tokenString, user);
        return tokenRepository.save(token);
    }

    @Transactional
    public boolean tokenExists(UnregisteredUser user) {
        return tokenRepository.existsVerificationTokenByUser(user);
    }

    @Transactional
    public void deleteToken(UnregisteredUser user) {
        tokenRepository.deleteVerificationTokenByUser(user);
    }

    @Transactional
    public VerificationToken findToken(String token) {
        return tokenRepository.findVerificationTokenByToken(token);
    }

    @Transactional
    public UnregisteredUser save(UnregisteredUser u) {
        return repository.save(u);
    }

    @Transactional
    public void deleteTokenById(String id) {
        tokenRepository.deleteById(id);
    }
}
