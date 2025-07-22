CREATE DATABASE IF NOT EXISTS phonestore;
USE phonestore;

CREATE TABLE Admin (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

CREATE TABLE Product (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         name VARCHAR(100) NOT NULL,
                         brand VARCHAR(50) NOT NULL,
                         price DECIMAL(12,2) NOT NULL,
                         stock INT NOT NULL
);

CREATE TABLE Customer (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(100) NOT NULL,
                          phone VARCHAR(20) NULL,
                          email VARCHAR(100) UNIQUE,
                          address VARCHAR(255) NULL
);

CREATE TABLE Invoice (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         customer_id INT,
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                         total_amount DECIMAL(12,2) NOT NULL,
                         FOREIGN KEY (customer_id) REFERENCES Customer(id)
);

CREATE TABLE Invoice_Details (
                                 id INT PRIMARY KEY AUTO_INCREMENT,
                                 invoice_id INT,
                                 product_id INT,
                                 quantity INT NOT NULL,
                                 unit_price DECIMAL(12,2) NOT NULL,
                                 FOREIGN KEY (invoice_id) REFERENCES Invoice(id),
                                 FOREIGN KEY (product_id) REFERENCES Product(id)
);

SELECT * FROM Admin;
SELECT * FROM Product;
SELECT * FROM Customer;
SELECT * FROM Invoice;
SELECT * FROM Invoice_Details;

INSERT INTO Admin (username, password) VALUES ('admin', 'admin123');

DELIMITER $$
CREATE PROCEDURE dummy_procedure()
BEGIN
SELECT 'Stored procedure sample';
END$$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE place_order (
    IN p_customer_id INT,
    IN p_product_id INT,
    IN p_quantity INT
)
BEGIN
    DECLARE v_price DECIMAL(12,2);
    DECLARE v_stock INT;
    DECLARE v_total DECIMAL(12,2);
    DECLARE v_invoice_id INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
ROLLBACK;
SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Có lỗi xảy ra, giao dịch bị huỷ!';
END;

START TRANSACTION;

SELECT price, stock INTO v_price, v_stock
FROM Product
WHERE id = p_product_id;

IF v_stock < p_quantity THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Không đủ hàng trong kho!';
END IF;

    SET v_total = v_price * p_quantity;

INSERT INTO Invoice (customer_id, total_amount)
VALUES (p_customer_id, v_total);

SET v_invoice_id = LAST_INSERT_ID();

INSERT INTO Invoice_Details (invoice_id, product_id, quantity, unit_price)
VALUES (v_invoice_id, p_product_id, p_quantity, v_price);

UPDATE Product
SET stock = stock - p_quantity
WHERE id = p_product_id;

COMMIT;
END$$

DELIMITER ;

