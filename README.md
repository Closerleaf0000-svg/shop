Shop 電商網站

一個使用 Vue.js + Spring Boot + MyBatis + PostgreSQL 開發的簡易電商網站。

本專案主要練習前後端分離、RESTful API、資料庫操作、會員登入、購物車、訂單以及基本的權限與資安處理。

---
本專案是一個簡易的電商系統，分為：
* 前端：Vue.js
* 後端：Spring Boot
* 資料庫：PostgreSQL
* ORM / SQL Mapping：MyBatis
* 建置工具：Maven
* 前後端資料傳輸：RESTful API
* Session：會員登入狀態管理

## 前後端資料流
```text
Vue.js
  ↓
Axios
  ↓
RESTful API
  ↓
Spring Boot
  ↓
Controller
  ↓
Service
  ↓
MyBatis
  ↓
PostgreSQL
```

系統分為一般會員與管理員兩種角色。

### 一般會員
可以：
* 註冊帳號
* 登入 / 登出
* 查看商品
* 搜尋商品
* 加入購物車
* 修改購物車商品數量
* 移除購物車商品
* 建立訂單
* 查看自己的訂單

### 管理員
可以：
* 登入管理員帳號
* 查看商品
* 新增商品
* 查看所有訂單

管理員無法：
* 使用購物車
* 加入商品至購物車
* 建立訂單
---


## 使用技術

### Frontend
* Vue 3
* Vite
* JavaScript
* HTML
* CSS
* Vue Router
* Pinia
* Axios

### Backend
* Java 17
* Spring Boot
* Spring MVC
* MyBatis
* Maven
* RESTful API

### Database
* PostgreSQL

### Development Tools
* Visual Studio Code
* DBeaver
* Docker
---


## 專案設定

本專案使用 Maven 管理後端專案與相依套件。

主要使用的 Maven Dependency：

- Spring Boot
- Spring Web
- MyBatis
- PostgreSQL Driver
- Validation
- Lombok

完整的套件與版本設定請參考 `pom.xml`。
---

## 資料庫

### Database SQL
專案的 `DB` 資料夾包含本專案所使用的資料庫 SQL。

內容包含：
- 資料表建立
- Stored Procedure
- 初始 / 測試資料

將 SQL 檔案依序執行於 PostgreSQL，即可建立本專案所需的資料庫環境。
---

### Member
儲存會員資料：
* MemberID
* Username
* Password
* Email
* Phone
* Role

### Product
儲存商品資料：
* ProductID
* ProductName
* Price
* Quantity

### Cart
儲存購物車資料：
* CartID
* MemberID
* ProductID
* Quantity

### Order
儲存訂單資料：
* OrderID
* MemberID
* Price
* PayStatus

### OrderDetail
儲存訂單明細：
* OrderItemSN
* OrderID
* ProductID
* Quantity
* StandPrice
* ItemPrice
---


## 系統流程
### 會員登入
```text
Vue
 ↓
Axios
 ↓
Spring Boot
 ↓
MemberService
 ↓
MyBatis
 ↓
PostgreSQL
```

登入成功後使用 Session 保存：
```text
memberId
username
email
role
```
---


### 購物車
```text
商品列表
   ↓
查看商品
   ↓
加入購物車
   ↓
Cart
   ↓
修改 / 移除商品
```
購物車資料會透過 `MemberID` 與會員關聯，避免不同會員看到彼此的購物車。
---


### 建立訂單

```text
購物車
   ↓
確認訂單
   ↓
Spring Boot
   ↓
確認商品是否存在
   ↓
確認數量是否正確
   ↓
確認庫存
   ↓
計算商品金額
   ↓
建立 Order
   ↓
建立 OrderDetail
   ↓
扣除 Product 庫存
```

建立訂單時使用：
```java
@Transactional
```
確保訂單、訂單明細與庫存更新可以一起成功或一起回滾。
---


