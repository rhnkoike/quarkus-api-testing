TODO
- 説明文
- 出力調整
- ソースコメント
- 図
- ログ
- レポート
- パイプライン

# API Test Automation Tutorial

REST APIのテスト自動化のチュートリアルです。

このチュートリアルでは、以下を通してQuarkusで実装するREST APIのテストを自動化する方法について学習します。
- プロジェクトの作成
- 基本的なQuarkusアプリケーションのテスト作成
- DBを使用するQuarkusアプリケーションのテスト作成
- 外部APIを使用するQuarkusアプリケーションのテスト作成
- テスト用リソースのセットアップ自動化

## 前提環境

- OpenJDK11 (or 8)
- Apache Maven
- Docker

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

```
\TESTAPP
├─.mvn
│  └─wrapper
├─.settings
└─src
    ├─main
    │  ├─docker
    │  ├─java
    │  │  └─com
    │  │      └─example
    │  │          └─sampleapp
    │  │              └─rest
    │  └─resources
    │      └─META-INF
    │          └─resources
    └─test
        └─java
            └─com
                └─example
                    └─sampleapp
                        └─rest
```

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
quarkus-junit5はテストフレームワークを制御する@QuarkusTestアノテーションを提供しているので、テストには必須です。rest-assuredは必須ではありませんが、HTTPエンドポイントをテストするのに便利であり、統合によりURLを自動設定する機能を提供します。

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
Rest Assuredは、Rest APIテストの自動化を可能にするJavaライブラリのグループです。
Rest AssuredはJavaベースであり、学習にはコアJavaの知識があれば十分です。
複雑な JSON 構造からリクエストとレスポンスの値を取得するのに役立ちます。
APIリクエストは、ヘッダー、クエリ、パスパラメータ、任意のセッションやクッキーを設定してカスタマイズすることができます。
アサート文や条件を設定するのに役立ちます。
Rest AssuredはレスポンスがJSONタイプの場合に非常に便利ですが、コンテンツのタイプがHTMLやプレーンテキストの場合には、そのメソッドがシームレスに動作しないことがあります。

![REST-Assured Methods](./img/restassured.png)



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
起動時のPortはREST-Assured統合により自動設定されます。（デフォルト8081）プロパティで変更することも可能です。

## 基本的なQuarkusアプリケーションのテスト

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


### モードによるDBの切り替え

テストの際はインメモリDBではなく別で稼働するDBへ実際に接続してテストを実行するようにモードによる切り替えを行います。

DBはPostgreSQLのDockerコンテナを使用します。（Docker環境がない場合はSkipしてください）

application.propertiesを以下のように変更します。
%{mode}.～の定義はモード別の設定を示します。%devはDEVモード、%testはTESTモードの設定です。
```
quarkus.datasource.username=quarkus_test
quarkus.datasource.password=quarkus_test
%dev.quarkus.datasource.db-kind=h2
%dev.quarkus.datasource.jdbc.url=jdbc:h2:mem:quarkus_test
%dev.quarkus.datasource.jdbc.driver=org.h2.Driver
%test.quarkus.datasource.db-kind=postgresql
%test.quarkus.datasource.jdbc.url=jdbc:postgresql://localhost/quarkus_test
%test.quarkus.datasource.jdbc.driver=org.postgresql.Driver
```

Extensionを追加します。PostgreSQL接続用ドライバの拡張になります。
```
$ mvn quarkus:add-extensions -Dextensions="jdbc-postgres"  
...
```

DBを起動します。今回はPostgreSQLのDockerコンテナを使用します。
```
$ docker run --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 --name quarkus_test -e POSTGRES_USER=quarkus_test 
-e POSTGRES_PASSWORD=quarkus_test -e POSTGRES_DB=quarkus_test -p 5432:5432 postgres:10.5
Unable to find image 'postgres:10.5' locally
10.5: Pulling from library/postgres
f17d81b4b692: Pull complete
....

PostgreSQL init process complete; ready for start up.

2020-12-27 06:11:40.658 UTC [1] LOG:  listening on IPv4 address "0.0.0.0", port 5432
2020-12-27 06:11:40.658 UTC [1] LOG:  listening on IPv6 address "::", port 5432
2020-12-27 06:11:40.668 UTC [1] LOG:  listening on Unix socket "/var/run/postgresql/.s.PGSQL.5432"
2020-12-27 06:11:40.713 UTC [61] LOG:  database system was shut down at 2020-12-27 06:11:40 UTC
2020-12-27 06:11:40.724 UTC [1] LOG:  database system is ready to accept connections
```

