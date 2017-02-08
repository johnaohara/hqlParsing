package org.jboss.perf;

/**
 * Created by johara on 08/02/17.
 */
public class HqlParserFactory {

   public static HQLParser buildHqlParser() throws IllegalAccessException, InstantiationException {
      Class<?> hqlParserClass = null;
      try {
         hqlParserClass = HqlParserFactory.class.getClassLoader().loadClass( "org.jboss.perf.ORM5HQLParser" );
      } catch (ClassNotFoundException e) {
      }
      if (hqlParserClass == null){
         try {
           hqlParserClass =HqlParserFactory.class.getClassLoader().loadClass( "org.jboss.perf.ORM6HQLParser" );
         } catch (ClassNotFoundException e) {
            e.printStackTrace();
         };
      }

      return (HQLParser) hqlParserClass.newInstance();
   }
}
