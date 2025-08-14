import React from 'react';

const VehicleDetails: React.FC = () => {
    // Sample vehicle details data
    const vehicle = {
        plateNumber: 'ABC1234',
        make: 'Toyota',
        model: 'Camry',
        year: 2020,
        owner: 'John Doe',
        inspectionHistory: [
            { date: '2023-01-15', status: 'Passed' },
            { date: '2022-06-10', status: 'Passed' },
        ],
    };

    return (
        <div>
            <h2>Vehicle Details</h2>
            <p><strong>Plate Number:</strong> {vehicle.plateNumber}</p>
            <p><strong>Make:</strong> {vehicle.make}</p>
            <p><strong>Model:</strong> {vehicle.model}</p>
            <p><strong>Year:</strong> {vehicle.year}</p>
            <p><strong>Owner:</strong> {vehicle.owner}</p>

            <h3>Inspection History</h3>
            <ul>
                {vehicle.inspectionHistory.map((inspection, index) => (
                    <li key={index}>
                        {inspection.date} - {inspection.status}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default VehicleDetails;