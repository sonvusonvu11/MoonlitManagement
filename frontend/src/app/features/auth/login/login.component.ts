import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <div class="container my-5" style="max-width: 480px;">
      <div class="card shadow-sm">
        <div class="card-body p-4">
          <div class="text-center mb-4">
            <i class="bi bi-buildings text-primary" style="font-size: 3rem;"></i>
            <h3 class="mt-2">Đăng nhập</h3>
            <p class="text-muted">Đăng nhập để đặt phòng và quản lý đơn của bạn</p>
          </div>

          @if (errorMessage) {
            <div class="alert alert-danger">
              <i class="bi bi-exclamation-triangle"></i> {{ errorMessage }}
            </div>
          }

          <form [formGroup]="form" (ngSubmit)="onSubmit()">
            <div class="mb-3">
              <label class="form-label">Tên đăng nhập hoặc Email</label>
              <div class="input-group">
                <span class="input-group-text"><i class="bi bi-person"></i></span>
                <input
                  type="text"
                  class="form-control"
                  formControlName="username"
                  autofocus
                />
              </div>
            </div>
            <div class="mb-3">
              <label class="form-label">Mật khẩu</label>
              <div class="input-group">
                <span class="input-group-text"><i class="bi bi-lock"></i></span>
                <input
                  type="password"
                  class="form-control"
                  formControlName="password"
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
                <i class="bi bi-box-arrow-in-right me-1"></i>
              }
              Đăng nhập
            </button>
          </form>

          <hr />
          <p class="text-center mb-0">
            Chưa có tài khoản?
            <a routerLink="/register" class="text-decoration-none">Đăng ký ngay</a>
          </p>
          <div class="alert alert-info mt-3 small mb-0">
            <b>Tài khoản admin demo:</b> <code>admin</code> / <code>admin123</code>
          </div>
        </div>
      </div>
    </div>
  `,
})
export class LoginComponent {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly toast = inject(ToastService);

  loading = false;
  errorMessage = '';

  readonly form = this.fb.nonNullable.group({
    username: ['', Validators.required],
    password: ['', Validators.required],
  });

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.errorMessage = '';
    this.auth.login(this.form.getRawValue()).subscribe({
      next: () => {
        this.toast.success('Đăng nhập thành công');
        const returnUrl =
          this.route.snapshot.queryParamMap.get('returnUrl') || '/';
        this.router.navigateByUrl(returnUrl);
      },
      error: (err: HttpErrorResponse) => {
        this.loading = false;
        this.errorMessage =
          err.error?.message || 'Đăng nhập thất bại. Vui lòng thử lại.';
      },
    });
  }
}
