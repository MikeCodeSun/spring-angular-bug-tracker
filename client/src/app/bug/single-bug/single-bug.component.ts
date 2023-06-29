import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BugService } from 'src/app/service/bug.service';
import { Bug, Status } from 'src/app/util/type';

@Component({
  selector: 'app-single-bug',
  templateUrl: './single-bug.component.html',
  styleUrls: ['./single-bug.component.css'],
})
export class SingleBugComponent implements OnInit {
  constructor(
    private bugService: BugService,
    private route: ActivatedRoute,
    private router: Router
  ) {}
  loading: boolean = true;
  bug?: Bug;
  isEdit: boolean = false;

  title: string = '';
  description: string = '';
  solution: string = '';
  status!: Status;

  selectedValue: string = '';
  updateTitleErr: string = '';

  showEdit() {
    this.isEdit = true;
  }
  closeEdit() {
    this.isEdit = false;
  }
  onChange() {
    if (this.selectedValue === 'UNSOLVED') {
      this.status = Status.UNSOLVED;
    } else {
      this.status = Status.SOLVED;
    }
  }
  editBug() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.bugService
      .updateBug(id, this.title, this.description, this.solution, this.status)
      .subscribe(
        (res) => {
          // console.log(res);
          this.bug = res;
          this.isEdit = false;
          this.updateTitleErr = '';
        },
        (err) => {
          console.log(err);
          this.updateTitleErr = err.error.title;
        }
      );
  }
  deleteBug() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.bugService.deleteBug(id).subscribe(
      (res) => {
        console.log(res);
        if (res.bug === 'delete success') {
          this.router.navigateByUrl('bug/list');
        }
      },
      (err) => {
        console.log(err);
      }
    );
  }
  singleBug() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.bugService.getSingleBug(id).subscribe(
      (res) => {
        // console.log(res);
        this.bug = res;
        this.loading = false;

        this.title = res.title;
        this.description = res.description;
        this.solution = res.solution;
        this.selectedValue = res.status;
      },
      (err) => {
        console.log(err);
        this.loading = false;
      }
    );
  }
  ngOnInit(): void {
    this.singleBug();
  }
}
