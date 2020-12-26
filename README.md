# API Test Automation Tutorial

REST APIのテスト自動化のチュートリアルです。

## 前提環境

- OpenJDK11 (or 8)
- Apache Maven

## プロジェクトの作成

```
$ mvn io.quarkus:quarkus-maven-plugin:1.7.5.Final-redhat-00011:create \
    -DprojectGroupId=com.example \
    -DprojectArtifactId=testapp \
    -DplatformGroupId=com.redhat.quarkus \
    -DplatformVersion=1.7.5.Final-redhat-00011 \
    -DclassName=com.example.sampleapp.rest.HelloResource

...
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  11.366 s
[INFO] Finished at: 2020-12-24T16:27:30+09:00
[INFO] ------------------------------------------------------------------------
```

![プロジェクト](./img/001.png)

pom.xmlには以下のような依存関係が定義されています。
デフォルトでQuarkusのテスト支援機能やREST-Assuredが入っています。

```
 <dependencies>
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-resteasy</artifactId>
    </dependency>
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-junit5</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>io.rest-assured</groupId>
      <artifactId>rest-assured</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>
```

## 実行確認
サンプルアプリのソースコードです。

```java
package com.example.sampleapp.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class HelloResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }
}
```

テストケースはこちらです。REST-Assuredベースの書き方になっています。
```java
package com.example.sampleapp.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class HelloResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(is("hello"));
    }

}
```


デフォルトで用意されたテストを実行します。

```txt
% cd testapp 
% mvn clean verify
[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------------< com.example:testapp >-------------------------
[INFO] Building testapp 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-clean-plugin:2.5:clean (default-clean) @ testapp ---
[INFO] 
[INFO] --- quarkus-maven-plugin:1.7.5.Final-redhat-00011:generate-code (default) @ testapp ---
[INFO] 
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ testapp ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 2 resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.8.1:compile (default-compile) @ testapp ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 1 source file to /Users/nkoike/trial/quarkus/rhboq/testapp/target/classes
[INFO] 
[INFO] --- quarkus-maven-plugin:1.7.5.Final-redhat-00011:generate-code-tests (default) @ testapp ---
[INFO] 
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ testapp ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory /Users/nkoike/trial/quarkus/rhboq/testapp/src/test/resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.8.1:testCompile (default-testCompile) @ testapp ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 2 source files to /Users/nkoike/trial/quarkus/rhboq/testapp/target/test-classes
[INFO] 
[INFO] --- maven-surefire-plugin:3.0.0-M5:test (default-test) @ testapp ---
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.sampleapp.rest.HelloResourceTest
2020-12-24 16:59:56,844 INFO  [io.quarkus] (main) Quarkus 1.7.5.Final-redhat-00011 on JVM started in 1.295s. Listening on: http://0.0.0.0:8081
2020-12-24 16:59:56,845 INFO  [io.quarkus] (main) Profile test activated. 
2020-12-24 16:59:56,845 INFO  [io.quarkus] (main) Installed features: [cdi, resteasy]
[INFO] Tests run: 1, Failures: 0, Erro**rs: 0, Skipped: 0, Time elapsed: 3.824 s - in com.example.sampleapp.rest.HelloResourceTest
2020-12-24 16:59:57,958 INFO  [io.quarkus] (main) Q**uarkus stopped in 0.020s
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] 
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ testapp ---
[INFO] Building jar: /Users/nkoike/trial/quarkus/rhboq/testapp/target/testapp-1.0-SNAPSHOT.jar
[INFO] 
[INFO] --- quarkus-maven-plugin:1.7.5.Final-redhat-00011:build (default) @ testapp ---
[INFO] [org.jboss.threads] JBoss Threads version 3.1.1.Final-redhat-00001
[INFO] [io.quarkus.deployment.pkg.steps.JarResultBuildStep] Building thin jar: /Users/nkoike/trial/quarkus/rhboq/testapp/target/testapp-1.0-SNAPSHOT-runner.jar
[INFO] [io.quarkus.deployment.QuarkusAugmentor] Quarkus augmentation completed in 946ms
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  9.283 s
[INFO] Finished at: 2020-12-24T16:59:59+09:00
[INFO] ------------------------------------------------------------------------
```

