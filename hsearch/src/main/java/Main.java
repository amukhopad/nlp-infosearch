import org.hibernate.HibernateException;
import org.hibernate.Metamodel;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.metamodel.EntityType;

import java.util.Map;

public class Main {
  private static final SessionFactory ourSessionFactory;

  static {
    try {
      Configuration configuration = new Configuration();
      configuration.configure();

      ourSessionFactory = configuration.buildSessionFactory();
    } catch (Throwable ex) {
      throw new ExceptionInInitializerError(ex);
    }
  }

  public static Session getSession() throws HibernateException {
    return ourSessionFactory.openSession();
  }

  public static void main(final String[] args) throws Exception {
    try (Session session = getSession()) {
      System.out.println("querying all the managed entities...");
      var metamodel = session.getSessionFactory().getMetamodel();
      for (var entityType : metamodel.getEntities()) {
        var entityName = entityType.getName();
        var query = session.createQuery("from " + entityName);
        System.out.println("executing: " + query.getQueryString());
        for (Object o : query.list()) {
          System.out.println("  " + o);
        }
      }
    }
  }
}
