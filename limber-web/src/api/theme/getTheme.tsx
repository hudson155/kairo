import ThemeModel from '../../models/ThemeModel';

export default async function getTheme(): Promise<ThemeModel> {
  const response: Response = await fetch(process.env['REACT_APP_API_URL'] + `/theme`);
  const json = await response.json();

  return json as ThemeModel;
}
