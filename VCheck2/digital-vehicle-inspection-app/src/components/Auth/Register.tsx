import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Register.css';

interface VehicleDetails {
    make: string;
    model: string;
    year: string;
    plateNumber: string;
    color: string;
    vin: string;
}

const Register: React.FC = () => {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [role, setRole] = useState<'inspector' | 'owner'>('owner');
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');
    const [vehicleDetails, setVehicleDetails] = useState<VehicleDetails>({
        make: '',
        model: '',
        year: '',
        plateNumber: '',
        color: '',
        vin: ''
    });
    const navigate = useNavigate();

    const handleVehicleDetailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setVehicleDetails(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleRegister = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        
        if (password !== confirmPassword) {
            setError('Passwords do not match');
            return;
        }

        setIsLoading(true);
        
        try {
            // Simulate API call
            await new Promise(resolve => setTimeout(resolve, 1500));
            
            const userData = {
                username,
                email,
                role,
                ...(role === 'owner' && { vehicleDetails })
            };

            localStorage.setItem('user', JSON.stringify(userData));
            
            navigate(role === 'inspector' ? '/inspector-dashboard' : '/owner-dashboard');
        } catch (err) {
            setError('Registration failed. Please try again.');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="register-container">
            <h2>Create Your Account</h2>
            {error && <div className="error-message">{error}</div>}
            
            <form className="register-form" onSubmit={handleRegister}>
                <div className="form-group">
                    <label>Username</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        placeholder="Enter your username"
                        required
                    />
                </div>
                
                <div className="form-group">
                    <label>Email</label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="Enter your email"
                        required
                    />
                </div>
                
                <div className="form-group">
                    <label>Password</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="Create a password"
                        required
                        minLength={6}
                    />
                </div>
                
                <div className="form-group">
                    <label>Confirm Password</label>
                    <input
                        type="password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        placeholder="Confirm your password"
                        required
                    />
                </div>
                
                <div className="form-group">
                    <label>Register As:</label>
                    <div className="radio-group">
                        <label className="radio-option">
                            <input
                                type="radio"
                                name="role"
                                value="owner"
                                checked={role === 'owner'}
                                onChange={() => setRole('owner')}
                            />
                            <span>Vehicle Owner</span>
                        </label>
                        <label className="radio-option">
                            <input
                                type="radio"
                                name="role"
                                value="inspector"
                                checked={role === 'inspector'}
                                onChange={() => setRole('inspector')}
                            />
                            <span>Inspector Officer</span>
                        </label>
                    </div>
                </div>

                {role === 'owner' && (
                    <div className="vehicle-details-section">
                        <h3>Vehicle Information</h3>
                        <div className="form-group">
                            <label>Make</label>
                            <input
                                type="text"
                                name="make"
                                value={vehicleDetails.make}
                                onChange={handleVehicleDetailChange}
                                placeholder="e.g. Toyota"
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>Model</label>
                            <input
                                type="text"
                                name="model"
                                value={vehicleDetails.model}
                                onChange={handleVehicleDetailChange}
                                placeholder="e.g. Camry"
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>Year</label>
                            <input
                                type="text"
                                name="year"
                                value={vehicleDetails.year}
                                onChange={handleVehicleDetailChange}
                                placeholder="e.g. 2020"
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>Plate Number</label>
                            <input
                                type="text"
                                name="plateNumber"
                                value={vehicleDetails.plateNumber}
                                onChange={handleVehicleDetailChange}
                                placeholder="e.g. ABC123"
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>Color</label>
                            <input
                                type="text"
                                name="color"
                                value={vehicleDetails.color}
                                onChange={handleVehicleDetailChange}
                                placeholder="e.g. Red"
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>VIN</label>
                            <input
                                type="text"
                                name="vin"
                                value={vehicleDetails.vin}
                                onChange={handleVehicleDetailChange}
                                placeholder="Vehicle Identification Number"
                                required
                            />
                        </div>
                    </div>
                )}

                <button 
                    type="submit" 
                    className="register-button"
                    disabled={isLoading || 
                        (role === 'owner' && (
                            !vehicleDetails.make || 
                            !vehicleDetails.model ||
                            !vehicleDetails.year ||
                            !vehicleDetails.plateNumber ||
                            !vehicleDetails.color ||
                            !vehicleDetails.vin
                        ))
                    }
                >
                    {isLoading ? 'Creating Account...' : 'Register Now'}
                </button>
            </form>
            
            <p className="login-link">
                Already have an account? <a href="/login">Sign in</a>
            </p>
        </div>
    );
};

export default Register;