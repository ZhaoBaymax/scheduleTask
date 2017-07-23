package hello;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import javax.xml.ws.RequestWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableScheduling
public class DynamicTask {
	
	@Autowired
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;
	
	private ScheduledFuture<?> future;
	
	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		return new ThreadPoolTaskScheduler();
	}
	
	@RequestMapping("/startTask")
	public String startTask() {
		future = threadPoolTaskScheduler.schedule(new MyRunnable(),new CronTrigger("0/5 * * * * *"));
		return "startTask";
	}
	
	@RequestMapping("/stopTask")
	public String stopTask() {
		if (future != null) {
			future.cancel(true);
		}
		System.out.println("stop task");
		return "stopTask";
	}
	
	public class MyRunnable implements Runnable{

		@Override
		public void run() {
			System.out.println("DynamicTask.MyRunnable.run()"+new Date());
			
		}
	}
	
	@RequestMapping("/changeCron")
	public String changeCron() {
		stopTask();
		future = threadPoolTaskScheduler.schedule(new MyRunnable(),new CronTrigger("0/10 * * * * *"));
		System.out.println("DynamicTask.changeCron()");
		return "changeCron";
	}
}
