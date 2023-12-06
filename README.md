# MyBatis with Spring boot
## environment ##
  - database.url = jdbc:postgresql://rain.db.elephantsql.com/rlfkxfnr
  - database.username = rlfkxfnr
  - database.password = wwxVYSMJTXoc4ExqcQyMc-XjXAMd89_t
## development ##
  - spring.thymeleaf.cache = false

## deployment ##
  - Command
    ```
    echo STEP1: build maven project
    echo "-v /usr/share/maven/.m2:/root/.m2" thông tin repository sẽ được lưu ở "/usr/share/maven/.m2"
    echo "-v ${WORKSPACE}:/usr/src/maven-project" gắn thư mục project vào thư mục "/usr/src/maven-project"
    echo "-w /usr/src/maven-project" chọn workspace để chạy maven
    docker run --rm --name maven-build-project -v /usr/share/maven/.m2:/root/.m2 -v ${WORKSPACE}:/usr/src/maven-project -w /usr/src/maven-project maven:3.8.7-openjdk-18-slim mvn clean package

    echo STEP2: build docker image
    docker compose down
    docker rmi -f spring-boot-mybatis-example:java
    docker build -t spring-boot-mybatis-example:java .

    echo STEP3: deploy docker image
    docker compose up -d
    ```
