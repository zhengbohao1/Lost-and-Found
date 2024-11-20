<template>
    <div style="text-align: center;">
        <div class="container">
            <div class="item item-1"></div>
            <div class="item item-2"></div>
            <div class="item item-3"></div>
            <div class="item item-4"></div>
        </div>
        <span class="load">Loading……</span>
    </div>
</template>

<style scpoed>
.container {
  position: relative;
  width: 200px;
  height: 200px;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  margin: auto;
}

.item {
  width: 50px;
  height: 50px;
  position: absolute;
}

.item-1 {
  background-color: #ff4637;
  top: 50px;
  left: 50px;
  z-index: 1;
  animation: item-1_move 1.8s cubic-bezier(.6,.01,.4,1) infinite;
}

.item-2 {
  background-color: #ffb400;
  top: 50px;
  right: 50px;
  animation: item-2_move 1.8s cubic-bezier(.6,.01,.4,1) infinite;
}

.item-3 {
  background-color: #ff5eae;
  bottom: 50px;
  right: 50px;
  z-index: 1;
  animation: item-3_move 1.8s cubic-bezier(.6,.01,.4,1) infinite;
}

.item-4 {
  background-color: #0283e0;
  bottom: 50px;
  left: 50px;
  animation: item-4_move 1.8s cubic-bezier(.6,.01,.4,1) infinite;
}

@keyframes item-1_move {
  0%, 100% {transform: translate(0, 0)} 
  25% {transform: translate(0, 50px)} 
  50% {transform: translate(50px, 50px)} 
  75% {transform: translate(50px, 0)} 
}

@keyframes item-2_move {
  0%, 100% {transform: translate(0, 0)} 
  25% {transform: translate(-50px, 0)} 
  50% {transform: translate(-50px, 50px)} 
  75% {transform: translate(0, 50px)} 
}

@keyframes item-3_move {
  0%, 100% {transform: translate(0, 0)} 
  25% {transform: translate(0, -50px)} 
  50% {transform: translate(-50px, -50px)} 
  75% {transform: translate(-50px, 0)} 
}

@keyframes item-4_move {
  0%, 100% {transform: translate(0, 0)} 
  25% {transform: translate(50px, 0)} 
  50% {transform: translate(50px, -50px)} 
  75% {transform: translate(0, -50px)} 
}

.load {
    font-size: 25px;
    font-weight: 600;
    line-height: 120%;
    background: -webkit-linear-gradient(135deg,
                    #ffb400,
                    #ac617c 20%,
                    #ff5eae 40%,
                    #0283e0 60%,
                    #ff4637);
            /* 文字颜色填充设置为透明 */
            -webkit-text-fill-color: transparent;
            /* 背景裁剪，即让文字使用背景色 */
            -webkit-background-clip: text;
            /* 背景图放大一下，看着柔和一些 */
            -webkit-background-size: 200% 100%;
            /* 应用动画flowCss 12秒速度 无限循环 线性匀速动画*/
            -webkit-animation: flowCss 6s infinite linear;
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
</style>