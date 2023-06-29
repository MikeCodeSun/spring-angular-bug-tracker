import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AppService } from 'src/app/service/app.service';
import { RegisteError } from 'src/app/util/type';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  constructor(private appService: AppService, private router: Router) {}
  loginError: RegisteError = {};
  email: string = '';
  password: string = '';

  login() {
    this.appService.login(this.email, this.password).subscribe(
      (res) => {
        if (res.login === 'success') {
          localStorage.setItem('token', res.token);
          // this.router.navigateByUrl('/')

          window.location.href = '/';
        }
      },
      (err) => {
        console.log(err);

        this.loginError = err.error;
      }
    );
  }
}
