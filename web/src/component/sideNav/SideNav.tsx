import { Dialog, Transition } from '@headlessui/react';
import classNames from 'classnames';
import SideNavHeader from 'component/sideNav/SideNavHeader';
import { useResizeObserver } from 'hook/useResizeObserver';
import React, { Fragment, ReactNode } from 'react';
import { transitions } from 'style/transitions';
import styles from './SideNav.module.scss';

/**
 * For a11y reasons, the collapsible sidenav must render within a dialog.
 * This requires it to mount at a different point in the DOM.
 * Therefore, we cannot use CSS as we would prefer.
 */
export const useCollapsibleSideNav = (): boolean => useResizeObserver((size) => size < 992);

interface Props {
  isOpen: boolean;
  setIsOpen: (isOpen: boolean) => void;
  children: ReactNode;
}

/**
 * This component styles the side navigation bar, but defines no functionality.
 * Functionality is defined in [SideNavImpl].
 *
 * DUPLICATE CODE ALERT: When updating this component,
 * there is some duplicate code to look out for.
 * This is necessitated by Headless UI's Dialog implementation.
 */
const SideNav: React.FC<Props> = ({ isOpen, setIsOpen, children }) => {
  const sideNavIsCollapsible = useCollapsibleSideNav();

  if (!sideNavIsCollapsible) {
    return (
      <div className={classNames(styles.container, { [styles.collapsible]: sideNavIsCollapsible })}>
        <nav className={styles.nav}>
          <ul className={styles.ul}>{children}</ul>
        </nav>
      </div>
    );
  }

  return (
    <Transition as={Fragment} show={isOpen}>
      <Dialog className={styles.dialog} onClose={() => setIsOpen(false)}>
        <Transition.Child as={Fragment} {...transitions(`fadeIn`, `fadeOut`)}>
          <div aria-hidden={true} className={styles.backdrop} />
        </Transition.Child>
        <Transition.Child as={Fragment} {...transitions(`slideIn`, `slideOut`)}>
          <Dialog.Panel as={Fragment}>
            <div className={classNames(styles.container, { [styles.collapsible]: sideNavIsCollapsible })}>
              <SideNavHeader onClose={() => setIsOpen(false)} />
              <nav className={styles.nav}>
                <ul className={styles.ul}>{children}</ul>
              </nav>
            </div>
          </Dialog.Panel>
        </Transition.Child>
      </Dialog>
    </Transition>
  );
};

export default SideNav;
