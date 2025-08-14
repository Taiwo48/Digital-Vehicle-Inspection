import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import Login from './components/Auth/Login';
import Register from './components/Auth/Register';
import InspectorDashboard from './pages/InspectorDashboard';
import OwnerDashboard from './pages/OwnerDashboard';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/inspector-dashboard" element={<InspectorDashboard />} />
        <Route path="/owner-dashboard" element={<OwnerDashboard />} />
      </Routes>
    </Router>
  );
}

export default App;