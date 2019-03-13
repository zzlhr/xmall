# XShop
一套基于springboot + vue的开源商城


## 项目结构
- xshop-admin vue后台管理
- xshop-admin-api 后台api
- xshop-core 核心代码
- xshop-dao dao层
- xshop-pojo pojo对象
- xshop-service service层
- xshop-web-api webapp api
- xshop-web-app vue移动端商场页面


## 启动
1. 安装依赖
```bash
mvn compile
mvn install
```

2. 编译后台
```bash
cd xshop-web-api
mvn package
java -jar target/xshop-web-api.jar
```


3. 编译页面
```bash
cd xshop-web-app
npm install
npm run dev
```