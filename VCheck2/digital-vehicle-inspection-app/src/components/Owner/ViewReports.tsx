import React, { useEffect, useState } from 'react';
import { getReports } from '../../api';
import { InspectionReport } from '../../types';
import './ViewReports.css'; // Create this CSS file

// Temporary mock auth hook for development
const useMockAuth = () => {
  return {
    user: {
      id: 'temp-user-id',
      email: 'temp@example.com',
      name: 'Temp User',
      role: 'owner'
    }
  };
};

const ViewReports: React.FC = () => {
    const [reports, setReports] = useState<InspectionReport[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [searchTerm, setSearchTerm] = useState<string>('');
    const [filterStatus, setFilterStatus] = useState<string>('all');
    
    const { user } = useMockAuth();

    useEffect(() => {
        const fetchReports = async () => {
            if (!user?.id) {
                setError('User not authenticated');
                setLoading(false);
                return;
            }

            try {
                const fetchedReports = await getReports(user.id);
                setReports(fetchedReports);
            } catch (err) {
                setError('Failed to fetch reports. Please try again later.');
                console.error('Error fetching reports:', err);
            } finally {
                setLoading(false);
            }
        };

        fetchReports();
    }, [user?.id]);

    const filteredReports = reports.filter(report => {
        const matchesSearch = report.vehicleId.toLowerCase().includes(searchTerm.toLowerCase()) || 
                             report.id.toLowerCase().includes(searchTerm.toLowerCase());
        const matchesStatus = filterStatus === 'all' || report.status.toLowerCase() === filterStatus;
        return matchesSearch && matchesStatus;
    });

    if (loading) {
        return (
            <div className="loading-container">
                <div className="spinner"></div>
                <p>Loading your inspection reports...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div className="error-container">
                <div className="error-icon">!</div>
                <p className="error-message">{error}</p>
                <button 
                    className="retry-button"
                    onClick={() => window.location.reload()}
                >
                    Try Again
                </button>
            </div>
        );
    }

    return (
        <div className="reports-view">
            <div className="reports-header">
                <h2>Inspection Reports</h2>
                <div className="controls">
                    <div className="search-box">
                        <input
                            type="text"
                            placeholder="Search by vehicle or report ID..."
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                        <span className="search-icon">🔍</span>
                    </div>
                    <select
                        className="status-filter"
                        value={filterStatus}
                        onChange={(e) => setFilterStatus(e.target.value)}
                    >
                        <option value="all">All Statuses</option>
                        <option value="approved">Approved</option>
                        <option value="pending">Pending</option>
                        <option value="rejected">Rejected</option>
                    </select>
                </div>
            </div>

            {filteredReports.length === 0 ? (
                <div className="empty-state">
                    <img src="/images/no-reports.svg" alt="No reports" className="empty-icon" />
                    <h3>No reports found</h3>
                    <p>{reports.length === 0 ? 
                        "You don't have any inspection reports yet." : 
                        "No reports match your search criteria."}
                    </p>
                </div>
            ) : (
                <div className="reports-grid">
                    {filteredReports.map(report => (
                        <div key={report.id} className="report-card">
                            <div className="card-header">
                                <h3>Report #{report.id.slice(0, 8).toUpperCase()}</h3>
                                <span className={`status-badge ${report.status.toLowerCase()}`}>
                                    {report.status}
                                </span>
                            </div>
                            <div className="card-body">
                                <div className="info-row">
                                    <span className="info-label">Vehicle ID:</span>
                                    <span className="info-value">{report.vehicleId}</span>
                                </div>
                                <div className="info-row">
                                    <span className="info-label">Inspection Date:</span>
                                    <span className="info-value">
                                        {new Date(report.date).toLocaleDateString('en-US', {
                                            year: 'numeric',
                                            month: 'short',
                                            day: 'numeric'
                                        })}
                                    </span>
                                </div>
                                <div className="info-row">
                                    <span className="info-label">Inspector:</span>
                                    <span className="info-value">ID-{report.inspectorId.slice(0, 6)}</span>
                                </div>
                                <div className="report-summary">
                                    <p>{report.details}</p>
                                </div>
                            </div>
                            <div className="card-footer">
                                <button className="action-button view-button">
                                    View Full Report
                                </button>
                                <button className="action-button download-button">
                                    Download PDF
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default ViewReports;