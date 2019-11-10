import ThemeModel from '../../models/ThemeModel';
import { LoadingStatus } from '../util/LoadingStatus';

export default interface ThemeState {
  loadingStatus: LoadingStatus;
  theme: ThemeModel;
}
