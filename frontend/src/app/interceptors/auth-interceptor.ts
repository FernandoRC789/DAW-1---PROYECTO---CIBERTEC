import { HttpInterceptorFn } from '@angular/common/http';
import { inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const platformId = inject(PLATFORM_ID);

  if (!isPlatformBrowser(platformId)) {
    return next(req);
  }

  const usuarioStr = localStorage.getItem('usuario');

  if (usuarioStr) {
    const usuario = JSON.parse(usuarioStr);

    if (usuario.username && usuario.password) {
      const basicAuth = btoa(`${usuario.username}:${usuario.password}`);

      req = req.clone({
        setHeaders: {
          Authorization: `Basic ${basicAuth}`
        }
      });
    }
  }

  return next(req);
};
