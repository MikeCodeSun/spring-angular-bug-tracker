export const pagenate = (arr: Array<any>, page: number, size: number): any => {
  // const totalPage = arr.length % size
  if (page <= 0) return;
  return arr.slice((page - 1) * size, page * size);
};
