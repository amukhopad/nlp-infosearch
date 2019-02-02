package ua.edu.ukma.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.hibernate.search.Search;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Repository;

import ua.edu.ukma.entity.Customer;

@Repository
public class DefaultCustomerRepository implements CustomerRepository {

  private SessionFactory sessionFactory;

  @Transactional
  @Override
  public void createCustomer(Customer customer) {
    sessionFactory.openSession().persist(customer);
  }

  @Override
  public List searchCustomer(String query) {
    var fullTextSession = Search.getFullTextSession(sessionFactory.openSession());
    var queryBuilder = fullTextSession
            .getSearchFactory()
            .buildQueryBuilder()
            .forEntity(Customer.class)
            .get();
    var luceneQuery = queryBuilder
            .keyword()
            .onFields("firstName", "lastName", "countryCode")
            .matching(query)
            .createQuery();

    var persistenceQuery = fullTextSession.createFullTextQuery(luceneQuery, Customer.class);

    return persistenceQuery.getResultList();
  }

  @Required
  public void setSessionFactory(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }
}
