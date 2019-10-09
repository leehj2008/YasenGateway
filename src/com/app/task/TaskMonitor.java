package com.app.task;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.app.main.util.TaskStatus;

@Component
public class TaskMonitor {
	Logger log = LoggerFactory.getLogger(TaskMonitor.class);
	@Autowired
	TaskDao dao;
	
	//@Scheduled(cron="${cron}")
	void doStatistics(){
		log.info("TaskMonitor run: getDeadTask...");
		List<Task> deadTasks = dao.getDeadTask();
		log.info("getDeadTask:count={}",deadTasks.size());
		for(Task task:deadTasks){
			log.info("trying to reset taskckey={} to new",task.getCkey());
			//dao.renewTask(task.getCkey());
			log.info("reset taskckey={} to new success.",task.getCkey());
		}
	}
	
	
}
