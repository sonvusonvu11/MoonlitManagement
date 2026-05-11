import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  ChangePasswordRequest,
  UpdateProfileRequest,
  User,
} from '../../shared/models/user.model';

@Injectable({ providedIn: 'root' })
export class ProfileService {
  private readonly http = inject(HttpClient);
  private readonly api = `${environment.apiUrl}/profile`;

  get(): Observable<User> {
    return this.http.get<User>(this.api);
  }

  update(req: UpdateProfileRequest): Observable<User> {
    return this.http.put<User>(this.api, req);
  }

  changePassword(req: ChangePasswordRequest): Observable<void> {
    return this.http.post<void>(`${this.api}/change-password`, req);
  }
}
