<template>
	<div>
		<wechat-mp-login v-if="isShowAuth" @passwordLogin="handleAuthSuccess" @success="handleSuccess" />
		<account-login v-else :openid="openid" @success="handleLoginSuccess" />
	</div>
</template>

<script>
import store from '@/store'
import {
	isWechat,
	checkLogin
} from '@/utils/app'
import AccountLogin from './components/account-login'
import WechatMpLogin from './components/wechat-mp-login'
import * as UserApi from '@/api/manage/user'
import * as LoginApi from '@/api/login'
import { isEmpty } from '@/utils/verify'

export default {
	components: {
		AccountLogin,
		WechatMpLogin
	},

	data() {
		return {
			openid: '',
			isShowAuth: false,
			redirectUrl: ''
		}
	},
	/**
	 * 生命周期函数--监听页面显示
	 */
	onLoad(data) {
		this.init(data)
	},
	methods: {
		init(data) {
			const app = this
			if (data && data.redirectUrl) {
				app.redirectUrl = data.redirectUrl
			}
			if (checkLogin()) {
				uni.reLaunch({url: '/pages/index/index'})
				return;
			}
			// #ifdef MP-WEIXIN
			app.isShowAuth = wx.canIUse('getUserProfile')
			// #endif
			if (isWechat() && isEmpty(app.openid)) {
				// #ifdef H5
				uni.showLoading({ title: '页面加载中..', mask: true });
				LoginApi.authLoginConfig().then(result => {
					uni.hideLoading();
					if (result.data.appId && result.data.domain) {
						const appId = result.data.appId;
						const domain = result.data.domain;
						const redirect_uri = encodeURIComponent(domain + "#pages/login/components/wehcat-auth");
						const url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri=" +
							redirect_uri + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
						window.location.href = url;
						return true;
					}
				})
				// #endif
			}
		},
		handleAuthSuccess(data) {
			this.openid = data.openid
			this.isShowAuth = false
		},
		handleSuccess() {
			if (this.redirectUrl) {
				uni.reLaunch({url: this.redirectUrl})
				return
			}
			uni.reLaunch({url: '/pages/index/index'})
		},
		handleLoginSuccess(openid) {
			const app = this
			const platform = store.getters.platform
			if (platform !== 'H5' || isWechat()) {
				app.handleUserBind(openid);
			} else {
				app.handleSuccess()
			}
		},
		handleUserBind(openid) {
			const app = this
			UserApi.bind(openid).then(result => {
				app.$toast(result.msg)
				app.handleSuccess()
			})
		}
	}
}
</script>

<style lang="scss" scoped>
	//empty
</style>