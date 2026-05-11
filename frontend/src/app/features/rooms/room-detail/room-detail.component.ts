import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { RoomService } from '../../../core/services/room.service';
import { Room } from '../../../shared/models/room.model';

@Component({
  selector: 'app-room-detail',
  standalone: true,
  imports: [RouterLink],
  template: `
    <div class="container my-5">
      <a routerLink="/rooms" class="btn btn-link mb-3">
        <i class="bi bi-arrow-left"></i> Quay lại danh sách
      </a>

      @if (loading()) {
        <div class="text-center py-5">
          <div class="spinner-border text-primary"></div>
        </div>
      } @else if (room()) {
        <div class="card shadow-sm">
          <div class="row g-0">
            <div class="col-md-5">
              <div
                style="height: 100%; min-height: 300px;
                       background: linear-gradient(45deg, #6610f2, #0d6efd);
                       display: flex; align-items: center; justify-content: center;
                       color: white; font-size: 6rem;
                       border-radius: .375rem 0 0 .375rem;"
              >
                <i class="bi bi-house-door-fill"></i>
              </div>
            </div>
            <div class="col-md-7">
              <div class="card-body p-4">
                <h2>Phòng #{{ room()!.roomNumber }}</h2>
                <span class="badge badge-status-{{ room()!.status }}">
                  {{ room()!.status }}
                </span>

                <table class="table mt-3">
                  <tr>
                    <th>Loại phòng:</th>
                    <td>{{ room()!.typeName }}</td>
                  </tr>
                  <tr>
                    <th>Mô tả:</th>
                    <td>{{ room()!.typeDescription }}</td>
                  </tr>
                  <tr>
                    <th>Sức chứa:</th>
                    <td>{{ room()!.capacity }} khách</td>
                  </tr>
                  <tr>
                    <th>Khách sạn:</th>
                    <td>{{ room()!.hotelName }}</td>
                  </tr>
                  <tr>
                    <th>Giá:</th>
                    <td>
                      <span class="price-tag">\${{ room()!.pricePerNight }}/đêm</span>
                    </td>
                  </tr>
                </table>

                @if (auth.isAuthenticated()) {
                  <a
                    [routerLink]="['/booking', room()!.roomNumber]"
                    class="btn btn-primary btn-lg"
                  >
                    <i class="bi bi-calendar-plus"></i> Đặt phòng
                  </a>
                } @else {
                  <a routerLink="/login" class="btn btn-primary btn-lg">
                    Đăng nhập để đặt
                  </a>
                }
              </div>
            </div>
          </div>
        </div>
      } @else {
        <div class="alert alert-warning text-center">Không tìm thấy phòng.</div>
      }
    </div>
  `,
})
export class RoomDetailComponent implements OnInit {
  readonly auth = inject(AuthService);
  private readonly roomService = inject(RoomService);
  private readonly route = inject(ActivatedRoute);

  readonly room = signal<Room | null>(null);
  readonly loading = signal(true);

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (Number.isNaN(id)) {
      this.loading.set(false);
      return;
    }
    this.roomService.get(id).subscribe({
      next: (r) => {
        this.room.set(r);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }
}
