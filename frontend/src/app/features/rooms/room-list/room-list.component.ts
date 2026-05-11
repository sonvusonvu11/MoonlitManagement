import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { RoomService } from '../../../core/services/room.service';
import { ToastService } from '../../../core/services/toast.service';
import { Room } from '../../../shared/models/room.model';

@Component({
  selector: 'app-room-list',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <div class="container my-5">
      <h2 class="mb-4">
        <i class="bi bi-door-open"></i>
        @if (filtered()) {
          Phòng trống
        } @else {
          Tất cả phòng
        }
      </h2>

      <div class="card shadow-sm mb-4">
        <div class="card-body">
          <form [formGroup]="filterForm" (ngSubmit)="onFilter()" class="row g-3 align-items-end">
            <div class="col-md-4">
              <label class="form-label">Ngày nhận phòng</label>
              <input type="date" formControlName="checkin" class="form-control" />
            </div>
            <div class="col-md-4">
              <label class="form-label">Ngày trả phòng</label>
              <input type="date" formControlName="checkout" class="form-control" />
            </div>
            <div class="col-md-4">
              <button class="btn btn-primary w-100" [disabled]="loading()">
                <i class="bi bi-search"></i> Tìm phòng trống
              </button>
            </div>
          </form>
        </div>
      </div>

      @if (filtered()) {
        <div class="alert alert-info">
          <i class="bi bi-info-circle"></i> Hiển thị các phòng còn trống từ
          <b>{{ filterForm.value.checkin }}</b> đến
          <b>{{ filterForm.value.checkout }}</b>.
          <button type="button" class="btn btn-link btn-sm p-0 ms-2" (click)="resetFilter()">
            Xem tất cả
          </button>
        </div>
      }

      @if (loading()) {
        <div class="text-center py-5">
          <div class="spinner-border text-primary"></div>
        </div>
      }

      @if (!loading() && rooms().length === 0) {
        <div class="alert alert-warning text-center">Không có phòng nào phù hợp.</div>
      }

      <div class="row g-4">
        @for (room of rooms(); track room.roomNumber) {
          <div class="col-md-6 col-lg-4">
            <div class="card room-card h-100 shadow-sm">
              <div class="room-img-placeholder">
                <i class="bi bi-house-door-fill"></i>
              </div>
              <div class="card-body">
                <div class="d-flex justify-content-between">
                  <h5 class="card-title">Phòng #{{ room.roomNumber }}</h5>
                  <span class="badge badge-status-{{ room.status }}">{{ room.status }}</span>
                </div>
                <p class="text-muted">
                  <i class="bi bi-tag"></i> {{ room.typeName }} |
                  <i class="bi bi-people"></i> {{ room.capacity }}
                </p>
                <p class="small">{{ room.typeDescription }}</p>
                <div class="d-flex justify-content-between align-items-center">
                  <span class="price-tag">\${{ room.pricePerNight }}/đêm</span>
                  <div>
                    <a [routerLink]="['/rooms', room.roomNumber]" class="btn btn-outline-secondary btn-sm">
                      Chi tiết
                    </a>
                    @if (auth.isAuthenticated()) {
                      <a
                        [routerLink]="['/booking', room.roomNumber]"
                        [queryParams]="bookingQueryParams()"
                        class="btn btn-primary btn-sm"
                      >
                        <i class="bi bi-calendar-plus"></i> Đặt
                      </a>
                    } @else {
                      <a routerLink="/login" class="btn btn-outline-primary btn-sm">Đăng nhập</a>
                    }
                  </div>
                </div>
              </div>
            </div>
          </div>
        }
      </div>
    </div>
  `,
})
export class RoomListComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly roomService = inject(RoomService);
  private readonly toast = inject(ToastService);
  readonly auth = inject(AuthService);

  readonly rooms = signal<Room[]>([]);
  readonly loading = signal(true);
  readonly filtered = signal(false);

  readonly filterForm = this.fb.nonNullable.group({
    checkin: [''],
    checkout: [''],
  });

  ngOnInit(): void {
    this.loadAll();
  }

  bookingQueryParams() {
    const v = this.filterForm.value;
    return v.checkin && v.checkout ? { checkin: v.checkin, checkout: v.checkout } : {};
  }

  loadAll(): void {
    this.loading.set(true);
    this.filtered.set(false);
    this.roomService.list().subscribe({
      next: (rooms) => {
        this.rooms.set(rooms);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  resetFilter(): void {
    this.filterForm.reset({ checkin: '', checkout: '' });
    this.loadAll();
  }

  onFilter(): void {
    const { checkin, checkout } = this.filterForm.getRawValue();
    if (!checkin || !checkout) {
      this.toast.warning('Vui lòng chọn cả ngày nhận và trả phòng');
      return;
    }
    if (checkout <= checkin) {
      this.toast.warning('Ngày trả phải sau ngày nhận');
      return;
    }
    this.loading.set(true);
    this.roomService.available(checkin, checkout).subscribe({
      next: (rooms) => {
        this.rooms.set(rooms);
        this.filtered.set(true);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.toast.error('Không tải được danh sách phòng trống');
      },
    });
  }
}