Quarkusアプリ（API）がテスト時に起動し、テストが実施されました。

## REST-Assuredによるテスト

### テストケースの作成

割愛します


## Quarkusアプリケーションのテスト

### サンプルアプリへの機能追加

JSON-B Extensionを追加
```
$ mvn quarkus:add-extensions -Dextensions="resteasy-jsonb"  
```

メソッドを追加
```java
    @GET
    @Path("/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String,Object> helloJson() {
        
        Map<String,Object> res = new HashMap<>();
        res.put("name","Yamada");
        res.put("age",20);
        res.put("birthdate","2000/12/25");

        return res;
    }
```
以下のJSONを返します
```
$ curl http://localhost:8080/hello/json
{"birthdate":"2000/12/25","name":"Yamada","age":20}
```
### テストの修正

HelloResourceTestには以下を追加
```java
    @Test
    public void testJson() {
      given()
        .when().get("/hello/json")
        .then()
          .log().all()
          .assertThat()
          .body(containsString("Yamada"));

    }
```
以下のimport文も追加しておきます。
```java
import static org.hamcrest.Matchers.*;
```
テストを実行
```
$ mvn test
...
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.sampleapp.rest.HelloResourceTest
2020-12-25 23:38:50,411 INFO  [io.quarkus] (main) Quarkus 1.7.5.Final-redhat-00011 on JVM started in 2.767s. Listening on: http://0.0.0.0:8081
2020-12-25 23:38:50,411 INFO  [io.quarkus] (main) Profile test activated.
2020-12-25 23:38:50,411 INFO  [io.quarkus] (main) Installed features: [cdi, resteasy, resteasy-jsonb]
HTTP/1.1 200 OK
Content-Length: 53
Content-Type: application/json

{
    "birthdate": "2000/12/25",
    "name": "Yamada",
    "age": "20"
}
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 5.553 s - in com.example.sampleapp.rest.HelloResourceTest
2020-12-25 23:38:52,567 INFO  [io.quarkus] (main) Quarkus stopped in 0.041s
[INFO] 
[INFO] Results:
[INFO]
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  12.024 s
[INFO] Finished at: 2020-12-25T23:38:52+09:00
[INFO] ------------------------------------------------------------------------
```
追加したテストも成功。
レスポンスの内容も出力されています。

次は以下のテストを追加。
JSONをParseしてプロパティの値をAssertします。
```java
   @Test
    public void testJson2() {
      given()
        .when().get("/hello/json")
        .then()
          .log().body()
          .assertThat()
          .body("age",equalTo("20"));

    }

```
今度はレスポンスのbodyを出力して成功。
```
$ mvn test
...
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.sampleapp.rest.HelloResourceTest
2020-12-25 23:59:38,885 INFO  [io.quarkus] (main) Quarkus 1.7.5.Final-redhat-00011 on JVM started in 2.693s. Listening on: http://0.0.0.0:8081
2020-12-25 23:59:38,887 INFO  [io.quarkus] (main) Profile test activated.
2020-12-25 23:59:38,887 INFO  [io.quarkus] (main) Installed features: [cdi, resteasy, resteasy-jsonb]
{
    "birthdate": "2000/12/25",
    "name": "Yamada",
    "age": "20"
}
HTTP/1.1 200 OK
Content-Length: 53
Content-Type: application/json

{
    "birthdate": "2000/12/25",
    "name": "Yamada",
    "age": "20"
}
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 5.752 s - in com.example.sampleapp.rest.HelloResourceTest
2020-12-25 23:59:41,357 INFO  [io.quarkus] (main) Quarkus stopped in 0.036s
[INFO] 
[INFO] Results:
[INFO]
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  11.370 s
[INFO] Finished at: 2020-12-25T23:59:41+09:00
[INFO] ------------------------------------------------------------------------
```
次は以下のテストを追加。
複数項目の検証を行います。
```java
@Test
    public void testJson3() {
      given()
        .when().get("/hello/json")
        .then()
          .log().body()
          .assertThat()
          .body("birthdate",not(empty()))
          .body("name", equalToIgnoringCase("yamada"))
          .body("gender",nullValue())
          .body("age",lessThan(30));

    }
```
成功しました。
```
$ mvn test
...
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.sampleapp.rest.HelloResourceTest
2020-12-26 00:24:37,874 INFO  [io.quarkus] (main) Quarkus 1.7.5.Final-redhat-00011 on JVM started in 2.215s. Listening on: http://0.0.0.0:8081
2020-12-26 00:24:37,876 INFO  [io.quarkus] (main) Profile test activated. 
2020-12-26 00:24:37,876 INFO  [io.quarkus] (main) Installed features: [cdi, resteasy, resteasy-jsonb]
{
    "birthdate": "2000/12/25",
    "name": "Yamada",
    "age": 20
}
{
    "birthdate": "2000/12/25",
    "name": "Yamada",
    "age": 20
}
HTTP/1.1 200 OK
Content-Length: 51
Content-Type: application/json

{
    "birthdate": "2000/12/25",
    "name": "Yamada",
    "age": 20
}
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 4.846 s - in com.example.sampleapp.rest.HelloResourceTest
2020-12-26 00:24:40,039 INFO  [io.quarkus] (main) Quarkus stopped in 0.038s
[INFO] 
[INFO] Results:
[INFO]
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  10.300 s
[INFO] Finished at: 2020-12-26T00:24:40+09:00
[INFO] ------------------------------------------------------------------------
```
ステータスコードを扱うテストは以下のように。
```java
    @Test
    public void testJson404() {
      given()
        .when().get("/hello/404")
        .then()
          .statusCode(405)
          .log().all();
    }
```

