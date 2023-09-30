package com.rish.learn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.rish.learn.model.Person;
import com.rish.learn.BatchProcessingApplication;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

    @Override
    public void afterJob(JobExecution jobExecution) {
      if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
        BatchProcessingApplication.LOG.info("!!! JOB FINISHED! Verifying results");
  
        jdbcTemplate.query("SELECT id, fullname, address, biography FROM Person ORDER BY id DESC",
          (rs, row) -> new Person(
            rs.getInt(1),
            rs.getString(2),
            rs.getString(3),
            rs.getString(4)
            )
        ).forEach(person -> BatchProcessingApplication.LOG.info("Found <{{}}> in the database.", person));
      }
      else {
        BatchProcessingApplication.LOG.info("!!! JOB FAILED (or something else)!");
      }
    }
}
