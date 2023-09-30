package com.rish.learn;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;
import com.rish.learn.BatchProcessingApplication;
import com.rish.learn.model.Person;
import com.rish.learn.model.PersonOut;
import com.rish.learn.processors.PersonProcessor;
import com.rish.learn.mappers.PersonRowMapper;

@Configuration
public class BatchConfiguration {
    private static final String QUERY_ALL_PERSONS = "SELECT id, fullname, address, biography FROM Person";

    private static final String OUTPUT_COLLECTION_NAME = "persons";

    @Bean
    @Primary
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix="app.datasource")
    public DataSource appDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
	public JdbcTemplate appJdbcTemplate(@Qualifier("appDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

    // @Bean
    // public FlatFileItemReader<Person> reader() {
    //     BatchProcessingApplication.LOG.info("reader()");
    //     return new FlatFileItemReaderBuilder<Person>()
    //         .name("personReader")
    //         .resource(new ClassPathResource("sample-data.csv"))
    //         .delimited()
    //         .names(new String[]{"id", "fullname", "address", "biography"})
    //         .fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
    //             setTargetType(Person.class);
    //         }})
    //         .build();
    // }

    @Bean
	public JdbcCursorItemReader<Person> reader(@Qualifier("appDataSource") DataSource dataSource) {
		BatchProcessingApplication.LOG.info("reader()");
        return new JdbcCursorItemReaderBuilder<Person>()
            .dataSource(dataSource)
            .name("PersonReader")
            .sql(QUERY_ALL_PERSONS)
			.rowMapper(new PersonRowMapper())
			.build();
	}

    @Bean
    public PersonProcessor processor() {
        BatchProcessingApplication.LOG.info("processor()");
        return new PersonProcessor();
    }

    // @Bean
    // public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
    //     BatchProcessingApplication.LOG.info("writer()");
    //     return new JdbcBatchItemWriterBuilder<Person>()
    //         .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
    //         .sql("INSERT INTO Person2 (fullname, address, biography) VALUES (:fullname, :address, :biography)")
    //         .dataSource(dataSource)
    //         .build();
    // }

	@Bean
	public MongoItemWriter<PersonOut> writer(MongoTemplate mongoTemplate) {
		BatchProcessingApplication.LOG.info("writer()");
		MongoItemWriter<PersonOut> writer = new MongoItemWriter<>();
		writer.setTemplate(mongoTemplate);
		writer.setCollection(OUTPUT_COLLECTION_NAME);
		return writer;
	}

    @Bean
    public Job importPersonJob(JobRepository jobRepository, JobCompletionNotificationListener listener, Step step1) {
        BatchProcessingApplication.LOG.info("importPersonJob()");
        return new JobBuilder("importPersonJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(step1)
            .end()
            .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository,
        PlatformTransactionManager transactionManager,
        JdbcCursorItemReader<Person> reader,
        MongoItemWriter<PersonOut> writer) {
        BatchProcessingApplication.LOG.info("step1()");
        return new StepBuilder("step1", jobRepository)
            .<Person, PersonOut> chunk(100, transactionManager)
            .reader(reader)
            .processor(processor())
            .writer(writer)
            .build();
    }
}