```
$ mvn test
...
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.sampleapp.rest.HelloResourceTest
2020-12-26 00:31:58,597 INFO  [io.quarkus] (main) Quarkus 1.7.5.Final-redhat-00011 on JVM started in 2.222s. Listening on: http://0.0.0.0:8081
2020-12-26 00:31:58,600 INFO  [io.quarkus] (main) Profile test activated.
2020-12-26 00:31:58,600 INFO  [io.quarkus] (main) Installed features: [cdi, resteasy, resteasy-jsonb]
HTTP/1.1 404 Not Found
Content-Length: 86
Content-Type: text/html;charset=UTF-8

<html>
  <body>RESTEASY003210: Could not find resource for full path: http://localhost:8081/hello/404</body>
</html>
{
    "birthdate": "2000/12/25",
    "name": "Yamada",
    "age": 20
}
{
    "birthdate": "2000/12/25",
    "name": "Yamada",
    "age": 20
}
HTTP/1.1 200 OK
Content-Length: 51
Content-Type: application/json

{
    "birthdate": "2000/12/25",
    "name": "Yamada",
    "age": 20
}
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 5.092 s - in com.example.sampleapp.rest.HelloResourceTest
2020-12-26 00:32:00,993 INFO  [io.quarkus] (main) Quarkus stopped in 0.057s
[INFO] 
[INFO] Results:
[INFO]
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  10.617 s
[INFO] Finished at: 2020-12-26T00:32:01+09:00
[INFO] ------------------------------------------------------------------------
```

## DBを使用するQuarkusアプリのテスト

### アプリの追加

以下のソースファイルを追加します。
- [FruitResource.java](./src/main/java/com/example/sampleapp/rest/FruitResource.java)
- [Fruit.java](./src/main/java/com/example/sampleapp/rest/Fruit.java)

以下のリソースファイルを追加します。
- [import.sql](./src/main/resource/import.sql)

application.propertiesに以下の定義を追加します。
DB接続情報および初期化のための設定になります。
DBはインメモリDB（H2）を使用します。
```properties
quarkus.datasource.db-kind=h2
quarkus.datasource.username=quarkus_test
quarkus.datasource.password=quarkus_test
%dev.quarkus.datasource.jdbc.url=jdbc:h2:mem:quarkus_test
%dev.quarkus.datasource.jdbc.driver=org.h2.Driver

quarkus.hibernate-orm.database.generation=drop-and-create
quarkus.hibernate-orm.log.sql=true
quarkus.hibernate-orm.sql-load-script=import.sql

```

