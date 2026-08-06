<template>
    <v-container>
        <v-row justify="center">
            <v-col md="4">
                <v-card>
                    <v-card-title class="text-h5 text-center">
                        로그인
                    </v-card-title>
                    <v-card-text>
                        <v-form>
                            <v-text-field
                                label="이메일"
                                v-model="email"
                            >
                            </v-text-field>
                            <v-text-field
                                label="비밀번호"
                                type="password"
                                v-model="password"
                            >
                            </v-text-field>
                            <v-btn type="button" color="primary" block @click="memberLogin()">로그인</v-btn>
                        </v-form>
                        <br />
                        <v-row>
                            <v-col class="d-flex justify-start">
                                클라이언트
                            </v-col>
                        </v-row>
                        <br />
                        <v-row>
                            <v-col class="d-flex justify-center">
                                <img
                                    src="@/assets/logo_google.svg"
                                    @click="googleLogin()"
                                />
                            </v-col>
                            <v-col class="d-flex justify-center">
                                <img
                                    src="@/assets/logo_kakao.svg"
                                    @click="kakaoLogin()"
                                />
                            </v-col>
                            <v-col class="d-flex justify-center">
                                <img
                                    src="@/assets/logo_naver.svg"
                                    @click="naverLogin()"
                                />
                            </v-col>
                        </v-row>
                        <br />
                        <v-row>
                            <v-col class="d-flex justify-start">
                                서버
                            </v-col>
                        </v-row>
                        <v-row>
                            <v-col class="d-flex justify-center">
                                <img
                                    src="@/assets/logo_google.svg"
                                    @click="serverGoogleLogin()"
                                />
                            </v-col>
                            <v-col class="d-flex justify-center">
                                <img
                                    src="@/assets/logo_kakao.svg"
                                    @click="serverKakaoLogin()"
                                />
                            </v-col>
                            <v-col class="d-flex justify-center">
                                <img
                                    src="@/assets/logo_naver.svg"
                                    @click="serverNaverLogin()"
                                />
                            </v-col>
                        </v-row>

                    </v-card-text>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            email: "",
            password: "",
            googleAuthUri: "https://accounts.google.com/o/oauth2/v2/auth",
            googleClientId: "87066797677-hgm50eo0bvh4rj0jh22g71h70l4rk8gv.apps.googleusercontent.com",
            googleRedirectUri: "http://localhost:3000/oauth/google/redirect",
            googleScope: "openid profile email",
            googleResponseType: "code",
            kakaoAuthUri: "https://kauth.kakao.com/oauth/authorize",
            kakaoClientId: "b78424646042559fd7a7d5f7d3dfd428",
            kakaoRedirectUri: "http://localhost:3000/oauth/kakao/redirect",
            kakaoResponseType: "code",
            naverAuthUri: "https://nid.naver.com/oauth2.0/authorize",
            naverClientId: "guRV0LEemnNVpTon_69J",
            naverRedirectUri: "http://localhost:3000/oauth/naver/redirect",
            naverResponseType: "code",
            serverGoogleAuthUri: "http://localhost:8080/oauth2/authorization/google",
            serverKakaoAuthUri: "http://localhost:8080/oauth2/authorization/kakao",
            serverNaverAuthUri: "http://localhost:8080/oauth2/authorization/naver",
        }
    },
    methods: {
        async memberLogin() {
            const loginData = {
                email: this.email,
                password: this.password
            }
            const response = await axios.post("http://localhost:8080/member/login", loginData)
            const token = response.data.token
            localStorage.setItem("token", token);
            window.location.href = "/"
        },
        googleLogin() {
            const authUri = `${this.googleAuthUri}?client_id=${this.googleClientId}&redirect_uri=${this.googleRedirectUri}&response_type=${this.googleResponseType}&scope=${this.googleScope}`;
            window.location.href = authUri;
        },
        kakaoLogin() {
            const authUri = `${this.kakaoAuthUri}?client_id=${this.kakaoClientId}&redirect_uri=${this.kakaoRedirectUri}&response_type=${this.kakaoResponseType}`;
            window.location.href = authUri;
        },
        naverLogin() {
            //const state = crypto.randomUUID();
            //const authUri = `${this.naverAuthUri}?client_id=${this.naverClientId}&redirect_uri=${this.naverRedirectUri}&response_type=${this.naverResponseType}&state=${state}`;
            const authUri = `${this.naverAuthUri}?client_id=${this.naverClientId}&redirect_uri=${this.naverRedirectUri}&response_type=${this.naverResponseType}`;
            window.location.href = authUri;
        },
        serverGoogleLogin() {
            const authUri = `${this.serverGoogleAuthUri}`;
            window.location.href = authUri;
        },
        serverKakaoLogin() {
            const authUri = `${this.serverKakaoAuthUri}`;
            window.location.href = authUri;
        },
        serverNaverLogin() {
            const authUri = `${this.serverNaverAuthUri}`;
            window.location.href = authUri;
        }
    }
}
</script>