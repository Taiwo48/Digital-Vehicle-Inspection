import React, { useState } from 'react';

const BookAppointment: React.FC = () => {
    const [date, setDate] = useState('');
    const [time, setTime] = useState('');
    const [vehiclePlate, setVehiclePlate] = useState('');
    const [message, setMessage] = useState('');

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        // Logic to book appointment goes here
        setMessage('Appointment booked successfully!');
    };

    return (
        <div>
            <h2>Book Appointment</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="vehiclePlate">Vehicle Plate Number:</label>
                    <input
                        type="text"
                        id="vehiclePlate"
                        value={vehiclePlate}
                        onChange={(e) => setVehiclePlate(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="date">Date:</label>
                    <input
                        type="date"
                        id="date"
                        value={date}
                        onChange={(e) => setDate(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="time">Time:</label>
                    <input
                        type="time"
                        id="time"
                        value={time}
                        onChange={(e) => setTime(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Book Appointment</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default BookAppointment;