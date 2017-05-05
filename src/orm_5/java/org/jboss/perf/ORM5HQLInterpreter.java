package org.jboss.perf;

import antlr.collections.AST;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.internal.ast.HqlParser;
import org.hibernate.hql.internal.ast.QueryTranslatorImpl;
import org.hibernate.hql.spi.QueryTranslatorFactory;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaConversionException;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Function;

/**
 * Created by johara on 25/01/17.
 */
public class ORM5HQLInterpreter implements BenchmarkHQLParser {
   private SessionFactoryImplementor sessionFactoryImplementor;

   Method reflected = null;
   Function lambda;
   MethodHandle mh;

   public ORM5HQLInterpreter() {

      try {

         // Define black magic.
         final MethodHandles.Lookup original = MethodHandles.lookup();
         final Field internal = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
         internal.setAccessible(true);
         final MethodHandles.Lookup trusted = (MethodHandles.Lookup) internal.get(original);

         // Invoke black magic.
         final MethodHandles.Lookup caller = trusted.in(QueryTranslatorImpl.class);

         reflected = QueryTranslatorImpl.class.getDeclaredMethod( "parse", boolean.class );
         reflected.setAccessible( true );

         mh = trusted.unreflect( reflected );

         lambda = parseLambda( caller, mh );

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
         parser = (HqlParser) lambda.apply( false );
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



   static Function parseLambda(final MethodHandles.Lookup caller,
                                     final MethodHandle methodHandle) throws Throwable {

      final Class<?> functionKlaz = Function.class;
      final String functionName = "apply";
      final Class<?> functionReturn = Object.class;
//      final Class<?>[] functionParams = new Class<?>[] { boolean.class };
      final Class<?>[] functionParams = new Class<?>[] { QueryTranslatorImpl.class, boolean.class };
      //

      final MethodType factoryMethodType = MethodType.methodType(functionKlaz);
      final MethodType functionMethodType = MethodType.methodType(functionReturn, functionParams);

      final CallSite parseFactory = LambdaMetafactory.metafactory( //
         caller, // Represents a lookup context.
         functionName, // The name of the method to implement.
         factoryMethodType, // Signature of the factory method.
         functionMethodType, // Signature of function implementation.
         methodHandle, // Function method implementation.
         methodHandle.type() // Function method type signature.
      );

      final MethodHandle parseInvoker = parseFactory.getTarget();

      final Function parseLambda = (Function) parseInvoker.invokeExact();

      return parseLambda;
   }
}
