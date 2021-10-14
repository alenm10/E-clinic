package mrs.eclinicapi.controller;


import mrs.eclinicapi.dto.AuthenticationRequest;
import mrs.eclinicapi.dto.TokenResponse;
import mrs.eclinicapi.dto.UnregisteredUserDTO;
import mrs.eclinicapi.model.*;
import mrs.eclinicapi.model.enums.UserType;
import mrs.eclinicapi.security.TokenUtils;
import mrs.eclinicapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {
    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private UnregisteredUserService unregisteredUserService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        if(user.getType().equals(UserType.Patient)) {
            Patient p = patientService.getByUserId(user.getId());
            if(p.getMedicalRecord() == null) return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }
        String jwt = tokenUtils.generateToken(user.getUsername());
        Clinic clinic = clinicService.findByUser(user.getId());

        return ResponseEntity.ok(new TokenResponse(jwt, user, clinic));
    }

    @GetMapping("/exists/{email}")
    public ResponseEntity<String> getUser(@PathVariable String email) {
        try {
            User u = (User) userDetailsService.loadUserByUsername(email);
            return new ResponseEntity<>(u.getName(), HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UnregisteredUserDTO> addUser(@RequestBody UnregisteredUserDTO userRequestDTO) {
        UnregisteredUser userRequest = this.convertUnregisteredToEntity(userRequestDTO);
        User existUser = (User) userDetailsService.loadUserByUsername(userRequest.getUser().getEmail());
        if (existUser != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        UnregisteredUser user;
        try {
            user = this.unregisteredUserService.addUnregisteredUser(userRequest);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(this.convertUnregisteredToDTO(user), HttpStatus.CREATED);
    }

    @GetMapping(path = "/confirmRegistration/{token}")
    public ResponseEntity<String> confirmRegistration(@PathVariable String token) {
        VerificationToken foundToken = unregisteredUserService.findToken(token);
        if (foundToken == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (foundToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String requestId = foundToken.getUser().getId();
        User user = foundToken.getUser().getUser();
        unregisteredUserService.deleteById(requestId);
        Patient p = new Patient();
        p.setUser(user);
        user.setType(UserType.Patient);

        patientService.addPatient(p);
        unregisteredUserService.deleteTokenById(foundToken.getId());

        return new ResponseEntity<>(user.getName(), HttpStatus.OK);
    }

    @PutMapping(value = "/changepassword/{id}")
    @PreAuthorize("!hasRole('unregisteredUser')")
    public ResponseEntity<TokenResponse> changePassword(@RequestBody PasswordChanger passwordChanger, @PathVariable String id) {
        try {
            User added = userService.changePassword(id, passwordChanger.oldPassword, passwordChanger.newPassword,
                    passwordChanger.personal);
            String jwt = tokenUtils.generateToken(added.getUsername());
            Clinic clinic = clinicService.findByUser(added.getId());
            System.out.println(added.getLastPasswordResetDate());
            return ResponseEntity.ok(new TokenResponse(jwt, added, clinic));
        } catch (UserService.UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


    }

    static class PasswordChanger {
        public String oldPassword;
        public String newPassword;
        public boolean personal;
    }

    public UnregisteredUser convertUnregisteredToEntity(UnregisteredUserDTO unregisteredUserDTO) {
        UnregisteredUser uuser;
        if(unregisteredUserDTO.getId() == null) {
            uuser = new UnregisteredUser();
        } else {
            uuser = unregisteredUserService.findById(unregisteredUserDTO.getId());
        }
        uuser.setUser(unregisteredUserDTO.getUser());
        uuser.setEmailSent(unregisteredUserDTO.isEmailSent());
        return uuser;
    }

    public UnregisteredUserDTO convertUnregisteredToDTO(UnregisteredUser unregisteredUser) {
        return new UnregisteredUserDTO(
                unregisteredUser.getId(),
                unregisteredUser.getUser(),
                unregisteredUser.isEmailSent()
        );
    }

}