Extensionを追加します。
```
$ mvn quarkus:add-extensions -Dextensions="hibernate-orm,jdbc-h2,resteasy-jackson"   
```
起動して動作確認します。
```
$ mvn clean compile quarkus:dev
...
[INFO] --- quarkus-maven-plugin:1.7.5.Final-redhat-00011:dev (default-cli) @ testapp ---
Listening for transport dt_socket at address: 5005
Hibernate: 

    drop table if exists known_fruits CASCADE
Hibernate: 

    drop sequence if exists known_fruits_id_seq
Hibernate: create sequence known_fruits_id_seq start with 10 increment by 1
Hibernate: 

    create table known_fruits (
       id integer not null,
        name varchar(40),
        primary key (id)
    )
Hibernate:

    alter table known_fruits
       add constraint UK_57g3m8wr3qxoj706a6hsqg6ye unique (name)
Hibernate: 
    INSERT INTO known_fruits(id, name) VALUES (1, 'Cherry')
Hibernate: 
    INSERT INTO known_fruits(id, name) VALUES (2, 'Apple')
Hibernate:
    INSERT INTO known_fruits(id, name) VALUES (3, 'Banana')
__  ____  __  _____   ___  __ ____  ______ 
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/
2020-12-26 17:38:42,904 INFO  [io.quarkus] (Quarkus Main Thread) testapp 1.0-SNAPSHOT on JVM (powered by Quarkus 1.7.5.Final-redhat-00011) started in 2.888s. Listening on: http://0.0.0.0:8080
2020-12-26 17:38:42,912 INFO  [io.quarkus] (Quarkus Main Thread) Profile dev activated. Live Coding activated.
2020-12-26 17:38:42,913 INFO  [io.quarkus] (Quarkus Main Thread) Installed features: [agroal, cdi, hibernate-orm, jdbc-h2, mutiny, narayana-jta, resteasy, resteasy-jackson, resteasy-jsonb, smallrye-context-propagation]
```
APIを呼び出します。
DBから取得した結果が表示されます。
```
$ curl http://localhost:8080/fruits
[{"id":2,"name":"Apple"},{"id":3,"name":"Banana"},{"id":1,"name":"Cherry"}]
```

### テストの追加
以下のソースファイルを追加します。

- [FruitsEndpointTest.java](./src/test/java/com/example/sampleapp/rest/FruitsEndpointTest.java)

