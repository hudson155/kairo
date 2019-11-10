import ThemeModel from '../../models/ThemeModel';

export default interface ThemeState {
  loadingStatus: LoadingStatus;
  theme: ThemeModel;
}
