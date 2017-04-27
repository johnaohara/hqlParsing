package org.jboss.perf;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.collections.AST;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.HqlParser;

/**
 * Created by johara on 25/01/17.
 */
public class ORM5HQLParser implements HQLParser {
   @Override
   public Object parseHQL(String hqlString) {

      HqlParser parser = HqlParser.getInstance( hqlString );

      //TODO: Filters
//      parser.setFilter( filter );
      AST ast = null;

      try {
         parser.statement();
         ast = parser.getAST();
      } catch (RecognitionException e) {
         e.printStackTrace();
      } catch (TokenStreamException e) {
         e.printStackTrace();
      }

      return ast;
   }

   @Override
   public void configure(SessionFactoryImplementor sessionFactoryImplementor) {

   }

}