テストを実行します。余計な標準出力は抑止してあります。
```
$ mvn test
...
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.sampleapp.rest.FruitsEndpointTest
2020-12-27 15:36:35,084 WARN  [io.qua.config] (main) Unrecognized configuration key "quarkus.http.test-timeout" was provided; it will be ignored; verify that the dependency extension for this configuration is set or you did not make a typo
2020-12-27 15:36:36,571 WARN  [org.hib.eng.jdb.spi.SqlExceptionHelper] (main) SQL Warning Code: 0, SQLState: 00000
2020-12-27 15:36:36,571 WARN  [org.hib.eng.jdb.spi.SqlExceptionHelper] (main) table "known_fruits" does not exist, skipping
2020-12-27 15:36:36,573 WARN  [org.hib.eng.jdb.spi.SqlExceptionHelper] (main) SQL Warning Code: 0, SQLState: 00000
2020-12-27 15:36:36,573 WARN  [org.hib.eng.jdb.spi.SqlExceptionHelper] (main) sequence "known_fruits_id_seq" does not exist, skipping
2020-12-27 15:36:37,218 INFO  [io.quarkus] (main) Quarkus 1.7.5.Final-redhat-00011 on JVM started in 4.893s. Listening on: http://0.0.0.0:8081
2020-12-27 15:36:37,222 INFO  [io.quarkus] (main) Profile test activated. 
2020-12-27 15:36:37,222 INFO  [io.quarkus] (main) Installed features: [agroal, cdi, hibernate-orm, jdbc-h2, jdbc-postgresql, mutiny, narayana-jta, rest-client, resteasy, resteasy-jackson, resteasy-jsonb, smallrye-context-propagation]
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 8.083 s - in com.example.sampleapp.rest.FruitsEndpointTest
[INFO] Running com.example.sampleapp.rest.GreetingResourceTest
called mock service
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.024 s - in com.example.sampleapp.rest.GreetingResourceTest
[INFO] Running com.example.sampleapp.rest.HelloResourceTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.762 s - in com.example.sampleapp.rest.HelloResourceTest
2020-12-27 15:36:40,608 INFO  [io.quarkus] (main) Quarkus stopped in 0.046s
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  16.567 s
[INFO] Finished at: 2020-12-27T15:36:40+09:00
[INFO] ------------------------------------------------------------------------
```
DBのテーブルを確認してみます。
```
$ docker exec -it quarkus_test psql -U quarkus_test -c "select * from known_fruits;"
 id |  name
----+--------
  2 | Apple
  3 | Banana
 10 | Pear
(3 rows)

```
処理結果がコンテナ上のDBに反映されています。


## 外部APIを呼び出すQuarkusアプリのテスト

### サンプルアプリの追加
以下のソースファイルを追加します。
- [GreetingResource.java](./src/main/java/com/example/sampleapp/rest/GreetingResource.java)
- [GreetingService.java](./src/main/java/com/example/sampleapp/rest/GreetingService.java)
- [GreetingExtResource.java](./src/main/java/com/example/sampleapp/external/GreetingExtResource.java) (これが外部API相当のリソースです)

Extensionを追加します。
```
$ mvn quarkus:add-extensions -Dextensions="rest-client"
```

GreetingResourceはMicroProfile RestClientのクライアントインターフェースとして以下のように定義されたGreetingServiceを経由してGreetingExtResourceを呼び出します。
```java
@Path("/")
@ApplicationScoped
@RegisterRestClient(baseUri = "http://localhost:8080/")
public interface GreetingService {

    @GET
    @Path("/helloext")
    @Produces(MediaType.TEXT_PLAIN)
    String hello();
}
```
通常のDEVモードでQuarkusアプリを起動して確認してみます。
```
$ mvn compile quarkus:dev
...
```
```
$ curl http://localhost:8080/greeting/test 
Hello test
```

