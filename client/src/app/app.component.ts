import { Component, OnInit } from '@angular/core';
import { AppService } from './service/app.service';
import { UserInfo, defaultUserImage } from './util/type';
import { Location } from '@angular/common';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  constructor(private appService: AppService, private _location: Location) {}
  title = 'client';
  isUserLog: boolean = false;
  userInfo: UserInfo = {
    ...defaultUserImage,
  };

  logout() {
    this.appService.logout().subscribe((data) => {
      console.log(data);

      this.isUserLog = false;
      this.userInfo = {
        ...defaultUserImage,
      };
      window.location.href = '/';
    });
  }

  getChangedName() {
    this.appService
      .getChangedNameEvent()
      .subscribe((res) => (this.userInfo.name = res));
  }
  goBack() {
    this._location.back();
  }
  ngOnInit() {
    this.appService.checkIsLogin().subscribe((data) => {
      if (data !== 0) {
        this.appService.getProfile(data).subscribe((res) => {
          this.userInfo = res;
          this.isUserLog = true;
        });
      }
    });
    this.getChangedName();
  }
}
