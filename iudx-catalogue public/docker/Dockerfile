FROM 	openjdk:8-jdk

ADD	https://github.com/JanusGraph/janusgraph/releases/download/v0.3.1/janusgraph-0.3.1-hadoop2.zip .

RUN	apt update && apt upgrade && \
	apt-get install unzip && \
	unzip janusgraph-0.3.1-hadoop2.zip 

WORKDIR ./janusgraph-0.3.1-hadoop2/ 

COPY	./conf/ ./conf/

RUN	sed -E -i.bak 's/storage\.hostname=.*$/storage\.hostname=cassandra/' conf/gremlin-server/janusgraph-cassandra-es-server.properties \
	&& sed -E -i.bak 's/index\.search\.hostname=.*$/index\.search\.hostname=es-kib/' conf/gremlin-server/janusgraph-cassandra-es-server.properties \
	&& sed -E -i.bak 's/storage\.backend=.*$/storage\.backend=cql/' conf/gremlin-server/janusgraph-cassandra-es-server.properties \
	&& sed -E -i.bak 's/channel\.WebSocketChannelizer/channel\.HttpChannelizer/' conf/gremlin-server/gremlin-server.yaml \
	&& rm conf/gremlin-server/gremlin-server.yaml.bak

ADD 	https://github.com/ufoscout/docker-compose-wait/releases/download/2.5.0/wait /wait
RUN	chmod +x /wait


CMD 	/wait && bin/gremlin-server.sh

