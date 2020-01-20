import ThemeModel from '../../models/theme/ThemeModel';
import { LoadingStatus } from '../util/LoadingStatus';

export default interface ThemeState {
  loadingStatus: LoadingStatus;
  theme: ThemeModel;
}
