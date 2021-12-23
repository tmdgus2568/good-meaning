<template>
  <div>
    <b-form @submit="onSubmit" v-if="show">
      <b-form-group class="group-user-id">
        <b-form-input
          id="userId"
          v-model="form.userId"
          type="text"
          placeholder="아이디"
          required
        ></b-form-input>
      </b-form-group>

      <b-form-group class="group-user-pw">
        <b-form-input
          id="userPw"
          v-model="form.userPw"
          type="password"
          placeholder="비밀번호"
          required
        ></b-form-input>
      </b-form-group>

      <b-form-group class="group-setting" v-slot="{ ariaDescribedby }">
        <b-form-checkbox-group
          v-model="form.setting"
          id="checkbox-group-setting"
          :aria-describedby="ariaDescribedby"
        >
          <b-form-checkbox value="auto">자동로그인</b-form-checkbox>
          <b-form-checkbox value="save_id">아이디저장</b-form-checkbox>
        </b-form-checkbox-group>
      </b-form-group>
      <router-link href="">회원가입</router-link>
      <router-link href="">아이디/비밀번호찾기</router-link>

      <b-button type="submit" variant="primary">로그인하기</b-button>
    
    </b-form>
    <b-card class="mt-3" header="Form Data Result">
      <pre class="m-0">{{ form }}</pre>
    </b-card>
    
  </div>
</template>

<script>
  import axios from 'axios'

  export default {
    name: "Login",
    
    data() {
      return {
        
        form: {
          userId: '',
        //   userPw: '',
        //   setting: []
        },
        
        show: true
      }
    },
    methods: {
      
      onSubmit(event) {
        event.preventDefault()
        
        // alert(JSON.stringify(this.form))
        console.log(this.form.userId, this.form.userPw)
        axios.post("/test/login").then(response => {
            console.log(response.data);

      })
      },

    }
  }
</script>