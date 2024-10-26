<template>
    <canvas id="canvas"></canvas>
    <div class="login">
      <div class="box">
        <div class="leftArea">
          <div class="title" style="text-align: center">Lost and Found</div>
          <img src="@/assets/logo.png" class="image" alt=""/>
        </div>        
        <!--用户登录表单-->
        <div class="rightArea" v-if="showWhich && isAdmin && needReset">
          <div class="title-right" style="text-align: center">登录</div>
          <div class="form">
            <el-form ref="formLoginRef" :model="formLogin" :rules="rulesLogin" label-position="right" label-width="0"
                     status-icon>
              <el-form-item prop="email" class="inputArea">
                <el-input  v-model="formLogin.email" placeholder="请输入邮箱号" :prefix-icon="User"/>
              </el-form-item>
              <el-form-item prop="password" class="inputArea">
                <el-input v-model="formLogin.password" placeholder="请输入密码" :prefix-icon="Lock" show-password/>
              </el-form-item>
              <el-form-item prop="checkCode" class="inputArea">
                <el-col :span="10">
                  <el-input v-model="formLogin.checkCode" placeholder="请输入验证码" :prefix-icon="ChatLineSquare"/>
                </el-col>
                <el-col :span="4"></el-col>
                <el-col :span="10">
                  <div class="demo-image__preview">
                    <el-image :src="imageSrcWithQuery" title="点击切换图片" @click="reloadImage()"/>
                  </div>
                </el-col>
              </el-form-item>
              <el-form-item prop="agree" label-width="22px" class="inputArea">
                <el-checkbox size="large" v-model="formLogin.agree">
                  我已同意隐私条款和服务条款
                </el-checkbox>
              </el-form-item>
              <el-button size="large" type="primary" round class="subBtn" @click="doLogin">点击登录</el-button>
            </el-form>
          </div>
          <el-divider content-position="center"></el-divider>
          <el-button size="large" type="primary" plain round class="subBtn" @click="newUserRes()">新用户注册</el-button>
          <el-row style="margin-top: 20px;">
            <el-col :span="8" style="text-align: center;">
              <el-link @click="toggleReset()">忘记密码</el-link>
            </el-col>
            <el-col :span="8"></el-col>
            <el-col :span="8" style="text-align: center;">
              <el-link @click="toggleAdmin()">管理员登录</el-link>
            </el-col>
          </el-row>
        </div>
        <!--管理员登录表单-->
        <div class="rightArea" v-if="!isAdmin">
          <div class="title-right" style="text-align: center">管理员登录</div>
          <div class="form">
            <el-form ref="formLoginRef" :model="formLogin" :rules="rulesLogin" label-position="right" label-width="0"
                     status-icon>
              <el-form-item prop="email" class="inputArea">
                <el-input  v-model="formLogin.email" placeholder="请输入邮箱号" :prefix-icon="User"/>
              </el-form-item>
              <el-form-item prop="password" class="inputArea">
                <el-input v-model="formLogin.password" placeholder="请输入密码" :prefix-icon="Lock" show-password/>
              </el-form-item>
              <el-form-item prop="checkCode" class="inputArea">
                <el-col :span="10">
                  <el-input v-model="formLogin.checkCode" placeholder="请输入验证码" :prefix-icon="ChatLineSquare"/>
                </el-col>
                <el-col :span="4"></el-col>
                <el-col :span="10">
                  <div class="demo-image__preview">
                    <el-image :src="imageSrcWithQuery" title="点击切换图片" @click="reloadImage()"/>
                  </div>
                </el-col>
              </el-form-item>
              <el-form-item prop="agree" label-width="22px" class="inputArea">
                <el-checkbox size="large" v-model="formLogin.agree">
                  我已同意隐私条款和服务条款
                </el-checkbox>
              </el-form-item>
              <el-button size="large" type="primary" round class="subBtn" @click="doAdminLogin">点击登录</el-button>
            </el-form>
          </div>
          <el-divider content-position="center"></el-divider>
          <el-row style="margin-top: 115px;">
            <el-col :span="8" style="text-align: center;">
              <el-link @click="exitAdmin()">返回</el-link>
            </el-col>
            <el-col :span="16"></el-col>
          </el-row>
        </div>
        <!--用户注册表单-->
        <div class="rightArea" v-if="!showWhich && isAdmin">
          <div class="title-right" style="text-align: center">注册</div>
            <div class="form">
              <el-form ref="formRegisterRef" :model="formRegister" :rules="rulesRegister" label-position="right"
                      label-width="0"
                      status-icon>
                <el-form-item prop="nickName" class="inputArea">
                  <el-input v-model="formRegister.nickName" placeholder="请输入用户名" :prefix-icon="User" maxlength="6" show-word-limit/>
                </el-form-item>
                <el-form-item prop="password" class="inputArea">
                  <el-input v-model="formRegister.password" placeholder="请输入密码" :prefix-icon="Lock" show-password/>
                </el-form-item>
                <el-form-item prop="retryPwd" class="inputArea">
                  <el-input v-model="formRegister.retryPwd" placeholder="请确认输入密码" :prefix-icon="Lock" show-password/>
                </el-form-item>
                <el-form-item prop="email" class="inputArea">
                  <el-input  v-model="formRegister.email" placeholder="请输入注册邮箱" 
                    :prefix-icon="Message" @click="toggleCode()"
                  />
                </el-form-item>
                <el-form-item prop="emailCode" class="inputArea" v-if="showCode">
                  <el-input  v-model="formRegister.emailCode" placeholder="输入验证码" :prefix-icon="ChatLineSquare">
                    <template #append>
                      <el-button type="primary" plain
                      :disabled="!isValidEmail(formRegister.email) || isDisabled"  
                        @click="sendCodeZero"
                        v-text="buttonText"
                      ></el-button>
                    </template>
                  </el-input>
                </el-form-item>
                <el-form-item prop="checkCode" class="inputArea" v-if="showCode">
                  <el-col :span="10">
                    <el-input v-model="formRegister.checkCode" placeholder="请输入验证码" :prefix-icon="ChatLineSquare"/>
                  </el-col>
                  <el-col :span="4"></el-col>
                  <el-col :span="10"><el-image :src="imageSrcWithQuery" title="点击切换图片" @click="reloadImage"/></el-col>
                </el-form-item>
                <el-form-item prop="agree" label-width="22px" class="inputArea">
                  <el-checkbox size="large" v-model="formRegister.agree">
                    我已同意隐私条款和服务条款
                  </el-checkbox>
                </el-form-item>
                <el-button size="large" type="primary" round class="subBtn" @click="doRegister">点击注册</el-button>
              </el-form>
            </div>
            <div v-if="!showCode">
              <el-divider content-position="center"></el-divider>
              <el-button size="large" type="primary" plain round class="subBtn" style="margin-top: 0;" @click="toggleForm()">立即登录</el-button>
            </div>
          </div>

        <!--重置密码表单-->
        <div class="rightArea" v-if="!needReset">
          <div class="title-right" style="text-align: center">重置密码</div>
            <div class="form">
              <el-form ref="formResetRef" :model="formReset" :rules="rulesReset" label-position="right"
                      label-width="0"
                      status-icon>
              <el-form-item prop="email" class="inputArea">
                  <el-input  v-model="formReset.email" placeholder="请输入注册邮箱" 
                    :prefix-icon="Message"
              />
              </el-form-item>
                <el-form-item prop="password" class="inputArea">
                  <el-input v-model="formReset.password" placeholder="请输入密码" :prefix-icon="Lock" show-password/>
                </el-form-item>
                <el-form-item prop="retryPwd" class="inputArea">
                  <el-input v-model="formReset.retryPwd" placeholder="请确认输入密码" :prefix-icon="Lock" show-password/>
                </el-form-item>
                <el-form-item prop="emailCode" class="inputArea">
                  <el-input  v-model="formReset.emailCode" placeholder="输入验证码" :prefix-icon="ChatLineSquare">
                    <template #append>
                      <el-button type="primary" plain
                      :disabled="!isValidEmail(formReset.email) || isDisabled2"  
                        @click="sendCodeOne"
                        v-text="buttonText2"
                      ></el-button>
                    </template>
                  </el-input>
                </el-form-item>
                <el-form-item prop="agree" label-width="22px" class="inputArea">
                  <el-checkbox size="large" v-model="formReset.agree">
                    我已同意隐私条款和服务条款
                  </el-checkbox>
                </el-form-item>
                <el-button size="large" type="primary" round class="subBtn" @click="doRest">重置密码</el-button>
              </el-form>
            </div>
            <div v-if="!showCode">
              <el-divider content-position="center"></el-divider>
              <el-button size="large" type="primary" plain round class="subBtn" style="margin-top: 0;" @click="toggleForm()">立即登录</el-button>
            </div>
          </div>
        </div>
      </div>

