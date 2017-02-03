package org.jboss.perf;


import org.hibernate.cfg.AvailableSettings;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.perfmock.PerfMockDriver;

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
      PerfMockDriver.getInstance(); // make sure PerfMockDriver is classloaded
   }

   protected static final String C3P0 = "c3p0";

   protected static void log(Throwable t) {
      if ( PRINT_STACK_TRACES ) t.printStackTrace();
   }

   @State(Scope.Benchmark)
   public abstract static class BenchmarkState<T> {

      String persistenceUnit = C3P0 + ".mock";

      private JndiHelper jndiHelper = new JndiHelper();
      private JtaHelper jtaHelper = new JtaHelper();
      protected EntityManagerFactory entityManagerFactory;


      @Setup
      public void setup() throws Throwable {
         try {
            jndiHelper.start();
            jtaHelper.start();

            entityManagerFactory = Persistence.createEntityManagerFactory( persistenceUnit, NON_CACHED_PROPERTIES );

            PerfMockDriver.getInstance().setMocking( true );
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

            PerfMockDriver.getInstance().setMocking( false );
            entityManagerFactory.close();
            jtaHelper.stop();
            jndiHelper.stop();
         } catch (Throwable t) {
            log( t );
            throw t;
         }
      }

   }

}
