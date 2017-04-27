package org.jboss.perf;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.collections.AST;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.internal.ast.HqlParser;
import org.hibernate.hql.internal.ast.QueryTranslatorImpl;
import org.hibernate.hql.spi.QueryTranslatorFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by johara on 25/01/17.
 */
public class ORM5HQLInterpreter implements HQLParser {
   private SessionFactoryImplementor sessionFactoryImplementor;

   @Override
   public Object parseHQL(String hqlString) {


      QueryTranslatorImpl translator = createNewQueryTranslator( "select count(*) from Human h" );


      //TODO: Filters
      AST ast = null;

      try {

         Method method = translator.getClass().getDeclaredMethod("parse");
         method.setAccessible(true);
         HqlParser parser  = (HqlParser) method.invoke(false);

         ast = parser.getAST();

      } catch (NoSuchMethodException e) {
         e.printStackTrace();
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      } catch (InvocationTargetException e) {
         e.printStackTrace();
      }

      return ast;
   }

   private QueryTranslatorImpl createNewQueryTranslator(String hql) {

      QueryTranslatorFactory ast = new ASTQueryTranslatorFactory();
      QueryTranslatorImpl newQueryTranslator = (QueryTranslatorImpl) ast.createQueryTranslator( hql, hql, Collections.EMPTY_MAP, sessionFactoryImplementor, null );
      newQueryTranslator.compile( new HashMap(), false );
      return newQueryTranslator;

   }

   @Override
   public void configure(SessionFactoryImplementor sessionFactoryImplementor) {
      this.sessionFactoryImplementor = sessionFactoryImplementor;
//      throw new RuntimeException( "Not yet Supported" );
   }


}
