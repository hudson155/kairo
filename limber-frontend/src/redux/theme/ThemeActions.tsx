import ThemeModel from '../../models/ThemeModel';
import { SetThemeAction } from './ThemeAction';

function set(theme: ThemeModel): SetThemeAction {
  return { type: 'THEME_SET', theme };
}

const ThemeActions = {
  set,
};
export default ThemeActions;
