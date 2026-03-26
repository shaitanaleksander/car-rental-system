-- Dummy data for local development
-- Password for all users: "password" (BCrypt encoded)

INSERT INTO users (name, email, password) VALUES
    ('John Doe', 'john@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
    ('Jane Smith', 'jane@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
    ('Bob Wilson', 'bob@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');

INSERT INTO cars (car_type, plate_number) VALUES
    ('SEDAN', 'AB-123-CD'),
    ('SEDAN', 'EF-456-GH'),
    ('SEDAN', 'IJ-789-KL'),
    ('SUV', 'MN-012-OP'),
    ('SUV', 'QR-345-ST'),
    ('VAN', 'UV-678-WX'),
    ('VAN', 'YZ-901-AB');

INSERT INTO reservations (user_id, car_id, start_date_time, days) VALUES
    (1, 1, '2026-04-01 09:00:00', 3),
    (2, 4, '2026-04-02 10:00:00', 5),
    (3, 6, '2026-04-03 08:00:00', 2);