このテストには更新系の処理が含まれるシナリオベースのテストになっています。
```java
@QuarkusTest
public class FruitsEndpointTest {

    @Test
    public void testListAllFruits() {
        //List all, should have all 3 fruits the database has initially:
        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body(
                        containsString("Cherry"),
                        containsString("Apple"),
                        containsString("Banana"));

        //Delete the Cherry:
        given()
                .when().delete("/fruits/1")
                .then()
                .statusCode(204);

        //List all, cherry should be missing now:
        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body(
                        not(containsString("Cherry")),
                        containsString("Apple"),
                        containsString("Banana"));

        //Create the Pear:
        given()
                .when()
                .body("{\"name\" : \"Pear\"}")
                .contentType("application/json")
                .post("/fruits")
                .then()
                .statusCode(201);

        //List all, cherry should be missing now:
        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body(
                        not(containsString("Cherry")),
                        containsString("Apple"),
                        containsString("Banana"),
                        containsString("Pear"));
    }

```
テストを実行します。
```
$ mvn test
...
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.sampleapp.rest.FruitsEndpointTest
Hibernate: 

    drop table if exists known_fruits CASCADE
Hibernate:

    drop sequence if exists known_fruits_id_seq
Hibernate: create sequence known_fruits_id_seq start with 10 increment by 1
Hibernate:

    create table known_fruits (
       id integer not null,
        name varchar(40),
        primary key (id)
    )
Hibernate:

    alter table known_fruits
       add constraint UK_57g3m8wr3qxoj706a6hsqg6ye unique (name)
Hibernate:
    INSERT INTO known_fruits(id, name) VALUES (1, 'Cherry')
Hibernate:
    INSERT INTO known_fruits(id, name) VALUES (2, 'Apple')
Hibernate:
    INSERT INTO known_fruits(id, name) VALUES (3, 'Banana')
2020-12-26 17:56:35,096 INFO  [io.quarkus] (main) Quarkus 1.7.5.Final-redhat-00011 on JVM started in 4.478s. Listening on: http://0.0.0.0:8081
2020-12-26 17:56:35,100 INFO  [io.quarkus] (main) Profile test activated.
2020-12-26 17:56:35,100 INFO  [io.quarkus] (main) Installed features: [agroal, cdi, hibernate-orm, jdbc-h2, mutiny, narayana-jta, resteasy, resteasy-jackson, resteasy-jsonb, smallrye-context-propagation]
Hibernate: 
    select
        fruit0_.id as id1_0_,
        fruit0_.name as name2_0_
    from
        known_fruits fruit0_
    order by
        fruit0_.name
Hibernate:
    delete
    from
        known_fruits
    where
        id=?
Hibernate:
    select
        fruit0_.id as id1_0_,
        fruit0_.name as name2_0_ 
    from
        known_fruits fruit0_
    order by
        fruit0_.name
Hibernate:
    call next value for known_fruits_id_seq
Hibernate:
    insert
    into
        known_fruits
        (name, id)
    values
        (?, ?)
Hibernate:
    select
        fruit0_.id as id1_0_,
        fruit0_.name as name2_0_
    from
        known_fruits fruit0_
    order by
        fruit0_.name
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 7.224 s - in com.example.sampleapp.rest.FruitsEndpointTest
[INFO] Running com.example.sampleapp.rest.HelloResourceTest
2020-12-26 17:56:37,252 ERROR [com.exa.sam.res.FruitResource] (executor-thread-1) Failed to handle request: javax.ws.rs.NotFoundException: RESTEASY003210: Could not find resource for full path: http://localhost:8081/hello/404
        at org.jboss.resteasy.core.registry.SegmentNode.match(SegmentNode.java:152)
        at org.jboss.resteasy.core.registry.RootNode.match(RootNode.java:73)
        at org.jboss.resteasy.core.registry.RootClassNode.match(RootClassNode.java:47)
        at org.jboss.resteasy.core.ResourceMethodRegistry.getResourceInvoker(ResourceMethodRegistry.java:481)
        at org.jboss.resteasy.core.SynchronousDispatcher.getInvoker(SynchronousDispatcher.java:330)
        at org.jboss.resteasy.core.SynchronousDispatcher.lambda$invoke$4(SynchronousDispatcher.java:251)
        at org.jboss.resteasy.core.SynchronousDispatcher.lambda$preprocess$0(SynchronousDispatcher.java:160)
        at org.jboss.resteasy.core.interception.jaxrs.PreMatchContainerRequestContext.filter(PreMatchContainerRequestContext.java:364)
        at org.jboss.resteasy.core.SynchronousDispatcher.preprocess(SynchronousDispatcher.java:163)
        at org.jboss.resteasy.core.SynchronousDispatcher.invoke(SynchronousDispatcher.java:245)
        at io.quarkus.resteasy.runtime.standalone.RequestDispatcher.service(RequestDispatcher.java:73)
        at io.quarkus.resteasy.runtime.standalone.VertxRequestHandler.dispatch(VertxRequestHandler.java:132)
        at io.quarkus.resteasy.runtime.standalone.VertxRequestHandler.access$000(VertxRequestHandler.java:37)
        at io.quarkus.resteasy.runtime.standalone.VertxRequestHandler$1.run(VertxRequestHandler.java:94)
        at org.jboss.threads.ContextClassLoaderSavingRunnable.run(ContextClassLoaderSavingRunnable.java:35)
        at org.jboss.threads.EnhancedQueueExecutor.safeRun(EnhancedQueueExecutor.java:2046)
        at org.jboss.threads.EnhancedQueueExecutor$ThreadBody.doRunTask(EnhancedQueueExecutor.java:1578)
        at org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run(EnhancedQueueExecutor.java:1452)
        at org.jboss.threads.DelegatingRunnable.run(DelegatingRunnable.java:29)
        at org.jboss.threads.ThreadLocalResettingRunnable.run(ThreadLocalResettingRunnable.java:29)
        at java.base/java.lang.Thread.run(Thread.java:834)
        at org.jboss.threads.JBossThread.run(JBossThread.java:479)

HTTP/1.1 404 Not Found
Content-Length: 0
{
    "birthdate": "2000/12/25",
    "name": "Yamada",
    "age": 20
}
{
    "birthdate": "2000/12/25",
    "name": "Yamada",
    "age": 20
}
HTTP/1.1 200 OK
Content-Length: 51
Content-Type: application/json

{
    "birthdate": "2000/12/25",
    "name": "Yamada",
    "age": 20
}
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.758 s - in com.example.sampleapp.rest.HelloResourceTest
2020-12-26 17:56:38,036 INFO  [io.quarkus] (main) Quarkus stopped in 0.052s
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  15.200 s
[INFO] Finished at: 2020-12-26T17:56:38+09:00
[INFO] ------------------------------------------------------------------------
```
これまで作成したテストも含めて全て成功しました。

