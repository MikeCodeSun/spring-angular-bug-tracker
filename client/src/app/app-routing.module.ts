import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './auth.guard';
import { BugsComponent } from './bug/bugs/bugs.component';
import { NewBugComponent } from './bug/new-bug/new-bug.component';
import { SingleBugComponent } from './bug/single-bug/single-bug.component';
import { HomeComponent } from './home/home.component';
import { NotfoundComponent } from './notfound/notfound.component';
import { NewProjectComponent } from './project/new-project/new-project.component';
import { ProjectsComponent } from './project/projects/projects.component';
import { SingleProjectComponent } from './project/single-project/single-project.component';
import { SearchComponent } from './search/search.component';
import { LoginComponent } from './user/login/login.component';
import { ProfileComponent } from './user/profile/profile.component';
import { RegisterComponent } from './user/register/register.component';

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'search', component: SearchComponent, canActivate: [AuthGuard] },
  { path: 'register', component: RegisterComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent, canActivate: [AuthGuard] },
  {
    path: 'project/list',
    component: ProjectsComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'project/new',
    component: NewProjectComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'project/singleproject/:id',
    component: SingleProjectComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'bug/list',
    component: BugsComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'bug/singlebug/:id',
    component: SingleBugComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'bug/newbug/:id',
    component: NewBugComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'user/profile/:id',
    component: ProfileComponent,
    canActivate: [AuthGuard],
  },
  {
    path: '**',
    component: NotfoundComponent,
    pathMatch: 'full',
    canActivate: [AuthGuard],
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
