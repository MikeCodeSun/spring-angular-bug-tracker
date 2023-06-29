export interface RegisteError {
  name?: string;
  email?: string;
  password?: string;
}

export interface Decoded {
  exp: number;
  iat: number;
  iss: string;
  sub: string;
}

export interface UserInfo {
  id?: number;
  email?: string;
  name?: string;
  image: string;
  createdAt?: string;
  updatedAt?: string;
}
export interface Project {
  id: number;
  description: string;
  link: string;
  title: string;
  createdAt: string;
  updateAt: string;
  user: UserInfo;
  bugs: Bug[];
}
export interface Bug {
  createdAt: string;
  description: string;
  id: number;
  project: Project;
  solution: string;
  status: string;
  title: string;
  updatedAt: string;
  user: UserInfo;
}
export const defaultUserImage = {
  image: '../../../assets/image/user.jpeg',
};

export enum Status {
  SOLVED,
  UNSOLVED,
}
