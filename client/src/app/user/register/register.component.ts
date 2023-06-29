import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AppService } from 'src/app/service/app.service';
import { RegisteError } from 'src/app/util/type';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
  constructor(private appService: AppService, private router: Router) {}
  name: string = '';
  email: string = '';
  password: string = '';
  registerError: RegisteError = {};

  register() {
    this.appService.register(this.name, this.email, this.password).subscribe(
      (res) => {
        console.log(res);
        if (res.login === 'success') {
          this.router.navigateByUrl('login');
        }
      },
      (err) => {
        this.registerError = err.error;
        console.log(err.error);
      }
    );
  }
}
