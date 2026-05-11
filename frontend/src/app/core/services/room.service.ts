import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Room, RoomType } from '../../shared/models/room.model';

@Injectable({ providedIn: 'root' })
export class RoomService {
  private readonly http = inject(HttpClient);
  private readonly api = `${environment.apiUrl}/rooms`;
  private readonly roomTypeApi = `${environment.apiUrl}/room-types`;

  list(): Observable<Room[]> {
    return this.http.get<Room[]>(this.api);
  }

  get(roomNumber: number): Observable<Room> {
    return this.http.get<Room>(`${this.api}/${roomNumber}`);
  }

  available(checkin: string, checkout: string): Observable<Room[]> {
    const params = new HttpParams().set('checkin', checkin).set('checkout', checkout);
    return this.http.get<Room[]>(`${this.api}/available`, { params });
  }

  listTypes(): Observable<RoomType[]> {
    return this.http.get<RoomType[]>(this.roomTypeApi);
  }
}
