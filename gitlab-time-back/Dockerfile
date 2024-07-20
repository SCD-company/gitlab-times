FROM maven:3.9.2

WORKDIR /app
COPY . /app
RUN mvn clean install -Dmaven.test.skip
 
RUN cp /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar" , "app.jar"]


