export const emptyClinic = {
    name: '',
    description: '',
    address: null,
    coordinates: null
};

export const emptyClinicAdmin = {
    name: '',
    surname: '',
    password: '',
    username: '',
    city : '',
    country : '',
    email : '',
    personalID : '',
    address: '',
    phoneNumber: '',
    clinic : null
};

export const emptyClinicCenterAdmin = {
    name: '',
    surname: '',
    password: '',
    username: '',
    city : '',
    country : '',
    email : '',
    personalID : '',
    address: '',
    phoneNumber: ''
};

export const emptyDisease = {
	id: "",
    name: null
};

export const emptyMedicine = {
    name: null,
    manufacturer: null
};

export const emptyClinicRoom = {
    name: null,
    clinicId: null
};

export const emptyDoctor = {
	id : null,
	email : '',
	password : '',
	name : '',
	surname : '',
	phoneNumber : '',
	address : '',
	city : '',
	country : '',
	personalID : '',
	specialties: [],
    clinicID: null
};

export const emptyNurse = {
    id : null,
    email : '',
    password : '',
    name : '',
    surname : '',
    phoneNumber : '',
    address : '',
    city : '',
    country : '',
    personalID : '',
    clinicID: null
};

export const emptyInterventionType = {
	id : '',
	name : '',
	price : '',
	clinic : ''
};

export const emptyIntervention = {
    id : '',
    dateTime: null,
    selectedClinicRoom: null,
    selectedDoctor: null,
    selectedInterventionType: '',
    clinic : '',
    duration: 0,
    price: 0
};

export const emptyOneClick = {
    dateTime: null,
    clinicRoomID: null,
    doctorID: null,
    typeID: null,
    clinicID : null,
};

export const emptyVacationRequest = {
	id : '',
	user: '',
    clinic: '',
	startDate: null,
    endDate: null,
    status: ''
};

export const emptyMedicalRecord = {
    height: 0,
    weight: 0,
    bloodType: '',
    allergies: [],
    diagnoses: [],
    medicines: [],
    visits: []
}

export const emptyUser = {
    email: '',
    name: '',
    surname: '',
    phoneNumber: '',
    address: '',
    city: '',
    country: '',
    personalID: '',
};

export const clinicSearchRequest = {
    date: null,
    interventionType: null,
    pageNumber: null,
    pageSizer: null
};

export const emailPattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
