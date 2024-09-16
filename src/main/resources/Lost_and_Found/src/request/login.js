import { postRequest } from "./api";

export const login = async ( user ) => {
    return await postRequest("/login", { user });
}