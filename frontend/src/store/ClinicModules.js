import AddClinicModule from "./Clinics/AddClinicModule";
import ListClinicsModule from "./Clinics/ListClinicsModule";
import VacationRequest from "./Clinics/VacationRequest";

const ClinicModules = {
    namespaced: true,
    modules: {
        addClinic: AddClinicModule,
        readClinics: ListClinicsModule,
        vacationRequest: VacationRequest
    }
};

export default ClinicModules;
