package mrs.eclinicapi;

import mrs.eclinicapi.model.ClinicalCentarAdministrator;
import mrs.eclinicapi.model.User;
import mrs.eclinicapi.model.enums.UserType;
import mrs.eclinicapi.repository.ClinicalCentarAdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    private ClinicalCentarAdministratorRepository repository;

    @Autowired
    public DataLoader(ClinicalCentarAdministratorRepository repository) {
        this.repository = repository;
    }

    public void run(ApplicationArguments args) {
        if (!repository.findAll().isEmpty()) return;
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        User ccadmin = new User
                (
                        "admin@eclinic.com",
                        encoder.encode("admin"),
                        "Admin",
                        "Admin",
                        UserType.clinicCentarAdministrator,
                        "0628401983",
                        "Rifata Burdzevica Trsa",
                        "Tutin",
                        "Srbija",
                        "JBMG"
                );
        ClinicalCentarAdministrator cca = new ClinicalCentarAdministrator();
        cca.setUser(ccadmin);
        repository.save(cca);
    }
}