### テストの追加
以下のテストを追加します。
- [GreetingResourceTest.java](./src/test/java/com/example/sampleapp/rest/GreetingResourceTest.java)

先ほどの確認と同じ内容です。
```java
    @Test
    public void testHelloEndpoint() {

        given()
          .when().get("/greeting/test")
          .then()
             .statusCode(200)
             .body(is("Hello test"));
    }
```
テストを実行します。
出力がわかりにくくなったので他のテストの出力は一旦止めておきます。

まずDEVモードで起動します。これは外部APIとして動作させるためです。
```
$ mvn compile quarkus:dev
...
```

このまま別のコンソールでテストを実行します。
```
$ mvn test
...
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.sampleapp.rest.FruitsEndpointTest
2020-12-27 01:25:55,280 WARN  [io.qua.config] (main) Unrecognized configuration key "quarkus.http.test-timeout" was provided; it will be ignored; verify that the dependency extension for this configuration is set or you did not make a typo
2020-12-27 01:25:57,215 INFO  [io.quarkus] (main) Quarkus 1.7.5.Final-redhat-00011 on JVM started in 4.440s. Listening on: http://0.0.0.0:8081
2020-12-27 01:25:57,215 INFO  [io.quarkus] (main) Profile test activated. 
2020-12-27 01:25:57,216 INFO  [io.quarkus] (main) Installed features: [agroal, cdi, hibernate-orm, jdbc-h2, mutiny, narayana-jta, rest-client, resteasy, resteasy-jackson, resteasy-jsonb, smallrye-context-propagation]
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 7.406 s - in com.example.sampleapp.rest.FruitsEndpointTest
[INFO] Running com.example.sampleapp.rest.GreetingResourceTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.232 s - in com.example.sampleapp.rest.GreetingResourceTest
[INFO] Running com.example.sampleapp.rest.HelloResourceTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.689 s - in com.example.sampleapp.rest.HelloResourceTest
2020-12-27 01:26:00,627 INFO  [io.quarkus] (main) Quarkus stopped in 0.043s
[INFO] 
[INFO] Results:
[INFO]
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  15.391 s
[INFO] Finished at: 2020-12-27T01:26:00+09:00
[INFO] ------------------------------------------------------------------------
```
テストが成功しました。DEVモード側も停止してください。

### 外部APIをMockする
外部API（GreetingExtResource）の実行をMockに置き換えてみます。

以下のソースファイルをテストと同じフォルダに追加します。
-[GreetingMockService.java](./src/test/java/com/example/sampleapp/rest/GreetingMockService.java)

これがGreetingServiceのMockとなります。Mockされたことがわかるように戻り値のStringを変えてあります。
```java
@Mock
@RestClient
@ApplicationScoped
public class GreetingMockService implements GreetingService{

    @Override
    public String hello() {
        System.out.println("called mock service");
        return "Hi ";
    }
    
}
```

テスト（GreetingResourceTest）を以下のように変更します。
Mockされたことがわかるように検証用の想定結果も変更してあります。
```java
@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testHelloEndpoint() {

        given()
          .when().get("/greeting/test")
          .then()
             .statusCode(200)
            //  .body(is("Hello test"));
            .body(is("Hi test"));
    }

```

