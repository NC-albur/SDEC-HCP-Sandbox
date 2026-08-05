CREATE TABLE THREAD (
                        id BIGSERIAL PRIMARY KEY,
                        thread_name VARCHAR(255) NOT NULL,
                        uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE THREAD_WORKER (
                               id BIGSERIAL PRIMARY KEY,
                               thread_id BIGINT NOT NULL,
                               case_worker_name VARCHAR(50),
                               case_worker_id BIGINT,
                               last_logged_in TIMESTAMP,
                               FOREIGN KEY (thread_id) REFERENCES THREAD(id) ON DELETE CASCADE
);
