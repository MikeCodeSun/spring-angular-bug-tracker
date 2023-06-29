import { Component, ElementRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BugService } from 'src/app/service/bug.service';
import { Status } from 'src/app/util/type';
import { Location } from '@angular/common';

@Component({
  selector: 'app-new-bug',
  templateUrl: './new-bug.component.html',
  styleUrls: ['./new-bug.component.css'],
})
export class NewBugComponent {
  constructor(
    private bugService: BugService,
    private route: ActivatedRoute,
    private router: Router,
    private _location: Location
  ) {}
  title: string = '';
  description: string = '';
  solution: string = '';
  status: Status = Status.UNSOLVED;
  titleErr: string = '';

  onChange(e: Event) {
    const input = e.target as HTMLInputElement;
    const statusStr = input.value;
    if (statusStr === 'SOLVED') {
      this.status = Status.SOLVED;
    } else {
      this.status = Status.UNSOLVED;
    }
  }
  addNewBug() {
    const project_id = Number(this.route.snapshot.paramMap.get('id'));
    this.bugService
      .addNewBug(
        project_id,
        this.title,
        this.description,
        this.solution,
        this.status
      )
      .subscribe(
        (res) => {
          // console.log(res);
          if (res) {
            this._location.back();
          }
        },
        (err) => {
          console.log(err);
          this.titleErr = err.error.title;
        }
      );
  }
}
