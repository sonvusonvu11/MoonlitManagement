import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../../core/services/auth.service';
import { ProfileService } from '../../core/services/profile.service';
import { ToastService } from '../../core/services/toast.service';
import { User } from '../../shared/models/user.model';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [ReactiveFormsModule],
  template: `
    <div class="container my-5">
      <h2 class="mb-4"><i class="bi bi-person-circle"></i> Hồ sơ cá nhân</h2>

      @if (loading()) {
        <div class="text-center py-5">
          <div class="spinner-border text-primary"></div>
        </div>
      } @else if (user()) {
        <div class="row g-4">
          <div class="col-md-4">
            <div class="card shadow-sm text-center">
              <div class="card-body">
                <i class="bi bi-person-circle text-primary" style="font-size: 5rem;"></i>
                <h4 class="mt-2">{{ user()!.fullName }}</h4>
                <p class="text-muted mb-1">
                  <i class="bi bi-at"></i> {{ user()!.username }}
                </p>
                <span class="badge bg-primary">{{ user()!.role }}</span>
                <hr />
                <p class="mb-1 small">
                  <i class="bi bi-envelope"></i> {{ user()!.email }}
                </p>
                @if (user()!.phone) {
                  <p class="mb-0 small">
                    <i class="bi bi-telephone"></i> {{ user()!.phone }}
                  </p>
                }
              </div>
            </div>
          </div>

          <div class="col-md-8">
            <div class="card shadow-sm mb-4">
              <div class="card-header"><b>Thông tin cá nhân</b></div>
              <div class="card-body">
                <form [formGroup]="profileForm" (ngSubmit)="onSaveProfile()">
                  <div class="row">
                    <div class="col-md-6 mb-3">
                      <label class="form-label">Họ tên</label>
                      <input class="form-control" formControlName="fullName" />
                    </div>
                    <div class="col-md-6 mb-3">
                      <label class="form-label">Email</label>
                      <input class="form-control" formControlName="email" />
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-6 mb-3">
                      <label class="form-label">Điện thoại</label>
                      <input class="form-control" formControlName="phone" />
                    </div>
                    <div class="col-md-6 mb-3">
                      <label class="form-label">Ngày sinh</label>
                      <input
                        type="date"
                        class="form-control"
                        formControlName="dateOfBirth"
                      />
                    </div>
                  </div>
                  <div class="mb-3">
                    <label class="form-label">Địa chỉ</label>
                    <input class="form-control" formControlName="address" />
                  </div>
                  <button
                    class="btn btn-primary"
                    type="submit"
                    [disabled]="profileForm.invalid || savingProfile"
                  >
                    @if (savingProfile) {
                      <span class="spinner-border spinner-border-sm me-2"></span>
                    } @else {
                      <i class="bi bi-save me-1"></i>
                    }
                    Lưu thay đổi
                  </button>
                </form>
              </div>
            </div>

            <div class="card shadow-sm">
              <div class="card-header"><b>Đổi mật khẩu</b></div>
              <div class="card-body">
                <form [formGroup]="passwordForm" (ngSubmit)="onChangePassword()">
                  <div class="mb-3">
                    <label class="form-label">Mật khẩu hiện tại</label>
                    <input
                      type="password"
                      class="form-control"
                      formControlName="oldPassword"
                    />
                  </div>
                  <div class="row">
                    <div class="col-md-6 mb-3">
                      <label class="form-label">Mật khẩu mới</label>
                      <input
                        type="password"
                        class="form-control"
                        formControlName="newPassword"
                      />
                    </div>
                    <div class="col-md-6 mb-3">
                      <label class="form-label">Xác nhận mật khẩu mới</label>
                      <input
                        type="password"
                        class="form-control"
                        formControlName="confirmPassword"
                      />
                    </div>
                  </div>
                  <button
                    class="btn btn-warning"
                    type="submit"
                    [disabled]="passwordForm.invalid || changingPassword"
                  >
                    @if (changingPassword) {
                      <span class="spinner-border spinner-border-sm me-2"></span>
                    } @else {
                      <i class="bi bi-key me-1"></i>
                    }
                    Đổi mật khẩu
                  </button>
                </form>
              </div>
            </div>
          </div>
        </div>
      }
    </div>
  `,
})
export class ProfileComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly profileService = inject(ProfileService);
  private readonly auth = inject(AuthService);
  private readonly toast = inject(ToastService);

  readonly user = signal<User | null>(null);
  readonly loading = signal(true);
  savingProfile = false;
  changingPassword = false;

  readonly profileForm = this.fb.nonNullable.group({
    fullName: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    phone: [''],
    address: [''],
    dateOfBirth: [''],
  });

  readonly passwordForm = this.fb.nonNullable.group({
    oldPassword: ['', Validators.required],
    newPassword: ['', [Validators.required, Validators.minLength(6)]],
    confirmPassword: ['', Validators.required],
  });

  ngOnInit(): void {
    this.profileService.get().subscribe({
      next: (u) => {
        this.user.set(u);
        this.profileForm.patchValue({
          fullName: u.fullName,
          email: u.email,
          phone: u.phone || '',
        });
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  onSaveProfile(): void {
    if (this.profileForm.invalid) return;
    this.savingProfile = true;
    this.profileService.update(this.profileForm.getRawValue()).subscribe({
      next: (u) => {
        this.user.set(u);
        this.auth.fetchCurrentUser().subscribe();
        this.toast.success('Cập nhật hồ sơ thành công');
        this.savingProfile = false;
      },
      error: (err: HttpErrorResponse) => {
        this.savingProfile = false;
        this.toast.error(err.error?.message || 'Cập nhật thất bại');
      },
    });
  }

  onChangePassword(): void {
    if (this.passwordForm.invalid) return;
    const v = this.passwordForm.getRawValue();
    if (v.newPassword !== v.confirmPassword) {
      this.toast.error('Mật khẩu xác nhận không khớp');
      return;
    }
    this.changingPassword = true;
    this.profileService
      .changePassword({ oldPassword: v.oldPassword, newPassword: v.newPassword })
      .subscribe({
        next: () => {
          this.toast.success('Đổi mật khẩu thành công');
          this.passwordForm.reset();
          this.changingPassword = false;
        },
        error: (err: HttpErrorResponse) => {
          this.changingPassword = false;
          this.toast.error(err.error?.message || 'Đổi mật khẩu thất bại');
        },
      });
  }
}
