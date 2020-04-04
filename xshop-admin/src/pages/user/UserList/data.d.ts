
// 表格查询条件
export interface SearchUserData {
  username?: string,
  phone?: string,
  email?: string
}


export interface User {
  uid: number,
  username: string,
  phone: string,
  email: string,
  header: string,
  admin: number,
  status: number
  createTime: number,
  updateTime: number
}
