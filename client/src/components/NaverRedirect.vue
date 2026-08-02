<template>
    <div>
        네이버 로그인 진행 중...
    </div>
</template>

<script>
import axios from 'axios';

export default {
    created() {
        const code = new URL(window.location.href).searchParams.get("code");
        this.sendCodeToServer(code);
    },
    methods: {
        async sendCodeToServer(code) {

            const response = await axios.post("http://localhost:8080/member/naver/login", {code});
            console.log(response);
            const token = response.data.token;
            localStorage.setItem("token", token);
            window.location.href = "/";
        }
    }
}
</script>