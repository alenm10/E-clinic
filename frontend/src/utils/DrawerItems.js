import {
    clinicAdministratorsPath,
    clinicRoomsPath,
    clinicsPath,
    homePath,
    medicinesPath,
    unregisteredUsersPath,
    doctorsPath,
    nursesPath,
    clinicDetailsPath,
    interventionTypePath,
    interventionPath,
    patientsPath,
    vacationRequestPath,
    clinicCenterAdministratorsPath,
    workingSchedulePath,
    appointmentRequestClinicPath,
    appointmentRequestPatientPath,
    oneClickAppointmentPath,
    diseasePath,
    historyPath,
    medicalRecordPath,
    medicalPrescriptionPath,
    reportPath
} from "./paths";

function doctorsItem(clinicID) {
    return {
        icon: 'mdi-account-tie',
        label: 'Doctors',
        path: doctorsPath.replace(":clinicID", clinicID)
    }
}

function medicalRecordItem(userID) {
    return {
        icon: 'mdi-view-list',
        label: 'Medical record',
        path: medicalRecordPath.replace(":id", userID)
    }
}

function oneClickAppointmentItem(clinicID) {
    return {
        icon: 'mdi-calendar-export',
        label: 'One click appointments',
        path: oneClickAppointmentPath.replace(":clinicID", clinicID)
    }
}

const nursesItem = {
    icon: 'mdi-account-tie',
    label: 'Nurses',
    path: nursesPath
};

const homeItem = {
    icon: 'mdi-home',
    label: 'Home',
    path: homePath
};

function clinicDetailsItem(id) {
    return {
        icon: 'mdi-home',
        label: 'Clinic',
        path: clinicDetailsPath.replace(":id", id)
    };
}

const interventionTypeItem = {
    icon: 'mdi-currency-eur',
    label: 'Intervention types and pricing',
    path: interventionTypePath
};

const interventionItem = {
    icon: 'mdi-home',
    label: 'Interventions',
    path: interventionPath
};

function appointmentRequestClinicItem(clinicID) {
    return {
        icon: 'mdi-calendar',
        label: 'Requests',
        path: appointmentRequestClinicPath.replace(':clinicID', clinicID)
    };
}

function appointmentRequestPatientItem(patientID) {
    return {
        icon: 'mdi-calendar',
        label: 'Requests',
        path: appointmentRequestPatientPath.replace(':patientID', patientID)
    };
}

const clinicsItem = {
    icon: 'mdi-hospital-building',
    label: 'Clinics',
    path: clinicsPath
};

const medicinesItem = {
    icon: 'mdi-pill',
    label: 'Medicine',
    path: medicinesPath
};

const diseaseItem = {
    icon: 'mdi-pill',
    label: 'Diagnosis',
    path: diseasePath
};

const clinicAdminsItem = {
    icon: 'mdi-account-tie',
    label: 'Clinic Administrators',
    path: clinicAdministratorsPath
};

const clinicCenterAdminsItem = {
    icon: 'mdi-account-tie',
    label: 'Clinic Center Administrators',
    path: clinicCenterAdministratorsPath
};

function clinicRoomsItem(id) {
    return {
        icon: 'mdi-bed-empty',
        label: 'Clinic Rooms',
        path: clinicRoomsPath.replace(":clinicID", id)
    };
}

const unregisteredUsersItem = {
    icon: 'mdi-account-multiple-check',
    label: 'User requests',
    path: unregisteredUsersPath,
};

const historyItem = {
	icon: 'mdi-history',
    label: 'History',
    path: historyPath,
};

const patientsItem = {
    icon: 'mdi-account-heart',
    label: 'Patients',
    path: patientsPath,
};


const vacationRequestItem = {
	icon: 'mdi-calendar',
	label: 'Vacation request',
    path: vacationRequestPath,
};

const workingScheduleItem = {
    icon: 'mdi-calendar',
    label: 'Working Schedule',
    path: workingSchedulePath
};

const medicalPrescriptionItem = {
    icon: 'mdi-format-page-break',
    label: 'Medical Prescription',
    path: medicalPrescriptionPath
};

const reportItem = {
    icon: 'mdi-cash',
    label: 'Report',
    path: reportPath
};

const ClinicalCenterAdmin = {
    name: 'Clinical Center Admin',
    code: 'clinicCentarAdministrator',
    items: [
        homeItem,
        clinicCenterAdminsItem,
        clinicsItem,
        diseaseItem,
        medicinesItem,
        clinicAdminsItem,
        patientsItem,
        unregisteredUsersItem
    ],
};

class ClinicalCenterAdminClass {
    static name = 'Clinical Center Admin';
    static code = 'clinicCentarAdministrator';
    constructor() {
        this.items = [
            homeItem,
            clinicCenterAdminsItem,
            clinicsItem,
            diseaseItem,
            medicinesItem,
            clinicAdminsItem,
            patientsItem,
            unregisteredUsersItem
        ]
    }
};


class ClinicalAdmin {
    static name = 'Clinical Admin';
    static code = 'clinicAdministrator';
    constructor(id) {
        this.items = [
            clinicDetailsItem(id),
            reportItem,
            interventionTypeItem,
            clinicRoomsItem(id),
            doctorsItem(id),
            nursesItem,
            vacationRequestItem,
            appointmentRequestClinicItem(id),
            oneClickAppointmentItem(id)
        ]
    }
}


class Patient {
    static name = 'Patient';
    static code = 'Patient';
    constructor(patientID, userID) {
        this.items = [
            homeItem,
            clinicsItem,
            interventionTypeItem,
            appointmentRequestPatientItem(patientID),
            medicalRecordItem(userID),
            historyItem
        ]
    }
}

class Doctor {
	static name = 'Doctor';
    static code = 'doctor';
    constructor() {
        this.items = [
            homeItem,
            workingScheduleItem,
            vacationRequestItem,
            patientsItem
        ]
    }
}
class Nurse {
    static name = 'Nurse';
    static code = 'nurse';
    constructor() {
        this.items = [
            homeItem,
            patientsItem,
            workingScheduleItem,
            vacationRequestItem,
            medicalPrescriptionItem

        ]
    }
}
export {ClinicalCenterAdmin, ClinicalCenterAdminClass, ClinicalAdmin, Patient, Doctor, Nurse};
