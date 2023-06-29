import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Pipe, PipeTransform } from '@angular/core';
import { lastValueFrom, pipe } from 'rxjs';

@Pipe({
  name: 'authImg',
})
export class AuthImgPipe implements PipeTransform {
  constructor(private http: HttpClient) {}
  async transform(src: string): Promise<string> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders();
    headers.append('Authorization', 'Bearer ' + token);
    const imgBlob$ = this.http.get(src, {
      headers,
      responseType: 'blob',
    });
    try {
      const imageBlob = await lastValueFrom(imgBlob$);
      const reader = new FileReader();
      return new Promise((resolve, reject) => {
        reader.onload = () => resolve(reader.result as string);
        reader.readAsDataURL(imageBlob);
      });
    } catch (error) {
      return '../../../assets/image/user.jpeg';
    }
  }
}
