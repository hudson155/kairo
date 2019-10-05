import ThemeModel from '../../models/ThemeModel';

export default interface ThemeAction {
  type: 'THEME_SET';
}

export interface SetThemeAction extends ThemeAction {
  type: 'THEME_SET';
  theme: ThemeModel;
}
