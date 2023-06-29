import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ProjectService } from 'src/app/service/project.service';

@Component({
  selector: 'app-new-project',
  templateUrl: './new-project.component.html',
  styleUrls: ['./new-project.component.css'],
})
export class NewProjectComponent {
  constructor(private projectService: ProjectService, private router: Router) {}
  title: string = '';
  link: string = '';
  description: string = '';
  projectErrMsg = {
    title: '',
    link: '',
  };
  addNewProject() {
    this.projectService
      .addNewProject(this.title, this.link, this.description)
      .subscribe(
        (res) => {
          // console.log(res);
          this.link = '';
          this.title = '';
          this.description = '';
          this.router.navigateByUrl('/project/list');
        },
        (err) => {
          // console.log(err);
          this.projectErrMsg = err.error;
        }
      );
  }
}
