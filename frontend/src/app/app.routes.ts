import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { guestGuard } from './core/guards/guest.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./features/home/home.component').then((m) => m.HomeComponent),
  },
  {
    path: 'login',
    canActivate: [guestGuard],
    loadComponent: () =>
      import('./features/auth/login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: 'register',
    canActivate: [guestGuard],
    loadComponent: () =>
      import('./features/auth/register/register.component').then(
        (m) => m.RegisterComponent,
      ),
  },
  {
    path: 'rooms',
    loadComponent: () =>
      import('./features/rooms/room-list/room-list.component').then(
        (m) => m.RoomListComponent,
      ),
  },
  {
    path: 'rooms/:id',
    loadComponent: () =>
      import('./features/rooms/room-detail/room-detail.component').then(
        (m) => m.RoomDetailComponent,
      ),
  },
  {
    path: 'booking/:roomNumber',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/booking/booking-form/booking-form.component').then(
        (m) => m.BookingFormComponent,
      ),
  },
  {
    path: 'my-bookings',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/booking/my-bookings/my-bookings.component').then(
        (m) => m.MyBookingsComponent,
      ),
  },
  {
    path: 'profile',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/profile/profile.component').then((m) => m.ProfileComponent),
  },
  { path: '**', redirectTo: '' },
];
