import {defineStore} from "pinia";
import {ref} from "vue";
import {login, queryUserFocus, Register, sendCode, queryUserInfo} from "@/apis/main";

//之后补写错误处理的逻辑

export const useUserStore = defineStore('user', () => {
    const isAdmin = ref(false);
    const userToken = ref('');
    const userInfo = ref({});
    const userFocus = ref([]);
    const userCollect = ref([]);
    const userFavorite = ref([]);
    const headersObj = ref({})

    const userSendCode = async (email,type) => {
        await sendCode({email,type});
    }

    const userRegister = async ({email, nickName, password, emailCode, checkCode}) => {
        await Register({email, nickName, password, emailCode, checkCode});
    };

    const userLogin = async ({email, password, checkCode}) => {
        await login({email, password, checkCode}).then(res => {
            userToken.value = res.data;
            console.log(userToken.value);
        })
    }

    const getUserInfo = async () => {
        userInfo.value = await queryUserInfo();
        console.log(userInfo.value);
    };

    const extendUserInfo = (type, id) => {
        if (type === 1) {
            userFocus.value = [...userFocus.value, id];
        } else if (type === 2) {
            userFavorite.value = [...userFavorite.value, id];
        } else if (type === 3) {
            userCollect.value = [...userCollect.value, id];
        }
    };

    const removeFocus = (type, id) => {
        if (type === 1) {
            const index = userFocus.value.indexOf(id);
            if (index !== -1) {
                userFocus.value.splice(index, 1);
            }
        } else if (type === 2) {
            const index = userFavorite.value.indexOf(id);
            if (index !== -1) {
                userFavorite.value.splice(index, 1);
            }
        } else if (type === 3) {
            const index = userCollect.value.indexOf(id);
            if (index !== -1) {
                userCollect.value.splice(index, 1);
            }
        }
    };

    const userLogout = async () => {
        userInfo.value = {};
        return {info: "成功退出登录"};
    };

    const changeInfo = ({username, signature, avatar}) => {
        userInfo.value.username = username;
        userInfo.value.signature = signature;
        userInfo.value.avatar = avatar;
    };

    return {
        isAdmin,
        userToken,
        userInfo,
        getUserInfo,
        userLogin,
        userLogout,
        userSendCode,
        userRegister,
        extendUserInfo,
        userFocus,
        removeFocus,
        changeInfo,
        userCollect,
        userFavorite,
        headersObj
    };
}, {
    persist: true,
});
