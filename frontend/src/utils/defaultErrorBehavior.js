import {redirectToLogin} from "../store/AuthModule";

export function defaultError(err, commit) {
    console.log("ERRORRRRRRRRRRRRRRRRRRRRRRCINA");
    console.log(err);
    if(err.response) {
        if(err.response.status === 401) {
            console.log(commit);
            redirectToLogin(commit);
        }
    }
}
