package com.app;


import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by Administrator on 2016/5/6.
 */
@ComponentScan(value = {"com.app","org.dcm4che3.tool","org.dcm4che2.tool"})  
@PropertySources(value={@PropertySource("ftp.properties"),@PropertySource("rpt.properties"),
		@PropertySource("storescu.properties"),
		@PropertySource("storescp.properties"),
		@PropertySource("movescu.properties")})
@EnableAutoConfiguration 
@EnableScheduling
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class,MybatisAutoConfiguration.class})
public class MainApp extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MainApp.class);
    }

    public static void main(String[] args) throws Exception {
    	
        MainApp app = new MainApp();
        app.startServer();
    }

    public void startServer() {
		//start main service of springboot
		SpringApplication.run(MainApp.class);

	}
}