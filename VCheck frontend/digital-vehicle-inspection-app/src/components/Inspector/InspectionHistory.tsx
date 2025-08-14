import React, { useEffect, useState } from 'react';
import { getInspectionHistory } from '../../api';


interface Inspection {
    id: string;
    date: string;
    vehiclePlate: string;
    inspectorName: string;
    report: string;
    status: 'clean' | 'violation' | 'verified' | 'rejected';
}

const InspectionHistory: React.FC = () => {
    const [apiHistory, setApiHistory] = useState<Inspection[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [localScanHistory, setLocalScanHistory] = useState<any[]>([]);

    useEffect(() => {
        // Load local scan history from localStorage
        const savedHistory = localStorage.getItem('scanHistory');
        if (savedHistory) {
            setLocalScanHistory(JSON.parse(savedHistory));
        }

        const loadInspectionHistory = async () => {
            try {
                const userData = JSON.parse(localStorage.getItem('user') || '{}');
                const inspectorId = userData.id || userData._id;
                
                if (!inspectorId) throw new Error('Inspector ID not found');

                const data = await getInspectionHistory(inspectorId);
                
                const mappedApiHistory: Inspection[] = data.map((item: any) => ({
                    id: item.id ?? item._id ?? '',
                    date: new Date(item.date ?? item.createdAt).toLocaleString(),
                    vehiclePlate: item.vehiclePlate ?? item.plate ?? '',
                    inspectorName: item.inspectorName ?? item.inspector?.name ?? '',
                    report: item.report ?? item.reportText ?? '',
                    status: item.status ?? 'clean'
                }));

                setApiHistory(mappedApiHistory);
            } catch (err) {
                console.error('Error loading history:', err);
                setError(err instanceof Error ? err.message : 'Failed to load inspection history');
            } finally {
                setLoading(false);
            }
        };

        loadInspectionHistory();
    }, []);

    // Combine API history with local scans
    const combinedHistory = [
        ...apiHistory,
        ...localScanHistory.map(scan => ({
            id: scan.id,
            date: new Date(scan.timestamp).toLocaleString(),
            vehiclePlate: scan.plateNumber || 'N/A',
            inspectorName: 'You',
            report: scan.message,
            status: scan.status
        }))
    ].sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime());

    const filteredHistory = combinedHistory.filter(inspection =>
        inspection.vehiclePlate.toLowerCase().includes(searchTerm.toLowerCase()) ||
        inspection.report.toLowerCase().includes(searchTerm.toLowerCase()) ||
        inspection.status.toLowerCase().includes(searchTerm.toLowerCase())
    );

    if (loading) {
        return <div className="loading-spinner">Loading...</div>;
    }

    if (error) {
        return <div className="error-message">{error}</div>;
    }

    return (
        <div className="inspection-history">
            <div className="history-header">
                <h2>Inspection History</h2>
                <input
                    type="text"
                    placeholder="Search by plate, report, or status..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="search-input"
                />
            </div>

            {filteredHistory.length === 0 ? (
                <p className="no-results">No inspection records found</p>
            ) : (
                <table className="history-table">
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Vehicle Plate</th>
                            <th>Inspector</th>
                            <th>Status</th>
                            <th>Report</th>
                        </tr>
                    </thead>
                    <tbody>
                        {filteredHistory.map((inspection) => (
                            <tr key={inspection.id} className={`status-${inspection.status}`}>
                                <td>{inspection.date}</td>
                                <td>{inspection.vehiclePlate}</td>
                                <td>{inspection.inspectorName}</td>
                                <td>
                                    <span className={`status-badge ${inspection.status}`}>
                                        {inspection.status.toUpperCase()}
                                    </span>
                                </td>
                                <td>{inspection.report}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default InspectionHistory;