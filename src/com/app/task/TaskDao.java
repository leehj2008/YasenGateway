package com.app.task;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface TaskDao {
	List<Task> getMoveTask(@Param("status")String status);
	List<Task> getZipTask();
	List<Task> getFtpTask();
	List<Task> getRptTask();
	List<Task> getTask(@Param("status")String status);
	List<Task> getDeadTask();
	int updateTask(@Param("status") String status,@Param("ckey") long ckey);
	int updateTaskStartMove(@Param("ckey") long ckey);
	int updateMoveTaskCkey(@Param("ckey") long ckey,
			@Param("status") String status,
			@Param("complete")int complete,@Param("remain")int remain,@Param("failed")int failed);
	int updateMoveTask(@Param("accessionNumber") String  accessionNumber,
			@Param("status") String status,
			@Param("complete")int complete,@Param("remain")int remain,@Param("failed")int failed);
}