テストを実行します。別コンソールでDEVモードでの起動は不要です。
```
$ mvn test
...
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.sampleapp.rest.FruitsEndpointTest
2020-12-27 02:29:42,749 WARN  [io.qua.config] (main) Unrecognized configuration key "quarkus.http.test-timeout" was provided; it will be ignored; verify that the dependency extension for this configuration is set or you did not make a typo
2020-12-27 02:29:44,594 INFO  [io.quarkus] (main) Quarkus 1.7.5.Final-redhat-00011 on JVM started in 4.446s. Listening on: http://0.0.0.0:8081
2020-12-27 02:29:44,594 INFO  [io.quarkus] (main) Profile test activated.
2020-12-27 02:29:44,595 INFO  [io.quarkus] (main) Installed features: [agroal, cdi, hibernate-orm, jdbc-h2, mutiny, narayana-jta, rest-client, resteasy, resteasy-jackson, resteasy-jsonb, smallrye-context-propagation]
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 10.625 s - in com.example.sampleapp.rest.FruitsEndpointTest
[INFO] Running com.example.sampleapp.rest.GreetingResourceTest
called mock service
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.025 s - in com.example.sampleapp.rest.GreetingResourceTest
[INFO] Running com.example.sampleapp.rest.HelloResourceTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.703 s - in com.example.sampleapp.rest.HelloResourceTest
2020-12-27 02:29:47,825 INFO  [io.quarkus] (main) Quarkus stopped in 0.048s
[INFO] 
[INFO] Results:
[INFO]
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  18.571 s
[INFO] Finished at: 2020-12-27T02:29:47+09:00
[INFO] ------------------------------------------------------------------------
```
成功しました。よく見るとMockが標準出力した`called mock service`もちゃんと出ています。



## 機能テスト自動化のためのテスト用リソースのセットアップ

インメモリDBや外部APIのMockによって機能テストの自動化は容易になります。
但し実際のDBMSを使用したい場合やHTTPによる通信を実行したい場合はそのためのDBやAPI（スタブの場合あり）を事前に起動してテストを実施する必要があります。
機能テストの一連の操作を自動化するため、外部リソース起動についても自動化を実施します。

以下のような状態を目指すことになります。
![testing.png](./img/testing.png)

### DBセットアップ自動化
テスト用DBのコンテナ起動を自動化します。
Dockerコンテナの操作をテストに組み込むにはTestContainerライブラリを使用します。

pom.xmlに以下の依存関係を追加します。
```xml
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>postgresql</artifactId>
      <version>1.15.1</version>
      <scope>test</scope>
    </dependency>
```
以下のソースファイルをテストフォルダに追加します。
- [TestDatabase.java](./src/test/java/com/example/sampleapp/rest/TestDatabase.java)

