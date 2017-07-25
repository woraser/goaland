package com.anosi.asset;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@SpringBootApplication
public class GoalandApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoalandApplication.class, args);
	}
	
	/***
	 * rememberMe 以后的默认路径
	 * @return
	 */
	@RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("redirect:/index");
    }

	/***
	 * 解决以下异常：
	 * 	Parameter 0 of method springAsyncExecutor in org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration required a single bean
	 * @return
	 */
	@Primary
	@Bean
	public TaskExecutor primaryTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// add necessary properties to the executor
		return executor;
	}

}
