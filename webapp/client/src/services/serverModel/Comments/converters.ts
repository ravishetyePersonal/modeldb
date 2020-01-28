import { IComment } from 'models/Comments';

export const convertServerComment = (serverComment: any): IComment => {
  return {
    id: serverComment.id,
    dateTime: new Date(Number(serverComment.date_time)),
    message: serverComment.message,
  };
};
