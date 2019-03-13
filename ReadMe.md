# XShop
一套基于springboot + vue的开源商城

**处于开发阶段，暂时欠缺部分功能**




## 项目结构
- xshop-admin vue后台管理
- xshop-admin-api 后台api
- xshop-core 核心代码
- xshop-dao dao层
- xshop-pojo pojo对象
- xshop-service service层
- xshop-web-api webapp api
- xshop-web-app vue移动端商场页面

## 如何贡献代码
参阅 [开发文档-如何贡献代码](./docs/dev.md)


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