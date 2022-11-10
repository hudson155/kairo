import { atom } from 'recoil';

const sideNavIsOpenState = atom<boolean>({
  key: `nav/sideNavIsOpen`,
  default: false,
});

export default sideNavIsOpenState;
