export interface User {
    id: string;
    username: string;
    password: string;
    role: 'inspector' | 'owner';
}

export interface Vehicle {
    plateNumber: string;
    make: string;
    model: string; 
    year: number;
    ownerId: string;
}

export interface InspectionReport {
    id: string;
    vehicleId: string;
    inspectorId: string;
    date: string;
    details: string;
    status: 'pending' | 'completed';
}

export interface Appointment {
    id: string;
    vehicleId: string;
    ownerId: string;
    date: string;
    time: string;
    status: 'scheduled' | 'completed' | 'canceled';
}