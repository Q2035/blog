import axios from 'axios'

export default {

    /**
     * 根据用户名查询用户信息
     */
    getAccountByUsername(username) {
        return axios.get(`/accounts/${username}`)
    },

    /**
     * 注册的新用户
     */
    registerAccount(account) {
        // 构造新的用户提交，避免影响界面显示
        return axios.post('/accounts', {
            ...account,
            password: api.encrypt.defaultEncode(account.password)
        })
    }
}
