import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Login.css';

const Login: React.FC = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [role, setRole] = useState<'inspector' | 'owner'>('owner');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setIsLoading(true);
        
        try {
            // Simulate API call
            await new Promise(resolve => setTimeout(resolve, 1500));
            
            // Redirect based on selected role
            if (role === 'inspector') {
                navigate('/inspectordashboard', { replace: true });
            } else {
                navigate('/ownerdashboard', { replace: true });
            }
            
        } catch (err) {
            setError('Login failed. Please check your credentials.');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="login-container">
            <h2>Vehicle Inspection Portal</h2>
            {error && <p className="error">{error}</p>}
            <form onSubmit={handleLogin}>
                <div className="form-group">
                    <label>Email Address</label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="Enter your email"
                        required
                        autoFocus
                    />
                </div>
                <div className="form-group">
                    <label>Password</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="Enter your password"
                        required
                    />
                </div>
                <div className="form-group">
                    <label>Login As:</label>
                    <div className="radio-group">
                        <label className="radio-option">
                            <input
                                type="radio"
                                name="role"
                                value="owner"
                                checked={role === 'owner'}
                                onChange={() => setRole('owner')}
                            />
                            <span className="radio-label">Vehicle Owner</span>
                        </label>
                        <label className="radio-option">
                            <input
                                type="radio"
                                name="role"
                                value="inspector"
                                checked={role === 'inspector'}
                                onChange={() => setRole('inspector')}
                            />
                            <span className="radio-label">Inspector Officer</span>
                        </label>
                    </div>
                </div>
                <button 
                    type="submit" 
                    className="login-button"
                    disabled={isLoading}
                >
                    {isLoading ? (
                        <span className="loading">Signing In...</span>
                    ) : (
                        'Sign In'
                    )}
                </button>
            </form>
        </div>
    );
};

export default Login;