<template>
  <div> this is order Info</div>
</template>

<script>
  export default {
    name: "OrderInfo",
    mounted() {
      if (this.$store.state.user.token === undefined || this.$store.state.user.token === null || this.$store.state.user.token === "") {
        const token = localStorage.getItem('token');
        if (token !== undefined) {
          const sendData = {
            'token': token
          }
          const that = this;
          http.postForm(this, 'user', 'tokenUse', sendData, function (data) {
            const dt = data.data;
            if (dt.code !== 0) {
              return;
            }
            const user = dt.data;
            that.$set(that.$data, 'user', user);
            that.$store.commit('loginSuccess', user);


          })
        }
      } else {

      }
    }
  }
</script>

<style scoped>

</style>
