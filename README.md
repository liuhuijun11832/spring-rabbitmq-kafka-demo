# 说明
该项目是一个spring整合rabbitmq和kafka的demo，采用注解式配置。
* rabbit-common：包含了公共模块
* rabbit-consumer：包含mq的消费者
* rabbit-provider: 包含mq的生产者
* kafka-provider：kafka的发送方
* kafka-consumer: kafka的消费方
# 启动说明
consumer和provider需要使用servlet容器启动，推荐jetty或者tomcat，启动前修改生产者和消费者resources/rabbit.properties以及resources/kafka.properties文件，修改为自己的所使用的队列信息即可。