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
        features.map((feature) => (
          <SideNavEntry
            key={feature.guid}
            label={feature.name}
            to={feature.rootPath}
          />
        ))
      }
    </SideNav>
  );
};

export default SideNavImpl;
