import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { RoomService } from '../../core/services/room.service';
import { Room } from '../../shared/models/room.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink],
  template: `
    <div class="hero">
      <div class="container text-center">
        <h1 class="display-4 fw-bold">Chào mừng đến với Moonlit Hotel</h1>
        <p class="lead">
          Trải nghiệm dịch vụ khách sạn 5 sao - Đặt phòng nhanh chóng, dễ dàng
        </p>
        @if (auth.isAuthenticated()) {
          <div class="alert alert-success d-inline-block mt-3">
            Xin chào <b>{{ auth.user()?.fullName }}</b>! Hãy chọn phòng yêu thích.
          </div>
        }
        <div class="mt-4">
          <a routerLink="/rooms" class="btn btn-light btn-lg me-2">
            <i class="bi bi-search"></i> Tìm phòng trống
          </a>
          @if (!auth.isAuthenticated()) {
            <a routerLink="/register" class="btn btn-outline-light btn-lg">
              <i class="bi bi-person-plus"></i> Đăng ký ngay
            </a>
          }
        </div>
      </div>
    </div>

    <div class="container my-5">
      <h2 class="mb-4 text-center">Phòng nổi bật</h2>

      @if (loading()) {
        <div class="text-center py-5">
          <div class="spinner-border text-primary"></div>
        </div>
      }

      @if (!loading() && rooms().length === 0) {
        <div class="alert alert-info text-center">Chưa có phòng nào.</div>
      }

      <div class="row g-4">
        @for (room of rooms(); track room.roomNumber) {
          <div class="col-md-6 col-lg-4">
            <div class="card room-card h-100 shadow-sm">
              <div class="room-img-placeholder">
                <i class="bi bi-house-door-fill"></i>
              </div>
              <div class="card-body">
                <div class="d-flex justify-content-between align-items-start">
                  <h5 class="card-title">Phòng #{{ room.roomNumber }}</h5>
                  <span class="badge badge-status-{{ room.status }}">
                    {{ room.status }}
                  </span>
                </div>
                <p class="text-muted mb-2">
                  <i class="bi bi-tag"></i> {{ room.typeName }} -
                  <i class="bi bi-people"></i> {{ room.capacity }} khách
                </p>
                <p class="card-text small">{{ room.typeDescription }}</p>
                <div class="d-flex justify-content-between align-items-center mt-3">
                  <span class="price-tag">\${{ room.pricePerNight }}/đêm</span>
                  @if (auth.isAuthenticated()) {
                    <a
                      [routerLink]="['/booking', room.roomNumber]"
                      class="btn btn-primary btn-sm"
                    >
                      <i class="bi bi-calendar-plus"></i> Đặt ngay
                    </a>
                  } @else {
                    <a routerLink="/login" class="btn btn-outline-primary btn-sm">
                      Đăng nhập để đặt
                    </a>
                  }
                </div>
              </div>
            </div>
          </div>
        }
      </div>
    </div>
  `,
})
export class HomeComponent implements OnInit {
  readonly auth = inject(AuthService);
  private readonly roomService = inject(RoomService);

  readonly rooms = signal<Room[]>([]);
  readonly loading = signal(true);

  ngOnInit(): void {
    this.roomService.list().subscribe({
      next: (rooms) => {
        this.rooms.set(rooms.slice(0, 6));
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }
}
