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
                console.log(error);
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
            console.log(error);
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
        await queryUserInfo().then(res => {
          userInfo.value = res.data;
        }
        )
    };

    const userLogout = async () => {
        userInfo.value = {};
        userToken.value = null;
        isAdmin.value = false;
        isUser.value = false;
        ElMessage({ type: "success", message: "退出登录成功" });
    };

    const loginOut = async () => {
      userInfo.value = {};
      userToken.value = null;
      isAdmin.value = false;
      isUser.value = false;
  };


    const testLink = async () => {
        if(userToken){
          await queryUserInfo()}
    }

    return {
        isAdmin,
        isUser,
        userToken,
        userInfo,
        userLogin,
        adminLog,
        userReset,
        getUserInfo,
        userSendCode,
        userLogout,
        loginOut,
        userRegister,

        testLink
    };
}, {
    persist: true,
});
