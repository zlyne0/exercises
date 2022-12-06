### examples 
https://developer.confluent.io/tutorials/kafka-console-consumer-producer-basics/kafka.html

```bash
docker-compose up -d
```
### create topic

```bash
docker-compose exec broker kafka-topics --create --topic orders --bootstrap-server broker:9092
```

or auto create by broker docker environment variable
```bash
KAFKA_AUTO_CREATE_TOPICS_ENABLE: "true"
```
or create by topic definition in spring bean
```java
	@Bean
	public NewTopic createTopic() {
		return new NewTopic(Conf.TOPIC_NAME, 1, (short)1);
	}
```

### start consumer
```bash
docker-compose exec broker bash

kafka-console-consumer \
  --topic orders \
  --bootstrap-server broker:9092
```

### start producer
```bash
kafka-console-producer \
  --topic orders \
  --bootstrap-server broker:9092
```

### read all from beggining

```bash
kafka-console-consumer \
  --topic orders \
  --bootstrap-server broker:9092 \
  --from-beginning
```
