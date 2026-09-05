---新增商品---
CREATE OR REPLACE PROCEDURE add_product(
    IN p_product_id VARCHAR(20),
    IN p_product_name VARCHAR(100),
    IN p_price INT,
    IN p_quantity INT
)
LANGUAGE plpgsql
AS $$
BEGIN

    INSERT INTO "Product" (
        ProductID,
        ProductName,
        Price,
        Quantity
    )
    VALUES (
        p_product_id,
        p_product_name,
        p_price,
        p_quantity
    );

END;
$$;

---建立訂單---
CREATE OR REPLACE PROCEDURE create_order(
    IN p_order_id VARCHAR(100),
    IN p_member_id BIGINT,
    IN p_price INT,
    IN p_pay_status INT
)
LANGUAGE plpgsql
AS $$
BEGIN

    INSERT INTO "Order" (
        OrderID,
        MemberID,
        Price,
        PayStatus
    )
    VALUES (
        p_order_id,
        p_member_id,
        p_price,
        p_pay_status
    );

END;
$$;


--- 加入購物車 ---
CREATE OR REPLACE PROCEDURE add_to_cart(
    IN in_member_id BIGINT,
    IN in_product_id VARCHAR(100),
    IN in_quantity INT
)
LANGUAGE plpgsql
AS $$
BEGIN

    IF in_quantity <= 0 THEN
        RAISE EXCEPTION '商品數量必須大於 0';
    END IF;

    INSERT INTO "Cart" (
        MemberID,
        ProductID,
        Quantity
    )
    VALUES (
        in_member_id,
        in_product_id,
        in_quantity
    )

    ON CONFLICT (MemberID, ProductID)
    DO UPDATE
    SET Quantity = "Cart".Quantity + in_quantity;

END;
$$;


---扣除商品庫存---
CREATE OR REPLACE PROCEDURE decrease_product_quantity(
    IN p_product_id VARCHAR(100),
    IN p_quantity INT
)
LANGUAGE plpgsql
AS $$
BEGIN

    UPDATE "Product"
    SET Quantity = Quantity - p_quantity
    WHERE ProductID = p_product_id
      AND Quantity >= p_quantity;

    IF NOT FOUND THEN
        RAISE EXCEPTION '商品不存在或庫存不足: %', p_product_id;
    END IF;

END;
$$;


--- 會員註冊 ---
CREATE OR REPLACE PROCEDURE register_member(
    IN p_username VARCHAR(100),
    IN p_password VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_phone VARCHAR(20)
)
LANGUAGE plpgsql
AS $$
BEGIN

    INSERT INTO "Member" (
        Username,
        Password,
        Email,
        Phone,
        Role
    )
    VALUES (
        p_username,
        p_password,
        p_email,
        p_phone,
        'USER'
    );

END;
$$;


--- 會員登入 ---
CREATE OR REPLACE FUNCTION login_member(
    p_username VARCHAR(100),
    p_password VARCHAR(100)
)
RETURNS TABLE (
    MemberID BIGINT,
    Username VARCHAR(100),
    Email VARCHAR(100),
    Phone VARCHAR(20),
    Role VARCHAR(100)
)
LANGUAGE plpgsql
AS $$
BEGIN

    RETURN QUERY
    SELECT
        m.MemberID,
        m.Username,
        m.Email,
        m.Phone,
        m.Role
    FROM "Member" m
    WHERE m.Username = p_username
      AND m.Password = p_password;

END;
$$;


--- 加入購物車 ---
CREATE OR REPLACE PROCEDURE add_to_cart(
    IN in_member_id BIGINT,
    IN in_product_id VARCHAR(100),
    IN in_quantity INT
)
LANGUAGE plpgsql
AS $$
BEGIN

    INSERT INTO "Cart" (
        CartID,
        MemberID,
        ProductID,
        Quantity
    )
    VALUES (
        p_cart_id,
        p_member_id,
        p_product_id,
        p_quantity
    )
    ON CONFLICT (MemberID, ProductID)
    DO UPDATE
    SET Quantity = "Cart".Quantity + p_quantity;

END;
$$;


--- 修改購物車數量 ---
CREATE OR REPLACE PROCEDURE update_cart_quantity(
    IN p_member_id BIGINT,
    IN p_product_id VARCHAR(100),
    IN p_quantity INT
)
LANGUAGE plpgsql
AS $$
BEGIN

    UPDATE "Cart"
    SET Quantity = p_quantity
    WHERE MemberID = p_member_id
      AND ProductID = p_product_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION '購物車中找不到此商品: %', p_product_id;
    END IF;

END;
$$;


--- 商品數量 +1 ---
CREATE OR REPLACE PROCEDURE increase_cart_quantity(
    IN p_member_id BIGINT,
    IN p_product_id VARCHAR(100)
)
LANGUAGE plpgsql
AS $$
BEGIN

    UPDATE "Cart"
    SET Quantity = Quantity + 1
    WHERE MemberID = p_member_id
      AND ProductID = p_product_id;

END;
$$;


--- 商品數量 -1 ---
CREATE OR REPLACE PROCEDURE decrease_cart_quantity(
    IN p_member_id BIGINT,
    IN p_product_id VARCHAR(100)
)
LANGUAGE plpgsql
AS $$
BEGIN

    UPDATE "Cart"
    SET Quantity = Quantity - 1
    WHERE MemberID = p_member_id
      AND ProductID = p_product_id
      AND Quantity > 1;

END;
$$;


--- 從購物車移除指定商品 ---
CREATE OR REPLACE PROCEDURE delete_cart_item(
    IN p_member_id BIGINT,
    IN p_product_id VARCHAR(100)
)
LANGUAGE plpgsql
AS $$
BEGIN

    DELETE FROM "Cart"
    WHERE MemberID = p_member_id
      AND ProductID = p_product_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION '購物車中找不到此商品: %', p_product_id;
    END IF;

END;
$$;