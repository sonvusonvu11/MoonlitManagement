import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  template: `
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow-sm">
      <div class="container">
        <a class="navbar-brand" routerLink="/">
          <i class="bi bi-buildings"></i> Moonlit Hotel
        </a>
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#mainNav"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="mainNav">
          <ul class="navbar-nav me-auto">
            <li class="nav-item">
              <a class="nav-link" routerLink="/" routerLinkActive="active" [routerLinkActiveOptions]="{ exact: true }">Trang chủ</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" routerLink="/rooms" routerLinkActive="active">Danh sách phòng</a>
            </li>
            @if (auth.isAuthenticated()) {
              <li class="nav-item">
                <a class="nav-link" routerLink="/my-bookings" routerLinkActive="active">Đơn của tôi</a>
              </li>
            }
            @if (auth.isAdmin()) {
              <li class="nav-item">
                <a class="nav-link text-warning" href="http://localhost:8080/dashboard">
                  <i class="bi bi-speedometer2"></i> Quản trị (Thymeleaf)
                </a>
              </li>
            }
          </ul>
          <ul class="navbar-nav">
            @if (auth.isAuthenticated()) {
              <li class="nav-item dropdown">
                <a
                  class="nav-link dropdown-toggle"
                  data-bs-toggle="dropdown"
                  href="#"
                  role="button"
                  aria-expanded="false"
                >
                  <i class="bi bi-person-circle"></i>
                  {{ auth.user()?.fullName || auth.user()?.username }}
                </a>
                <ul class="dropdown-menu dropdown-menu-end">
                  <li>
                    <a class="dropdown-item" routerLink="/profile">
                      <i class="bi bi-person"></i> Hồ sơ
                    </a>
                  </li>
                  <li>
                    <a class="dropdown-item" routerLink="/my-bookings">
                      <i class="bi bi-calendar-check"></i> Đơn của tôi
                    </a>
                  </li>
                  <li><hr class="dropdown-divider" /></li>
                  <li>
                    <button class="dropdown-item text-danger" type="button" (click)="onLogout()">
                      <i class="bi bi-box-arrow-right"></i> Đăng xuất
                    </button>
                  </li>
                </ul>
              </li>
            } @else {
              <li class="nav-item">
                <a class="nav-link" routerLink="/login">Đăng nhập</a>
              </li>
              <li class="nav-item">
                <a class="btn btn-primary ms-2" routerLink="/register">Đăng ký</a>
              </li>
            }
          </ul>
        </div>
      </div>
    </nav>
  `,
})
export class NavbarComponent {
  readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly toast = inject(ToastService);

  onLogout(): void {
    this.auth.logout().subscribe({
      next: () => {
        this.toast.success('Đăng xuất thành công');
        this.router.navigate(['/']);
      },
      error: () => {
        this.auth.clearSession();
        this.router.navigate(['/']);
      },
    });
  }
}
