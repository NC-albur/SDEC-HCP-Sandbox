CREATE TABLE THREAD (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       thread_name VARCHAR(255) NOT NULL,
                       uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);