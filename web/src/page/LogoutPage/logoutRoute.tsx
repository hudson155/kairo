import { Route } from 'react-router-dom';
import LogoutPage from 'page/LogoutPage/LogoutPage';

const logoutRoute = () => <Route path="/logout" element={<LogoutPage />} />;

export default logoutRoute;
