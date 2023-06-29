import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Project } from '../util/type';

@Injectable({
  providedIn: 'root',
})
export class ProjectService {
  constructor(private http: HttpClient) {}

  getMyProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(`${environment.baseUrl}/project/myproject`);
  }

  getSingleProject(id: number): Observable<Project> {
    return this.http.get<Project>(`${environment.baseUrl}/project/id/${id}`);
  }
  addNewProject(
    title: string,
    link: string,
    description: string
  ): Observable<any> {
    return this.http.post(`${environment.baseUrl}/project/add`, {
      title,
      link,
      description,
    });
  }
  deleteProject(id: number): Observable<any> {
    return this.http.delete(`${environment.baseUrl}/project/delete/${id}`);
  }
  editProject(
    title: string,
    link: string,
    description: string,
    id: number
  ): Observable<any> {
    return this.http.patch(`${environment.baseUrl}/project/update/${id}`, {
      title,
      link,
      description,
    });
  }
  getMyProjectsWithPage(page: number, size: number): Observable<any> {
    return this.http.get(
      `${environment.baseUrl}/project/page/myproject?page=${page}&size=${size}`
    );
  }
}
