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

![](./img/001.png)

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


