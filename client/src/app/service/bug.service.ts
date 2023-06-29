import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Bug, Status } from '../util/type';

@Injectable({
  providedIn: 'root',
})
export class BugService {
  constructor(private http: HttpClient) {}
  getMyBugs(): Observable<Bug[]> {
    return this.http.get<Bug[]>(`${environment.baseUrl}/bug/mybug`);
  }
  getSingleBug(id: number): Observable<Bug> {
    return this.http.get<Bug>(`${environment.baseUrl}/bug/id/${id}`);
  }
  deleteBug(id: number): Observable<any> {
    return this.http.delete(`${environment.baseUrl}/bug/delete/${id}`);
  }

  addNewBug(
    project_id: number,
    title: string,
    description: string,
    solution: string,
    status: Status
  ): Observable<any> {
    return this.http.post(`${environment.baseUrl}/bug/add/${project_id}`, {
      title,
      description,
      solution,
      status,
    });
  }
  updateBug(
    id: number,
    title: string,
    description: string,
    solution: string,
    status: Status
  ): Observable<any> {
    return this.http.patch(`${environment.baseUrl}/bug/update/${id}`, {
      title,
      description,
      solution,
      status,
    });
  }
  getMyBugWithPage(page: number, size: number): Observable<any> {
    return this.http.get(
      `${environment.baseUrl}/bug/page/mybug?page=${page}&size=${size}`
    );
  }
}
