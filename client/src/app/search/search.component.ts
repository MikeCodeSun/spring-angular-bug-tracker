import { Component } from '@angular/core';
import { AppService } from '../service/app.service';
import { Bug, Project } from '../util/type';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css'],
})
export class SearchComponent {
  constructor(private appService: AppService) {}
  searchValue: string = '';
  bugs: Bug[] = [];
  projects: Project[] = [];
  search() {
    if (!this.searchValue && this.searchValue.trim() === '') return;
    console.log(this.searchValue);
    this.appService.searchBugAndProject(this.searchValue).subscribe(
      (res) => {
        console.log(res);
        this.bugs = res.bugs;
        this.projects = res.projects;
      },
      (err) => {
        console.log(err);
      }
    );
  }
}
