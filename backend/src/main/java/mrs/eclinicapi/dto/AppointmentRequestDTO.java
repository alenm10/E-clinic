package mrs.eclinicapi.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class AppointmentRequestDTO {

    private String id;
    private LocalDateTime dateTime;
    private String interventionTypeID;
    private String clinicID;
    private String doctorID;
    private String patientID;
    private LocalDate dateOfCreation;

    public static class ConcurrentRequest extends Exception {

    }
}
