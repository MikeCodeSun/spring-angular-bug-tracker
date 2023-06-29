import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProjectService } from 'src/app/service/project.service';
import { pagenate } from 'src/app/util/pagenate';
import { Bug, Project } from 'src/app/util/type';
import { convertNumToArr } from 'src/app/util/convertNumToArr';

@Component({
  selector: 'app-single-project',
  templateUrl: './single-project.component.html',
  styleUrls: ['./single-project.component.css'],
})
export class SingleProjectComponent implements OnInit {
  constructor(
    private projectService: ProjectService,
    private route: ActivatedRoute,
    private router: Router
  ) {}
  project?: Project;
  loading: boolean = true;
  title: string = '';
  link: string = '';
  description: string = '';
  isEdit: boolean = false;
  bugs: Bug[] = [];
  updateErr = {
    title: '',
    link: '',
  };
  totalBugPage: number[] = [];
  size: number = 4;
  page: number = 1;
  pageBugs: Bug[] = [];
  currentPage: number = 1;
  changePage(page: number) {
    this.currentPage = page;
    this.pageBugs = pagenate(this.bugs, page, this.size);
  }

  singleProject() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.projectService.getSingleProject(id).subscribe(
      (res) => {
        // console.log(res);
        this.project = res;
        this.title = res.title;
        this.link = res.link;
        this.description = res.description;
        this.loading = false;
        this.bugs = res.bugs.slice().reverse();
        this.pageBugs = res.bugs.reverse().slice(0, 4);
        this.totalBugPage = convertNumToArr(res.bugs.length / this.size);
      },
      (err) => {
        console.log(err);
        this.loading = false;
      }
    );
  }
  deleteProject() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.projectService.deleteProject(id).subscribe(
      (res) => {
        // console.log(res);
        if (res.project === 'delete success') {
          this.router.navigateByUrl('/project/list');
        }
      },
      (err) => {
        console.log(err);
      }
    );
  }

  showEdit() {
    this.isEdit = true;
  }
  closeEdit() {
    this.isEdit = false;
  }
  editProject() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.projectService
      .editProject(this.title, this.link, this.description, id)
      .subscribe(
        (res) => {
          console.log(res);
          this.project = res;
          this.title = res.title;
          this.link = res.link;
          this.description = res.description;
          this.isEdit = false;
          this.updateErr = {
            title: '',
            link: '',
          };
        },
        (err) => {
          console.log(err);
          this.updateErr = err.error;
        }
      );
  }

  ngOnInit(): void {
    this.singleProject();
  }
}
