import ListClinicCenterAdminModule from "./ClinicCenterAdministrator/ListClinicCenterAdminModule";

const ClinicCenterAdministratorModules = {
    namespaced: true,
    modules: {
        clinicCenterAdmins: ListClinicCenterAdminModule
    }
};

export default ClinicCenterAdministratorModules;
