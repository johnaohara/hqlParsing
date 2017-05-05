package org.jboss.perf;

/**
 * Created by johara on 08/02/17.
 */
public class HqlParserFactory {

   public static BenchmarkHQLParser buildHqlParser() throws IllegalAccessException, InstantiationException {
      return getParser  (new String[] {"org.jboss.perf.ORM5HQLParser", "org.jboss.perf.ORM6HQLParser"}) ;
   }

   public static BenchmarkHQLParser buildHqlInterpreter() throws IllegalAccessException, InstantiationException {
      return getParser  (new String[] {"org.jboss.perf.ORM5HQLInterpreter", "org.jboss.perf.ORM6HQLInterpreter"}) ;
   }

   private static BenchmarkHQLParser getParser(String[] classes){

      Class<?> hqlParserClass = null;

      for(String _class: classes){
         try {
            hqlParserClass =HqlParserFactory.class.getClassLoader().loadClass( _class );
         } catch (ClassNotFoundException e) {
            //do nothing
         };

         if ( hqlParserClass != null ) {
            try {
               return (BenchmarkHQLParser) hqlParserClass.newInstance();
            } catch (InstantiationException e) {
               e.printStackTrace();
            } catch (IllegalAccessException e) {
               e.printStackTrace();
            }
         }

      }
      return  null;
   }
}
