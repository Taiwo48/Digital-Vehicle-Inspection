import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Home.css'; // Create this for styling

const Home: React.FC = () => {
  const navigate = useNavigate();

  return (
    <div className="home-container">
      <h1>Welcome to the Digital Vehicle Inspection Application</h1>
      <p>This application allows vehicle owners to manage their vehicle inspections and inspectors to verify vehicle authenticity.</p>
      
      <div className="auth-options">
        <button 
          onClick={() => navigate('/login')}
          className="auth-button"
        >
          Log In
        </button>
        <button 
          onClick={() => navigate('/register')}
          className="auth-button"
        >
          Register
        </button>
      </div>
    </div>
  );
};

export default Home;