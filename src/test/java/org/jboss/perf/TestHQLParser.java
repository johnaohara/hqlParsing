package org.jboss.perf;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestHQLParser {

   private BenchmarkHQLParser hqlParser;

   @Before
   public void setup(){
      try {
         hqlParser = HqlParserFactory.buildHqlParser();
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      } catch (InstantiationException e) {
         e.printStackTrace();
      }
      hqlParser.configure( null );
   }

   @Test
   public void testHqlParser(){
      Object result = hqlParser.parseHQL( "Select p from Person p" );
      Assert.assertNotNull( result );
   }


}
