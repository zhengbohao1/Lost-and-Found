export function waterFallInit(columns, card_columns, arrHeight, cards) {
    columns.value = waterfall_column() // 设置列数
    console.log('列数', columns.value)
    card_columns.value = {} // 初始化每一列的卡片数组
    arrHeight.value = [] // 清空高度数组

    // 初始化每一列的卡片数组
    for (let i = 0; i < columns.value; i++) {
        card_columns.value[i] = []
    }

    // 遍历卡片数组，分配卡片到每一列
    for (let i = 0; i < cards.value.length; i++) {
        var height = cards.value[i].height / (cards.value[i].width / 240) // 计算卡片高度
        
        // 如果卡片数量小于列数，则直接分配到对应的列,铺满第一行
        if (i < columns.value) {
            card_columns.value[i].push(cards.value[i])
            arrHeight.value.push(height)
        } else {
            // 找到高度最小的一列，并将卡片分配到该列
            let obj = {
                minH: arrHeight.value[0],
                minI: 0
            }
            for (let j = 0; j < columns.value; j++) {
                if (arrHeight.value[j] < obj.minH) {
                    obj.minH = arrHeight.value[j]
                    obj.minI = j
                }
            }
            card_columns.value[obj.minI].push(cards.value[i])
            arrHeight.value[obj.minI] += height
        }
    }
}

export function waterFallMore(arrHeight, card_columns, more) {
    for (let i = 0; i < more.length; i++) {
        var height = more[i].height / (more[i].width / 240) // 计算卡片高度
        
        // 找到高度最小的一列，并将卡片分配到该列
        let obj = {
            minH: arrHeight.value[0],
            minI: 0
        }
        for (let j = 0; j < arrHeight.value.length; j++) {
            if (arrHeight.value[j] < obj.minH) {
                obj.minH = arrHeight.value[j]
                obj.minI = j
            }
        }
        card_columns.value[obj.minI].push(more[i])
        arrHeight.value[obj.minI] += height
    }
}

//当窗口大小发生变化时，延迟调用 waterFallInit 函数重新初始化瀑布流布局。
export function resizeWaterFall(columns, card_columns, arrHeight, cards) {
    var timerId = null;
    window.onresize = function () {
        if (timerId) {
            clearTimeout(timerId); // 取消之前的定时器
        }
        timerId = setTimeout(() => {
            waterFallInit(columns, card_columns, arrHeight, cards) // 重新初始化瀑布流
        }, 300); // 延迟300毫秒执行
    }
}

const waterfall_column = () => {
    // 获取主容器的宽度,id为content
    var content = document.getElementById('content')
    var contentWidth = content.offsetWidth
    console.log('waterfall_test',contentWidth)  //可以正常获取
    return Math.floor(contentWidth / (280))
}