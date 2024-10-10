import {defineStore} from "pinia";
import {ref} from "vue";
import {login, Register, sendCode, queryUserInfo } from "@/apis/main";
import { adminLogin } from "@/apis/admin";
import { resetPsw } from "@/apis/user";

//之后补写错误处理的逻辑

export const useUserStore = defineStore('user', () => {
    const isAdmin = ref(false);
    const isUser = ref(true);
    const userToken = ref(null);
    const userInfo = ref({});
    const userFocus = ref([]);
    const userCollect = ref([]);
    const userFavorite = ref([]);
    const headersObj = ref({})

    const userSendCode = async (email,type) => {
        await sendCode({email,type});
    }

    const userRegister = async ({email, nickName, password, emailCode, checkCode}) => {
        try{
            const res = await Register({email, nickName, password, emailCode, checkCode});
            if (res.code === 1) {
                ElMessage({type: 'success', message: '注册成功'})
                return 1;
            }else{
                ElMessage({type: 'error', message: `${res.data}`})
                return 0;
            }
        }catch (error) {
            ElMessage({
              type: 'error',
              message: `网络请求失败。${error.message}`
            });
          }
    };

    const userLogin = async ({email, password, checkCode}) => {
            try {
                const res = await login({ email, password, checkCode });
                if (res.code === 1) {
                  userToken.value = res.data;
                  isUser.value = true;
                  return 1;
                } else {
                  ElMessage({
                    type: 'error',
                    message: `${res.data}`
                  });
                  return 0; 
                }
              } catch (error) {
                ElMessage({
                  type: 'error',
                  message: `网络请求失败。${error.message}`
                });
              }
        };

    const adminLog = async ({email, password, checkCode}) => {
        try {
            const res = await adminLogin({ email, password, checkCode });
            if (res.code === 1) {
              userToken.value = res.data;
              isAdmin.value = true;
              return 1;
            } else {
              ElMessage({
                type: 'error',
                message: `${res.data}`
              });
              return 0; 
            }
          } catch (error) {
            ElMessage({
              type: 'error',
              message: `网络请求失败。${error.message}`
            });
          }
    }

    const userReset = async ({email, password, emailCode}) => {
      const res = await resetPsw({email, password, emailCode});
      if (res.code === 1) {
        ElMessage({
          type: 'success',
          message: `密码重置成功。`
        })
      }else{
        ElMessage({
          type: 'error',
          message: `${res.data}`
        })
      }
    }

    const getUserInfo = async () => {
        userInfo.value = await queryUserInfo();
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
        userToken.value = null;
        isAdmin.value = false;
        isUser.value = false;
        ElMessage({ type: "success", message: "退出登录成功" });
    };

    const changeInfo = ({username, signature, avatar}) => {
        userInfo.value.username = username;
        userInfo.value.signature = signature;
        userInfo.value.avatar = avatar;
    };

    return {
        isAdmin,
        isUser,
        userToken,
        userInfo,
        userLogin,
        adminLog,
        userReset,
        getUserInfo,

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
