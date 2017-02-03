package org.jboss.perf;


import org.hibernate.cfg.AvailableSettings;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;


public abstract class BenchmarkBase<T> {
   private static final Map<String, String> NON_CACHED_PROPERTIES = new HashMap<String, String>();
   private static final boolean PRINT_STACK_TRACES = Boolean.valueOf( System.getProperty( "printStackTraces", "true" ) );

   static {

      NON_CACHED_PROPERTIES.put( AvailableSettings.USE_SECOND_LEVEL_CACHE, "false" );
   }

   protected static final String PERSISTENCE_UNIT = "default";

   protected static void log(Throwable t) {
      if ( PRINT_STACK_TRACES ) t.printStackTrace();
   }

   @State(Scope.Benchmark)
   public abstract static class BenchmarkState<T> {

      protected EntityManagerFactory entityManagerFactory;


      @Setup
      public void setup() throws Throwable {
         try {

            entityManagerFactory = Persistence.createEntityManagerFactory( PERSISTENCE_UNIT, NON_CACHED_PROPERTIES );

         } catch (Throwable t) {
            t.printStackTrace();
            log( t );
            throw t;
         }
      }

      @TearDown
      public void shutdown() throws Throwable {
         try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.close();

            entityManagerFactory.close();
         } catch (Throwable t) {
            log( t );
            throw t;
         }
      }

   }

}
