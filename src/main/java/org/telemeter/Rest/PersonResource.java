package org.telemeter.Rest;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telemeter.Entity.Person;


@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {

    @Inject
    EntityManager em;

    private static final Logger logger = LogManager.getLogger(PersonResource.class);

    @GET
    public List<Person> getAllPersons() {
        logger.info("Retrieving all persons");
        return em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
    }

    @GET
    @Path("/{id}")
    public Person getPersonById(@PathParam("id") Long id) {
        logger.info("Retrieving person by ID: {}", id);
        return em.find(Person.class, id);
    }

    @POST
    @Transactional
    public void createPerson(Person person) {
        em.merge(person);
        logger.info("Created new person: {}", person.getName());
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public void updatePerson(@PathParam("id") Long id, Person updatedPerson) {
        Person person = em.find(Person.class, id);
        if (person != null) {
            person.setName(updatedPerson.getName());
            person.setAge(updatedPerson.getAge());
            logger.info("Updated person with ID {}: {}", id, updatedPerson.getName());
        } else {
            logger.warn("Person with ID {} not found", id);
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void deletePerson(@PathParam("id") Long id) {
        Person person = em.find(Person.class, id);
        if (person != null) {
            em.remove(person);
            logger.info("Deleted person with ID {}", id);
        } else {
            logger.warn("Person with ID {} not found", id);
        }
    }

    @GET
    @Path("/details")
     public List<Person> getErrorPersons() {
        logger.error("Retrieving all persons");
        return em.createQuery("SELECT p FROM Person q", Person.class).getResultList();
    }
}