## 基本資安處理
本專案有實作基本的資安防護。
### 1. DTO

前端不直接將 Entity 當作所有 API 的輸入資料。

例如註冊使用：
```text
RegisterRequest
```

只接收需要的資料：
```text
username
password
confirmPassword
email
phone
```

管理員角色不由前端傳入，而是由後端強制設定：
```text
USER
```
---

### 2. 輸入驗證
使用 Jakarta Validation：
```java
@NotBlank
@NotNull
@Email
@Pattern
@Min
@Max
```

例如：
```text
密碼：
6～20 碼
至少一個英文字母
至少一個數字

手機：
09 開頭的 10 位數字
```
---


### 3. SQL Injection
MyBatis 使用：
```xml
#{keyword}
```

而不是：
```xml
${keyword}
```
讓使用者輸入的內容不會直接被當成 SQL 語法執行。
---


### 4. XSS
Vue 顯示使用：
```vue
{{ product.productName }}
```

避免直接使用：
```vue
v-html
```
以降低惡意 HTML / JavaScript 被直接插入頁面的風險。
---


### 5. Session 權限控制
後端會確認目前登入者的角色。
例如管理員：
```text
RULER
```

才能新增商品及查看所有訂單。
一般會員：
```text
USER
```
才能使用購物車及建立訂單。
權限判斷由 **後端** 執行，不只依靠 Vue 隱藏按鈕。
---


## Stored Procedure
資料庫部分使用 PostgreSQL Stored Procedure 處理部分資料異動。

例如：
```text
register_member
add_product
add_to_cart
create_order
add_order_detail
decrease_product_quantity
```

MyBatis 再透過 Callable Statement 呼叫 Stored Procedure。
例如：
```xml
<insert id="addProduct" statementType="CALLABLE">
    CALL add_product(
        #{productId},
        #{productName},
        #{price},
        #{quantity}
    )
</insert>
```
---


## 執行方式

### 1. 建立 PostgreSQL 資料庫

建立：

```text
shop_db
```

再執行：

```text
DB
```

資料夾中的 SQL 與 Stored Procedure。
---


### 2. 啟動 Spring Boot
進入後端專案：
```bash
mvn spring-boot:run
```

或使用：
```bash
mvn clean package
```

產生 JAR 後執行。

Backend 預設：
```text
http://localhost:8080
```
---


### 3. 啟動 Vue

進入：

```text
vue
```

執行：
```bash
npm install
npm run dev
```

Frontend 預設：
```text
http://localhost:5173
```
---


## 測試帳號

### 管理員
```text
帳號：ruler
密碼：ruler123
角色：RULER
```

### 一般會員
可以透過註冊頁面自行建立：
```text
角色：USER
```

註冊時不需要輸入角色，後端會自動設定為 `USER`。
---


## API 範例

### 取得商品

```http
GET /api/product/list
```

### 搜尋商品

```http
GET /api/product/search?keyword=蛋糕
```

### 登入

```http
POST /api/login
```

```json
{
  "username": "AAA",
  "password": "AAA11111"
}
```

### 加入購物車

```http
POST /api/cart
```

```json
{
  "productId": "P001",
  "quantity": 1
}
```

### 建立訂單

```http
POST /api/order
```

```json
{
  "orderId": "TEST20260905001",
  "payStatus": 0,
  "orderDetails": [
    {
      "productId": "P001",
      "quantity": 1
    },
    {
      "productId": "P002",
      "quantity": 2
    }
  ]
}
```
---


## 開發目的

本專案主要用於學習與實作：
* Java
* Spring Boot
* Spring MVC
* MyBatis
* PostgreSQL
* Vue.js
* RESTful API
* 前後端分離
* Session 登入
* 角色權限
* DTO
* SQL Injection 防護
* XSS 基本防護
* Stored Procedure
* Transaction
* 電商系統基本流程
---

Java / Spring Boot / Vue.js 學習專案
