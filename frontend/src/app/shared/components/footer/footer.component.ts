import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  standalone: true,
  template: `
    <footer>
      <div class="container">
        <div class="row">
          <div class="col-md-6">
            <h5 class="text-white">Moonlit Hotel</h5>
            <p class="mb-0">Hotel management system - Spring Boot + Angular</p>
          </div>
          <div class="col-md-6 text-md-end">
            <p class="mb-0">
              <i class="bi bi-telephone"></i> 0123 456 789 |
              <i class="bi bi-envelope"></i> contact&#64;moonlit.local
            </p>
            <small>&copy; 2026 Moonlit Hotel.</small>
          </div>
        </div>
      </div>
    </footer>
  `,
})
export class FooterComponent {}
