CREATE DATABASE IF NOT EXISTS phonestore;
USE phonestore;

-- bảng admin
CREATE TABLE Admin (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);
-- bảng sản phẩm
CREATE TABLE Product (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         name VARCHAR(100) NOT NULL,
                         brand VARCHAR(50) NOT NULL,
                         price DECIMAL(12,2) NOT NULL,
                         stock INT NOT NULL
);
-- bảng khách hàng
CREATE TABLE Customer (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(100) NOT NULL,
                          phone VARCHAR(20) NULL,
                          email VARCHAR(100) UNIQUE,
                          address VARCHAR(255) NULL
);
-- bảng hóa đơn
CREATE TABLE Invoice (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         customer_id INT,
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                         total_amount DECIMAL(12,2) NOT NULL,
                         FOREIGN KEY (customer_id) REFERENCES Customer(id)
);
-- bảng chi tiết hóa đơn
CREATE TABLE Invoice_Details (
                                 id INT PRIMARY KEY AUTO_INCREMENT,
                                 invoice_id INT,
                                 product_id INT,
                                 quantity INT NOT NULL,
                                 unit_price DECIMAL(12,2) NOT NULL,
                                 FOREIGN KEY (invoice_id) REFERENCES Invoice(id),
                                 FOREIGN KEY (product_id) REFERENCES Product(id)
);
-- gọi các bảng
SELECT * FROM Admin;
SELECT * FROM Product;
SELECT * FROM Customer;
SELECT * FROM Invoice;
SELECT * FROM Invoice_Details;

-- thêm user - pass vào bảng admin
INSERT INTO Admin (username, password) VALUES ('admin', 'admin123');

DELIMITER $$
CREATE PROCEDURE dummy_procedure()
BEGIN
    SELECT 'Stored procedure sample';
END$$
DELIMITER ;


DELIMITER $$
-- tạo lưu trữ đặt hàng
CREATE PROCEDURE place_order (
    IN p_customer_id INT, -- mã kh
    IN p_product_id INT,  -- mã sp
    IN p_quantity INT  -- sl mua
)
BEGIN
    -- khai báo nội bộ
    DECLARE v_price DECIMAL(12,2); -- giá sp
    DECLARE v_stock INT;  -- sl tồn kho
    DECLARE v_total DECIMAL(12,2);  -- tổng tiền
    DECLARE v_invoice_id INT; -- mã hđ được tạo

    -- lỗi trong quá trình chạy
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK; -- hủy nếu c lôic
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Có lỗi xảy ra, giao dịch bị huỷ!';
        END;

    -- bắt đầu giao dịch
    START TRANSACTION;

    -- lấy tt giá và sl tồn kho của sp
    SELECT price, stock INTO v_price, v_stock
    FROM Product
    WHERE id = p_product_id;

    -- kt tồn kho có đủ không
    IF v_stock < p_quantity THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Không đủ hàng trong kho!';
    END IF;

    -- giá nhân sl = tổng tiền
    SET v_total = v_price * p_quantity;

    -- thêm hóa đơn mới
    INSERT INTO Invoice (customer_id, total_amount)
    VALUES (p_customer_id, v_total);

    -- id hđ vừa tạo
    SET v_invoice_id = LAST_INSERT_ID();

    -- chi tiết hđ
    INSERT INTO Invoice_Details (invoice_id, product_id, quantity, unit_price)
    VALUES (v_invoice_id, p_product_id, p_quantity, v_price);

    -- cập nhật lại sl sai khi bán
    UPDATE Product
    SET stock = stock - p_quantity
    WHERE id = p_product_id;

    -- kết thúc
    COMMIT;
END$$
DELIMITER ;

