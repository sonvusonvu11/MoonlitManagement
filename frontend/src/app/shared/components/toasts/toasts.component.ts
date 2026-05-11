import { Component, inject } from '@angular/core';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-toasts',
  standalone: true,
  template: `
    <div class="toast-stack" aria-live="polite">
      @for (t of toast.toasts(); track t.id) {
        <div
          class="toast show align-items-center text-bg-{{ t.type }} border-0 shadow"
          role="alert"
        >
          <div class="d-flex">
            <div class="toast-body">{{ t.message }}</div>
            <button
              type="button"
              class="btn-close btn-close-white me-2 m-auto"
              aria-label="Close"
              (click)="toast.dismiss(t.id)"
            ></button>
          </div>
        </div>
      }
    </div>
  `,
})
export class ToastsComponent {
  readonly toast = inject(ToastService);
}
