CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100),
    action VARCHAR(50),
    endpoint VARCHAR(200),
    ip_address VARCHAR(50),
    status_code INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);