import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {
  username = '';
  password = '';
  error = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ingresar() {
    this.error = '';

    this.authService.login(this.username, this.password)
      .subscribe({
        next: (resp) => {
          this.authService.guardarSesion(resp, this.password);
          this.router.navigate(['/ciudadanos']);
        },
        error: (err) => {
          console.error(err);
          this.error = 'Usuario o contraseña incorrectos';
        }
      });
  }
}
