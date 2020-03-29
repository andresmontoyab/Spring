package com.learnig.database.databasedemo.jdbc;

import com.learnig.database.databasedemo.jdbc.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class PersonJdbcDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Person> findAll() {
        return jdbcTemplate.query("select * from person",
                new BeanPropertyRowMapper<Person>(Person.class));
    }

    public Person findById(Integer id) {
        return jdbcTemplate.queryForObject
                ("select * from person p where p.id = ?",
                        new Object[]{id},
                        new personRowMapper());
    }

    public Integer deleteById(Integer id) {
        return jdbcTemplate.update("delete from person p where p.id = ?", id);
    }

    public Integer insert(Person person) {
        return jdbcTemplate.update(
                "insert into person (id, name, location, birth_date)" +
                    " values (?,?,?,?)",
                    person.getId(),
                    person.getName(),
                    person.getLocation(),
                    new Timestamp(person.getBirthDate().getTime()));
    }

    public Integer update(Person person) {
        return jdbcTemplate.update(
                "update person set name=?, location=?, birth_date=?" +
                    "where id = ?",
                    person.getName(),
                    person.getLocation(),
                    new Timestamp(person.getBirthDate().getTime()),
                    person.getId());
    }

    /**
     * Row Mapper
     * If the data which is coming back from the table is a diferent structure from your entity
     * is require to create a custom Row Mapper
     */

    class personRowMapper implements RowMapper<Person> {
        @Override
        public Person mapRow(ResultSet resultSet, int i) throws SQLException {
            Person person = new Person();
            person.setId(resultSet.getInt("id"));
            person.setName(resultSet.getString("name"));
            person.setLocation(resultSet.getString("location"));
            person.setBirthDate(resultSet.getTimestamp("birth_date"));
            return person;
        }
    }
}
