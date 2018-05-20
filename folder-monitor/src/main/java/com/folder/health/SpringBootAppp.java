package com.folder.health;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAutoConfiguration
@SpringBootApplication
@EnableScheduling
public class SpringBootAppp {
	
	private static final int POLL_INTERVAL = 60*1000;
	
	public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringBootAppp.class, args);
        FileAlterationObserver observer = new FileAlterationObserver("secured");
        FileAlterationMonitor monitor = new FileAlterationMonitor(POLL_INTERVAL);
        observer.checkAndNotify();
        observer.addListener(new FolderMonitorListner());
        monitor.addObserver(observer);
        monitor.start();
    }

}