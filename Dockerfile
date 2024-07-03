FROM openjdk:11
EXPOSE 8080
ADD ./target/spring-starbucks-client-2.2.jar /srv/spring-starbucks-client-2.2.jar
CMD java -jar /srv/spring-starbucks-client-2.2.jar