package com.rish.learn.processors;

import org.springframework.batch.item.ItemProcessor;
import com.rish.learn.model.Person;
import com.rish.learn.model.PersonOut;
import com.rish.learn.BatchProcessingApplication;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class PersonProcessor implements ItemProcessor<Person, PersonOut> {

    @Override
    public PersonOut process(final Person person) throws Exception {
        final int id = person.getId();
        final String fullname = person.getFullname();

        final List<String> address = Arrays.asList(person.getAddress().split(","));

        final String biography = person.getBiography();

        final PersonOut transformedPerson = new PersonOut(id, fullname, address, biography);

        BatchProcessingApplication.LOG.info("Converting (" + person + ") into (" + transformedPerson + ")");

        return transformedPerson;
    }


}
