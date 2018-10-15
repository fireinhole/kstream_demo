use pocdb;
CREATE TABLE IF NOT EXISTS clients (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    age INT,
    gender VARCHAR(1)
) ENGINE=INNODB;
CREATE TABLE IF NOT EXISTS addresses (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    client_id VARCHAR(255),
    address TEXT,
    city TEXT,
    postalcode TEXT,
    country TEXT,
    INDEX client_idx (client_id),
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
) ENGINE=INNODB;
CREATE TABLE IF NOT EXISTS transactions (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    client_id VARCHAR(255),
    type VARCHAR(32),
    amount DOUBLE,
    INDEX client_idx (client_id),
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
) ENGINE=INNODB;