<template>
  <div id="home">
    <div class="container">
      <img src="../../assets/logo.png" alt="Logo" />
      <h1 class="title">欢迎来到失物招领平台</h1>
      <div class="btnarea">
        <button class="btn btn--stripe" @click="toMainPage">前往主页</button>
        <button class="btn btn--stripe" @click="toLogin">前往登录</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const toMainPage = () => {
  router.push('/user');
}

const toLogin = () => {
  router.push('/login');
}

// Some random colors
const colors = ["#3CC157", "#2AA7FF", "#1B1B1B", "#FCBC0F", "#F85F36", "#b86a70", "#efc348", "#cb740a"];

onMounted(() => {
  const numBalls = 70;
  const balls = [];

  for (let i = 0; i < numBalls; i++) {
    let ball = document.createElement("div");
    ball.classList.add("ball");
    ball.style.background = colors[Math.floor(Math.random() * colors.length)];
    ball.style.left = `${Math.floor(Math.random() * 100)}vw`;
    ball.style.top = `${Math.floor(Math.random() * 100)}vh`;
    ball.style.transform = `scale(${Math.random()})`;
    ball.style.width = `${Math.random() * 1.2}em`;
    ball.style.height = ball.style.width;

    balls.push(ball);
    document.getElementById('home').append(ball);
  }

  // Keyframes
  balls.forEach((el, i, ra) => {
    let to = {
      x: Math.random() * (i % 2 === 0 ? -11 : 11),
      y: Math.random() * 12
    };

    let anim = el.animate(
      [
        { transform: "translate(0, 0)" },
        { transform: `translate(${to.x}rem, ${to.y}rem)` }
      ],
      {
        duration: (Math.random() + 1) * 2000, // random duration
        direction: "alternate",
        fill: "both",
        iterations: Infinity,
        easing: "ease-in-out"
      }
    );
  });
})
</script>

<style lang="scss" scoped>
$color-gray: #666;
$color-black: #000;
$stripe-height: 7px;
$btn-color: $color-gray;
$btn-background: #fff;
$btn-color-hover: #fff;
$btn-background-hover: $color-gray;
$border-color: $color-gray;
$border-color-hover: $color-black;

@mixin reset-button {
    overflow : visible;
    margin : 0;
    padding : 0;
    border : 0;
    background : transparent;
    font : inherit;
    line-height : normal;
    cursor : pointer;
    -moz-user-select : text;
    
    &:-moz-focus-inner {
        padding : 0;
        border : 0;
    }
}

@keyframes stripe-slide {
    0% { background-position: 0% 0; }
    100% { background-position: 100% 0; }
}

.btn {
    @include reset-button;
    display: block;
    text-decoration: none;
    text-transform: uppercase;
    padding: 16px 36px 22px;
    background-color: $btn-background;
    color: $btn-color;
    border: 2px solid $border-color;
    border-radius: 6px;
    margin-bottom: 16px;
    transition: all .5s ease;
    
    &--stripe {
        overflow: hidden;
        position: relative;
        
        &:after {
            content: '';
            display: block;
            height: $stripe-height;
            width: 100%;
            background-image: repeating-linear-gradient(
                45deg,
                $border-color,
                $border-color 1px,
                transparent 2px,
                transparent 5px
            );
            -webkit-backface-visibility: hidden;
            backface-visibility: hidden;
            border-top: 1px solid $border-color;
            position: absolute;
            left: 0;
            bottom: 0;
            background-size: $stripe-height $stripe-height;
        }

        &:hover {
            background-color: $btn-background-hover;
            color: $btn-color-hover;
            border-color: $border-color-hover;

            &:after {
                background-image: repeating-linear-gradient(
                    45deg,
                    $btn-color-hover,
                    $btn-color-hover 1px,
                    transparent 2px,
                    transparent 5px
                );
                border-top: 1px solid $border-color-hover;
                animation: stripe-slide 12s infinite linear forwards;
            }
        }
    }
    
    &--large {
        width: 50%;
    }
    
    &--radius {
        border-radius: 36px;
    }
}
</style>

<style>
  .ball {
    position: absolute;
    border-radius: 100%;
    opacity: 0.7;
  }
</style>

<style scoped>
.container {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}

.container .btnarea {
  display: flex;
  flex-direction: row;
  gap: 50px;
}

.container img {
  width: 280px;
  height: 280px;
}

@keyframes blink {
    0% { opacity: 1; }
    50% { opacity: 0; }
    100% { opacity: 1; }
}

.container .title {
  margin: 20px;
  color: #888;
  font-size: 46px;
  -webkit-animation: blink 1.5s linear infinite; 
     -moz-animation: blink 1.5s linear infinite; 
      -ms-animation: blink 1.5s linear infinite; 
       -o-animation: blink 1.5s linear infinite; 
          animation: blink 1.5s linear infinite; 
}
</style>