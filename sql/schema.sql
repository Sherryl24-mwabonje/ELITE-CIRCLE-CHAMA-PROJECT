-- ELITE CIRCLE CHAMA : database schema (MySQL 8+)
CREATE DATABASE IF NOT EXISTS elite_circle_chama;
USE elite_circle_chama;

-- Main table 1: chama members
CREATE TABLE IF NOT EXISTS members (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    national_id VARCHAR(10) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(100),
    join_date DATE NOT NULL,
    monthly_pledge DECIMAL(10,2) NOT NULL,
    status VARCHAR(10) NOT NULL DEFAULT 'Active'
);

-- Main table 2: contributions linked to members
CREATE TABLE IF NOT EXISTS contributions (
    contribution_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    contribution_date DATE NOT NULL,
    payment_method VARCHAR(15) NOT NULL,
    reference_code VARCHAR(30),
    CONSTRAINT fk_contrib_member FOREIGN KEY (member_id)
        REFERENCES members(member_id) ON DELETE CASCADE
);