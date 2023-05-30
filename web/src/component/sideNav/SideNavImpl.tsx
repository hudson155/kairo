import SideNav from 'component/sideNav/SideNav';
import SideNavEntry from 'component/sideNav/SideNavEntry';
import React from 'react';
import { useRecoilState, useRecoilValue } from 'recoil';
import featuresState from 'state/core/features';
import sideNavIsOpenState from 'state/nav/sideNavIsOpen';

/**
 * This is the functional implementation of the side navigation bar.
 */
const SideNavImpl: React.FC = () => {
  const features = useRecoilValue(featuresState);
  const [isOpen, setIsOpen] = useRecoilState(sideNavIsOpenState);

  return (
    <SideNav isOpen={isOpen} setIsOpen={setIsOpen}>
      {
        features.map((feature) => {
          return (
            <SideNavEntry
              key={feature.id}
              iconName={feature.iconName ?? undefined}
              label={feature.name}
              to={feature.rootPath}
              onClick={() => setIsOpen(false)}
            />
          );
        })
      }
    </SideNav>
  );
};

export default SideNavImpl;
