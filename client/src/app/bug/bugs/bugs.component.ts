import { Component, OnInit } from '@angular/core';
import { BugService } from 'src/app/service/bug.service';
import { convertNumToArr } from 'src/app/util/convertNumToArr';
import { Bug } from 'src/app/util/type';

@Component({
  selector: 'app-bugs',
  templateUrl: './bugs.component.html',
  styleUrls: ['./bugs.component.css'],
})
export class BugsComponent implements OnInit {
  constructor(private bugService: BugService) {}
  bugs: Bug[] = [];
  loading: boolean = true;
  totalPageArray: number[] = [];
  page: number = 0;
  size: number = 6;
  currentPage: number = 1;
  myBug() {
    this.bugService.getMyBugs().subscribe(
      (res) => {
        // console.log(res);
        this.bugs = res;
        this.loading = false;
      },
      (err) => {
        console.log(err);
        this.loading = false;
      }
    );
  }

  myBugWithPage() {
    this.bugService.getMyBugWithPage(this.page, this.size).subscribe(
      (res) => {
        // console.log(res);
        this.loading = false;
        this.bugs = res.bugs;
        this.totalPageArray = convertNumToArr(res.totalPage);
      },
      (err) => {
        console.log(err);
      }
    );
  }

  changePage(page: number) {
    this.currentPage = page;
    this.bugService.getMyBugWithPage(page - 1, this.size).subscribe(
      (res) => {
        // console.log(res);
        this.bugs = res.bugs;
      },
      (err) => {
        console.log(err);
      }
    );
  }

  ngOnInit(): void {
    // this.myBug();
    this.myBugWithPage();
  }
}
