import Vue from 'vue'
import Vuex from 'vuex'
import ClinicModules from "./ClinicModules";
import ClinicAdministratorModules from "./ClinicAdministratorModules";
import ClinicRoomModules from "./ClinicRoomModules";
import DoctorModules from "./DoctorModules";
import NurseModules from "./NurseModules";
import InterventionTypeModule from "./InterventionTypeModule";
import InterventionModule from "./InterventionModule";
import AuthModule from "./AuthModule";
import MedicinesModules from "./MedicinesModules";
import DiseaseModules from "./DiseaseModules";
import UnregisteredUsersModule from "./UnregisteredUsersModules";
import ProfileModule from "./ProfileModule";
import PatientsModule from "./PatientsModule";
import ClinicCenterAdministratorModules from "./ClinicCenterAdministratorModule";
import AppointmentRequestModule from "./AppointmentRequestModule";
import OneClickAppointments from "./OneClickAppointments";
import MedicalRecord from "./MedicalRecord";
import Visit from "./VisitModules";
Vue.use(Vuex);

export default new Vuex.Store({
  namespaced: true,
  modules: {
    clinics: ClinicModules,
    clinicAdmins: ClinicAdministratorModules,
    clinicRooms: ClinicRoomModules,
    clinicCenterAdmins: ClinicCenterAdministratorModules,
    medicines : MedicinesModules,
    doctor: DoctorModules,
    nurse: NurseModules,
    auth: AuthModule,
    visit:Visit,
    disease: DiseaseModules,
    unregisteredUsers: UnregisteredUsersModule,
    interventionType : InterventionTypeModule,
    intervention : InterventionModule,
    profile: ProfileModule,
    patients: PatientsModule,
    appointmentRequests: AppointmentRequestModule,
    oneClicks: OneClickAppointments,
    medicalRecord: MedicalRecord
  }
})