</template>
  
  <script setup>
  import {onMounted, ref} from "vue";
  import 'element-plus/theme-chalk/el-message.css'
  import {User, Lock, ChatLineSquare, Message} from "@element-plus/icons-vue";
  import {ElMessage} from "element-plus";
  import {useUserStore} from "@/stores/user";
  import {useRouter} from "vue-router";

  const script = document.createElement("script");
script.src="../../index.js/pop.js";
document.body.appendChild(script);

const script2 = document.createElement("script");
script2.src="../../../index.js/TweenMax.min.js";
document.body.appendChild(script2);
  
  // 定义路由
  const emit = defineEmits(['changeShow']);
  const router = useRouter()
  
  // 账户加密码校验
  // 准备表单对象
  const formLogin = ref({
    email: '',
    password: '',
    checkCode: '',
    agree: false
  })
  const formRegister = ref({
    email: '',
    nickName: '',
    password: '',
    retryPwd: '',
    emailCode : '',
    checkCode : '',
    agree: false
  })
  const formReset = ref({
    email: '',
    password: '',
    retryPwd: '',
    emailCode : '',
    agree: false
  })
  
  // 注册表单规则
  const rulesRegister = {
    email: [
      {required: true, message: '邮箱不能为空', trigger: 'blur'},
      {
        validator: (rule, value, callback) => { //验证邮箱的有效格式
          var emailRegExp = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;
          var emailRegExp1 = /^([a-zA-Z]|[0-9])(\w|-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
          if ((!emailRegExp.test(value) && value !== '') || (!emailRegExp1.test(value) && value !== '')) {
            callback(new Error('请输入有效邮箱格式！'));
          } else {
            callback();
          }
        }
      }
    ],
    nickName: [
      {required: true, message: '用户名不能为空！', trigger: 'blur'}
    ],
    password: [
      {required: true, message: '密码不能为空', trigger: 'blur'},
      {min: 6, max: 18, message: '密码应为6-18位', trigger: 'blur'},
      {
        validator: (rule, value, callback) => { //验证邮箱的有效格式
          var pswRegExp = /^(?=.*\d)(?=.*[a-zA-Z]).*$/;
          if (!pswRegExp.test(value) && value !== '') {
            callback(new Error('密码需要含有字母和数字！'));
          } else {
            callback();
          }
        }
      }
    ],
    retryPwd: [
      {required: true, message: '密码不能为空', trigger: 'blur'},
      {min: 6, max: 14, message: '密码不符合要求', trigger: 'blur'},
      {
        validator: (rule, value, callback) => {
          if (value !== formRegister.value.password) {
            callback(new Error('两次密码不一致！'));
          } else {
            callback();
          }
        }
      }
    ],
    emailCode: [
      {required: true, message: '请输入验证码', trigger: 'blur'},
    ],
    checkCode: [
      {required: true, message: '请输入验证码', trigger: 'blur'},
    ],
    agree: [
      {
        validator: (rule, value, callback) => {
          if (value) {
            callback()
          } else {
            callback(new Error('请勾选协议'))
          }
        }
      }
    ]
  }
  
  //登录表单规则
  const rulesLogin = {
    email: [
      {required: true, message: '邮箱不能为空', trigger: 'blur'},
      {
        validator: (rule, value, callback) => {
          var emailRegExp = /^([a-zA-Z]|[0-9])(\w|-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
          var emailRegExp1 = /^([a-zA-Z]|[0-9])(\w|-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
          if ((!emailRegExp.test(value) && value !== '') || (!emailRegExp1.test(value) && value !== '')) {
            callback(new Error('请输入有效邮箱格式！'));
          } else {
            callback();
          }
        }
      }
    ],
    password: [
      {required: true, message: '密码不能为空', trigger: 'blur'},
      {min: 6, max: 18, message: '密码应为6-18位', trigger: 'blur'},
      {
        validator: (rule, value, callback) => { //验证邮箱的有效格式
          var pswRegExp = /^(?=.*\d)(?=.*[a-zA-Z]).*$/;
          if (!pswRegExp.test(value) && value !== '') {
            callback(new Error('密码需要含有字母和数字！'));
          } else {
            callback();
          }
        }
      }
    ],
    checkCode: [
      {required: true, message: '填写验证码', trigger: 'blur'},
    ],
    agree: [
      {
        validator: (rule, value, callback) => {
          // 自定义校验逻辑
          if (value) {
            callback()
          } else {
            callback(new Error('请勾选协议'))
          }
        }
      }
    ]
  }

  // 重置密码表单
  const rulesReset = {
    email: [
      {required: true, message: '邮箱不能为空', trigger: 'blur'},
      {
        validator: (rule, value, callback) => {
          var emailRegExp = /^([a-zA-Z]|[0-9])(\w|-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
          var emailRegExp1 = /^([a-zA-Z]|[0-9])(\w|-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
          if ((!emailRegExp.test(value) && value !== '') || (!emailRegExp1.test(value) && value !== '')) {
            callback(new Error('请输入有效邮箱格式！'));
          } else {
            callback();
          }
        }
      }
    ],
    password: [
      {required: true, message: '密码不能为空', trigger: 'blur'},
      {min: 6, max: 18, message: '密码应为6-18位', trigger: 'blur'},
      {
        validator: (rule, value, callback) => { //验证邮箱的有效格式
          var pswRegExp = /^(?=.*\d)(?=.*[a-zA-Z]).*$/;
          if (!pswRegExp.test(value) && value !== '') {
            callback(new Error('密码需要含有字母和数字！'));
          } else {
            callback();
          }
        }
      }
    ],
    retryPwd: [
      {required: true, message: '密码不能为空', trigger: 'blur'},
      {min: 6, max: 14, message: '密码不符合要求', trigger: 'blur'},
      {
        validator: (rule, value, callback) => {
          if (value !== formReset.value.password) {
            callback(new Error('两次密码不一致！'));
          } else {
            callback();
          }
        }
      }
    ],
    emailCode: [
      {required: true, message: '请输入验证码', trigger: 'blur'},
    ],
    agree: [
      {
        validator: (rule, value, callback) => {
          // 自定义校验逻辑
          if (value) {
            callback()
          } else {
            callback(new Error('请勾选协议'))
          }
        }
      }
    ]
  }

  
  // 获取form实例校验
  const formLoginRef = ref(null)
  const formRegisterRef = ref(null)
  const formResetRef = ref(null)

  const imageSrc = 'http://localhost:8090/user/checkCode';  // 验证码图片地址
  const imageSrcWithQuery = ref(`${imageSrc}?version=0`);  
  // 计数器，用于生成唯一的查询参数  
  const imageVersion = ref(0);  
  // 点击图片时，增加版本号并更新URL  
  const reloadImage = () => {  
    imageVersion.value++;  
    imageSrcWithQuery.value = `${imageSrc}?version=${imageVersion.value}`;  
  }  
  
  const showWhich = ref(true) //控制登录注册页面的条件性渲染
  const newUserRes = () =>{
    showWhich.value = !showWhich.value
  }

  const toggleForm = () => {
    showWhich.value = true
    needReset.value = true
    reloadImage()
  }
  
  const showCode = ref(false) //控制验证码的显示
  const toggleCode = () => {
    showCode.value = !showCode.value
  }

  const isAdmin = ref(true) //控制管理员登录的显示
  const toggleAdmin = () => {
    isAdmin.value = !isAdmin.value
    reloadImage()
  }
  const exitAdmin = () => {
    isAdmin.value = !isAdmin.value
    reloadImage()
  }

  const needReset = ref(true) //控制重置密码的显示
  const toggleReset = () => {
      needReset.value = !needReset.value
  }

  function isValidEmail(email) {
  const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return re.test(String(email).toLowerCase());
}

  const isDisabled = ref(false);  //控发送验证码按钮的禁用
  const buttonText = ref('发送验证码');
  let countdown = null;

  const startCountdown = () => {
    if (!isDisabled.value) {
      isDisabled.value = true;
      let secondsLeft = 60;
      buttonText.value = `${secondsLeft} 秒后重试`;
      countdown = setInterval(() => {
        if (--secondsLeft > 0) {
          buttonText.value = `${secondsLeft} 秒后重试`;
        } else {
          clearInterval(countdown);
          buttonText.value = '发送验证码';
          isDisabled.value = false;
        }
      }, 1000);
    }
  }

  
  const isDisabled2 = ref(false);  //控发送验证码按钮的禁用
  const buttonText2 = ref('发送验证码');
  let countdown2 = null;
  const startCountdown2 = () => {
    if (!isDisabled2.value) {
      isDisabled2.value = true;
      let secondsLeft = 60;
      buttonText2.value = `${secondsLeft} 秒后重试`;
      countdown2 = setInterval(() => {
        if (--secondsLeft > 0) {
          buttonText2.value = `${secondsLeft} 秒后重试`;
        } else {
          clearInterval(countdown2);
          buttonText2.value = '发送验证码';
          isDisabled2.value = false;
        }
      }, 1000);
    }
  }

  // 准备用户
  const userStore = useUserStore();

  const doLogin = () => {
    const {email, password, checkCode} = formLogin.value
    formLoginRef.value.validate(async (valid) => {
      if (valid) {
        if(await userStore.userLogin({email, password, checkCode}) === 1)
        {
          await userStore.getUserInfo()
          ElMessage({type: 'success', message: '登录成功'})
          router.push({path: '/user'})
        }else{
          reloadImage() //改变验证码
        }
      }
    })
  }

  const doAdminLogin = () => {
    const {email, password, checkCode} = formLogin.value
    formLoginRef.value.validate(async (valid) => {
      if (valid) {
        if(await userStore.adminLog({email, password, checkCode}) === 1)
        {
          await userStore.getUserInfo()
          ElMessage({type: 'success', message: '登录成功'})
          console.log(userStore.isAdmin)
          router.push({path: '/control'})
        }else{
          reloadImage() //改变验证码
        }
      }
    })
  }

  const doRegister = () => {
    const {email, nickName, password, emailCode, checkCode} = formRegister.value
    formRegisterRef.value.validate(async (valid) => {
      if (valid) {
        if(await userStore.userRegister({email, nickName, password, emailCode, checkCode})){
          toggleForm()
        }else{
            reloadImage() //改变验证码
        }
      }
    })
  }

  const doRest = () => {
    const {email, password, emailCode} = formReset.value
    formResetRef.value.validate(async (valid) => {
      if (valid) {
        if(await userStore.userReset({email, password, emailCode})){
          toggleForm()
        }else{
            reloadImage() //改变验证码
        }
      }
    })
  }

  const sendCodeZero = () => {
    const email = formRegister.value.email;
    startCountdown();
    userStore.userSendCode(email, 0);
  };

  const sendCodeOne = () => {
    const email = formReset.value.email;
    startCountdown2();
    userStore.userSendCode(email, 1);
  }

  onMounted(() => {
    ElMessage.warning('不要重复登录')
    if(userStore.userToken){
      router.push({path: '/user'})
    }
  })
  </script>
  
  <style scoped>
  .image {
    position: absolute;
    width: 300px;
    left: 60px;
    top: 150px;
    object-fit: contain;
  }
  
  .title {
    margin-top: 50px;
    font-size: 25px;
    font-weight: 600;
    line-height: 120%;
    background: -webkit-linear-gradient(135deg,
                    #0eaf6d,
                    #ff6ac6 25%,
                    #147b96 50%,
                    #e6d205 55%,
                    #2cc4e0 60%,
                    #8b2ce0 80%,
                    #ff6384 95%,
                    #08dfb4);
            /* 文字颜色填充设置为透明 */
            -webkit-text-fill-color: transparent;
            /* 背景裁剪，即让文字使用背景色 */
            -webkit-background-clip: text;
            /* 背景图放大一下，看着柔和一些 */
            -webkit-background-size: 200% 100%;
            /* 应用动画flowCss 12秒速度 无限循环 线性匀速动画*/
            -webkit-animation: flowCss 12s infinite linear;
        }
        @-webkit-keyframes flowCss {
            0% {
                /* 移动背景位置 */
                background-position: 0 0;
            }
            100% {
                background-position: -400% 0;
            }
  }

  .title:hover {
      -webkit-animation: flowCss 4s infinite linear;
  }

  
  .title-right{
    margin-top: 50px;
    font-size: 18px;
    font-weight: 600;
    line-height: 120%;
  }

  .login {
  background-color: rgba(240, 240, 240, 0.8);
  border-radius: 4rem;
  position: absolute;
  left: 290px;
  top: 130px;
  z-index: 10;
}
  
  .box {
    box-shadow: 32px 18px 21px -3px rgba(0, 0, 0, 0.2);
    width: 50rem;
    height: 35rem;
    border-radius: 4rem;
    background-color: transparent;
    border-color: #f3f4f6;
  }
  
  .leftArea {
    position: absolute;
    left: 0;
    width: 25rem;
    height: 30rem;
    border-radius: 4rem;
    background-color: transparent;
  }
  
  .rightArea {
    position: absolute;
    right: 0;
    width: 25rem;
    height: 30rem;
    border-radius: 4rem;
    background-color: transparent;
  }
  
  .subBtn {
    position: relative;
    left: 50px;
    width: 300px;
    margin-top: 30px;
    font-weight: 600;
    cursor: pointer;
    transition: all .2s;
  }
  
  .subBtn:hover {
    opacity: 1;
  }
  
  .inputArea {
    top: 30px;
    font-size: 16px;
    border-radius: 999px;
    width: 80%;
    position: relative;
    left: 35px;
  }
  
  .box::after {
    content: "";
    position: absolute;
    left: 50%;
    display: block;
    width: 2px;
    background-color: #d5d0d0;
    height: 100%;
  }

  #canvas {
margin-top: 70px;
display: block;
width: 100%;
z-index: -1;
}
  </style>