import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <div class="container my-5" style="max-width: 560px;">
      <div class="card shadow-sm">
        <div class="card-body p-4">
          <div class="text-center mb-4">
            <i class="bi bi-person-plus text-primary" style="font-size: 3rem;"></i>
            <h3 class="mt-2">Đăng ký tài khoản</h3>
            <p class="text-muted">Tạo tài khoản để bắt đầu đặt phòng</p>
          </div>

          @if (errorMessage) {
            <div class="alert alert-danger">{{ errorMessage }}</div>
          }

          <form [formGroup]="form" (ngSubmit)="onSubmit()">
            <div class="mb-3">
              <label class="form-label">Họ tên</label>
              <input type="text" class="form-control" formControlName="fullName" />
            </div>
            <div class="row">
              <div class="col-md-6 mb-3">
                <label class="form-label">Tên đăng nhập</label>
                <input type="text" class="form-control" formControlName="username" />
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label">Email</label>
                <input type="email" class="form-control" formControlName="email" />
              </div>
            </div>
            <div class="mb-3">
              <label class="form-label">Số điện thoại</label>
              <input type="text" class="form-control" formControlName="phone" />
            </div>
            <div class="row">
              <div class="col-md-6 mb-3">
                <label class="form-label">Mật khẩu</label>
                <input
                  type="password"
                  class="form-control"
                  formControlName="password"
                />
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label">Xác nhận mật khẩu</label>
                <input
                  type="password"
                  class="form-control"
                  formControlName="confirmPassword"
                />
              </div>
            </div>
            <button
              type="submit"
              class="btn btn-primary w-100"
              [disabled]="form.invalid || loading"
            >
              @if (loading) {
                <span class="spinner-border spinner-border-sm me-2"></span>
              } @else {
                <i class="bi bi-person-plus me-1"></i>
              }
              Đăng ký
            </button>
          </form>

          <hr />
          <p class="text-center mb-0">
            Đã có tài khoản?
            <a routerLink="/login" class="text-decoration-none">Đăng nhập</a>
          </p>
        </div>
      </div>
    </div>
  `,
})
export class RegisterComponent {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly toast = inject(ToastService);

  loading = false;
  errorMessage = '';

  readonly form = this.fb.nonNullable.group({
    fullName: ['', Validators.required],
    username: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    phone: [''],
    password: ['', [Validators.required, Validators.minLength(6)]],
    confirmPassword: ['', Validators.required],
  });

  onSubmit(): void {
    if (this.form.invalid) return;
    const value = this.form.getRawValue();
    if (value.password !== value.confirmPassword) {
      this.errorMessage = 'Mật khẩu xác nhận không khớp';
      return;
    }
    this.loading = true;
    this.errorMessage = '';
    this.auth.register(value).subscribe({
      next: () => {
        this.toast.success('Đăng ký thành công, mời bạn đăng nhập');
        this.router.navigate(['/login']);
      },
      error: (err: HttpErrorResponse) => {
        this.loading = false;
        this.errorMessage =
          err.error?.message ||
          (err.error?.fieldErrors
            ? Object.values(err.error.fieldErrors).join(', ')
            : 'Đăng ký thất bại');
      },
    });
  }
}
