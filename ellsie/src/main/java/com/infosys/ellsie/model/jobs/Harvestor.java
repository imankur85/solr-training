/**
 * 
 */
package com.infosys.ellsie.model.jobs;

import java.util.Arrays;
import java.util.List;

import com.infosys.ellsie.model.Job;
import com.infosys.ellsie.model.Task;
import com.infosys.ellsie.model.tasks.Downloader;

/**
 * @author Ankur_Jain22
 *
 */
public class Harvestor implements Job {
	
	private List<Task> tasks = Arrays.asList(new Downloader());
	private String jobName = "Harvestor";

	/* (non-Javadoc)
	 * @see com.infosys.ellsie.model.Job#getTasks()
	 */
	@Override
	public List<Task> getTasks() {
		return tasks;
	}

	@Override
	public void executeJob() {
		for (Task task : tasks) {
			task.executeTask();
		}
	}

	@Override
	public String getJobName() {
		return jobName;
	}

	@Override
	public void setJobName(String name) {
		jobName = name;
	}

	@Override
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	

}
