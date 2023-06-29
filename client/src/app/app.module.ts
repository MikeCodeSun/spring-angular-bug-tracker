import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { RegisterComponent } from './user/register/register.component';
import { LoginComponent } from './user/login/login.component';
import { FormsModule } from '@angular/forms';
import { ProfileComponent } from './user/profile/profile.component';
import { HomeComponent } from './home/home.component';
import { NotfoundComponent } from './notfound/notfound.component';
import { HttpInterceptorInterceptor } from './http-interceptor.interceptor';
import { AuthImgPipe } from './auth-img.pipe';
import { ProjectsComponent } from './project/projects/projects.component';
import { BugsComponent } from './bug/bugs/bugs.component';
import { SingleProjectComponent } from './project/single-project/single-project.component';
import { LoadingComponent } from './component/loading/loading.component';
import { NewProjectComponent } from './project/new-project/new-project.component';
import { SingleBugComponent } from './bug/single-bug/single-bug.component';
import { NewBugComponent } from './bug/new-bug/new-bug.component';
import { SearchComponent } from './search/search.component';

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    ProfileComponent,
    HomeComponent,
    NotfoundComponent,
    AuthImgPipe,
    ProjectsComponent,
    BugsComponent,
    SingleProjectComponent,
    LoadingComponent,
    NewProjectComponent,
    SingleBugComponent,
    NewBugComponent,
    SearchComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    NgbModule,
    FormsModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
