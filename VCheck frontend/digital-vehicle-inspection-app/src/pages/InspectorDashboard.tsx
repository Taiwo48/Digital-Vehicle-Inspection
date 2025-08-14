import React, { useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import ScanPlate from '../components/Inspector/ScanPlate';
import InspectionHistory from '../components/Inspector/InspectionHistory';
import UploadReport from '../components/Inspector/UploadReport';
import './InspectorDashboard.css';

const InspectorDashboard: React.FC = () => {
    const historyRef = useRef<HTMLElement>(null);
    const reportRef = useRef<HTMLElement>(null);
    const navigate = useNavigate();

    const scrollToSection = (ref: React.RefObject<HTMLElement>) => {
        ref.current?.scrollIntoView({ behavior: 'smooth' });
    };

    const handleLogout = () => {
        // Clear authentication data
        localStorage.removeItem('authToken');
        localStorage.removeItem('userRole');
        localStorage.removeItem('isAuthenticated');
        
        // Redirect to login page
        navigate('/login');
    };

    return (
        <div className="inspector-dashboard">
            <header className="dashboard-header">
                <div className="header-content">
                    <h1>Inspector Dashboard</h1>
                    <div className="user-profile">
                        <span className="welcome-message">Welcome back, Inspector</span>
                        <div className="avatar">I</div>
                        <button onClick={handleLogout} className="logout-btn">
                            Logout
                        </button>
                    </div>
                </div>
                <nav className="dashboard-nav">
                    <button className="nav-btn active">Scan Plate</button>
                    <button className="nav-btn" onClick={() => scrollToSection(historyRef)}>History</button>
                    <button className="nav-btn" onClick={() => scrollToSection(reportRef)}>Reports</button>
                </nav>
            </header>

            <div className="dashboard-content">
                <section className="scan-section">
                    <ScanPlate />
                </section>

                <section className="history-section" ref={historyRef}>
                    <h2 className="section-title">Recent Inspections</h2>
                    <InspectionHistory />
                </section>

                <section className="upload-section" ref={reportRef}>
                    <h2 className="section-title">Upload Report</h2>
                    <UploadReport />
                </section>
            </div>
        </div>
    );
};

export default InspectorDashboard;