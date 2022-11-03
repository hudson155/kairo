import LoginPage from 'page/loginPage/LoginPage';
import { Route } from 'react-router-dom';

const loginRoute = () => <Route path="/login" element={<LoginPage />} />;

export default loginRoute;
