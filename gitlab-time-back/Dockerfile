FROM maven:3.9.2

WORKDIR /app
COPY . /app
RUN mvn clean install
 
RUN cp /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar" , "app.jar"]


