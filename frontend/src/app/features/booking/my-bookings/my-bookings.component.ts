import { DatePipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { BookingService } from '../../../core/services/booking.service';
import { ToastService } from '../../../core/services/toast.service';
import { Booking } from '../../../shared/models/booking.model';

@Component({
  selector: 'app-my-bookings',
  standalone: true,
  imports: [DatePipe, RouterLink],
  template: `
    <div class="container my-5">
      <h2 class="mb-4">
        <i class="bi bi-calendar-check"></i> Đơn đặt phòng của tôi
      </h2>

      @if (loading()) {
        <div class="text-center py-5">
          <div class="spinner-border text-primary"></div>
        </div>
      } @else if (bookings().length === 0) {
        <div class="alert alert-info text-center">
          Bạn chưa có đơn đặt phòng nào.
          <a routerLink="/rooms" class="btn btn-primary btn-sm ms-2">Tìm phòng ngay</a>
        </div>
      } @else {
        <div class="card shadow-sm">
          <div class="table-responsive">
            <table class="table table-hover mb-0 align-middle">
              <thead class="table-light">
                <tr>
                  <th>Mã đơn</th>
                  <th>Phòng</th>
                  <th>Loại phòng</th>
                  <th>Nhận phòng</th>
                  <th>Trả phòng</th>
                  <th>Số đêm</th>
                  <th>Tổng tiền</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                @for (b of bookings(); track b.bookingID) {
                  <tr>
                    <td>#{{ b.bookingID }}</td>
                    <td>{{ b.roomNumber }}</td>
                    <td>{{ b.roomTypeName }}</td>
                    <td>{{ b.checkinDate | date: 'dd/MM/yyyy' }}</td>
                    <td>{{ b.checkoutDate | date: 'dd/MM/yyyy' }}</td>
                    <td>{{ b.nights }}</td>
                    <td><b>\${{ b.totalPrice }}</b></td>
                    <td>
                      <button
                        type="button"
                        class="btn btn-outline-danger btn-sm"
                        [disabled]="cancellingId() === b.bookingID"
                        (click)="onCancel(b.bookingID)"
                      >
                        <i class="bi bi-x-circle"></i> Huỷ
                      </button>
                    </td>
                  </tr>
                }
              </tbody>
            </table>
          </div>
        </div>
      }
    </div>
  `,
})
export class MyBookingsComponent implements OnInit {
  private readonly bookingService = inject(BookingService);
  private readonly toast = inject(ToastService);

  readonly bookings = signal<Booking[]>([]);
  readonly loading = signal(true);
  readonly cancellingId = signal<number | null>(null);

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.bookingService.myBookings().subscribe({
      next: (list) => {
        this.bookings.set(list);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  onCancel(id: number): void {
    if (!confirm('Bạn có chắc muốn huỷ đơn đặt phòng này?')) return;
    this.cancellingId.set(id);
    this.bookingService.cancel(id).subscribe({
      next: () => {
        this.toast.success('Đã huỷ đơn đặt phòng');
        this.cancellingId.set(null);
        this.load();
      },
      error: (err) => {
        this.cancellingId.set(null);
        this.toast.error(err.error?.message || 'Không thể huỷ đơn');
      },
    });
  }
}
