import { Component, OnInit } from '@angular/core';
import { ProjectService } from 'src/app/service/project.service';
import { convertNumToArr } from 'src/app/util/convertNumToArr';
import { Project } from 'src/app/util/type';

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.css'],
})
export class ProjectsComponent implements OnInit {
  constructor(private projectService: ProjectService) {}
  projects: Project[] = [];
  loading = true;

  page: number = 0;
  size: number = 8;
  currentPage: number = 1;
  totalPage: number[] = [];

  myProject() {
    this.projectService.getMyProjects().subscribe(
      (res) => {
        // console.log(res);
        this.loading = false;
        this.projects = res;
        // this.totalPage = convertNumToArr(res.length % this.size);
      },
      (err) => {
        this.loading = false;
        console.log(err);
      }
    );
  }
  myProjectWithPage() {
    this.projectService.getMyProjectsWithPage(this.page, this.size).subscribe(
      (res) => {
        // console.log(res);
        this.totalPage = convertNumToArr(res.totalPage);
        this.projects = res.projects;
        this.loading = false;
      },
      (err) => {
        console.log(err);
      }
    );
  }
  changPage(page: number) {
    this.currentPage = page;
    this.projectService.getMyProjectsWithPage(page - 1, this.size).subscribe(
      (res) => {
        this.projects = res.projects;
      },
      (err) => {
        console.log(err);
      }
    );
  }
  ngOnInit(): void {
    // this.myProject();
    this.myProjectWithPage();
  }
}
