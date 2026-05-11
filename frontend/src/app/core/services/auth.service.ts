import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  User,
} from '../../shared/models/user.model';

const ACCESS_KEY = 'hm_access';
const REFRESH_KEY = 'hm_refresh';
const USER_KEY = 'hm_user';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly api = `${environment.apiUrl}/auth`;

  private readonly _user = signal<User | null>(this.readStoredUser());
  readonly user = this._user.asReadonly();
  readonly isAuthenticated = computed(() => this._user() !== null);
  readonly isAdmin = computed(() => {
    const u = this._user();
    return u?.role === 'ADMIN' || u?.role === 'STAFF';
  });

  login(req: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.api}/login`, req).pipe(
      tap((res) => this.persistSession(res)),
    );
  }

  register(req: RegisterRequest): Observable<User> {
    return this.http.post<User>(`${this.api}/register`, req);
  }

  refresh(): Observable<LoginResponse> {
    const refreshToken = this.getRefreshToken();
    return this.http
      .post<LoginResponse>(`${this.api}/refresh`, { refreshToken })
      .pipe(tap((res) => this.persistSession(res)));
  }

  logout(): Observable<void> {
    return this.http.post<void>(`${this.api}/logout`, {}).pipe(
      tap(() => this.clearSession()),
    );
  }

  fetchCurrentUser(): Observable<User> {
    return this.http
      .get<User>(`${this.api}/me`)
      .pipe(tap((u) => this.setUser(u)));
  }

  getAccessToken(): string | null {
    return localStorage.getItem(ACCESS_KEY);
  }

  getRefreshToken(): string | null {
    return localStorage.getItem(REFRESH_KEY);
  }

  private persistSession(res: LoginResponse): void {
    localStorage.setItem(ACCESS_KEY, res.accessToken);
    localStorage.setItem(REFRESH_KEY, res.refreshToken);
    this.setUser(res.user);
  }

  private setUser(user: User): void {
    localStorage.setItem(USER_KEY, JSON.stringify(user));
    this._user.set(user);
  }

  clearSession(): void {
    localStorage.removeItem(ACCESS_KEY);
    localStorage.removeItem(REFRESH_KEY);
    localStorage.removeItem(USER_KEY);
    this._user.set(null);
  }

  private readStoredUser(): User | null {
    try {
      const raw = localStorage.getItem(USER_KEY);
      return raw ? (JSON.parse(raw) as User) : null;
    } catch {
      return null;
    }
  }
}
