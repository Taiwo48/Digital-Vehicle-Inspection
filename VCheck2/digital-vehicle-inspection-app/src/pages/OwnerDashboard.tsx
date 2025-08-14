import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import BookAppointment from '../components/Owner/BookAppointment';
import ViewReports from '../components/Owner/ViewReports';
import './OwnerDashboard.css';

const OwnerDashboard: React.FC = () => {
    const navigate = useNavigate();
    const [report, setReport] = useState({
        type: '',
        description: ''
    });
    const [isSubmitted, setIsSubmitted] = useState(false);

    const handleReportSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        console.log('Report submitted:', report);
        setIsSubmitted(true);
        setTimeout(() => setIsSubmitted(false), 3000);
        setReport({ type: '', description: '' });
    };

    const handleLogout = () => {
        // Clear any user session/token
        localStorage.removeItem('authToken');
        localStorage.removeItem('user');
        
        // Redirect to login page
        navigate('/login');
    };

    return (
        <div className="owner-dashboard">
            <header className="dashboard-header">
                <h1>Vehicle Owner Dashboard</h1>
                <button onClick={handleLogout} className="logout-button">
                    Logout
                </button>
            </header>
            
            {/* Rest of your dashboard remains exactly the same */}
            <div className="dashboard-grid">
                <section className="dashboard-card vehicle-details-section">
                    <h2>My Vehicle Details</h2>
                    <div className="empty-section">
                        Vehicle details will appear here
                    </div>
                </section>
                
                <section className="dashboard-card appointment-section">
                    <h2>Book Inspection Appointment</h2>
                    <BookAppointment />
                </section>
                
                <section className="dashboard-card reports-section">
                    <h2>Inspection Reports</h2>
                    <ViewReports />
                </section>

                <section className="dashboard-card report-section">
                    <h2>Make a Report</h2>
                    {isSubmitted ? (
                        <div className="success-message">Report submitted successfully!</div>
                    ) : (
                        <form onSubmit={handleReportSubmit}>
                            {/* Existing report form remains unchanged */}
                        </form>
                    )}
                </section>
            </div>
        </div>
    );
};

export default OwnerDashboard;