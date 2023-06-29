export const convertNumToArr = (num: number): number[] => {
  let i;
  let arr = [];
  for (i = 1; i <= num; i++) {
    arr.push(i);
  }
  return arr;
};
