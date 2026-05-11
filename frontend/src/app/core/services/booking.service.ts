import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Booking, BookingRequest } from '../../shared/models/booking.model';

@Injectable({ providedIn: 'root' })
export class BookingService {
  private readonly http = inject(HttpClient);
  private readonly api = `${environment.apiUrl}/bookings`;

  create(req: BookingRequest): Observable<Booking> {
    return this.http.post<Booking>(this.api, req);
  }

  myBookings(): Observable<Booking[]> {
    return this.http.get<Booking[]>(`${this.api}/me`);
  }

  cancel(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }
}
