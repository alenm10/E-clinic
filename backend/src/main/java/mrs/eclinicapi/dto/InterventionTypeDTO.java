package mrs.eclinicapi.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class InterventionTypeDTO {

    String id;
    String name;
    String clinic;
    double price;

    @Override
    public String toString() {
        return "InterventionTypeDTO [id=" + id + ", name=" + name + ", clinic=" + clinic + ", price=" + price + "]";
    }
}
