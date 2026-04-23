// vue.config.js
module.exports = {
    lintOnSave: false,
    devServer: {
        disableHostCheck: true,
        host: "0.0.0.0",
        port: 3000,

        // 代理配置
        proxy: {
            '/api': {
                target: 'http://localhost:9999',
                changeOrigin: true,
                secure: false,
                pathRewrite: {
                    '^/api': ''
                }
            },
            '/payment': {  // 支付宝支付相关接口
                target: 'http://localhost:9999',
                changeOrigin: true,
                secure: false
            },
            '/order': {  // 订单相关接口
                target: 'http://localhost:9999',
                changeOrigin: true,
                secure: false
            },
            '/patient': {  // 患者相关接口
                target: 'http://localhost:9999',
                changeOrigin: true,
                secure: false
            },
            '/doctor': {  // 医生相关接口
                target: 'http://localhost:9999',
                changeOrigin: true,
                secure: false
            },
            '/admin': {  // 管理员相关接口
                target: 'http://localhost:9999',
                changeOrigin: true,
                secure: false
            },
            '/arrange': {  // 排班相关接口
                target: 'http://localhost:9999',
                changeOrigin: true,
                secure: false
            },
            '/bed': {  // 床位相关接口
                target: 'http://localhost:9999',
                changeOrigin: true,
                secure: false
            },
            '/drug': {  // 药品相关接口
                target: 'http://localhost:9999',
                changeOrigin: true,
                secure: false
            },
            '/check': {  // 检查相关接口
                target: 'http://localhost:9999',
                changeOrigin: true,
                secure: false
            },
            '/email-code': {  // 添加这个配置
                target: 'http://localhost:9999',
                changeOrigin: true,
                secure: false
            },
            '/send-login-code':{
                target: 'http://localhost:9999',
                changeOrigin: true,
                secure: false
            },
            '/ai': {
                target: 'http://localhost:9999',
                changeOrigin: true,
                secure: false
            },
        },

        // 允许所有主机
        allowedHosts: [
            'localhost',
            '127.0.0.1',
            '.cpolar.top',
            '.cpolar.cn'
        ],

        // 设置 public
        public: 'http://7eb0ad75.r39.cpolar.top'

    }
}
