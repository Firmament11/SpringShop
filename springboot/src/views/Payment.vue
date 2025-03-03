<template>
  <div class="payment">
    <!-- 支付按钮 -->
    <el-button type="primary" @click="handlePayment">支付宝支付</el-button>
    
    <!-- 用于显示支付宝表单的容器 -->
    <div ref="alipayForm"></div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Payment',
  data() {
    return {
      orderId: this.$route.params.orderId // 假设从路由参数获取订单ID
    }
  },
  methods: {
    handlePayment() {
      // 调用后端创建支付接口
      axios.post(`/api/alipay/create/${this.orderId}`)
        .then(response => {
          if (response.data.code === 200) {
            // 获取支付表单字符串
            const divForm = response.data.data
            
            // 将表单字符串转换为HTML并插入到页面中
            const div = this.$refs.alipayForm
            div.innerHTML = divForm
            
            // 自动提交表单
            document.forms[0].submit()
          } else {
            this.$message.error('创建支付失败：' + response.data.msg)
          }
        })
        .catch(error => {
          this.$message.error('创建支付失败：' + error.message)
        })
    }
  }
}
</script>

<style scoped>
.payment {
  padding: 20px;
}
</style> 