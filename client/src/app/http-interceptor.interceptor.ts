import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpHeaders,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppService } from './service/app.service';

@Injectable()
export class HttpInterceptorInterceptor implements HttpInterceptor {
  constructor(private appService: AppService) {}
  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    const auth = this.appService.isLog;
    const token = localStorage.getItem('token');
    if (auth && token) {
      const authReq = request.clone({
        headers: request.headers.set('Authorization', `Bearer ${token}`),
      });
      return next.handle(authReq);
    }
    return next.handle(request);
  }
}
