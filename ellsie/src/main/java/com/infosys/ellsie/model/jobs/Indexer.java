/**
 * 
 */
package com.infosys.ellsie.model.jobs;

import java.util.List;

import com.infosys.ellsie.model.Job;
import com.infosys.ellsie.model.Task;

/**
 * @author Ankur_Jain22
 *
 */
public class Indexer implements Job {
	
	private List<Task> tasks;
	private String jobName = "Indexer";

	
	@Override
	public List<Task> getTasks() {
		return tasks;
	}

	
	@Override
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;

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

}
