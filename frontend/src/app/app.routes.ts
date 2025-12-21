import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { CiudadanosComponent} from './components/ciudadano/ciudadano';
import { authGuard } from './guards/auth-guard';
import { RegistrarCiudadanoComponent } from './components/ciudadano/registrar-ciudadano/registrar-ciudadano';

export const routes: Routes = [
  { path: '', component: LoginComponent },
  {
    path: 'ciudadanos',
    component: CiudadanosComponent,
    canActivate: [authGuard]
  },
  {
    path: 'registrar-ciudadano',
    component: RegistrarCiudadanoComponent,
    canActivate: [authGuard]
  }
];
