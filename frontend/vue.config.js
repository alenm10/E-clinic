module.exports = {
    "devServer": {
        proxy: {
            "^/api": {
                target: "http://localhost:8089",
                changeOrigin: true,
                logLevel: "debug"
            }
        }
    },
    "outputDir": "target/dist",
    "assetsDir": "static",
    "transpileDependencies": [
        "vuetify"
    ]
};
