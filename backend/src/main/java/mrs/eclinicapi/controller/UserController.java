package mrs.eclinicapi.controller;

import mrs.eclinicapi.dto.UserDTO;
import mrs.eclinicapi.model.User;
import mrs.eclinicapi.model.enums.UserType;
import mrs.eclinicapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/user")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping()
    public User addUser(@RequestParam String email,
                        @RequestParam String password,
                        @RequestParam String name,
                        @RequestParam int type) {
        User newUser = new User(email, password, name, UserType.fromInteger(type));
        service.addUser(newUser);

        return newUser;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") String id) {

        User user = service.findOne(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<User>> getAllUsers() {

        List<User> users = service.findAll();

        if (users == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
        User user = service.findOne(id);
        if (user == null) {
            return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
        }

        service.deleteById(id);
        return new ResponseEntity<>("deleted user", HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<User> modifyUser(@PathVariable String id, @RequestBody UserDTO modified) {
        User updated;
        try {
            updated = service.updateUser(id, modified);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (updated == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
}
