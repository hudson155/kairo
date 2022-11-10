import { Dialog } from '@headlessui/react';
import classNames from 'classnames';
import { useResizeObserver } from 'hook/useResizeObserver';
import React, { PropsWithChildren, ReactNode } from 'react';
import styles from './SideNav.module.scss';

/**
 * For a11y reasons, the collapsible sidenav must render within a dialog.
 * This requires it to mount at a different point in the DOM.
 * Therefore, we cannot use CSS as we would prefer.
 */
export const useCollapsibleSideNav = (): boolean => useResizeObserver((size) => size < 992);

interface Props extends PropsWithChildren {
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
      <nav className={classNames(styles.nav, { [styles.collapsible]: sideNavIsCollapsible })}>
        <ul className={styles.ul}>{children}</ul>
      </nav>
    );
  }

  return (
    <Dialog open={isOpen} onClose={() => setIsOpen(false)}>
      <div aria-hidden={true} className={styles.backdrop} />
      <Dialog.Panel as="nav" className={classNames(styles.nav, { [styles.collapsible]: sideNavIsCollapsible })}>
        <ul className={styles.ul}>{children}</ul>
      </Dialog.Panel>
    </Dialog>
  );
};

export default SideNav;