このクラスは`QuarkusTestResourceLifecycleManager`インターフェースを実施しており、テストの開始前にPostgreSQLコンテナの起動を実行します。
```java
public class TestDatabase implements QuarkusTestResourceLifecycleManager {

	public static final PostgreSQLContainer<?> DATABASE = new PostgreSQLContainer<>("postgres:10.5")
			.withDatabaseName("quarkus_test")
			.withUsername("quarkus_test")
			.withPassword("quarkus_test");

	@Override
	public Map<String, String> start() {
		DATABASE.start();
		Map<String, String> datasourceProperties = new HashMap<>();
		datasourceProperties.put("quarkus.datasource.username", "quarkus_test");
		datasourceProperties.put("quarkus.datasource.password", "quarkus_test");
		datasourceProperties.put("quarkus.datasource.jdbc.url", DATABASE.getJdbcUrl());
		return datasourceProperties;
	}

	@Override
	public void stop() {

	}
}

```
このPostgreSQLコンテナを使用するテスト（FruitsEndpointTest）に`@QuarkusTestResource`を追加します。
```java

@QuarkusTest
@QuarkusTestResource(TestDatabase.class)
public class FruitsEndpointTest {
...
```
テストを実行します。
```
$ mvn test
...
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.sampleapp.rest.FruitsEndpointTest
2020-12-28 23:24:32,221 INFO  [org.tes.doc.DockerClientProviderStrategy] (main) Loaded org.testcontainers.dockerclient.NpipeSocketClientProviderStrategy from 
~/.testcontainers.properties, will try it first
2020-12-28 23:24:32,832 INFO  [org.tes.doc.DockerClientProviderStrategy] (main) Found Docker environment with local Npipe socket (npipe:////./pipe/docker_engine)
2020-12-28 23:24:32,833 INFO  [org.tes.DockerClientFactory] (main) Docker host IP address is localhost
2020-12-28 23:24:32,870 INFO  [org.tes.DockerClientFactory] (main) Connected to docker:
  Server Version: 20.10.0
  API Version: 1.41
  Operating System: Docker Desktop
  Total Memory: 12601 MB
2020-12-28 23:24:32,873 INFO  [org.tes.uti.ImageNameSubstitutor] (main) Image name substitution will be performed by: DefaultImageNameSubstitutor (composite of 'ConfigurationFileImageNameSubstitutor' and 'PrefixingImageNameSubstitutor')
2020-12-28 23:24:33,161 INFO  [org.tes.uti.RegistryAuthLocator] (main) Credential helper/store (docker-credential-desktop) does not have credentials for index.docker.io
2020-12-28 23:24:34,202 INFO  [org.tes.DockerClientFactory] (main) Ryuk started - will monitor and terminate Testcontainers containers on JVM exit
2020-12-28 23:24:34,202 INFO  [org.tes.DockerClientFactory] (main) Checking the system...
2020-12-28 23:24:34,203 INFO  [org.tes.DockerClientFactory] (main) ?? Docker server version should be at least 1.6.0
2020-12-28 23:24:35,777 INFO  [org.tes.DockerClientFactory] (main) ?? Docker environment should have more than 2GB free disk space
2020-12-28 23:24:35,799 INFO  [doc.5]] (main) Creating container for image: postgres:10.5
2020-12-28 23:24:35,863 INFO  [doc.5]] (main) Starting container with ID: 724cfeb47cb8d808ec4275c33f3fd852a767ecf3ef42543615ca345480152b65
2020-12-28 23:24:36,206 INFO  [doc.5]] (main) Container postgres:10.5 is starting: 724cfeb47cb8d808ec4275c33f3fd852a767ecf3ef42543615ca345480152b65
2020-12-28 23:24:38,113 INFO  [doc.5]] (main) Container postgres:10.5 started in PT2.33471S
2020-12-28 23:24:38,344 INFO  [org.ecl.jet.uti.log] (main) Logging initialized @11047ms to org.eclipse.jetty.util.log.Slf4jLog
2020-12-28 23:24:38,472 INFO  [org.ecl.jet.ser.Server] (main) jetty-9.4.18.v20190429; built: 2019-04-29T20:42:08.989Z; git: e1bc35120a6617ee3df052294e433f3a25ce7097; jvm 11.0.9.1+1-LTS
2020-12-28 23:24:38,497 INFO  [org.ecl.jet.ser.han.ContextHandler] (main) Started o.e.j.s.ServletContextHandler@617449dd{/__admin,null,AVAILABLE}
2020-12-28 23:24:38,501 INFO  [org.ecl.jet.ser.han.ContextHandler] (main) Started o.e.j.s.ServletContextHandler@1a21f43f{/,null,AVAILABLE}
2020-12-28 23:24:38,538 INFO  [org.ecl.jet.ser.AbstractConnector] (main) Started NetworkTrafficServerConnector@647b9364{HTTP/1.1,[http/1.1]}{0.0.0.0:8080}
2020-12-28 23:24:38,538 INFO  [org.ecl.jet.ser.Server] (main) Started @11242ms
2020-12-28 23:24:38,818 INFO  [org.ecl.jet.ser.han.Con.__admin] (qtp428160758-108) RequestHandlerClass from context returned com.github.tomakehurst.wiremock.http.AdminRequestHandler. Normalized mapped under returned 'null'
2020-12-28 23:24:40,368 WARN  [org.hib.eng.jdb.spi.SqlExceptionHelper] (main) SQL Warning Code: 0, SQLState: 00000
2020-12-28 23:24:40,368 WARN  [org.hib.eng.jdb.spi.SqlExceptionHelper] (main) table "known_fruits" does not exist, skipping
2020-12-28 23:24:40,371 WARN  [org.hib.eng.jdb.spi.SqlExceptionHelper] (main) SQL Warning Code: 0, SQLState: 00000
2020-12-28 23:24:40,371 WARN  [org.hib.eng.jdb.spi.SqlExceptionHelper] (main) sequence "known_fruits_id_seq" does not exist, skipping
2020-12-28 23:24:40,942 INFO  [io.quarkus] (main) Quarkus 1.7.5.Final-redhat-00011 on JVM started in 12.012s. Listening on: http://0.0.0.0:8081
2020-12-28 23:24:40,947 INFO  [io.quarkus] (main) Profile test activated.
2020-12-28 23:24:40,947 INFO  [io.quarkus] (main) Installed features: [agroal, cdi, hibernate-orm, jdbc-h2, jdbc-postgresql, mutiny, narayana-jta, rest-client, resteasy, resteasy-jackson, resteasy-jsonb, smallrye-context-propagation]
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 15.001 s - in com.example.sampleapp.rest.FruitsEndpointTest
...
```
PostgreSQLコンテナが自動で起動し、テストが成功します。

