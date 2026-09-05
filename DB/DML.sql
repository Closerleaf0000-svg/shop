--- 商品資料 ---
INSERT INTO "Product" 
(ProductID, ProductName, Price, Quantity) VALUES
('P001', 'osii舒壓按摩椅', 98000, 5),
('P002', '網友最愛起司蛋糕', 1200, 50),
('P003', '真愛密碼項鍊', 8500, 20);


--- 訂單資料 ---
INSERT INTO "Order"
(OrderID, MemberID, Price, PayStatus) VALUES
('Ms20250801186230', 458, 98000, 1),
('Ms20250805157824', 55688, 9700, 0),
('Ms20250805258200', 1713, 2400, 1);


--- 訂單明細 ---
INSERT INTO "OrderDetail"
(OrderItemSN, OrderID, ProductID,Quantity,StandPrice,ItemPrice) VALUES
(1, 'Ms20250801186230', 'P001', 1, 98000, 98000),
(2, 'Ms20250805157824', 'P002', 1, 1200, 1200),
(3, 'Ms20250805157824', 'P003', 1, 8500, 8500),
(4, 'Ms20250805258200', 'P002', 2, 1200, 2400);


--- 會員資訊 ---
--- 唯一管理員 ---
INSERT INTO "Member"
(MemberID, Username, Password, Email, Phone, Role)
VALUES
(1, 'ruler', 'ruler123', 'ruler@test.com', '0912345678', 'RULER');


--- 一般會員 ---
INSERT INTO "Member"
(MemberID, Username, Password, Email, Phone)
VALUES
(2, 'AAA', 'AAA11111', 'AAA@test.com', '0911111111');


--- 購物車 ---
INSERT INTO "Cart"
(CartID, MemberID, ProductID, Quantity) VALUES
(1, 2, 'P001', 1);