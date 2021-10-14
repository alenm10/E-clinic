package mrs.eclinicapi.model.enums;

public enum UserType {
    unregisteredUser,
    clinicCentarAdministrator,
    clinicAdministrator,
    doctor,
    nurse,
    Patient;

    public static UserType fromInteger(int x) {
        switch (x) {
            case 0:
                return unregisteredUser;
            case 1:
                return clinicCentarAdministrator;
            case 2:
                return clinicAdministrator;
            case 3:
                return doctor;
            case 4:
                return nurse;
            case 5:
                return Patient;
        }
        return null;
    }
}
