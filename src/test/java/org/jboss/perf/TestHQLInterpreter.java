package org.jboss.perf;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestHQLInterpreter {

   protected SessionFactory sessionFactory;
   protected Session session;
   protected HQLParser hqlParser;

   @Before
   public void setup() {
      try {
         hqlParser = HqlParserFactory.buildHqlInterpreter();

         final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure() // configures settings from hibernate.cfg.xml
            .build();
         sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();

         session = sessionFactory.openSession();

         try {
            //TODO: determine benchmark to run from configuration
            if ( false ) {
               hqlParser = HqlParserFactory.buildHqlParser();
            } else {
               hqlParser = HqlParserFactory.buildHqlInterpreter();
            }
         } catch (IllegalAccessException e) {
            e.printStackTrace();
         } catch (InstantiationException e) {
            e.printStackTrace();
         }
         hqlParser.configure( this.sessionFactory.getSessionFactory() );

      } catch (Throwable t) {
         t.printStackTrace();
      }
   }

   @Test
   public void testHqlInterpreter() {
      Object result = hqlParser.parseHQL( "Select p from Person p" );
      Assert.assertNotNull( result );
   }


}
