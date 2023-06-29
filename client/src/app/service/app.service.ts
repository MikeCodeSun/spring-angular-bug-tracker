import { EventEmitter, Injectable, Output } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, Subject } from 'rxjs';
import { environment } from 'src/environments/environment';
import jwt_decode from 'jwt-decode';
import { Decoded, UserInfo, defaultUserImage } from '../util/type';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AppService {
  constructor(private http: HttpClient, private router: Router) {}
  isLog: boolean = false;
  userInfo: UserInfo = {
    ...defaultUserImage,
  };

  nameEvent: EventEmitter<string> = new EventEmitter<string>();
  changeNameEvent(name: string): void {
    this.nameEvent.emit(name);
  }
  getChangedNameEvent(): EventEmitter<any> {
    return this.nameEvent;
  }

  register(name: string, email: string, password: string): Observable<any> {
    return this.http.post(`${environment.baseUrl}/user/register`, {
      name,
      email,
      password,
    });
  }

  login(email: string, password: string): Observable<any> {
    return this.http.post(`${environment.baseUrl}/user/login`, {
      email,
      password,
    });
  }

  logout(): Observable<string> {
    localStorage.removeItem('token');
    this.isLog = false;
    return of('logout');
  }

  checkIsLogin(): Observable<number> {
    const token = localStorage.getItem('token');
    if (!token) {
      this.isLog = false;
      return of(0);
    } else {
      const decoded: Decoded = jwt_decode(token);
      // console.log(decoded);
      if (decoded.exp * 1000 < Date.now()) {
        this.isLog = false;
        localStorage.removeItem('token');
        return of(0);
      } else {
        this.isLog = true;
        const infoArr = decoded.sub.split(',');
        return of(Number(infoArr[0]));
      }
    }
  }

  getProfile(id: number): Observable<any> {
    return this.http.get(`${environment.baseUrl}/user/profile/${id}`);
  }

  editName(id: number, name: string): Observable<any> {
    return this.http.patch(`${environment.baseUrl}/user/name`, { id, name });
  }

  uploadImage(file: File, id: number): Observable<any> {
    const formDate = new FormData();
    formDate.append('image', file);
    return this.http.post(`${environment.baseUrl}/user/image/${id}`, formDate);
  }

  hello(): Observable<string> {
    return this.http.get(`${environment.baseUrl}/user/hello`, {
      responseType: 'text',
    });
  }

  searchBugAndProject(searchValue: string): Observable<any> {
    return this.http.get(
      `${environment.baseUrl}/search/title?title=${searchValue}`
    );
  }
}
