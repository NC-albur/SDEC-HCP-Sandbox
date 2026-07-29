INSERT INTO THREAD
(thread_name) VALUES
                  ('Thread1'),
                  ('Thread2');

INSERT INTO THREAD_WORKER (thread_id, case_worker_name, case_worker_id, last_logged_in)
VALUES (1, 'Joe', 123, CURRENT_TIMESTAMP)