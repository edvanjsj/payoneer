-- should be retrieved
INSERT INTO job (id, type, status, queued_at, scheduled_to, priority) VALUES (1, 'EMAIL', 'QUEUED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);

-- should not be retrieved - status is RUNNING
INSERT INTO job (id, type, status, queued_at, scheduled_to, priority) VALUES (2, 'EMAIL', 'RUNNING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);

-- should not be retrieved - status is SUCCESS
INSERT INTO job (id, type, status, queued_at, scheduled_to, priority) VALUES (3, 'EMAIL', 'SUCCESS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);

-- should not be retrieved - status is FAILED
INSERT INTO job (id, type, status, queued_at, scheduled_to, priority) VALUES (4, 'EMAIL', 'FAILED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);

-- should not be retrieved - scheduled to future date
INSERT INTO job (id, type, status, queued_at, scheduled_to, priority) VALUES (5, 'EMAIL', 'QUEUED', CURRENT_TIMESTAMP, DATEADD('DAY', 3, CURRENT_TIMESTAMP), 1);

-- should be retrieved
INSERT INTO job (id, type, status, queued_at, scheduled_to, priority) VALUES (6, 'EMAIL', 'QUEUED', DATEADD('DAY', -3, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP, 1);