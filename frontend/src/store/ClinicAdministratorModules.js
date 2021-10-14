import ListClinicAdminModule from "./ClinicAdministrator/ListClinicAdminModule";

const ClinicAdministratorModules = {
    namespaced: true,
    modules: {
        readClinicAdmins: ListClinicAdminModule
    }
};

export default ClinicAdministratorModules;
