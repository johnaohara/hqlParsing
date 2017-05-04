package org.jboss.perf;

import antlr.collections.AST;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.internal.ast.HqlParser;
import org.hibernate.hql.internal.ast.QueryTranslatorImpl;
import org.hibernate.hql.spi.QueryTranslatorFactory;

import java.lang.invoke.LambdaConversionException;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Function;

/**
 * Created by johara on 25/01/17.
 */
public class ORM5HQLInterpreter implements HQLParser {
   private SessionFactoryImplementor sessionFactoryImplementor;

   Method reflected = null;
   Function<QueryTranslatorImpl, HqlParser> lambda;
   MethodHandle mh;

   public ORM5HQLInterpreter() {

      try {
         reflected = QueryTranslatorImpl.class.getDeclaredMethod( "parse", boolean.class );
         reflected.setAccessible( true );
         final MethodHandles.Lookup lookup = MethodHandles.lookup();

         

         mh = lookup.unreflect( reflected );

         lambda = (Function) LambdaMetafactory.metafactory(
            lookup, "apply", MethodType.methodType( Function.class ),
            mh.type(), mh, mh.type() ).getTarget().invokeExact();
      } catch (NoSuchMethodException e) {
         e.printStackTrace();
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      } catch (LambdaConversionException e) {
         e.printStackTrace();
      } catch (Throwable throwable) {
         throwable.printStackTrace();
      }
   }

   @Override
   public Object parseHQL(String hqlString) {

      QueryTranslatorImpl translator = createNewQueryTranslator( hqlString );

      AST ast = null;

      HqlParser parser = null;
      try {
//         parser = (HqlParser) reflected.invoke( translator, false );
//         parser = (HqlParser) result;
//         parser = (HqlParser) mh.invoke( translator, false );
         parser = lambda.apply( translator );
      } catch (Throwable throwable) {
         throwable.printStackTrace();
      }

      ast = parser.getAST();


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
