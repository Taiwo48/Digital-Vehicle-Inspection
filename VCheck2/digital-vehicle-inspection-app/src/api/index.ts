import axios from 'axios';

const API_BASE_URL = 'https://api.yourservice.com'; // Replace with your actual API base URL

// Types (you can expand these as needed)
interface Credentials {
    email: string;
    password: string;
}

interface UserData {
    name: string;
    email: string;
    password: string;
    // add other fields as needed
}

interface ReportData {
    // define report fields
}

interface AppointmentData {
    // define appointment fields
}

// Authentication
export const login = async (credentials: Credentials) => {
    const response = await axios.post(`${API_BASE_URL}/auth/login`, credentials);
    return response.data;
};

export const register = async (userData: UserData) => {
    const response = await axios.post(`${API_BASE_URL}/auth/register`, userData);
    return response.data;
};

// Vehicle Verification
export const verifyVehicle = async (plateNumber: string) => {
    const response = await axios.get(`${API_BASE_URL}/vehicles/verify/${plateNumber}`);
    return response.data;
};

// Inspection Reports
export const uploadReport = async (reportData: ReportData) => {
    const response = await axios.post(`${API_BASE_URL}/reports/upload`, reportData);
    return response.data;
};

export const getInspectionHistory = async (inspectorId: string) => {
    const response = await axios.get(`${API_BASE_URL}/inspections/history/${inspectorId}`);
    return response.data;
};

// Vehicle Owner Functions
export const getVehicleDetails = async (vehicleId: string) => {
    const response = await axios.get(`${API_BASE_URL}/vehicles/${vehicleId}`);
    return response.data;
};

export const bookAppointment = async (appointmentData: AppointmentData) => {
    const response = await axios.post(`${API_BASE_URL}/appointments/book`, appointmentData);
    return response.data;
};

export const getReports = async (ownerId: string) => {
    const response = await axios.get(`${API_BASE_URL}/reports/${ownerId}`);
    return response.data;
};