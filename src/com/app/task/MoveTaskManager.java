package com.app.task;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dcm4che2.tool.dcmqr.MoveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.app.main.util.TaskStatus;
import com.app.schedule.OrderServiceImpl;

@Component
public class MoveTaskManager implements Runnable{
	Logger log = LoggerFactory.getLogger(MoveTaskManager.class);
	@Autowired
	TaskDao taskdao;
	@Autowired
	MoveService moveService;
	//MoveClient moveClient;
	@Autowired
	OrderServiceImpl orderServiceImpl;
	@Value("${pacs.schedule}")
	String pacsSchedule;
	@Override
	public void run() {
		//moveClient.configure();
		while(true){
			List<Task> tasks = taskdao.getMoveTask(TaskStatus.INIT);
			for(Task task : tasks){
				log.info("task="+task.getCkey());
				log.info("Moving accno={},suid={}",task.getAccessionNumber(),task.getStudyInstanceUID());
				taskdao.updateTaskStartMove( task.getCkey());
				if(pacsSchedule!=null&&pacsSchedule.equalsIgnoreCase("true")){
					try {
						orderServiceImpl.doSendSchedule(task.getAccessionNumber());
					} catch (Exception e) {
						e.printStackTrace();
						log.error(e.getMessage());
						taskdao.updateTask(TaskStatus.MOVE_ERROR, task.getCkey());
						continue;
					}
				}
				/*String accno = task.getAccessionNumber();
				if(StringUtils.isEmpty(accno)){
					log.info("ACCNumber is Empty");
					taskdao.updateTask(TaskStatus.MOVE_ERROR, task.getCkey());
					continue;
				}
				String suid = task.getStudyInstanceUID();*/
				try {
					//moveClient.move();
//					moveService.moveByAcc(task.getCkey(),accno);
					moveService.moveByExpression(task);
				} catch (Exception e) {
					e.printStackTrace();
					log.error(e.getMessage());
					taskdao.updateTask(TaskStatus.MOVE_FAILED, task.getCkey());
					continue;
				}
				/*Thread t = new Thread(moveClient);
				t.start();*/
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
