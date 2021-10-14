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
public class InterventionDTO {

    String id;
    TimePeriod<LocalDateTime> dateTime;
    String clinicRoomID;
    String clinicRoomName;
    String doctorID;
    String doctorName;
    String interventionTypeID;
    String interventionTypeName;
    double interventionTypePrice;
    String patientID;
    String clinicID;
    String clinicName;
    double price;

    @Override
    public String toString() {
        return "InterventionDTO";
    }

}
