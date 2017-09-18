/**
 * 
 */
package com.infosys.ellsie.model;

import java.util.List;

/**
 * @author Ankur_Jain22
 *
 */
public interface Job {
	
	List<Task> getTasks();
	
	void setTasks(List<Task> tasks);
	
	void executeJob();
	
	String getJobName();
	
	void setJobName(String name);

}
