import React, { useState } from 'react';

const UploadReport: React.FC = () => {
    const [file, setFile] = useState<File | null>(null);
    const [typedReport, setTypedReport] = useState('');
    const [message, setMessage] = useState('');
    const [uploading, setUploading] = useState(false);

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const selectedFile = event.target.files?.[0];
        if (selectedFile) {
            setFile(selectedFile);
            setMessage('');
        }
    };

    const handleTypedReportChange = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
        setTypedReport(event.target.value);
        setMessage('');
    };

    const handleUpload = async () => {
        if (!file && !typedReport.trim()) {
            setMessage('Please select a file or type a report.');
            return;
        }

        const formData = new FormData();
        if (file) formData.append('report', file);
        if (typedReport.trim()) formData.append('reportText', typedReport);

        try {
            setUploading(true);
            const response = await fetch('/api/upload-report', {
                method: 'POST',
                body: formData,
            });

            if (response.ok) {
                setMessage('✅ Report uploaded successfully!');
                setFile(null);
                setTypedReport('');
            } else {
                setMessage('❌ Failed to upload report.');
            }
        } catch (error) {
            setMessage('⚠️ An error occurred during upload.');
        } finally {
            setUploading(false);
        }
    };

    return (
        <div style={{ padding: '1rem', border: '1px solid #ddd', borderRadius: '8px' }}>
            <h2>📤 Upload or Type Inspection Report</h2>
            <input type="file" onChange={handleFileChange} />
            <p style={{ margin: '8px 0' }}>— or —</p>
            <textarea
                placeholder="Type your report here..."
                value={typedReport}
                onChange={handleTypedReportChange}
                rows={8}
                style={{ width: '100%', resize: 'vertical' }}
            />
            <br />
            <button onClick={handleUpload} disabled={uploading} style={{ marginTop: '12px' }}>
                {uploading ? 'Uploading...' : 'Upload'}
            </button>
            {message && <p style={{ marginTop: '10px', fontWeight: 'bold' }}>{message}</p>}
        </div>
    );
};

export default UploadReport;