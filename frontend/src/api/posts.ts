import client, { Result } from './client';

export interface PostCreateRequest {
  title: string;
  summary?: string;
  content: string;
  tags?: string[];
  status?: 'DRAFT' | 'PUBLISHED';
}

export interface PostResponse {
  id: number;
  title: string;
  summary: string;
  content?: string;
  status: 'DRAFT' | 'PUBLISHED';
  pv: number;
  tags: string[];
  createTime: string;
  updateTime: string;
  author: {
    id: number;
    username: string;
    nickname: string;
  };
}

export interface Page<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  numberOfElements: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}

export const createPost = (data: PostCreateRequest) => {
  return client.post<any, Result<number>>('/posts/create', data);
};

export const updatePost = (id: number, data: PostCreateRequest) => {
  return client.post<any, Result<number>>(`/posts/update/${id}`, data);
};

export const publishPost = (id: number) => {
  return client.post<any, Result<string>>(`/posts/publish/${id}`);
};

export const getPostList = (page: number = 0, size: number = 10) => {
  return client.get<any, Result<Page<PostResponse>>>(`/posts/get/list`, {
    params: { page, size },
  });
};

export const getPostDetail = (id: number) => {
  return client.get<any, Result<PostResponse>>(`/posts/get/${id}`);
};

export const deletePost = (id: number) => {
  return client.post<any, Result<string>>(`/posts/delete/${id}`);
};
