package com.example.sampleapp.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class ExampleLifecycleManager implements QuarkusTestResourceLifecycleManager {

    Process process;

    @Override
    public Map<String, String> start() {
        // start external service
        String command = 
            "mvn quarkus:dev";
            // "java.exe -jar target\\testapp-1.0-SNAPSHOT-runner.jar";
        process(command);
        // command = "docker run --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 --name quarkus_test -e POSTGRES_USER=quarkus_test -e POSTGRES_PASSWORD=quarkus_test -e POSTGRES_DB=quarkus_test -p 5432:5432 postgres:10.5";
        // process(command);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private Process process(String command) {
        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", command);
        pb.redirectErrorStream(true);
        // Process process;
        try {
            process = pb.start();
            System.err.println("started : "+command);
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int exitCode;
        try {
            // 標準出力をすべて読み込む
            new Thread(() -> {
                try (InputStream is = process.getInputStream()) {
                    while (is.read() >= 0); 
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
            
            // int timeoutSec = 10;
            // boolean end = process.waitFor(timeoutSec, TimeUnit.SECONDS);
            // if (end) {
            //     exitCode = process.exitValue();
            // } else {
            //     throw new RuntimeException("Command timeout. [CommandPath: " + command + "]");
            // }

        // } catch (InterruptedException e) {
        //     throw new RuntimeException("Command interrupted. [CommandPath: " + command + "]", e);
        } finally {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (process.isAlive()) {
                process.destroyForcibly(); // プロセスを強制終了
            }
        }

        return process;
       
    }

    @Override
    public void stop() {
        if (process.isAlive()) {
            System.out.println("process is alive "+process.toString());
            process.destroyForcibly(); // プロセスを強制終了
            // long pid = process.pid();
            
        }
        System.out.println("test end");
    }
    
}
