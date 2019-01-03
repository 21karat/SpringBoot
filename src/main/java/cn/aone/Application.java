package cn.aone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication//(scanBasePackages={"cn.aone"})
//扫描路径(mybatis注解使用)
//@MapperScan(basePackages="cn.aone.model.mapper")
@EnableCaching//缓存注解
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
