# Moonlit Hotel Management

Hệ thống quản lý khách sạn full-stack:

- **Backend**: Spring Boot 3.5 + JPA + SQL Server + Spring Security (JWT). Vừa serve REST API (`/api/**`) cho Angular SPA, vừa serve Thymeleaf admin dashboard (`/dashboard/**`).
- **Frontend**: Angular 18 standalone components + Bootstrap 5 cho phần customer-facing (homepage, đặt phòng, hồ sơ).

## Kiến trúc

```
+---------------------+       Bearer JWT       +-------------------------+
| Angular SPA :4200   |  -------------------->  | Spring Boot :8080      |
| (customer-facing)   |                          |  /api/**  (stateless) |
+---------------------+                          |                       |
                                                 |  /dashboard/** (Web)  |
+---------------------+    Cookie session/JWT    |  (Thymeleaf admin)    |
| Browser (admin)     |  -------------------->  +-------------------------+
+---------------------+                                       |
                                                              v
                                                       +-------------+
                                                       | SQL Server  |
                                                       +-------------+
```

## Yêu cầu hệ thống

- JDK 21+
- Maven (đã đi kèm `mvnw`/`mvnw.cmd`)
- Microsoft SQL Server (port 1433)
- Node.js 20 LTS + npm 10+
- (Tuỳ chọn) Postman / Swagger UI để thử API tại `http://localhost:8080/swagger-ui.html`

## Cấu hình DB

Sửa [src/main/resources/application.properties](src/main/resources/application.properties) nếu cần:

```
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=HoltelManagement;...
spring.datasource.username=sa
spring.datasource.password=123
```

DB sẽ được Hibernate tự động `update` khi khởi động. Lần đầu chạy, `DataInitializer` sẽ tạo:

- Tài khoản admin: `admin` / `admin123`
- 1 khách sạn, 3 loại phòng, 10 phòng

## Chạy ở chế độ dev

Cần 2 terminal:

### Terminal 1 - Backend

```bash
./mvnw spring-boot:run
```

Server chạy ở `http://localhost:8080`.

- Thymeleaf admin: <http://localhost:8080/dashboard> (cần login admin)
- Swagger UI: <http://localhost:8080/swagger-ui.html>
- REST API base: `http://localhost:8080/api`

### Terminal 2 - Frontend

```bash
cd frontend
npm install     # chỉ cần lần đầu
npm start
```

Angular dev server chạy ở `http://localhost:4200` và proxy `/api/**` sang backend (xem [frontend/proxy.conf.json](frontend/proxy.conf.json)) nên không bị CORS lúc dev.

## Cấu trúc dự án

