#springboot http-log2es es异步日志收集 分布式定时任务框架

依赖于springboot打造http日志收集器 同时规划分布式定时任务 结合redis、 mysql等完成基本web框架

##功能
- 日志收集 (使用java直连es进行日志收集);
- 分布式任务调度（结合redisson分布式锁进行多实例定时任务抢占）。


###克隆代码到本地

- $ git clone https://github.com/jiasion/eslog.git
- $ cd eslog/
###修改配置文件

        $ vim src/main/resources/config/application-prod.properties
###IDEA

        $ mvn clean && mvn package

###访问
        $ curl -X POST \
  http://127.0.0.1:9090/log/save \
  -H 'Content-Type: application/json;charset=UTF-8' \
  -H 'Postman-Token: b6ae2eeb-12d9-4049-8148-1101c9cb5388' \
  -H 'cache-control: no-cache' \
  -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
  -F key=3817848ef191468810fc4b1cfc855ba1 \
  -F 'tag=测试mysql' \
  -F value=ok \
  -F request_id=dfeeeeeeeee 

###es-7.3.1 es工厂模式 

###定时任务 redission分布式锁+多线程

