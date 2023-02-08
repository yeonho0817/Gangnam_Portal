# build 
# maven을 사용하기 위해 미리 설정된 이미지 파일을 불러오는 것
FROM maven:3.8.6-jdk-11 AS build

RUN mkdir gnpt-api
WORKDIR gnpt-api

# 빌드하기 위한 프로젝트 파일을 복사
COPY . .
# 빌드 명령어 실행
RUN mvn -f pom.xml clean package -DskipTests

# run
FROM amazoncorretto:11-alpine-jdk

# WORKDIR로 사용할 폴더 생성
RUN mkdir gnpt-api
#
WORKDIR gnpt-api
COPY --from=build /gnpt-api/target/portal-0.0.1-SNAPSHOT.jar .

CMD ["java","-jar", "-Dspring.profiles.active=ip", "/gnpt-api/portal-0.0.1-SNAPSHOT.jar"]
# CMD ["java","-jar", "/gnpt-api/portal-0.0.1-SNAPSHOT.jar"]



# COPY --from=build /usr/src/app/target/helloworld-1.0.0-SNAPSHOT.jar /usr/app/helloworld-1.0.0-SNAPSHOT.jar
# EXPOSE 8080
# ENTRYPOINT ["java","-jar","/usr/app/helloworld-1.0.0-SNAPSHOT.jar"]