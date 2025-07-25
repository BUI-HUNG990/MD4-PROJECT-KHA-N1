CREATE DATABASE IF NOT EXISTS phonestore; -- tạo csdl
USE phonestore;
-- chọn csdl

-- bảng quản trị viên
CREATE TABLE Admin
(
    id       INT PRIMARY KEY AUTO_INCREMENT, -- nhận dạng
    username VARCHAR(50)  NOT NULL UNIQUE,   -- tên người dùng
    password VARCHAR(255) NOT NULL           -- mk
);

-- bảng sản phẩm
CREATE TABLE Product
(
    id    INT PRIMARY KEY AUTO_INCREMENT, -- nhận dạng
    name  VARCHAR(100)   NOT NULL,        -- tên sp
    brand VARCHAR(50)    NOT NULL,        -- thương hiệu
    price DECIMAL(12, 2) NOT NULL,        -- giá
    stock INT            NOT NULL         -- phần/cột
);

-- bảng khách hàng
CREATE TABLE Customer
(
    id      INT PRIMARY KEY AUTO_INCREMENT, -- nhận dạng
    name    VARCHAR(100) NOT NULL,          -- tên kh
    phone   VARCHAR(20)  NULL,              -- sđt
    email   VARCHAR(100) UNIQUE,            -- email
    address VARCHAR(255) NULL               -- địa chỉ
);

-- bảng hóa đơn
CREATE TABLE Invoice
(
    id           INT PRIMARY KEY AUTO_INCREMENT,       -- nhận dạng
    customer_id  INT,                                  -- id khách hàng
    invoice_date   DATETIME DEFAULT CURRENT_TIMESTAMP,   -- hd đã tạo
    total_amount DECIMAL(12, 2) NOT NULL,              -- tổng tiền
    FOREIGN KEY (customer_id) REFERENCES Customer (id) -- khóa ngoại
);


DROP TABLE IF EXISTS Invoice_Details;
DROP TABLE IF EXISTS Invoice;
DROP TABLE IF EXISTS Customer;
DROP TABLE IF EXISTS Product;
DROP TABLE IF EXISTS Admin;


-- bảng chi tiết hóa đơn
CREATE TABLE Invoice_Details
(
    id         INT PRIMARY KEY AUTO_INCREMENT, -- nhận dạng
    invoice_id INT,                            -- id hóa đơn
    product_id INT,                            -- id sản phẩm
    quantity   INT            NOT NULL,        -- số lượng
    unit_price DECIMAL(12, 2) NOT NULL,        -- đơn giá
    FOREIGN KEY (invoice_id) REFERENCES Invoice (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Product (id) ON DELETE CASCADE
);

-- gọi các bảng
SELECT * FROM invoice;
SELECT * FROM Admin;
SELECT * FROM Product;
SELECT * FROM Customer;
SELECT * FROM Invoice;
SELECT * FROM Invoice_Details;

-- tạo user pass admin
INSERT INTO Admin (username, password)
VALUES ('admin', 'admin123');

-- tạo thủ tục lưu trữ
DELIMITER $$
CREATE PROCEDURE dummy_procedure()
BEGIN
    SELECT 'Stored procedure sample';
END$$
DELIMITER ;



DELIMITER $$

CREATE PROCEDURE place_order(
    IN p_customer_id INT,  -- ID khách hàng
    IN p_product_id INT,   -- ID sản phẩm
    IN p_quantity INT      -- Số lượng đặt hàng
)
BEGIN
    -- Khai báo biến
    DECLARE v_price DECIMAL(12, 2);  -- Giá sản phẩm
    DECLARE v_stock INT;             -- Số lượng tồn kho
    DECLARE v_total DECIMAL(12, 2);  -- Tổng tiền
    DECLARE v_invoice_id INT;        -- ID hóa đơn mới

    -- Bắt lỗi và rollback nếu có lỗi SQL
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Có lỗi xảy ra, giao dịch bị huỷ!';
        END;

    START TRANSACTION;

    -- Lấy giá và tồn kho từ bảng Product
    SELECT price, stock
    INTO v_price, v_stock
    FROM Product
    WHERE id = p_product_id;

    -- Kiểm tra sản phẩm có tồn tại hay không
    IF v_price IS NULL OR v_stock IS NULL THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Sản phẩm không tồn tại!';
    END IF;

    -- Kiểm tra tồn kho đủ không
    IF v_stock < p_quantity THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Không đủ hàng trong kho!';
    END IF;

    -- Tính tổng tiền
    SET v_total = v_price * p_quantity;

    -- Tạo hóa đơn
    INSERT INTO Invoice (customer_id, invoice_date, total_amount)
    VALUES (p_customer_id, NOW(), v_total);

    -- Lấy ID hóa đơn vừa tạo
    SET v_invoice_id = LAST_INSERT_ID();

    -- Thêm chi tiết hóa đơn
    INSERT INTO Invoice_Details (invoice_id, product_id, quantity, unit_price)
    VALUES (v_invoice_id, p_product_id, p_quantity, v_price);

    -- Cập nhật tồn kho sản phẩm
    UPDATE Product
    SET stock = stock - p_quantity
    WHERE id = p_product_id;

    COMMIT;
END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS place_order;
