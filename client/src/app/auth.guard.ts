import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Router,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { Observable } from 'rxjs';
import { AppService } from './service/app.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard {
  constructor(private appService: AppService, private router: Router) {}
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    const componentName = route.component?.name;
    const auth = this.appService.isLog;

    if (
      auth &&
      componentName !== 'LoginComponent' &&
      componentName !== 'RegisterComponent'
    ) {
      return true;
    } else if (
      auth &&
      (componentName === 'LoginComponent' ||
        componentName === 'RegisterComponent')
    ) {
      this.router.navigateByUrl('home');
      return false;
    } else if (
      !auth &&
      (componentName === 'LoginComponent' ||
        componentName === 'RegisterComponent')
    ) {
      return true;
    } else {
      this.router.navigateByUrl('login');
      return false;
    }
  }
}
