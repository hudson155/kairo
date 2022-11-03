import LogoutPage from 'page/logoutPage/LogoutPage';
import { Route } from 'react-router-dom';

const logoutRoute = () => <Route path="/logout" element={<LogoutPage />} />;

export default logoutRoute;
