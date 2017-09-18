package com.infosys.ellsie.main;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infosys.ellsie.model.Job;
import com.infosys.ellsie.model.Task;
import com.infosys.ellsie.util.ConfigLoaderFactory;

public class ArxivTraining {
	
	private static final Logger LOG = LoggerFactory.getLogger(ArxivTraining.class); 

	public static void main(String[] args) {
		// load configuration
		ConfigLoaderFactory.loadConfig(args[0]);
		
		List<Job> jobs = new ArrayList<Job>();
		
		prepareJobs(jobs);
		
		for (Job job : jobs) {
			job.executeJob();
		}

	}

	private static void prepareJobs(List<Job> jobs) {
		for (Entry<String, List<String>> jobName : ConfigLoaderFactory.getConfig().getJobs().entrySet()) {
			try {
				Job job = (Job) Class.forName(jobName.getKey()).getConstructors()[0].newInstance();
				List<Task> tasks = new ArrayList<Task>();
				for (String taskName : jobName.getValue()) {
					Task task = (Task) Class.forName(taskName).getConstructors()[0].newInstance();
					tasks.add(task);
				}
				job.setTasks(tasks);
				jobs.add(job);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | SecurityException
					| ClassNotFoundException e) {
				LOG.error(e.getMessage(),e);
				throw new RuntimeException(e);
			}
		}
	}

}
