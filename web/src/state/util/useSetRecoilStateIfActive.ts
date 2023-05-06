import { RecoilState, SetterOrUpdater, useGetRecoilValueInfo_UNSTABLE, useSetRecoilState } from 'recoil';

/**
 * This utility only sets the value of a Recoil state if it's currently active.
 * This is useful for ensuring mutations don't cause Recoil errors when trying to synchronize state.
 */
export const useSetRecoilStateIfActive = <T>(recoilState: RecoilState<T>): SetterOrUpdater<T> => {
  const getInfo = useGetRecoilValueInfo_UNSTABLE();
  const set = useSetRecoilState(recoilState);

  return (valOrUpdater) => {
    const info = getInfo(recoilState);
    if (!info.isActive) return;
    set(valOrUpdater);
  };
};
