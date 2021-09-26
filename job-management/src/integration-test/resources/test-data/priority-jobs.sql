-- Second one
INSERT INTO job (id, type, status, queued_at, scheduled_to, priority) VALUES (-1, 'EMAIL', 'QUEUED', DATEADD('MINUTE', -2, CURRENT_TIMESTAMP), DATEADD('MINUTE', -2, CURRENT_TIMESTAMP), 1);

-- Fourth one
INSERT INTO job (id, type, status, queued_at, scheduled_to, priority) VALUES (-2, 'DWH', 'QUEUED', DATEADD('MINUTE', -2, CURRENT_TIMESTAMP), DATEADD('MINUTE', -2, CURRENT_TIMESTAMP), 2);

-- Third one
INSERT INTO job (id, type, status, queued_at, scheduled_to, priority) VALUES (-3, 'EMAIL', 'QUEUED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);

-- First one
INSERT INTO job (id, type, status, queued_at, scheduled_to, priority) VALUES (-4, 'DWH', 'QUEUED',  DATEADD('MINUTE', -3, CURRENT_TIMESTAMP),  DATEADD('MINUTE', -3, CURRENT_TIMESTAMP), 1);

-- Sixth one
INSERT INTO job (id, type, status, queued_at, scheduled_to, priority) VALUES (-6, 'EMAIL', 'QUEUED', DATEADD('DAY', -3, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP, 3);

-- Fifth one
INSERT INTO job (id, type, status, queued_at, scheduled_to, priority) VALUES (-5, 'DWH', 'QUEUED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2);