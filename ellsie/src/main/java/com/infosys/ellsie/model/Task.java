/**
 * 
 */
package com.infosys.ellsie.model;

import java.io.Serializable;

/**
 * @author Ankur_Jain22
 *
 */
public interface Task extends Serializable {
	
	void executeTask();
	
	String getTaskName();
	
	void setTaskName(String name);

}
