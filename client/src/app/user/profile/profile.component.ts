import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AppService } from 'src/app/service/app.service';
import { UserInfo } from 'src/app/util/type';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
})
export class ProfileComponent implements OnInit {
  constructor(private appService: AppService, private route: ActivatedRoute) {}
  userProfile: UserInfo = {
    image: '',
  };

  name: string = '';
  isEditName: boolean = false;
  nameErr: string = '';

  file?: File;
  isUploadImage: boolean = false;
  imageErr: string = '';
  //
  showUploadImage() {
    this.isUploadImage = true;
  }
  closeUploadImage() {
    this.isUploadImage = false;
  }
  chooseFile(e: Event) {
    const input = e.target as HTMLInputElement;
    if (!input.files?.length) {
      this.imageErr = 'no image file';
      return;
    }
    this.file = input.files[0];
  }
  uploadFile() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!this.file) {
      this.imageErr = 'no image file';
      return;
    }
    this.appService.uploadImage(this.file, id).subscribe(
      (res) => {
        this.userProfile.image = res.image;
        this.imageErr = '';
        this.isUploadImage = false;
        window.location.reload();
      },
      (err) => {
        console.log(err);
      }
    );
  }
  //
  showEditName() {
    this.isEditName = true;
  }
  closeEditName() {
    this.isEditName = false;
    this.nameErr = '';
  }
  editName() {
    if (!this.name || this.name.trim() === '') {
      this.nameErr = 'name not valid';
      return;
    }
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.appService.editName(id, this.name).subscribe(
      (res) => {
        this.userProfile.name = res.name;
        this.appService.changeNameEvent(res.name);
        this.name = '';
        this.isEditName = false;
        this.nameErr = '';
      },
      (err) => {
        console.log(err);
        this.nameErr = err.error.name;
      }
    );
  }
  //
  getUserProfile(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.appService.getProfile(id).subscribe(
      (res) => {
        this.userProfile = {
          ...res,
          image: res.image
            ? `${environment.baseUrl}/image/` + res.image
            : '../../../assets/image/user.jpeg',
        };
      },
      (err) => {
        console.log(err);
      }
    );
  }
  ngOnInit(): void {
    this.getUserProfile();
  }
}
