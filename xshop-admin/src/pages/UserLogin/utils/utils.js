import {parse} from 'qs';

export function getPageQuery() {
  return parse(window.location.href.split('?')[1]);
}

export function setAuthority(user) {
  // 保存user
  localStorage.setItem('user', JSON.stringify(user));

  const proAuthority = [];
  for (let i = 0; i < user.authGroups.length; i++) {
    proAuthority[i] = user.authGroups[i].agCode;
  }
  return localStorage.setItem('antd-pro-authority', JSON.stringify(proAuthority));
}
