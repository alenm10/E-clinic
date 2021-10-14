import LoginRegister from "../components/Auth/LoginRegister";
import LoginView from "../components/Auth/LoginView";
import RegisterView from "../components/Auth/RegisterView";

const authRoutes = [
    {
        path: '/auth',
        component: LoginView,
    },
    {
        path: '/auth/register',
        component: RegisterView,
    }
];

export default authRoutes;