### 外部API（スタブ）セットアップの自動化

テスト用外部APIのスタブサーバーを自動で起動します。
先ほどはQuarkusアプリを起動していましたが、以下では[ここを参考に](https://quarkus.io/guides/rest-client#using-a-mock-http-server-for-tests)Wiremockによるスタブサーバーを使用します。

pom.xmlに以下の依存関係を追加します。
```xml
    <dependency>
      <groupId>com.github.tomakehurst</groupId>
      <artifactId>wiremock-jre8</artifactId>
      <version>2.26.3</version>
      <scope>test</scope>
    </dependency>
```

以下のソースファイルをテストフォルダに追加します。
- [TestStubService.java](./src/test/java/com/example/sampleapp/rest/TestStubService.java)

このクラスも`QuarkusTestResourceLifecycleManager`インターフェースを実施しており、テストの開始前にWiremockスタブサーバーの起動を実行します。
```java
public class TestStubService implements QuarkusTestResourceLifecycleManager {

	private WireMockServer wireMockServer;

	@Override
	public Map<String, String> start() {
		wireMockServer = new WireMockServer();
		wireMockServer.start();

		stubFor(get(urlEqualTo("/helloext"))
				.willReturn(aResponse()
						.withHeader("Content-Type", "text/plain")
						.withBody("Hi ")));

		stubFor(get(urlMatching(".*")).atPriority(10)
				.willReturn(aResponse().proxiedFrom("https://localhost:8080/")));

		return Collections.singletonMap("org.example.sampleapp.rest.GreetingService/mp-rest/url", wireMockServer.baseUrl());
	}

	@Override
	public void stop() {
		if (null != wireMockServer) {
			wireMockServer.stop();
		}
	}
}

```
先ほどのMockと同じ動作を実行するスタブサーバーになります。

このスタブサーバーを使用するテスト（GreetingResourceTest）に`@QuarkusTestResource`を追加します。
```java

@QuarkusTest
@QuarkusTestResource(TestStubService.class)
public class GreetingResourceTest {
...
```
先ほどのMock（GreetingMockService）があるとそちらが使われてしまうので、無効化しておきます。
```java
// @Mock
// @RestClient
// @ApplicationScoped
// public class GreetingMockService implements GreetingService
public class GreetingMockService 
{
...
```
テストを実行します。
```
$ mvn test
...
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
...
[INFO] Running com.example.sampleapp.rest.GreetingResourceTest
service is RESTEASY004635: Resteasy Client Proxy for : com.example.sampleapp.rest.GreetingService
2020-12-28 23:43:50,374 INFO  [org.ecl.jet.ser.han.Con.ROOT] (qtp1954035189-99) RequestHandlerClass from context returned com.github.tomakehurst.wiremock.http.StubRequestHandler. Normalized mapped under returned 'null'
HTTP/1.1 200 OK
Content-Length: 7
Content-Type: text/plain;charset=UTF-8

Hi test
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.111 s - in com.example.sampleapp.rest.GreetingResourceTest
...
```
スタブサーバーが自動で起動し、テストが成功します。



### （参考）テスト実行フェーズの指定

単体テストと機能テストが同じプロジェクト内に配置している場合などのため、[ここを参考に](https://quarkus.io/guides/tests-with-coverage#separating-executions-of-unit-tests-and-integration-tests)テストに対して実行するフェーズを指定します。

`integration`タグを対象の機能テストに付与します。タグを付与するには`@Tag`を使用します。
```java
@QuarkusTest
@QuarkusTestResource(TestDatabase.class)
@Tag("integration")
public class FruitsEndpointTest {
...
```

pom.xmlで対象のテストの実行フェーズを指定します。以下が`integration`タグの付いたテストはintegration-testフェーズでのみ実行するという指定になります。
```xml
...
<plugin>
<artifactId>maven-surefire-plugin</artifactId>
<version>${surefire-plugin.version}</version>
<configuration>
    <!-- exclude tests with integration tag -->
    <excludedGroups>integration</excludedGroups>
    <systemPropertyVariables>
    <java.util.logging.manager>org.jboss.logmanager.LogManager</java.util.logging.manager>
    <quarkus.log.level>INFO</quarkus.log.level>
    <maven.home>${maven.home}</maven.home>
    </systemPropertyVariables>
</configuration>
<executions>
    <!-- in integration-test phase, execute tests with integration tag only -->
    <execution>
        <id>integration-tests</id>
        <phase>integration-test</phase>
        <goals>
            <goal>test</goal>
        </goals>
        <configuration>
            <excludedGroups>!integration</excludedGroups>
            <groups>integration</groups>
        </configuration>
    </execution>
</executions>
</plugin>
...
```

integration-testフェーズを含むテストを実行します。
```
$ mvn clean verify
...
[INFO] --- maven-surefire-plugin:3.0.0-M5:test (default-test) @ testapp ---
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.sampleapp.rest.GreetingResourceTest
...
2020-12-29 00:29:29,798 INFO  [io.quarkus] (main) Quarkus 1.7.5.Final-redhat-00011 on JVM started in 9.904s. Listening on: http://0.0.0.0:8081
2020-12-29 00:29:29,798 INFO  [io.quarkus] (main) Profile test activated.
2020-12-29 00:29:29,802 INFO  [io.quarkus] (main) Installed features: [agroal, cdi, hibernate-orm, jdbc-h2, jdbc-postgresql, mutiny, narayana-jta, rest-client, resteasy, resteasy-jackson, resteasy-jsonb, smallrye-context-propagation]
...
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 15.857 s - in com.example.sampleapp.rest.GreetingResourceTest
[INFO] Running com.example.sampleapp.rest.HelloResourceTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.748 s - in com.example.sampleapp.rest.HelloResourceTest
2020-12-29 00:29:32,248 INFO  [io.quarkus] (main) Quarkus stopped in 0.068s
[INFO] 
[INFO] Results:
[INFO]
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] 
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ testapp ---
[INFO] Building jar: C:\Nori\Trial\Quarkus\testapp\target\testapp-1.0-SNAPSHOT.jar
[INFO] 
[INFO] --- quarkus-maven-plugin:1.7.5.Final-redhat-00011:build (default) @ testapp ---
[INFO] [org.jboss.threads] JBoss Threads version 3.1.1.Final-redhat-00001
[INFO] [org.hibernate.Version] HHH000412: Hibernate ORM core version 5.4.21.Final-redhat-00005
[INFO] [io.quarkus.deployment.pkg.steps.JarResultBuildStep] Building thin jar: C:\Nori\Trial\Quarkus\testapp\target\testapp-1.0-SNAPSHOT-runner.jar
[INFO] [io.quarkus.deployment.QuarkusAugmentor] Quarkus augmentation completed in 2813ms
[INFO] 
[INFO] --- maven-surefire-plugin:3.0.0-M5:test (integration-tests) @ testapp ---
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------

...
2020-12-29 00:29:48,085 INFO  [io.quarkus] (main) Quarkus 1.7.5.Final-redhat-00011 on JVM started in 9.370s. Listening on: http://0.0.0.0:8081
2020-12-29 00:29:48,085 INFO  [io.quarkus] (main) Profile test activated.
2020-12-29 00:29:48,086 INFO  [io.quarkus] (main) Installed features: [agroal, cdi, hibernate-orm, jdbc-h2, jdbc-postgresql, mutiny, narayana-jta, rest-client, resteasy, resteasy-jackson, resteasy-jsonb, smallrye-context-propagation]
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 11.995 s - in com.example.sampleapp.rest.FruitsEndpointTest
2020-12-29 00:29:50,069 INFO  [io.quarkus] (main) Quarkus stopped in 0.047s
[INFO] 
[INFO] Results:
[INFO]
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```
対象のテストのみがintegration-testフェーズで実行されていることがわかります。

## おわりに

Quarkusを利用したREST APIのテスト自動化について学習しました。

お疲れさまでした。
