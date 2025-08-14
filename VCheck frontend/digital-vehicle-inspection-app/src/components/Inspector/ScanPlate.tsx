import React, { useState, useRef, useEffect } from 'react';
import './ScanPlate.css';

const ScanPlate: React.FC = () => {
    const [plateNumber, setPlateNumber] = useState('');
    const [verificationResult, setVerificationResult] = useState<string | null>(null);
    const [scanMethod, setScanMethod] = useState<'camera' | 'manual'>('manual');
    const [cameraActive, setCameraActive] = useState(false);
    const [isValidPlate, setIsValidPlate] = useState<boolean | null>(null);
    const [capturedImage, setCapturedImage] = useState<string | null>(null);
    const videoRef = useRef<HTMLVideoElement>(null);
    const canvasRef = useRef<HTMLCanvasElement>(null);
    const [userEnteredPlate, setUserEnteredPlate] = useState(''); // For manual correction

    // Enhanced plate validation
    const PLATE_REGEX = /^[A-Z]{1,3}[ -]?\d{1,4}[A-Z]?$/i;

    const validatePlateNumber = (plate: string): boolean => {
        if (!plate) return false;
        
        const cleanPlate = plate.replace(/[ -]/g, '');
        const patternValid = PLATE_REGEX.test(plate);
        const hasLetters = /[A-Z]/i.test(cleanPlate);
        const hasNumbers = /\d/.test(cleanPlate);
        const minLength = cleanPlate.length >= 4;
        
        return patternValid && hasLetters && hasNumbers && minLength;
    };

    // Clean up camera
    useEffect(() => {
        return () => {
            if (videoRef.current?.srcObject) {
                const stream = videoRef.current.srcObject as MediaStream;
                stream.getTracks().forEach(track => track.stop());
            }
        };
    }, []);

    // Camera initialization
    useEffect(() => {
        if (scanMethod === 'camera' && cameraActive) {
            const startCamera = async () => {
                try {
                    const stream = await navigator.mediaDevices.getUserMedia({ 
                        video: { facingMode: 'environment' } 
                    });
                    if (videoRef.current) {
                        videoRef.current.srcObject = stream;
                    }
                } catch (err) {
                    console.error('Camera error:', err);
                    setVerificationResult('Camera access denied. Using manual entry.');
                    setScanMethod('manual');
                    setCameraActive(false);
                }
            };
            startCamera();
        }
    }, [scanMethod, cameraActive]);

    const captureImage = () => {
        if (videoRef.current && canvasRef.current) {
            const context = canvasRef.current.getContext('2d');
            if (context) {
                canvasRef.current.width = videoRef.current.videoWidth;
                canvasRef.current.height = videoRef.current.videoHeight;
                context.drawImage(videoRef.current, 0, 0);
                
                // Convert canvas to image URL
                const imageUrl = canvasRef.current.toDataURL('image/jpeg');
                setCapturedImage(imageUrl);
                
                // Simulate OCR processing with loading state
                setVerificationResult("Processing image...");
                setTimeout(() => {
                    // This would be replaced with actual OCR in a real app
                    // For demo, we'll show the captured image and let user enter
                    setVerificationResult("Please confirm the plate number");
                    setUserEnteredPlate(''); // Reset manual entry
                }, 1500);
            }
        }
    };

    const handleManualInput = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value.toUpperCase();
        setPlateNumber(value);
        setVerificationResult(null);
        setIsValidPlate(validatePlateNumber(value));
    };

    const handleManualCorrection = (e: React.ChangeEvent<HTMLInputElement>) => {
        setUserEnteredPlate(e.target.value.toUpperCase());
    };

    const confirmPlateNumber = () => {
        if (userEnteredPlate) {
            setPlateNumber(userEnteredPlate);
            setIsValidPlate(validatePlateNumber(userEnteredPlate));
            setVerificationResult(null);
        }
    };

    const handleScan = () => {
        if (!plateNumber.trim()) {
            setVerificationResult('Please enter a plate number');
            setIsValidPlate(false);
            return;
        }

        const isValid = validatePlateNumber(plateNumber);
        setIsValidPlate(isValid);

        if (!isValid) {
            setVerificationResult('Invalid plate number format');
            return;
        }

        // Simulate API verification
        setVerificationResult("Verifying plate...");
        setTimeout(() => {
            setVerificationResult(`Vehicle ${plateNumber} verified successfully`);
            // In real app: send to backend, add to history, etc.
        }, 1000);
    };

    const toggleCamera = () => {
        setCameraActive(!cameraActive);
        if (!cameraActive) {
            setPlateNumber('');
            setCapturedImage(null);
            setIsValidPlate(null);
            setVerificationResult(null);
        }
    };

    return (
        <div className="scan-container">
            <h2>Vehicle Plate Scanner</h2>
            
            <div className="scan-method-toggle">
                <button
                    className={`method-btn ${scanMethod === 'camera' ? 'active' : ''}`}
                    onClick={() => setScanMethod('camera')}
                >
                    Camera Scan
                </button>
                <button
                    className={`method-btn ${scanMethod === 'manual' ? 'active' : ''}`}
                    onClick={() => setScanMethod('manual')}
                >
                    Manual Entry
                </button>
            </div>

            {scanMethod === 'camera' ? (
                <div className="camera-section">
                    <div className={`camera-feed ${cameraActive ? 'active' : 'inactive'}`}>
                        {cameraActive ? (
                            capturedImage ? (
                                <img src={capturedImage} alt="Captured plate" className="captured-image" />
                            ) : (
                                <video ref={videoRef} autoPlay playsInline muted className="video-preview" />
                            )
                        ) : (
                            <div className="camera-placeholder">
                                <p>Camera is off</p>
                            </div>
                        )}
                    </div>

                    <div className="camera-controls">
                        {cameraActive && (
                            <>
                                {!capturedImage ? (
                                    <button 
                                        className="capture-btn"
                                        onClick={captureImage}
                                    >
                                        Capture Plate
                                    </button>
                                ) : (
                                    <div className="capture-actions">
                                        <button 
                                            className="retry-btn"
                                            onClick={() => {
                                                setCapturedImage(null);
                                                setVerificationResult(null);
                                            }}
                                        >
                                            Retry
                                        </button>
                                        {userEnteredPlate ? (
                                            <button 
                                                className="verify-btn"
                                                onClick={confirmPlateNumber}
                                            >
                                                Confirm Plate
                                            </button>
                                        ) : (
                                            <button 
                                                className="verify-btn"
                                                onClick={handleScan}
                                                disabled={!plateNumber || isValidPlate === false}
                                            >
                                                Verify Plate
                                            </button>
                                        )}
                                    </div>
                                )}
                            </>
                        )}
                        <button 
                            className={`camera-toggle ${cameraActive ? 'active' : ''}`}
                            onClick={toggleCamera}
                        >
                            {cameraActive ? 'Stop Camera' : 'Start Camera'}
                        </button>
                    </div>

                    {capturedImage && verificationResult === "Please confirm the plate number" && (
                        <div className="plate-confirmation">
                            <input
                                type="text"
                                value={userEnteredPlate}
                                onChange={handleManualCorrection}
                                placeholder="Enter plate number from image"
                                className="correction-input"
                            />
                            <div className="current-plate">
                                {plateNumber && `Detected: ${plateNumber}`}
                            </div>
                        </div>
                    )}

                    {plateNumber && verificationResult !== "Please confirm the plate number" && (
                        <div className="detection-result">
                            <p>Plate Number: <strong>{plateNumber}</strong></p>
                            <div className={`validation ${isValidPlate ? 'valid' : 'invalid'}`}>
                                {isValidPlate ? 'Valid format' : 'Invalid format'}
                            </div>
                        </div>
                    )}
                </div>
            ) : (
                <div className="manual-entry">
                    <input
                        type="text"
                        value={plateNumber}
                        onChange={handleManualInput}
                        placeholder="e.g. ABC123 or AB 1234"
                        autoFocus
                    />
                    {plateNumber && (
                        <div className={`validation ${isValidPlate ? 'valid' : 'invalid'}`}>
                            {isValidPlate ? 'Valid format' : 'Invalid format'}
                        </div>
                    )}
                    <button 
                        className="verify-btn"
                        onClick={handleScan}
                        disabled={!plateNumber || isValidPlate === false}
                    >
                        Verify Plate
                    </button>
                </div>
            )}

            {verificationResult && (
                <div className={`result ${verificationResult.includes('Invalid') ? 'error' : 
                                verificationResult.includes('success') ? 'success' : 'info'}`}>
                    {verificationResult}
                </div>
            )}

            <canvas ref={canvasRef} style={{ display: 'none' }} />
        </div>
    );
};

export default ScanPlate;