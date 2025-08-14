import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Home from '../pages/Home';
import InspectorDashboard from '../pages/InspectorDashboard';
import OwnerDashboard from '../pages/OwnerDashboard';
import Login from '../components/Auth/Login';
import Register from '../components/Auth/Register';
import ScanPlate from '../components/Inspector/ScanPlate'; // Verify this path
import InspectionHistory from '../components/Inspector/InspectionHistory';
import UploadReport from '../components/Inspector/UploadReport';
import VehicleDetails from '../components/Owner/VehicleDetails';
import BookAppointment from '../components/Owner/BookAppointment';
import ViewReports from '../components/Owner/ViewReports';

const setRoutes = () => {
    return (
        <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/inspector/dashboard" element={<InspectorDashboard />} />
            <Route path="/inspector/scan-plate" element={<ScanPlate />} />
            <Route path="/inspector/inspection-history" element={<InspectionHistory />} />
            <Route path="/inspector/upload-report" element={<UploadReport />} />
            <Route path="/owner/dashboard" element={<OwnerDashboard />} />
            <Route path="/owner/vehicle-details" element={<VehicleDetails />} />
            <Route path="/owner/book-appointment" element={<BookAppointment />} />
            <Route path="/owner/view-reports" element={<ViewReports />} />
        </Routes>
    );
};

export default setRoutes;