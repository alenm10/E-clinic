package mrs.eclinicapi.controller;

import mrs.eclinicapi.EClinicApiApplication;
import mrs.eclinicapi.dto.EmailEvent;
import mrs.eclinicapi.model.UnregisteredUser;
import mrs.eclinicapi.service.UnregisteredUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "api/unregisteredusers")
public class UnregisteredUserController {

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    private UnregisteredUserService service;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UnregisteredUser>> getUUsers() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @PutMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UnregisteredUser> sendEmail(@PathVariable String id, HttpServletRequest request) {
        UnregisteredUser unregisteredUser = service.findById(id);
        if (unregisteredUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String token = UUID.randomUUID().toString();
        if (service.tokenExists(unregisteredUser)) {
            service.deleteToken(unregisteredUser);
        }
        service.createVerificationToken(unregisteredUser, token);
        String url = EClinicApiApplication.APP_URL;

        if(url.contains("local")) {
            url += ":" + request.getServerPort();
        }
        String confirmationUrl = url + "/confirmRegistration/" + token;
        String content = unregisteredUser.getUser().getName() + ", verify your account for eClinic" +
                "\r\n" + confirmationUrl +
                "\r\n\r\nThis link will be active for only 24 hours.";
        String[] sendTo = {unregisteredUser.getUser().getEmail()};
        eventPublisher.publishEvent(new EmailEvent(unregisteredUser, "Confirm registration", content, sendTo));
        unregisteredUser.setEmailSent(true);
        service.save(unregisteredUser);
        return new ResponseEntity<>(unregisteredUser, HttpStatus.OK);
    }

    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteRequest(@PathVariable String id) {
        UnregisteredUser found = service.findById(id);

        if (found == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String content = found.getUser().getName() + ", we are sorry to inform you that your" +
                " registration request has been denied. \r\n\r\nYou can try to send another request.";
        String[] sendTo = {found.getUser().getEmail()};
        eventPublisher.publishEvent(new EmailEvent(found, "Registration request denied", content, sendTo));

        service.deleteById(id);
        return new ResponseEntity<>(found.getId(), HttpStatus.OK);

    }

}
