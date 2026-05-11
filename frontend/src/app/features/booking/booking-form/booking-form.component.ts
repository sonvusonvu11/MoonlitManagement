import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { BookingService } from '../../../core/services/booking.service';
import { RoomService } from '../../../core/services/room.service';
import { ToastService } from '../../../core/services/toast.service';
import { Room } from '../../../shared/models/room.model';

@Component({
  selector: 'app-booking-form',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <div class="container my-5" style="max-width: 720px;">
      <h2 class="mb-4"><i class="bi bi-calendar-plus"></i> Đặt phòng</h2>

      @if (loading()) {
        <div class="text-center py-5">
          <div class="spinner-border text-primary"></div>
        </div>
      } @else if (room()) {
        <div class="card shadow-sm">
          <div class="card-body">
            <h5>
              Phòng #{{ room()!.roomNumber }}
              <span class="badge bg-secondary">{{ room()!.typeName }}</span>
            </h5>
            <p class="text-muted">{{ room()!.typeDescription }}</p>
            <p class="price-tag">\${{ room()!.pricePerNight }}/đêm</p>
            <hr />

            @if (errorMessage) {
              <div class="alert alert-danger">{{ errorMessage }}</div>
            }

            <form [formGroup]="form" (ngSubmit)="onSubmit()">
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label class="form-label">Ngày nhận phòng</label>
                  <input
                    type="date"
                    formControlName="checkinDate"
                    class="form-control"
                  />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label">Ngày trả phòng</label>
                  <input
                    type="date"
                    formControlName="checkoutDate"
                    class="form-control"
                  />
                </div>
              </div>

              <div class="alert alert-info">
                <i class="bi bi-info-circle"></i> Số đêm:
                <b>{{ nights() }}</b> | Tổng tiền dự kiến:
                <b>\${{ totalPrice() }}</b>
              </div>

              <div class="d-flex justify-content-between">
                <a routerLink="/rooms" class="btn btn-outline-secondary">
                  <i class="bi bi-arrow-left"></i> Huỷ
                </a>
                <button
                  type="submit"
                  class="btn btn-primary"
                  [disabled]="form.invalid || submitting"
                >
                  @if (submitting) {
                    <span class="spinner-border spinner-border-sm me-2"></span>
                  } @else {
                    <i class="bi bi-check-circle me-1"></i>
                  }
                  Xác nhận đặt phòng
                </button>
              </div>
            </form>
          </div>
        </div>
      }
    </div>
  `,
})
export class BookingFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly bookingService = inject(BookingService);
  private readonly roomService = inject(RoomService);
  private readonly toast = inject(ToastService);

  readonly room = signal<Room | null>(null);
  readonly loading = signal(true);
  submitting = false;
  errorMessage = '';

  readonly form = this.fb.nonNullable.group({
    checkinDate: ['', Validators.required],
    checkoutDate: ['', Validators.required],
  });

  readonly nights = computed(() => {
    const v = this.formValueSignal();
    if (!v.checkinDate || !v.checkoutDate) return 0;
    const inDate = new Date(v.checkinDate);
    const outDate = new Date(v.checkoutDate);
    if (outDate <= inDate) return 0;
    return Math.round((outDate.getTime() - inDate.getTime()) / 86_400_000);
  });

  readonly totalPrice = computed(() => {
    const r = this.room();
    return r ? this.nights() * r.pricePerNight : 0;
  });

  private formValueSignal = signal({ checkinDate: '', checkoutDate: '' });

  ngOnInit(): void {
    const roomNumber = Number(this.route.snapshot.paramMap.get('roomNumber'));
    const today = new Date();
    const tomorrow = new Date(today.getTime() + 86_400_000);
    const dayAfter = new Date(today.getTime() + 2 * 86_400_000);

    const qp = this.route.snapshot.queryParamMap;
    this.form.patchValue({
      checkinDate: qp.get('checkin') || tomorrow.toISOString().substring(0, 10),
      checkoutDate: qp.get('checkout') || dayAfter.toISOString().substring(0, 10),
    });
    this.formValueSignal.set(this.form.getRawValue());
    this.form.valueChanges.subscribe(() =>
      this.formValueSignal.set(this.form.getRawValue()),
    );

    this.roomService.get(roomNumber).subscribe({
      next: (r) => {
        this.room.set(r);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.toast.error('Không tải được thông tin phòng');
      },
    });
  }

  onSubmit(): void {
    const r = this.room();
    if (!r) return;
    if (this.form.invalid) return;
    if (this.nights() <= 0) {
      this.errorMessage = 'Ngày trả phải sau ngày nhận';
      return;
    }
    this.submitting = true;
    this.errorMessage = '';
    const value = this.form.getRawValue();
    this.bookingService
      .create({
        roomNumber: r.roomNumber,
        checkinDate: value.checkinDate,
        checkoutDate: value.checkoutDate,
      })
      .subscribe({
        next: (b) => {
          this.toast.success(`Đặt phòng thành công! Mã đơn #${b.bookingID}`);
          this.router.navigate(['/my-bookings']);
        },
        error: (err: HttpErrorResponse) => {
          this.submitting = false;
          this.errorMessage =
            err.error?.message || 'Không thể đặt phòng. Vui lòng thử lại.';
        },
      });
  }
}