以下のテストを追加します。
レスポンスBodyをモデルクラスに変換して取得したり、リクエストBodyをモデルクラスのインスタンスの形で渡すことも可能です。
```java
    @Test
    public void testUpdateFruits() {
        //Get fruit (id=1):
        Fruit f = given()
                .when().get("/fruits/1")
                .then()
                .statusCode(200)
                .extract().as(Fruit.class);

        //Update the Cherry:
        f.setName("Red Cherry");
        given()
                .when()
                .body(f)
                .contentType("application/json")
                .put("/fruits/1")
                .then()
                .statusCode(200);

        //List all, cherry should be missing now:
        given()
                .when().get("/fruits")
                .then().log().body()
                .statusCode(200)
                .body(
                        containsString("Red Cherry"),
                        containsString("Apple"),
                        containsString("Banana"));
    }
```
テスト結果も一応。
```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.sampleapp.rest.FruitsEndpointTest
Hibernate: 

    drop table if exists known_fruits CASCADE
Hibernate:

    drop sequence if exists known_fruits_id_seq
Hibernate: create sequence known_fruits_id_seq start with 10 increment by 1
Hibernate:

    create table known_fruits (
       id integer not null,
        name varchar(40),
        primary key (id)
    )
Hibernate:

    alter table known_fruits 
       add constraint UK_57g3m8wr3qxoj706a6hsqg6ye unique (name)
Hibernate:
    INSERT INTO known_fruits(id, name) VALUES (1, 'Cherry')
Hibernate:
    INSERT INTO known_fruits(id, name) VALUES (2, 'Apple')
Hibernate:
    INSERT INTO known_fruits(id, name) VALUES (3, 'Banana')
2020-12-26 22:11:56,430 INFO  [io.quarkus] (main) Quarkus 1.7.5.Final-redhat-00011 on JVM started in 4.894s. Listening on: http://0.0.0.0:8081
2020-12-26 22:11:56,434 INFO  [io.quarkus] (main) Profile test activated.
2020-12-26 22:11:56,434 INFO  [io.quarkus] (main) Installed features: [agroal, cdi, hibernate-orm, jdbc-h2, mutiny, narayana-jta, resteasy, resteasy-jackson, resteasy-jsonb, smallrye-context-propagation]
Hibernate: 
    select
        fruit0_.id as id1_0_0_,
        fruit0_.name as name2_0_0_
    from
        known_fruits fruit0_
    where
        fruit0_.id=?
Hibernate:
    update
        known_fruits
    set
        name=?
    where
        id=?
Hibernate:
    select
        fruit0_.id as id1_0_,
        fruit0_.name as name2_0_
    from
        known_fruits fruit0_
    order by
        fruit0_.name
[
    {
        "id": 2,
        "name": "Apple"
    },
    {
        "id": 3,
        "name": "Banana"
    },
    {
        "id": 1,
        "name": "Red Cherry"
    }
]
Hibernate: 
    delete
    from
        known_fruits
    where
        id=?
Hibernate:
    select
        fruit0_.id as id1_0_,
        fruit0_.name as name2_0_ 
    from
        known_fruits fruit0_
    order by
        fruit0_.name
Hibernate:
    call next value for known_fruits_id_seq
Hibernate:
    insert
    into
        known_fruits
        (name, id)
    values
        (?, ?)
Hibernate:
    select
        fruit0_.id as id1_0_,
        fruit0_.name as name2_0_
    from
        known_fruits fruit0_ 
    order by
        fruit0_.name
[
    {
        "id": 2,
        "name": "Apple"
    },
    {
        "id": 3,
        "name": "Banana"
    },
    {
        "id": 10,
        "name": "Pear"
    }
]
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 8.273 s - in com.example.sampleapp.rest.FruitsEndpointTest
[INFO] Running com.example.sampleapp.rest.HelloResourceTest
2020-12-26 22:11:59,194 ERROR [com.exa.sam.res.FruitResource] (executor-thread-1) Failed to handle request: javax.ws.rs.NotFoundException: RESTEASY003210: Could not find resource for full path: http://localhost:8081/hello/404
        at org.jboss.resteasy.core.registry.SegmentNode.match(SegmentNode.java:152)
        at org.jboss.resteasy.core.registry.RootNode.match(RootNode.java:73)
        at org.jboss.resteasy.core.registry.RootClassNode.match(RootClassNode.java:47)
        at org.jboss.resteasy.core.ResourceMethodRegistry.getResourceInvoker(ResourceMethodRegistry.java:481)
        at org.jboss.resteasy.core.SynchronousDispatcher.getInvoker(SynchronousDispatcher.java:330)
        at org.jboss.resteasy.core.SynchronousDispatcher.lambda$invoke$4(SynchronousDispatcher.java:251)
        at org.jboss.resteasy.core.SynchronousDispatcher.lambda$preprocess$0(SynchronousDispatcher.java:160)
        at org.jboss.resteasy.core.interception.jaxrs.PreMatchContainerRequestContext.filter(PreMatchContainerRequestContext.java:364)
        at org.jboss.resteasy.core.SynchronousDispatcher.preprocess(SynchronousDispatcher.java:163)
        at org.jboss.resteasy.core.SynchronousDispatcher.invoke(SynchronousDispatcher.java:245)
        at io.quarkus.resteasy.runtime.standalone.RequestDispatcher.service(RequestDispatcher.java:73)
        at io.quarkus.resteasy.runtime.standalone.VertxRequestHandler.dispatch(VertxRequestHandler.java:132)
        at io.quarkus.resteasy.runtime.standalone.VertxRequestHandler.access$000(VertxRequestHandler.java:37)
        at io.quarkus.resteasy.runtime.standalone.VertxRequestHandler$1.run(VertxRequestHandler.java:94)
        at org.jboss.threads.ContextClassLoaderSavingRunnable.run(ContextClassLoaderSavingRunnable.java:35)
        at org.jboss.threads.EnhancedQueueExecutor.safeRun(EnhancedQueueExecutor.java:2046)
        at org.jboss.threads.EnhancedQueueExecutor$ThreadBody.doRunTask(EnhancedQueueExecutor.java:1578)
        at org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run(EnhancedQueueExecutor.java:1452)
        at org.jboss.threads.DelegatingRunnable.run(DelegatingRunnable.java:29)
        at org.jboss.threads.ThreadLocalResettingRunnable.run(ThreadLocalResettingRunnable.java:29)
        at java.base/java.lang.Thread.run(Thread.java:834)
        at org.jboss.threads.JBossThread.run(JBossThread.java:479)

HTTP/1.1 404 Not Found
Content-Length: 0
{
    "birthdate": "2000/12/25",
    "name": "Yamada",
    "age": 20
}
{
    "birthdate": "2000/12/25",
    "name": "Yamada",
    "age": 20
}
HTTP/1.1 200 OK
Content-Length: 51
Content-Type: application/json

{
    "birthdate": "2000/12/25",
    "name": "Yamada",
    "age": 20
}
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.84 s - in com.example.sampleapp.rest.HelloResourceTest
2020-12-26 22:12:00,110 INFO  [io.quarkus] (main) Quarkus stopped in 0.102s
[INFO] 
[INFO] Results:
[INFO]
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  17.163 s
[INFO] Finished at: 2020-12-26T22:12:00+09:00
[INFO] ------------------------------------------------------------------------
```
