package mrs.eclinicapi.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mrs.eclinicapi.model.TimePeriod;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class OneClickAppointmentDTO {

    private String id;
    private TimePeriod<LocalDateTime> dateTime;
    private String typeID;
    private String typeName;
    private double price;
    private String clinicID;
    private String doctorID;
    private String doctorFirstName;
    private String doctorLastName;
    private String clinicRoomID;
    private String clinicRoomName;

    public static class ConcurrentRequest extends Exception {

    }
}
