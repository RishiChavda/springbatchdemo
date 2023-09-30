package com.rish.learn.mappers;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.rish.learn.model.Person;
import com.rish.learn.BatchProcessingApplication;

public class PersonRowMapper implements RowMapper<Person> {

    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        BatchProcessingApplication.LOG.info("mapRow()");
        return new Person(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
    }
}