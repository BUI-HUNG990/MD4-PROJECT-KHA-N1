CREATE DATABASE IF NOT EXISTS phonestore; -- tạo csdl
USE phonestore; -- chọn csdl

-- bảng quản trị viên
CREATE TABLE Admin (
                       id INT PRIMARY KEY AUTO_INCREMENT, -- nhận dạng
                       username VARCHAR(50)  NOT NULL UNIQUE, -- tên người dùng
                       password VARCHAR(255) NOT NULL -- mk
);

-- bảng sản phẩm
CREATE TABLE Product (
                         id INT PRIMARY KEY AUTO_INCREMENT, -- nhận dạng
                         name VARCHAR(100)   NOT NULL,  -- tên sp
                         brand VARCHAR(50)    NOT NULL,  -- thương hiệu
                         price DECIMAL(12, 2) NOT NULL,  -- giá
                         stock INT            NOT NULL -- phần/cột
);

-- bảng khách hàng
CREATE TABLE Customer (
                          id INT PRIMARY KEY AUTO_INCREMENT, -- nhận dạng
                          name VARCHAR(100) NOT NULL, -- tên kh
                          phone VARCHAR(20)  NULL, -- sđt
                          email VARCHAR(100) UNIQUE, -- email
                          address VARCHAR(255) NULL -- địa chỉ
);

-- bảng hóa đơn
CREATE TABLE Invoice (
                         id INT PRIMARY KEY AUTO_INCREMENT,  -- nhận dạng
                         customer_id INT,                             -- id khách hàng
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- hd đã tạo
                         total_amount DECIMAL(12, 2) NOT NULL,  -- tổng tiền
                         FOREIGN KEY (customer_id) REFERENCES Customer (id)  -- khóa ngoại
);

-- bảng chi tiết hóa đơn
CREATE TABLE Invoice_Details (
                                 id INT PRIMARY KEY AUTO_INCREMENT, -- nhận dạng
                                 invoice_id INT,                            -- id hóa đơn
                                 product_id INT,                            -- id sản phẩm
                                 quantity INT NOT NULL,                   -- số lượng
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
CREATE PROCEDURE place_order( -- tạo place_order có 3 tham số
    IN p_customer_id INT, -- id kh
    IN p_product_id INT, -- id sp
    IN p_quantity INT -- sl muốn đặt
)
BEGIN
    -- khai báo biến
    DECLARE v_price DECIMAL(12, 2); -- đơn giá sp
    DECLARE v_stock INT; -- sl hàng ở kho
    DECLARE v_total DECIMAL(12, 2); -- thành tiền
    DECLARE v_invoice_id INT; -- id hđ mới

    -- lỗi xảy ra thì rollback và báo lỗi
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Có lỗi xảy ra, giao dịch bị huỷ!';
        END;

    START TRANSACTION;

    -- lấy đơn giá và số lượng tồn kho của sản phẩm
    SELECT price, stock
    INTO v_price, v_stock
    FROM Product
    WHERE id = p_product_id;


    -- ktra sl tồn kho
    IF v_stock < p_quantity THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Không đủ hàng trong kho!';
    END IF;

    -- tổng tiền đơn hàng
    SET v_total = v_price * p_quantity;

    -- chi tiết hóa đơn
    INSERT INTO Invoice (customer_id, total_amount)
    VALUES (p_customer_id, v_total);

    -- hd vừa tạo
    SET v_invoice_id = LAST_INSERT_ID();


    -- thêm chi tiết hđ
    INSERT INTO Invoice_Details (invoice_id, product_id, quantity, unit_price)
    VALUES (v_invoice_id, p_product_id, p_quantity, v_price);

    -- cập nhật lại tồn kho
    UPDATE Product
    SET stock = stock - p_quantity
    WHERE id = p_product_id;
    COMMIT;
END$$

DELIMITER ;
