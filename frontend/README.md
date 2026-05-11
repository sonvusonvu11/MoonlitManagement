# Moonlit Hotel - Frontend (Angular 18)

Customer-facing SPA cho hệ thống quản lý khách sạn. Phần admin/dashboard vẫn được render bằng Thymeleaf trong backend.

## Yêu cầu

- Node.js >= 18.19 hoặc 20.x (khuyến nghị 20 LTS)
- npm 10+
- Angular CLI (cài tự động qua devDependency)

## Cài đặt

```bash
cd frontend
npm install
```

## Chạy dev server

```bash
npm start
```

Lệnh này dùng `proxy.conf.json` để forward `/api/**` sang `http://localhost:8080`, do đó **không cần lo CORS lúc dev**.

Mở trình duyệt: <http://localhost:4200>

## Build production

```bash
npm run build
```

Kết quả ở `dist/frontend/`.

## Cấu trúc

```
src/app/
  core/
    guards/         # authGuard, guestGuard
    interceptors/   # authInterceptor (Bearer + auto refresh)
    services/       # AuthService, RoomService, BookingService, ProfileService, ToastService
  shared/
    components/     # NavbarComponent, FooterComponent, ToastsComponent
    models/         # User, Room, Booking interfaces
  features/
    home/           # /
    auth/           # /login, /register
    rooms/          # /rooms, /rooms/:id
    booking/        # /booking/:roomNumber, /my-bookings
    profile/        # /profile
  app.config.ts
  app.routes.ts
  app.component.ts
```

## Tài khoản demo

- Admin: `admin` / `admin123` (được seed bởi `DataInitializer` ở backend, có thể login để mở Thymeleaf dashboard tại `http://localhost:8080/dashboard`)
- Customer: tự đăng ký qua `/register`
