INSERT INTO job (id, type, status, queued_at, scheduled_to, priority) VALUES (1, 'EMAIL', 'RUNNING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);
INSERT INTO job (id, type, status, queued_at, scheduled_to, priority) VALUES (2, 'EMAIL', 'RUNNING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);
INSERT INTO job (id, type, status, queued_at, scheduled_to, priority) VALUES (3, 'EMAIL', 'QUEUED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);
INSERT INTO job (id, type, status, queued_at, scheduled_to, priority) VALUES (4, 'EMAIL', 'FAILED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);
INSERT INTO job (id, type, status, queued_at, scheduled_to, priority) VALUES (5, 'EMAIL', 'RUNNING', CURRENT_TIMESTAMP, DATEADD('DAY', 3, CURRENT_TIMESTAMP), 1);
INSERT INTO job (id, type, status, queued_at, scheduled_to, priority) VALUES (6, 'EMAIL', 'QUEUED', DATEADD('DAY', -3, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP, 1);