```
HotelManagement/
├── pom.xml
├── mvnw, mvnw.cmd
├── src/main/java/org/example/hotelmanagement/
│   ├── HotelManagementApplication.java
│   ├── config/             # CorsConfig, DataInitializer
│   ├── controller/
│   │   ├── api/            # REST controllers cho Angular (/api/**)
│   │   ├── auth/           # AuthController (Thymeleaf form)
│   │   ├── booking/        # Booking + MyBooking controllers (Thymeleaf)
│   │   ├── dashBoard/      # Dashboard admin (Thymeleaf)
│   │   ├── home/           # Trang chủ Thymeleaf
│   │   ├── profile/        # Profile Thymeleaf
│   │   ├── room/           # Rooms Thymeleaf
│   │   └── error/          # GlobalExceptionHandler + RestExceptionHandler
│   ├── dto/
│   │   ├── api/            # LoginRequest, LoginResponse, ApiError, UserSummary, ChangePasswordRequest
│   │   ├── auth/           # RegisterRequest, UpdateProfileRequest
│   │   ├── booking/        # BookingDTO, BookingRequest
│   │   ├── room/, roomType/, guest/, payment/, staff/
│   ├── entity/             # User, Role, Hotel, Room, RoomType, Guest, Booking, Payment, Staff
│   ├── mapper/             # Room, Booking, Payment, User mappers
│   ├── repository/         # 8 Spring Data repositories
│   ├── security/
│   │   ├── SecurityConfig.java                       # 2 SecurityFilterChain
│   │   ├── CustomUserDetailsService.java
│   │   ├── RoleBasedAuthenticationSuccessHandler.java
│   │   └── jwt/
│   │       ├── JwtProperties.java
│   │       ├── JwtService.java
│   │       ├── JwtAuthenticationFilter.java
│   │       └── JwtAuthEntryPoint.java
│   └── service/            # interface + impl cho Booking, Room, RoomType, Guest, Payment, User
├── src/main/resources/
│   ├── application.properties
│   ├── static/             # CSS/JS Thymeleaf (homePage, dashBoard themes)
│   └── templates/
│       ├── layout/, home/, auth/, rooms/, booking/, profile/, dashboard/, error/
└── frontend/               # Angular 18 SPA
    ├── package.json
    ├── angular.json
    ├── proxy.conf.json
    ├── src/
    │   ├── app/
    │   │   ├── core/{guards,interceptors,services}
    │   │   ├── shared/{components,models}
    │   │   ├── features/{home,auth,rooms,booking,profile}
    │   │   ├── app.config.ts
    │   │   ├── app.routes.ts
    │   │   └── app.component.ts
    │   ├── environments/
    │   ├── index.html
    │   ├── main.ts
    │   └── styles.scss
    └── README.md
```

## REST API summary

| Endpoint | Method | Auth | Mô tả |
|----------|--------|------|-------|
| `/api/auth/register` | POST | Public | Đăng ký |
| `/api/auth/login` | POST | Public | Đăng nhập (trả JWT + set cookie) |
| `/api/auth/refresh` | POST | Public | Renew access token bằng refresh token |
| `/api/auth/logout` | POST | Auth | Xoá cookie JWT |
| `/api/auth/me` | GET | Auth | Thông tin user hiện tại |
| `/api/rooms` | GET | Public | Danh sách phòng |
| `/api/rooms/{n}` | GET | Public | Chi tiết phòng |
| `/api/rooms/available?checkin&checkout` | GET | Public | Tìm phòng trống |
| `/api/room-types` | GET | Public | Danh sách loại phòng |
| `/api/bookings` | POST | Auth | Tạo đơn đặt phòng |
| `/api/bookings/me` | GET | Auth | Đơn của tôi |
| `/api/bookings/{id}` | DELETE | Auth | Huỷ đơn |
| `/api/profile` | GET | Auth | Hồ sơ |
| `/api/profile` | PUT | Auth | Cập nhật hồ sơ |
| `/api/profile/change-password` | POST | Auth | Đổi mật khẩu |
| `/api/admin/bookings` | GET | ADMIN/STAFF | Tất cả đơn |
| `/api/admin/guests` | GET | ADMIN/STAFF | Tất cả khách |
| `/api/admin/stats` | GET | ADMIN/STAFF | Tổng hợp dashboard |

## Test API bằng curl

```bash
# Đăng nhập
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Lấy token rồi gọi
TOKEN=eyJhbGciOiJIUzI1NiJ9...
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/auth/me
```

## Build production (tương lai)

Ý tưởng đơn giản: build Angular vào static của Spring Boot.

```bash
cd frontend && npm run build
# copy frontend/dist/frontend/* -> backend/src/main/resources/static/
./mvnw package
```

Sau đó WAR/JAR duy nhất sẽ serve cả 2 phần.

## Bảo mật

- JWT secret cấu hình ở `app.jwt.secret` (đang là demo, **PHẢI ĐỔI khi deploy**).
- Access token TTL: 1h. Refresh token TTL: 7 ngày.
- BCrypt cho mật khẩu.
- CORS chỉ cho phép `http://localhost:4200` mặc định (`app.cors.allowed-origins`).
- Thymeleaf admin chain đặt cookie `JWT_TOKEN`/`JWT_REFRESH` (HttpOnly, SameSite=Lax).
