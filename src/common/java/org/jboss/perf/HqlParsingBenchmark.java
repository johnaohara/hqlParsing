package org.jboss.perf;

import org.hibernate.query.Query;
import org.jboss.perf.model.Person;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;


public class HqlParsingBenchmark extends BenchmarkBase<Person> {

   @Benchmark
   public Query simpleSelect(BenchmarkState benchmarkState) {
      return parseQuery( "SELECT p FROM Person p", benchmarkState );
   }

   @Benchmark
   public Query simpleWhere(BenchmarkState benchmarkState) {
      return parseQuery( "SELECT p FROM Person p where p.firstName = ''", benchmarkState );
   }

   private Query parseQuery(String query, BenchmarkState benchmarkState) {
      benchmarkState.session.beginTransaction();
      Query _query = benchmarkState.session.createQuery( query );
      benchmarkState.session.getSession().getTransaction().commit();
      return _query;
   }

   @Benchmark
   public void testDocoExamples92(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = result = parse( "from eg.Cat", benchmarkState );
      bh.consume( result );
      result = result = parse( "from eg.Cat as cat", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.Cat cat", benchmarkState );
      bh.consume( result );
      bh.consume( result );
      result = parse( "from Formula, Parameter", benchmarkState );
      bh.consume( result );
      bh.consume( result );
      result = parse( "from Formula as form, Parameter as param", benchmarkState );
      bh.consume( result );
      bh.consume( result );
   }

   @Benchmark
   public void testDocoExamples93(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "from eg.Cat as cat inner join cat.mate as mate left outer join cat.kittens as kitten", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.Cat as cat left join cat.mate.kittens as kittens", benchmarkState );
      bh.consume( result );
      result = parse( "from Formula form full join form.parameter param", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.Cat as cat join cat.mate as mate left join cat.kittens as kitten", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.Cat as cat\ninner join fetch cat.mate\nleft join fetch cat.kittens", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testDocoExamples94(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "select mate from eg.Cat as cat inner join cat.mate as mate", benchmarkState );
      bh.consume( result );
      result = parse( "select cat.mate from eg.Cat cat", benchmarkState );
      bh.consume( result );
      result = parse( "select elements(cat.kittens) from eg.Cat cat", benchmarkState );
      bh.consume( result );
      result = parse( "select cat.name from eg.DomesticCat cat where cat.name like 'fri%'", benchmarkState );
      bh.consume( result );
      result = parse( "select cust.name.firstName from Customer as cust", benchmarkState );
      bh.consume( result );
      result = parse( "select mother, offspr, mate.name from eg.DomesticCat\n"
         + " as mother inner join mother.mate as mate left outer join\n"
         + "mother.kittens as offspr", benchmarkState );
      bh.consume( result );
      result = parse( "select new Family(mother, mate, offspr)\n"
         + "from eg.DomesticCat as mother\n"
         + "join mother.mate as mate\n"
         + "left join mother.kittens as offspr\n", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testDocoExamples95(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "select avg(cat.weight), sum(cat.weight), max(cat.weight), count(cat)\n"
         + "from eg.Cat cat", benchmarkState );
      bh.consume( result );
      result = parse( "select cat, count( elements(cat.kittens) )\n"
         + " from eg.Cat cat group by cat", benchmarkState );
      bh.consume( result );
      result = parse( "select distinct cat.name from eg.Cat cat", benchmarkState );
      bh.consume( result );
      result = parse( "select count(distinct cat.name), count(cat) from eg.Cat cat", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testDocoExamples96(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "from eg.Cat as cat", benchmarkState );
      bh.consume( result );
      result = parse( "from java.lang.Object o", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.Named n, eg.Named m where n.name = m.name", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testDocoExamples97(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "from eg.Cat as cat where cat.name='Fritz'", benchmarkState );
      bh.consume( result );
      result = parse( "select foo\n"
         + "from eg.Foo foo, eg.Bar bar\n"
         + "where foo.startDate = bar.date\n", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.Cat cat where cat.mate.name is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.Cat cat, eg.Cat rival where cat.mate = rival.mate", benchmarkState );
      bh.consume( result );
      result = parse( "select cat, mate\n"
         + "from eg.Cat cat, eg.Cat mate\n"
         + "where cat.mate = mate", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.Cat as cat where cat.id = 123", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.Cat as cat where cat.mate.id = 69", benchmarkState );
      bh.consume( result );
      result = parse( "from bank.Person person\n"
         + "where person.id.country = 'AU'\n"
         + "and person.id.medicareNumber = 123456", benchmarkState );
      bh.consume( result );
      result = parse( "from bank.Account account\n"
         + "where account.owner.id.country = 'AU'\n"
         + "and account.owner.id.medicareNumber = 123456", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.Cat cat where cat.class = eg.DomesticCat", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.AuditLog log, eg.Payment payment\n"
         + "where log.item.class = 'eg.Payment' and log.item.id = payment.id", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testDocoExamples98(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "from eg.DomesticCat cat where cat.name between 'A' and 'B'", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.DomesticCat cat where cat.name in ( 'Foo', 'Bar', 'Baz' )", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.DomesticCat cat where cat.name not between 'A' and 'B'", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.DomesticCat cat where cat.name not in ( 'Foo', 'Bar', 'Baz' )", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.Cat cat where cat.kittens.size > 0", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.Cat cat where size(cat.kittens) > 0", benchmarkState );
      bh.consume( result );
      result = parse( "from Order ord where maxindex(ord.items) > 100", benchmarkState );
      bh.consume( result );
      result = parse( "from Order ord where minelement(ord.items) > 10000", benchmarkState );
      bh.consume( result );

      result = parse( "select mother from eg.Cat as mother, eg.Cat as kit\n"
         + "where kit in elements(foo.kittens)", benchmarkState );
      bh.consume( result );
      result = parse( "select p from eg.NameList list, eg.Person p\n"
         + "where p.name = some elements(list.names)", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.Cat cat where exists elements(cat.kittens)", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.Player p where 3 > all elements(p.scores)", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.Show show where 'fizard' in indices(show.acts)", benchmarkState );
      bh.consume( result );

      result = parse( "from Order ord where ord.items[0].id = 1234", benchmarkState );
      bh.consume( result );
      result = parse( "select person from Person person, Calendar calendar\n"
         + "where calendar.holidays['national day'] = person.birthDay\n"
         + "and person.nationality.calendar = calendar", benchmarkState );
      bh.consume( result );
      result = parse( "select item from Item item, Order ord\n"
         + "where ord.items[ ord.deliveredItemIndices[0] ] = item and ord.id = 11", benchmarkState );
      bh.consume( result );
      result = parse( "select item from Item item, Order ord\n"
         + "where ord.items[ maxindex(ord.items) ] = item and ord.id = 11", benchmarkState );
      bh.consume( result );

      result = parse( "select item from Item item, Order ord\n"
         + "where ord.items[ size(ord.items) - 1 ] = item", benchmarkState );
      bh.consume( result );

      result = parse( "from eg.DomesticCat cat where upper(cat.name) like 'FRI%'", benchmarkState );
      bh.consume( result );

      result = parse( "select cust from Product prod, Store store\n"
         + "inner join store.customers cust\n"
         + "where prod.name = 'widget'\n"
         + "and store.location.name in ( 'Melbourne', 'Sydney' )\n"
         + "and prod = all elements(cust.currentOrder.lineItems)", benchmarkState );
      bh.consume( result );

   }

   @Benchmark
   public void testDocoExamples99(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "from eg.DomesticCat cat\n"
         + "order by cat.name asc, cat.weight desc, cat.birthdate", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testDocoExamples910(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "select cat.color, sum(cat.weight), count(cat)\n"
         + "from eg.Cat cat group by cat.color", benchmarkState );
      bh.consume( result );
      result = parse( "select foo.id, avg( elements(foo.names) ), max( indices(foo.names) )\n"
         + "from eg.Foo foo group by foo.id", benchmarkState );
      bh.consume( result );
      result = parse( "select cat.color, sum(cat.weight), count(cat)\n"
         + "from eg.Cat cat group by cat.color\n"
         + "having cat.color in (eg.Color.TABBY, eg.Color.BLACK)", benchmarkState );
      bh.consume( result );
      result = parse( "select cat from eg.Cat cat join cat.kittens kitten\n"
         + "group by cat having avg(kitten.weight) > 100\n"
         + "order by count(kitten) asc, sum(kitten.weight) desc", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testDocoExamples911(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "from eg.Cat as fatcat where fatcat.weight > (\n"
         + "select avg(cat.weight) from eg.DomesticCat cat)", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.DomesticCat as cat where cat.name = some (\n"
         + "select name.nickName from eg.Name as name)\n", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.Cat as cat where not exists (\n"
         + "from eg.Cat as mate where mate.mate = cat)", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.DomesticCat as cat where cat.name not in (\n"
         + "select name.nickName from eg.Name as name)", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testDocoExamples912(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "select ord.id, sum(price.amount), count(item)\n"
         + "from Order as ord join ord.lineItems as item\n"
         + "join item.product as product, Catalog as catalog\n"
         + "join catalog.prices as price\n"
         + "where ord.paid = false\n"
         + "and ord.customer = :customer\n"
         + "and price.product = product\n"
         + "and catalog.effectiveDate < sysdate\n"
         + "and catalog.effectiveDate >= all (\n"
         + "select cat.effectiveDate from Catalog as cat where cat.effectiveDate < sysdate)\n"
         + "group by ord\n"
         + "having sum(price.amount) > :minAmount\n"
         + "order by sum(price.amount) desc", benchmarkState );
      bh.consume( result );

      result = parse( "select ord.id, sum(price.amount), count(item)\n"
         + "from Order as ord join ord.lineItems as item join item.product as product,\n"
         + "Catalog as catalog join catalog.prices as price\n"
         + "where ord.paid = false and ord.customer = :customer\n"
         + "and price.product = product and catalog = :currentCatalog\n"
         + "group by ord having sum(price.amount) > :minAmount\n"
         + "order by sum(price.amount) desc", benchmarkState );
      bh.consume( result );

      result = parse( "select count(payment), status.name \n"
         + "from Payment as payment \n"
         + "    join payment.currentStatus as status\n"
         + "    join payment.statusChanges as statusChange\n"
         + "where payment.status.name <> PaymentStatus.AWAITING_APPROVAL\n"
         + "    or (\n"
         + "        statusChange.timeStamp = ( \n"
         + "            select max(change.timeStamp) \n"
         + "            from PaymentStatusChange change \n"
         + "            where change.payment = payment\n"
         + "        )\n"
         + "        and statusChange.user <> :currentUser\n"
         + "    )\n"
         + "group by status.name, status.sortOrder\n"
         + "order by status.sortOrder", benchmarkState );
      bh.consume( result );
      result = parse( "select count(payment), status.name \n"
         + "from Payment as payment\n"
         + "    join payment.currentStatus as status\n"
         + "where payment.status.name <> PaymentStatus.AWAITING_APPROVAL\n"
         + "    or payment.statusChanges[ maxIndex(payment.statusChanges) ].user <> :currentUser\n"
         + "group by status.name, status.sortOrder\n"
         + "order by status.sortOrder", benchmarkState );
      bh.consume( result );
      result = parse( "select account, payment\n"
         + "from Account as account\n"
         + "    left outer join account.payments as payment\n"
         + "where :currentUser in elements(account.holder.users)\n"
         + "    and PaymentStatus.UNPAID = isNull(payment.currentStatus.name, PaymentStatus.UNPAID)\n"
         + "order by account.type.sortOrder, account.accountNumber, payment.dueDate", benchmarkState );
      bh.consume( result );
      result = parse( "select account, payment\n"
         + "from Account as account\n"
         + "    join account.holder.users as user\n"
         + "    left outer join account.payments as payment\n"
         + "where :currentUser = user\n"
         + "    and PaymentStatus.UNPAID = isNull(payment.currentStatus.name, PaymentStatus.UNPAID)\n"
         + "order by account.type.sortOrder, account.accountNumber, payment.dueDate", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testExamples1(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "select new org.hibernate.test.S(s.count, s.address)\n"
         + "from s in class Simple", benchmarkState );
      bh.consume( result );
      result = parse( "select s.name, sysdate, trunc(s.pay), round(s.pay) from s in class Simple", benchmarkState );
      bh.consume( result );
      result = parse( "select round(s.pay, 2) from s", benchmarkState );
      bh.consume( result );
      result = parse( "select abs(round(s.pay)) from s in class Simple", benchmarkState );
      bh.consume( result );
      result = parse( "select trunc(round(sysdate)) from s in class Simple", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testArrayExpr(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "from Order ord where ord.items[0].id = 1234", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testMultipleActualParameters(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "select round(s.pay, 2) from s", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testMultipleFromClasses(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "FROM eg.mypackage.Cat qat, com.toadstool.Foo f", benchmarkState );
      bh.consume( result );
      result = parse( "FROM eg.mypackage.Cat qat, org.jabberwocky.Dipstick", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testFromWithJoin(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "FROM eg.mypackage.Cat qat, com.toadstool.Foo f join net.sf.blurb.Blurb", benchmarkState );
      bh.consume( result );
      result = parse( "FROM eg.mypackage.Cat qat  left join com.multijoin.JoinORama , com.toadstool.Foo f join net.sf.blurb.Blurb", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testSelect(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "SELECT f FROM eg.mypackage.Cat qat, com.toadstool.Foo f join net.sf.blurb.Blurb", benchmarkState );
      bh.consume( result );
      result = parse( "SELECT DISTINCT bar FROM eg.mypackage.Cat qat  left join com.multijoin.JoinORama as bar, com.toadstool.Foo f join net.sf.blurb.Blurb", benchmarkState );
      bh.consume( result );
      result = parse( "SELECT count(*) FROM eg.mypackage.Cat qat", benchmarkState );
      bh.consume( result );
      result = parse( "SELECT avg(qat.weight) FROM eg.mypackage.Cat qat", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testWhere(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "FROM eg.mypackage.Cat qat where qat.name like '%fluffy%' or qat.toes > 5", benchmarkState );
      bh.consume( result );
      result = parse( "FROM eg.mypackage.Cat qat where not qat.name like '%fluffy%' or qat.toes > 5", benchmarkState );
      bh.consume( result );
      result = parse( "FROM eg.mypackage.Cat qat where not qat.name not like '%fluffy%'", benchmarkState );
      bh.consume( result );
      result = parse( "FROM eg.mypackage.Cat qat where qat.name in ('crater','bean','fluffy')", benchmarkState );
      bh.consume( result );
      result = parse( "FROM eg.mypackage.Cat qat where qat.name not in ('crater','bean','fluffy')", benchmarkState );
      bh.consume( result );
      result = parse( "from Animal an where sqrt(an.bodyWeight)/2 > 10", benchmarkState );
      bh.consume( result );
      result = parse( "from Animal an where (an.bodyWeight > 10 and an.bodyWeight < 100) or an.bodyWeight is null", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testGroupBy(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "FROM eg.mypackage.Cat qat group by qat.breed", benchmarkState );
      bh.consume( result );
      result = parse( "FROM eg.mypackage.Cat qat group by qat.breed, qat.eyecolor", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testOrderBy(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "FROM eg.mypackage.Cat qat order by avg(qat.toes)", benchmarkState );
      bh.consume( result );
      result = parse( "from Animal an order by sqrt(an.bodyWeight)/2", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testDoubleLiteral(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "from eg.Cat as tinycat where fatcat.weight < 3.1415", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.Cat as enormouscat where fatcat.weight > 3.1415e3", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testComplexConstructor(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "select new Foo(count(bar)) from bar", benchmarkState );
      bh.consume( result );
      result = parse( "select new Foo(count(bar),(select count(*) from doofus d where d.gob = 'fat' )) from bar", benchmarkState );
      bh.consume( result );
   }


   @Benchmark
   public void testInNotIn(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "from foo where foo.bar in ('a' , 'b', 'c')", benchmarkState );
      bh.consume( result );
      result = parse( "from foo where foo.bar not in ('a' , 'b', 'c')", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testOperatorPrecedence(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "from foo where foo.bar = 123 + foo.baz * foo.not", benchmarkState );
      bh.consume( result );
      result = parse( "from foo where foo.bar like 'testzzz' || foo.baz or foo.bar in ('duh', 'gob')", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testUnitTestHql(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "select foo from foo in class org.hibernate.test.Foo, fee in class org.hibernate.test.Fee where foo.dependent = fee order by foo.string desc, foo.component.count asc, fee.id", benchmarkState );
      bh.consume( result );
      result = parse( "select foo.foo, foo.dependent from foo in class org.hibernate.test.Foo order by foo.foo.string desc, foo.component.count asc, foo.dependent.id", benchmarkState );
      bh.consume( result );
      result = parse( "select foo from foo in class org.hibernate.test.Foo order by foo.dependent.id, foo.dependent.fi", benchmarkState );
      bh.consume( result );
      result = parse( "SELECT one FROM one IN CLASS org.hibernate.test.One ORDER BY one.value ASC", benchmarkState );
      bh.consume( result );
      result = parse( "SELECT many.one FROM many IN CLASS org.hibernate.test.Many ORDER BY many.one.value ASC, many.one.id", benchmarkState );
      bh.consume( result );
      result = parse( "select foo.id from org.hibernate.test.Foo foo where foo.joinedProp = 'foo'", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Foo foo inner join fetch foo.foo", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz left outer join fetch baz.fooToGlarch", benchmarkState );
      bh.consume( result );
      result = parse( "select foo.foo.foo.string from foo in class org.hibernate.test.Foo where foo.foo = 'bar'", benchmarkState );
      bh.consume( result );
      result = parse( "select foo.foo.foo.foo.string from foo in class org.hibernate.test.Foo where foo.foo.foo = 'bar'", benchmarkState );
      bh.consume( result );
      result = parse( "select foo.foo.foo.string from foo in class org.hibernate.test.Foo where foo.foo.foo.foo.string = 'bar'", benchmarkState );
      bh.consume( result );
      result = parse( "select foo.string from foo in class org.hibernate.test.Foo where foo.foo.foo = 'bar' and foo.foo.foo.foo = 'baz'", benchmarkState );
      bh.consume( result );
      result = parse( "select foo.string from foo in class org.hibernate.test.Foo where foo.foo.foo.foo.string = 'a' and foo.foo.string = 'b'", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Foo as foo where foo.component.glarch.name is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Foo as foo left outer join foo.component.glarch as glarch where glarch.name = 'foo'", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Foo", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Foo foo left outer join foo.foo", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Foo, org.hibernate.test.Bar", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz left join baz.fooToGlarch, org.hibernate.test.Bar bar join bar.foo", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz left join baz.fooToGlarch join baz.fooSet", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz left join baz.fooToGlarch join fetch baz.fooSet foo left join fetch foo.foo", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo where foo.string='osama bin laden' and foo.boolean = true order by foo.string asc, foo.component.count desc", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo where foo.string='osama bin laden' order by foo.string asc, foo.component.count desc", benchmarkState );
      bh.consume( result );
      result = parse( "select foo.foo from foo in class org.hibernate.test.Foo", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo where foo.component.count is null order by foo.component.count", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo where foo.component.name='foo'", benchmarkState );
      bh.consume( result );
      result = parse( "select distinct foo.component.name, foo.component.name from foo in class org.hibernate.test.Foo where foo.component.name='foo'", benchmarkState );
      bh.consume( result );
      result = parse( "select distinct foo.component.name, foo.id from foo in class org.hibernate.test.Foo where foo.component.name='foo'", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo where foo.id=?", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo where foo.key=?", benchmarkState );
      bh.consume( result );
      result = parse( "select foo.foo from foo in class org.hibernate.test.Foo where foo.string='fizard'", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo where foo.component.subcomponent.name='bar'", benchmarkState );
      bh.consume( result );
      result = parse( "select foo.foo from foo in class org.hibernate.test.Foo where foo.foo.id=?", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo where foo.foo = ?", benchmarkState );
      bh.consume( result );
      result = parse( "from bar in class org.hibernate.test.Bar where bar.string='a string' or bar.string='a string'", benchmarkState );
      bh.consume( result );
      result = parse( "select foo.component.name, elements(foo.component.importantDates) from foo in class org.hibernate.test.Foo where foo.foo.id=?", benchmarkState );
      bh.consume( result );
      result = parse( "select max(elements(foo.component.importantDates)) from foo in class org.hibernate.test.Foo group by foo.id", benchmarkState );
      bh.consume( result );
      result = parse( "select foo.foo.foo.foo from foo in class org.hibernate.test.Foo, foo2 in class org.hibernate.test.Foo where foo = foo2.foo and not not ( not foo.string='fizard' ) and foo2.string between 'a' and (foo.foo.string) and ( foo2.string in ( 'fiz', 'blah') or 1=1 )", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo where foo.string='from BoogieDown  -tinsel town  =!@#$^&*())'", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo where not foo.string='foo''bar'", benchmarkState );
      bh.consume( result ); // Added quote quote is an escape
      result = parse( "from foo in class org.hibernate.test.Foo where foo.component.glarch.next is null", benchmarkState );
      bh.consume( result );
      result = parse( " from bar in class org.hibernate.test.Bar where bar.baz.count=667 and bar.baz.count!=123 and not bar.baz.name='1-E-1'", benchmarkState );
      bh.consume( result );
      result = parse( " from i in class org.hibernate.test.Bar where i.baz.name='Bazza'", benchmarkState );
      bh.consume( result );
      result = parse( "select count(distinct foo.foo) from foo in class org.hibernate.test.Foo", benchmarkState );
      bh.consume( result );
      result = parse( "select count(foo.foo.boolean) from foo in class org.hibernate.test.Foo", benchmarkState );
      bh.consume( result );
      result = parse( "select count(*), foo.int from foo in class org.hibernate.test.Foo group by foo.int", benchmarkState );
      bh.consume( result );
      result = parse( "select sum(foo.foo.int) from foo in class org.hibernate.test.Foo", benchmarkState );
      bh.consume( result );
      result = parse( "select count(foo) from foo in class org.hibernate.test.Foo where foo.id=?", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo where foo.boolean = ?", benchmarkState );
      bh.consume( result );
      result = parse( "select new Foo(fo.x) from org.hibernate.test.Fo fo", benchmarkState );
      bh.consume( result );
      result = parse( "select new Foo(fo.integer) from org.hibernate.test.Foo fo", benchmarkState );
      bh.consume( result );
      result = parse( "select new Foo(fo.x) from org.hibernate.test.Foo fo", benchmarkState );
      bh.consume( result );
      result = parse( "select foo.long, foo.component.name, foo, foo.foo from foo in class org.hibernate.test.Foo", benchmarkState );
      bh.consume( result );
      result = parse( "select avg(foo.float), max(foo.component.name), count(distinct foo.id) from foo in class org.hibernate.test.Foo", benchmarkState );
      bh.consume( result );
      result = parse( "select foo.long, foo.component, foo, foo.foo from foo in class org.hibernate.test.Foo", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.MoreStuff", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Many", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Fee", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Qux", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Y", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Fumm", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.X", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Simple", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Location", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Holder", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Part", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Baz", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Vetoer", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Sortable", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Contained", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Stuff", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Immutable", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Container", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.X$XX", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.One", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Foo", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Fo", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Glarch", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Fum", benchmarkState );
      bh.consume( result );
      result = parse( "from n in class org.hibernate.test.Holder", benchmarkState );
      bh.consume( result );
      result = parse( "from n in class org.hibernate.test.Baz", benchmarkState );
      bh.consume( result );
      result = parse( "from n in class org.hibernate.test.Bar", benchmarkState );
      bh.consume( result );
      result = parse( "from n in class org.hibernate.test.Glarch", benchmarkState );
      bh.consume( result );
      result = parse( "from n in class org.hibernate.test.Holder where n.name is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from n in class org.hibernate.test.Baz where n.name is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from n in class org.hibernate.test.Bar where n.name is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from n in class org.hibernate.test.Glarch where n.name is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from n in class org.hibernate.test.Holder", benchmarkState );
      bh.consume( result );
      result = parse( "from n in class org.hibernate.test.Baz", benchmarkState );
      bh.consume( result );
      result = parse( "from n in class org.hibernate.test.Bar", benchmarkState );
      bh.consume( result );
      result = parse( "from n in class org.hibernate.test.Glarch", benchmarkState );
      bh.consume( result );
      result = parse( "from n0 in class org.hibernate.test.Holder, n1 in class org.hibernate.test.Holder where n0.name = n1.name", benchmarkState );
      bh.consume( result );
      result = parse( "from n0 in class org.hibernate.test.Baz, n1 in class org.hibernate.test.Holder where n0.name = n1.name", benchmarkState );
      bh.consume( result );
      result = parse( "from n0 in class org.hibernate.test.Bar, n1 in class org.hibernate.test.Holder where n0.name = n1.name", benchmarkState );
      bh.consume( result );
      result = parse( "from n0 in class org.hibernate.test.Glarch, n1 in class org.hibernate.test.Holder where n0.name = n1.name", benchmarkState );
      bh.consume( result );
      result = parse( "from n0 in class org.hibernate.test.Holder, n1 in class org.hibernate.test.Baz where n0.name = n1.name", benchmarkState );
      bh.consume( result );
      result = parse( "from n0 in class org.hibernate.test.Baz, n1 in class org.hibernate.test.Baz where n0.name = n1.name", benchmarkState );
      bh.consume( result );
      result = parse( "from n0 in class org.hibernate.test.Bar, n1 in class org.hibernate.test.Baz where n0.name = n1.name", benchmarkState );
      bh.consume( result );
      result = parse( "from n0 in class org.hibernate.test.Glarch, n1 in class org.hibernate.test.Baz where n0.name = n1.name", benchmarkState );
      bh.consume( result );
      result = parse( "from n0 in class org.hibernate.test.Holder, n1 in class org.hibernate.test.Bar where n0.name = n1.name", benchmarkState );
      bh.consume( result );
      result = parse( "from n0 in class org.hibernate.test.Baz, n1 in class org.hibernate.test.Bar where n0.name = n1.name", benchmarkState );
      bh.consume( result );
      result = parse( "from n0 in class org.hibernate.test.Bar, n1 in class org.hibernate.test.Bar where n0.name = n1.name", benchmarkState );
      bh.consume( result );
      result = parse( "from n0 in class org.hibernate.test.Glarch, n1 in class org.hibernate.test.Bar where n0.name = n1.name", benchmarkState );
      bh.consume( result );
      result = parse( "from n0 in class org.hibernate.test.Holder, n1 in class org.hibernate.test.Glarch where n0.name = n1.name", benchmarkState );
      bh.consume( result );
      result = parse( "from n0 in class org.hibernate.test.Baz, n1 in class org.hibernate.test.Glarch where n0.name = n1.name", benchmarkState );
      bh.consume( result );
      result = parse( "from n0 in class org.hibernate.test.Bar, n1 in class org.hibernate.test.Glarch where n0.name = n1.name", benchmarkState );
      bh.consume( result );
      result = parse( "from n0 in class org.hibernate.test.Glarch, n1 in class org.hibernate.test.Glarch where n0.name = n1.name", benchmarkState );
      bh.consume( result );
      result = parse( "from n in class org.hibernate.test.Holder where n.name = :name", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.MoreStuff", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Many", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Fee", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Qux", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Y", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Fumm", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.X", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Simple", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Location", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Holder", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Part", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Baz", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Vetoer", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Sortable", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Contained", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Stuff", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Immutable", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Container", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.X$XX", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.One", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Foo", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Fo", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Glarch", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Fum", benchmarkState );
      bh.consume( result );
      result = parse( "select baz.code, min(baz.count) from baz in class org.hibernate.test.Baz group by baz.code", benchmarkState );
      bh.consume( result );
      result = parse( "selecT baz from baz in class org.hibernate.test.Baz where baz.stringDateMap['foo'] is not null or baz.stringDateMap['bar'] = ?", benchmarkState );
      bh.consume( result );
      result = parse( "select baz from baz in class org.hibernate.test.Baz where baz.stringDateMap['now'] is not null", benchmarkState );
      bh.consume( result );
      result = parse( "select baz from baz in class org.hibernate.test.Baz where baz.stringDateMap['now'] is not null and baz.stringDateMap['big bang'] < baz.stringDateMap['now']", benchmarkState );
      bh.consume( result );
      result = parse( "select index(date) from org.hibernate.test.Baz baz join baz.stringDateMap date", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo where foo.integer not between 1 and 5 and foo.string not in ('cde', 'abc') and foo.string is not null and foo.integer<=3", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz inner join baz.collectionComponent.nested.foos foo where foo.string is null", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz inner join baz.fooSet where '1' in (from baz.fooSet foo where foo.string is not null)", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz where 'a' in elements(baz.collectionComponent.nested.foos) and 1.0 in elements(baz.collectionComponent.nested.floats)", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Foo foo join foo.foo where foo.foo in ('1','2','3')", benchmarkState );
      bh.consume( result );
      result = parse( "select foo.foo from org.hibernate.test.Foo foo where foo.foo in ('1','2','3')", benchmarkState );
      bh.consume( result );
      result = parse( "select foo.foo.string from org.hibernate.test.Foo foo where foo.foo in ('1','2','3')", benchmarkState );
      bh.consume( result );
      result = parse( "select foo.foo.string from org.hibernate.test.Foo foo where foo.foo.string in ('1','2','3')", benchmarkState );
      bh.consume( result );
      result = parse( "select foo.foo.long from org.hibernate.test.Foo foo where foo.foo.string in ('1','2','3')", benchmarkState );
      bh.consume( result );
      result = parse( "select count(*) from org.hibernate.test.Foo foo where foo.foo.string in ('1','2','3') or foo.foo.long in (1,2,3)", benchmarkState );
      bh.consume( result );
      result = parse( "select count(*) from org.hibernate.test.Foo foo where foo.foo.string in ('1','2','3') group by foo.foo.long", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Foo foo1 left join foo1.foo foo2 left join foo2.foo where foo1.string is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Foo foo1 left join foo1.foo.foo where foo1.string is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Foo foo1 left join foo1.foo foo2 left join foo1.foo.foo foo3 where foo1.string is not null", benchmarkState );
      bh.consume( result );
      result = parse( "select foo.formula from org.hibernate.test.Foo foo where foo.formula > 0", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Foo as foo join foo.foo as foo2 where foo2.id >'a' or foo2.id <'a'", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Holder", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz left outer join fetch baz.manyToAny", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz join baz.manyToAny", benchmarkState );
      bh.consume( result );
      result = parse( "select baz from org.hibernate.test.Baz baz join baz.manyToAny a where index(a) = 0", benchmarkState );
      bh.consume( result );
      result = parse( "select bar from org.hibernate.test.Bar bar where bar.baz.stringDateMap['now'] is not null", benchmarkState );
      bh.consume( result );
      result = parse( "select bar from org.hibernate.test.Bar bar join bar.baz b where b.stringDateMap['big bang'] < b.stringDateMap['now'] and b.stringDateMap['now'] is not null", benchmarkState );
      bh.consume( result );
      result = parse( "select bar from org.hibernate.test.Bar bar where bar.baz.stringDateMap['big bang'] < bar.baz.stringDateMap['now'] and bar.baz.stringDateMap['now'] is not null", benchmarkState );
      bh.consume( result );
      result = parse( "select foo.string, foo.component, foo.id from org.hibernate.test.Bar foo", benchmarkState );
      bh.consume( result );
      result = parse( "select elements(baz.components) from org.hibernate.test.Baz baz", benchmarkState );
      bh.consume( result );
      result = parse( "select bc.name from org.hibernate.test.Baz baz join baz.components bc", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Foo foo where foo.integer < 10 order by foo.string", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Fee", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Holder h join h.otherHolder oh where h.otherHolder.name = 'bar'", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz join baz.fooSet foo join foo.foo.foo foo2 where foo2.string = 'foo'", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz join baz.fooArray foo join foo.foo.foo foo2 where foo2.string = 'foo'", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz join baz.stringDateMap date where index(date) = 'foo'", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz join baz.topGlarchez g where index(g) = 'A'", benchmarkState );
      bh.consume( result );
      result = parse( "select index(g) from org.hibernate.test.Baz baz join baz.topGlarchez g", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz left join baz.stringSet", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz join baz.stringSet str where str='foo'", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz left join fetch baz.stringSet", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz join baz.stringSet string where string='foo'", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz inner join baz.components comp where comp.name='foo'", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Glarch g inner join g.fooComponents comp where comp.fee is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Glarch g inner join g.fooComponents comp join comp.fee fee where fee.count > 0", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Glarch g inner join g.fooComponents comp where comp.fee.count is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz left join fetch baz.fooBag", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Glarch", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Fee", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz left join fetch baz.sortablez order by baz.name asc", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz order by baz.name asc", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Foo foo, org.hibernate.test.Baz baz left join fetch baz.fees", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Foo foo, org.hibernate.test.Bar bar", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Foo foo", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Foo foo, org.hibernate.test.Bar bar, org.hibernate.test.Bar bar2", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.X x", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Foo foo", benchmarkState );
      bh.consume( result );
      result = parse( "select distinct foo from org.hibernate.test.Foo foo", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Glarch g where g.multiple.glarch=g and g.multiple.count=12", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Bar bar left join bar.baz baz left join baz.cascadingBars b where bar.name like 'Bar %'", benchmarkState );
      bh.consume( result );
      result = parse( "select bar, b from org.hibernate.test.Bar bar left join bar.baz baz left join baz.cascadingBars b where bar.name like 'Bar%'", benchmarkState );
      bh.consume( result );
      result = parse( "select bar, b from org.hibernate.test.Bar bar left join bar.baz baz left join baz.cascadingBars b where ( bar.name in (:nameList0_, :nameList1_, :nameList2_) or bar.name in (:nameList0_, :nameList1_, :nameList2_) ) and bar.string = :stringVal", benchmarkState );
      bh.consume( result );
      result = parse( "select bar, b from org.hibernate.test.Bar bar inner join bar.baz baz inner join baz.cascadingBars b where bar.name like 'Bar%'", benchmarkState );
      bh.consume( result );
      result = parse( "select bar, b from org.hibernate.test.Bar bar left join bar.baz baz left join baz.cascadingBars b where bar.name like :name and b.name like :name", benchmarkState );
      bh.consume( result );
      result = parse( "select bar from org.hibernate.test.Bar as bar where bar.x > ? or bar.short = 1 or bar.string = 'ff ? bb'", benchmarkState );
      bh.consume( result );
      result = parse( "select bar from org.hibernate.test.Bar as bar where bar.string = ' ? ' or bar.string = '?'", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz, baz.fooArray foo", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Stuff where s.foo.id = ? and s.id.id = ? and s.moreStuff.id.intId = ? and s.moreStuff.id.stringId = ?", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Stuff where s.foo.id = ? and s.id.id = ? and s.moreStuff.name = ?", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Stuff where s.foo.string is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Stuff where s.foo > '0' order by s.foo", benchmarkState );
      bh.consume( result );
      result = parse( "from ms in class org.hibernate.test.MoreStuff", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo", benchmarkState );
      bh.consume( result );
      result = parse( "from fee in class org.hibernate.test.Fee", benchmarkState );
      bh.consume( result );
      result = parse( "select new Result(foo.string, foo.long, foo.integer) from foo in class org.hibernate.test.Foo", benchmarkState );
      bh.consume( result );
      result = parse( "select new Result( baz.name, foo.long, count(elements(baz.fooArray)) ) from org.hibernate.test.Baz baz join baz.fooArray foo group by baz.name, foo.long", benchmarkState );
      bh.consume( result );
      result = parse( "select new Result( baz.name, max(foo.long), count(foo) ) from org.hibernate.test.Baz baz join baz.fooArray foo group by baz.name", benchmarkState );
      bh.consume( result );
      result = parse( "select max( elements(bar.baz.fooArray) ) from org.hibernate.test.Bar as bar", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz left join baz.fooToGlarch join fetch baz.fooArray foo left join fetch foo.foo", benchmarkState );
      bh.consume( result );
      result = parse( "select baz.name from org.hibernate.test.Bar bar inner join bar.baz baz inner join baz.fooSet foo where baz.name = bar.string", benchmarkState );
      bh.consume( result );
      result = parse( "SELECT baz.name FROM org.hibernate.test.Bar AS bar INNER JOIN bar.baz AS baz INNER JOIN baz.fooSet AS foo WHERE baz.name = bar.string", benchmarkState );
      bh.consume( result );
      result = parse( "select baz.name from org.hibernate.test.Bar bar join bar.baz baz left outer join baz.fooSet foo where baz.name = bar.string", benchmarkState );
      bh.consume( result );
      result = parse( "select baz.name from org.hibernate.test.Bar bar, bar.baz baz, baz.fooSet foo where baz.name = bar.string", benchmarkState );
      bh.consume( result );
      result = parse( "SELECT baz.name FROM org.hibernate.test.Bar AS bar, bar.baz AS baz, baz.fooSet AS foo WHERE baz.name = bar.string", benchmarkState );
      bh.consume( result );
      result = parse( "select baz.name from org.hibernate.test.Bar bar left join bar.baz baz left join baz.fooSet foo where baz.name = bar.string", benchmarkState );
      bh.consume( result );
      result = parse( "select foo.string from org.hibernate.test.Bar bar left join bar.baz.fooSet foo where bar.string = foo.string", benchmarkState );
      bh.consume( result );
      result = parse( "select baz.name from org.hibernate.test.Bar bar left join bar.baz baz left join baz.fooArray foo where baz.name = bar.string", benchmarkState );
      bh.consume( result );
      result = parse( "select foo.string from org.hibernate.test.Bar bar left join bar.baz.fooArray foo where bar.string = foo.string", benchmarkState );
      bh.consume( result );
      result = parse( "select foo from bar in class org.hibernate.test.Bar inner join bar.baz as baz inner join baz.fooSet as foo", benchmarkState );
      bh.consume( result );
      result = parse( "select foo from bar in class org.hibernate.test.Bar inner join bar.baz.fooSet as foo", benchmarkState );
      bh.consume( result );
      result = parse( "select foo from bar in class org.hibernate.test.Bar, bar.baz as baz, baz.fooSet as foo", benchmarkState );
      bh.consume( result );
      result = parse( "select foo from bar in class org.hibernate.test.Bar, bar.baz.fooSet as foo", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Bar bar join bar.baz.fooArray foo", benchmarkState );
      bh.consume( result );
      result = parse( "from bar in class org.hibernate.test.Bar, foo in elements( bar.baz.fooArray )", benchmarkState );
      bh.consume( result );
      result = parse( "select one.id, elements(one.manies) from one in class org.hibernate.test.One", benchmarkState );
      bh.consume( result );
      result = parse( "select max( elements(one.manies) ) from one in class org.hibernate.test.One", benchmarkState );
      bh.consume( result );
      result = parse( "select one, elements(one.manies) from one in class org.hibernate.test.One", benchmarkState );
      bh.consume( result );
      result = parse( "select one, max(elements(one.manies)) from one in class org.hibernate.test.One group by one", benchmarkState );
      bh.consume( result );
      result = parse( "select elements(baz.fooArray) from baz in class org.hibernate.test.Baz where baz.id=?", benchmarkState );
      bh.consume( result );
      result = parse( "select elements(baz.fooArray) from baz in class org.hibernate.test.Baz where baz.id=?", benchmarkState );
      bh.consume( result );
      result = parse( "select indices(baz.fooArray) from baz in class org.hibernate.test.Baz where baz.id=?", benchmarkState );
      bh.consume( result );
      result = parse( "select baz, max(elements(baz.timeArray)) from baz in class org.hibernate.test.Baz group by baz", benchmarkState );
      bh.consume( result );
      result = parse( "select baz, baz.stringSet.size, count(distinct elements(baz.stringSet)), max(elements(baz.stringSet)) from baz in class org.hibernate.test.Baz group by baz", benchmarkState );
      bh.consume( result );
      result = parse( "select max( elements(baz.timeArray) ) from baz in class org.hibernate.test.Baz where baz.id=?", benchmarkState );
      bh.consume( result );
      result = parse( "select max(elements(baz.stringSet)) from baz in class org.hibernate.test.Baz where baz.id=?", benchmarkState );
      bh.consume( result );
      result = parse( "select size(baz.stringSet) from baz in class org.hibernate.test.Baz where baz.id=?", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Foo foo where foo.component.glarch.id is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from baz in class org.hibernate.test.Baz", benchmarkState );
      bh.consume( result );
      result = parse( "select elements(baz.stringArray) from baz in class org.hibernate.test.Baz", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo", benchmarkState );
      bh.consume( result );
      result = parse( "select elements(baz.stringList) from baz in class org.hibernate.test.Baz", benchmarkState );
      bh.consume( result );
      result = parse( "select count(*) from org.hibernate.test.Bar", benchmarkState );
      bh.consume( result );
      result = parse( "select count(*) from b in class org.hibernate.test.Bar", benchmarkState );
      bh.consume( result );
      result = parse( "from g in class org.hibernate.test.Glarch", benchmarkState );
      bh.consume( result );
      result = parse( "select baz, baz from baz in class org.hibernate.test.Baz", benchmarkState );
      bh.consume( result );
      result = parse( "select baz from baz in class org.hibernate.test.Baz order by baz", benchmarkState );
      bh.consume( result );
      result = parse( "from bar in class org.hibernate.test.Bar", benchmarkState );
      bh.consume( result );
      result = parse( "from g in class org.hibernate.test.Glarch", benchmarkState );
      bh.consume( result );
      result = parse( "from f in class org.hibernate.test.Foo", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.One", benchmarkState );
      bh.consume( result );
      result = parse( "from q in class org.hibernate.test.Qux", benchmarkState );
      bh.consume( result );
      result = parse( "select foo from foo in class org.hibernate.test.Foo where foo.string='foo bar'", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo order by foo.string, foo.date", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo where foo.class='B'", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo where foo.class=Bar", benchmarkState );
      bh.consume( result );
      result = parse( "select bar from bar in class org.hibernate.test.Bar, foo in class org.hibernate.test.Foo where bar.string = foo.string and not bar=foo", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo where foo.string='foo bar'", benchmarkState );
      bh.consume( result );
      result = parse( "select foo from foo in class org.hibernate.test.Foo", benchmarkState );
      bh.consume( result );
      result = parse( "from bar in class org.hibernate.test.Bar where bar.barString='bar bar'", benchmarkState );
      bh.consume( result );
      result = parse( "from t in class org.hibernate.test.Trivial", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo where foo.date = ?", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.MoreStuff", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Many", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Fee", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Qux", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Y", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Fumm", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.X", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Simple", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Location", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Holder", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Part", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Baz", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Vetoer", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Sortable", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Contained", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Stuff", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Immutable", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Container", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.X$XX", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.One", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Foo", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Fo", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Glarch", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Fum", benchmarkState );
      bh.consume( result );
      result = parse( "from q in class org.hibernate.test.Qux where q.stuff is null", benchmarkState );
      bh.consume( result );
      result = parse( "from q in class org.hibernate.test.Qux where q.stuff=?", benchmarkState );
      bh.consume( result );
      result = parse( "from q in class org.hibernate.test.Qux", benchmarkState );
      bh.consume( result );
      result = parse( "from g in class org.hibernate.test.Glarch where g.version=2", benchmarkState );
      bh.consume( result );
      result = parse( "from g in class org.hibernate.test.Glarch where g.next is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from g in class org.hibernate.test.Glarch order by g.order asc", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo order by foo.string asc", benchmarkState );
      bh.consume( result );
      result = parse( "select parent, child from parent in class org.hibernate.test.Foo, child in class org.hibernate.test.Foo where parent.foo = child", benchmarkState );
      bh.consume( result );
      result = parse( "select count(distinct child.id), count(distinct parent.id) from parent in class org.hibernate.test.Foo, child in class org.hibernate.test.Foo where parent.foo = child", benchmarkState );
      bh.consume( result );
      result = parse( "select child.id, parent.id, child.long from parent in class org.hibernate.test.Foo, child in class org.hibernate.test.Foo where parent.foo = child", benchmarkState );
      bh.consume( result );
      result = parse( "select child.id, parent.id, child.long, child, parent.foo from parent in class org.hibernate.test.Foo, child in class org.hibernate.test.Foo where parent.foo = child", benchmarkState );
      bh.consume( result );
      result = parse( "select parent, child from parent in class org.hibernate.test.Foo, child in class org.hibernate.test.Foo where parent.foo = child and parent.string='a string'", benchmarkState );
      bh.consume( result );
      result = parse( "from fee in class org.hibernate.test.Fee", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Foo foo where foo.custom.s1 = 'one'", benchmarkState );
      bh.consume( result );
      result = parse( "from im in class org.hibernate.test.Immutable where im = ?", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo where foo.char='X'", benchmarkState );
      bh.consume( result );
      result = parse( "select elements(baz.stringArray) from baz in class org.hibernate.test.Baz", benchmarkState );
      bh.consume( result );
      result = parse( "select distinct elements(baz.stringArray) from baz in class org.hibernate.test.Baz", benchmarkState );
      bh.consume( result );
      result = parse( "select elements(baz.fooArray) from baz in class org.hibernate.test.Baz", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Fo", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo where foo.dependent.qux.foo.string = 'foo2'", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Bar bar where bar.object.id = ? and bar.object.class = ?", benchmarkState );
      bh.consume( result );
      result = parse( "select one from org.hibernate.test.One one, org.hibernate.test.Bar bar where bar.object.id = one.id and bar.object.class = 'O'", benchmarkState );
      bh.consume( result );
      result = parse( "from l in class org.hibernate.test.Location where l.countryCode = 'AU' and l.description='foo bar'", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Bar bar", benchmarkState );
      bh.consume( result );
      result = parse( "From org.hibernate.test.Bar bar", benchmarkState );
      bh.consume( result );
      result = parse( "From org.hibernate.test.Foo foo", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Baz", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Foo", benchmarkState );
      bh.consume( result );
      result = parse( "from f in class org.hibernate.test.Foo", benchmarkState );
      bh.consume( result );
      result = parse( "select fum.id from fum in class org.hibernate.test.Fum where not fum.fum='FRIEND'", benchmarkState );
      bh.consume( result );
      result = parse( "select fum.id from fum in class org.hibernate.test.Fum where not fum.fum='FRIEND'", benchmarkState );
      bh.consume( result );
      result = parse( "from fum in class org.hibernate.test.Fum where not fum.fum='FRIEND'", benchmarkState );
      bh.consume( result );
      result = parse( "from fo in class org.hibernate.test.Fo where fo.id.string like 'an instance of fo'", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Inner", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Outer o where o.id.detailId = ?", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Outer o where o.id.master.id.sup.dudu is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Outer o where o.id.master.id.sup.id.akey is not null", benchmarkState );
      bh.consume( result );
      result = parse( "select o.id.master.id.sup.dudu from org.hibernate.test.Outer o where o.id.master.id.sup.dudu is not null", benchmarkState );
      bh.consume( result );
      result = parse( "select o.id.master.id.sup.id.akey from org.hibernate.test.Outer o where o.id.master.id.sup.id.akey is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Outer o where o.id.master.bla = ''", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Outer o where o.id.master.id.one = ''", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Inner inn where inn.id.bkey is not null and inn.backOut.id.master.id.sup.id.akey > 'a'", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Outer as o left join o.id.master m left join m.id.sup where o.bubu is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Outer as o left join o.id.master.id.sup s where o.bubu is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Outer as o left join o.id.master m left join o.id.master.id.sup s where o.bubu is not null", benchmarkState );
      bh.consume( result );
      result = parse( "select fum1.fo from fum1 in class org.hibernate.test.Fum where fum1.fo.fum is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from fum1 in class org.hibernate.test.Fum where fum1.fo.fum is not null order by fum1.fo.fum", benchmarkState );
      bh.consume( result );
      result = parse( "select elements(fum1.friends) from fum1 in class org.hibernate.test.Fum", benchmarkState );
      bh.consume( result );
      result = parse( "from fum1 in class org.hibernate.test.Fum, fr in elements( fum1.friends )", benchmarkState );
      bh.consume( result );
      result = parse( "select new Jay(eye) from org.hibernate.test.Eye eye", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Category cat where cat.name='new foo'", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Category cat where cat.name='new sub'", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Up up order by up.id2 asc", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Down down", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Up up", benchmarkState );
      bh.consume( result );
      result = parse( "from m in class org.hibernate.test.Master", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Several", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Single", benchmarkState );
      bh.consume( result );
      result = parse( "\n" +
         "		from d in class \n" +
         "			org.hibernate.test.Detail\n" +
         "	", benchmarkState );
      bh.consume( result );
      result = parse( "from c in class org.hibernate.test.Category where c.name = org.hibernate.test.Category.ROOT_CATEGORY", benchmarkState );
      bh.consume( result );
      result = parse( "select c from c in class org.hibernate.test.Container, s in class org.hibernate.test.Simple where c.oneToMany[2] = s", benchmarkState );
      bh.consume( result );
      result = parse( "select c from c in class org.hibernate.test.Container, s in class org.hibernate.test.Simple where c.manyToMany[2] = s", benchmarkState );
      bh.consume( result );
      result = parse( "select c from c in class org.hibernate.test.Container, s in class org.hibernate.test.Simple where s = c.oneToMany[2]", benchmarkState );
      bh.consume( result );
      result = parse( "select c from c in class org.hibernate.test.Container, s in class org.hibernate.test.Simple where s = c.manyToMany[2]", benchmarkState );
      bh.consume( result );
      result = parse( "select c from c in class org.hibernate.test.Container where c.oneToMany[0].name = 's'", benchmarkState );
      bh.consume( result );
      result = parse( "select c from c in class org.hibernate.test.Container where c.manyToMany[0].name = 's'", benchmarkState );
      bh.consume( result );
      result = parse( "select c from c in class org.hibernate.test.Container where 's' = c.oneToMany[2 - 2].name", benchmarkState );
      bh.consume( result );
      result = parse( "select c from c in class org.hibernate.test.Container where 's' = c.manyToMany[(3+1)/4-1].name", benchmarkState );
      bh.consume( result );
      result = parse( "select c from c in class org.hibernate.test.Container where c.manyToMany[ maxindex(c.manyToMany) ].count = 2", benchmarkState );
      bh.consume( result );
      result = parse( "select c from c in class org.hibernate.test.Container where c.oneToMany[ c.manyToMany[0].count ].name = 's'", benchmarkState );
      bh.consume( result );
      result = parse( "select c from org.hibernate.test.Container c where c.manyToMany[ c.oneToMany[0].count ].name = 's'", benchmarkState );
      bh.consume( result );
      result = parse( "select count(comp.name) from org.hibernate.test.Container c join c.components comp", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Parent p left join fetch p.child", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Parent p join p.child c where c.x > 0", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Child c join c.parent p where p.x > 0", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Child", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.MoreStuff", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Many", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Fee", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Qux", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Fumm", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Parent", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Simple", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Holder", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Part", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Vetoer", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Sortable", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Contained", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Circular", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Stuff", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Immutable", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Container", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.One", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Foo", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Fo", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Glarch", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Fum", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Glarch g", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Part", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz join baz.parts", benchmarkState );
      bh.consume( result );
      result = parse( "from c in class org.hibernate.test.Child where c.parent.count=66", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Parent p join p.child c where p.count=66", benchmarkState );
      bh.consume( result );
      result = parse( "select c, c.parent from c in class org.hibernate.test.Child order by c.parent.count", benchmarkState );
      bh.consume( result );
      result = parse( "select c, c.parent from c in class org.hibernate.test.Child where c.parent.count=66 order by c.parent.count", benchmarkState );
      bh.consume( result );
      result = parse( "select c, c.parent, c.parent.count from c in class org.hibernate.test.Child order by c.parent.count", benchmarkState );
      bh.consume( result );
      result = parse( "FROM p IN CLASS org.hibernate.test.Parent WHERE p.count = ?", benchmarkState );
      bh.consume( result );
      result = parse( "select count(*) from org.hibernate.test.Container as c join c.components as ce join ce.simple as s where ce.name='foo'", benchmarkState );
      bh.consume( result );
      result = parse( "select c, s from org.hibernate.test.Container as c join c.components as ce join ce.simple as s where ce.name='foo'", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Simple", benchmarkState );
      bh.consume( result );
      result = parse( "from m in class org.hibernate.test.Many", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.One", benchmarkState );
      bh.consume( result );
      result = parse( "from c in class org.hibernate.test.Container", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Child", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.MoreStuff", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Many", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Fee", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Qux", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Fumm", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Parent", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Simple", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Holder", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Part", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Baz", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Vetoer", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Sortable", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Contained", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Circular", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Stuff", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Immutable", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Container", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.One", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Foo", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Fo", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Glarch", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Fum", benchmarkState );
      bh.consume( result );
      result = parse( "from c in class org.hibernate.test.C2 where 1=1 or 1=1", benchmarkState );
      bh.consume( result );
      result = parse( "from b in class org.hibernate.test.B", benchmarkState );
      bh.consume( result );
      result = parse( "from a in class org.hibernate.test.A", benchmarkState );
      bh.consume( result );
      result = parse( "from b in class org.hibernate.test.B", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.E e join e.reverse as b where b.count=1", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.E e join e.as as b where b.count=1", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.B", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.C1", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.C2", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.E e, org.hibernate.test.A a where e.reverse = a.forward and a = ?", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.E e join fetch e.reverse", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.E e", benchmarkState );
      bh.consume( result );
      result = parse( "select max(s.count) from s in class org.hibernate.test.Simple", benchmarkState );
      bh.consume( result );
      result = parse( "select new org.hibernate.test.S(s.count, s.address) from s in class org.hibernate.test.Simple", benchmarkState );
      bh.consume( result );
      result = parse( "select max(s.count) from s in class org.hibernate.test.Simple", benchmarkState );
      bh.consume( result );
      result = parse( "select count(*) from s in class org.hibernate.test.Simple", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Simple where s.name=:name and s.count=:count", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Simple where s.name in (:several0_, :several1_)", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Simple where s.name in (:stuff0_, :stuff1_)", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Simple s where s.name=?", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Simple s where s.name=:name", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Simple where upper( s.name ) ='SIMPLE 1'", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Simple where not( upper( s.name ) ='yada' or 1=2 or 'foo'='bar' or not('foo'='foo') or 'foo' like 'bar' )", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Simple where lower( s.name || ' foo' ) ='simple 1 foo'", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Simple where upper( s.other.name ) ='SIMPLE 2'", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Simple where not ( upper( s.other.name ) ='SIMPLE 2' )", benchmarkState );
      bh.consume( result );
      result = parse( "select distinct s from s in class org.hibernate.test.Simple where ( ( s.other.count + 3 ) = (15*2)/2 and s.count = 69) or ( ( s.other.count + 2 ) / 7 ) = 2", benchmarkState );
      bh.consume( result );
      result = parse( "select s from s in class org.hibernate.test.Simple where ( ( s.other.count + 3 ) = (15*2)/2 and s.count = 69) or ( ( s.other.count + 2 ) / 7 ) = 2 order by s.other.count", benchmarkState );
      bh.consume( result );
      result = parse( "select sum(s.count) from s in class org.hibernate.test.Simple group by s.count having sum(s.count) > 10", benchmarkState );
      bh.consume( result );
      result = parse( "select s.count from s in class org.hibernate.test.Simple group by s.count having s.count = 12", benchmarkState );
      bh.consume( result );
      result = parse( "select s.id, s.count, count(t), max(t.date) from s in class org.hibernate.test.Simple, t in class org.hibernate.test.Simple where s.count = t.count group by s.id, s.count order by s.count", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Simple", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Simple where s.name = ?", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Simple where s.name = ? and upper(s.name) = ?", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Simple where s.name = :foo and upper(s.name) = :bar or s.count=:count or s.count=:count + 1", benchmarkState );
      bh.consume( result );
      result = parse( "select s.id from s in class org.hibernate.test.Simple", benchmarkState );
      bh.consume( result );
      result = parse( "select all s, s.other from s in class org.hibernate.test.Simple where s = :s", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Simple where s.name in (:name_list0_, :name_list1_) and s.count > :count", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Simple s", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Simple s", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Assignable", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Category", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Simple", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.A", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo where foo.string=?", benchmarkState );
      bh.consume( result );
      result = parse( "from foo in class org.hibernate.test.Foo", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Po po, org.hibernate.test.Lower low where low.mypo = po", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Po po join po.set as sm where sm.amount > 0", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Po po join po.top as low where low.foo = 'po'", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.SubMulti sm join sm.children smc where smc.name > 'a'", benchmarkState );
      bh.consume( result );
      result = parse( "select s, ya from org.hibernate.test.Lower s join s.yetanother ya", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Lower s1 join s1.bag s2", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Lower s1 left join s1.bag s2", benchmarkState );
      bh.consume( result );
      result = parse( "select s, a from org.hibernate.test.Lower s join s.another a", benchmarkState );
      bh.consume( result );
      result = parse( "select s, a from org.hibernate.test.Lower s left join s.another a", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Top s, org.hibernate.test.Lower ls", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Lower ls join ls.set s where s.name > 'a'", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Po po join po.list sm where sm.name > 'a'", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Lower ls inner join ls.another s where s.name is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Lower ls where ls.other.another.name is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Multi m where m.derived like 'F%'", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.SubMulti m where m.derived like 'F%'", benchmarkState );
      bh.consume( result );
      result = parse( "select s from org.hibernate.test.SubMulti as sm join sm.children as s where s.amount>-1 and s.name is null", benchmarkState );
      bh.consume( result );
      result = parse( "select elements(sm.children) from org.hibernate.test.SubMulti as sm", benchmarkState );
      bh.consume( result );
      result = parse( "select distinct sm from org.hibernate.test.SubMulti as sm join sm.children as s where s.amount>-1 and s.name is null", benchmarkState );
      bh.consume( result );
      result = parse( "select distinct s from s in class org.hibernate.test.SubMulti where s.moreChildren[1].amount < 1.0", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.TrivialClass where s.id = 2", benchmarkState );
      bh.consume( result );
      result = parse( "select s.count from s in class org.hibernate.test.Top", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Lower where s.another.name='name'", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Lower where s.yetanother.name='name'", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Lower where s.yetanother.name='name' and s.yetanother.foo is null", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Top where s.count=1", benchmarkState );
      bh.consume( result );
      result = parse( "select s.count from s in class org.hibernate.test.Top, ls in class org.hibernate.test.Lower where ls.another=s", benchmarkState );
      bh.consume( result );
      result = parse( "select elements(ls.bag), elements(ls.set) from ls in class org.hibernate.test.Lower", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Lower", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Top", benchmarkState );
      bh.consume( result );
      result = parse( "from sm in class org.hibernate.test.SubMulti", benchmarkState );
      bh.consume( result );
      result = parse( "select\n" +
         "\n" +
         "s from s in class org.hibernate.test.Top where s.count>0", benchmarkState );
      bh.consume( result );
      result = parse( "from m in class org.hibernate.test.Multi where m.count>0 and m.extraProp is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from m in class org.hibernate.test.Top where m.count>0 and m.name is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from m in class org.hibernate.test.Lower where m.other is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from m in class org.hibernate.test.Multi where m.other.id = 1", benchmarkState );
      bh.consume( result );
      result = parse( "from m in class org.hibernate.test.SubMulti where m.amount > 0.0", benchmarkState );
      bh.consume( result );
      result = parse( "from m in class org.hibernate.test.Multi", benchmarkState );
      bh.consume( result );
      result = parse( "from m in class org.hibernate.test.Multi where m.class = SubMulti", benchmarkState );
      bh.consume( result );
      result = parse( "from m in class org.hibernate.test.Top where m.class = Multi", benchmarkState );
      bh.consume( result );
      result = parse( "from s in class org.hibernate.test.Top", benchmarkState );
      bh.consume( result );
      result = parse( "from ls in class org.hibernate.test.Lower", benchmarkState );
      bh.consume( result );
      result = parse( "from ls in class org.hibernate.test.Lower, s in elements(ls.bag) where s.id is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from ls in class org.hibernate.test.Lower, s in elements(ls.set) where s.id is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Top", benchmarkState );
      bh.consume( result );
      result = parse( "from o in class org.hibernate.test.Po", benchmarkState );
      bh.consume( result );
      result = parse( "from ChildMap cm where cm.parent is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from ParentMap cm where cm.child is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Componentizable", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testUnnamedParameter(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "select foo, bar from org.hibernate.test.Foo foo left outer join foo.foo bar where foo = ?", benchmarkState );
      bh.consume( result ); // Added '?' as a valid expression.
   }

   @Benchmark
   public void testInElements(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "from bar in class org.hibernate.test.Bar, foo in elements(bar.baz.fooArray)", benchmarkState );
      bh.consume( result );   // Added collectionExpr as a valid 'in' clause.
   }

   @Benchmark
   public void testDotElements(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "select distinct foo from baz in class org.hibernate.test.Baz, foo in elements(baz.fooArray)", benchmarkState );
      bh.consume( result );
      result = parse( "select foo from baz in class org.hibernate.test.Baz, foo in elements(baz.fooSet)", benchmarkState );
      bh.consume( result );
      result = parse( "select foo from baz in class org.hibernate.test.Baz, foo in elements(baz.fooArray)", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Baz baz where 'b' in elements(baz.collectionComponent.nested.foos) and 1.0 in elements(baz.collectionComponent.nested.floats)", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testSelectAll(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "select all s, s.other from s in class org.hibernate.test.Simple where s = :s", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testNot(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "from eg.Cat cat where not ( cat.kittens.size < 1 )", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.Cat cat where not ( cat.kittens.size > 1 )", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.Cat cat where not ( cat.kittens.size >= 1 )", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.Cat cat where not ( cat.kittens.size <= 1 )", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.DomesticCat cat where not ( cat.name between 'A' and 'B' ) ", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.DomesticCat cat where not ( cat.name not between 'A' and 'B' ) ", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.Cat cat where not ( not cat.kittens.size <= 1 )", benchmarkState );
      bh.consume( result );
      result = parse( "from eg.Cat cat where not  not ( not cat.kittens.size <= 1 )", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testOtherSyntax(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "select bar from org.hibernate.test.Bar bar order by ((bar.x - :valueX)*(bar.x - :valueX))", benchmarkState );
      bh.consume( result );
      result = parse( "from bar in class org.hibernate.test.Bar, foo in elements(bar.baz.fooSet)", benchmarkState );
      bh.consume( result );
      result = parse( "from one in class org.hibernate.test.One, many in elements(one.manies) where one.id = 1 and many.id = 1", benchmarkState );
      bh.consume( result );
      result = parse( "from org.hibernate.test.Inner _inner join _inner.middles middle", benchmarkState );
      bh.consume( result );
      result = parse( "FROM m IN CLASS org.hibernate.test.Master WHERE NOT EXISTS ( FROM d IN elements(m.details) WHERE NOT d.i=5 )", benchmarkState );
      bh.consume( result );
      result = parse( "FROM m IN CLASS org.hibernate.test.Master WHERE NOT 5 IN ( SELECT d.i FROM d IN elements(m.details) )", benchmarkState );
      bh.consume( result );
      result = parse( "SELECT m FROM m IN CLASS org.hibernate.test.Master, d IN elements(m.details) WHERE d.i=5", benchmarkState );
      bh.consume( result );
      result = parse( "SELECT m FROM m IN CLASS org.hibernate.test.Master, d IN elements(m.details) WHERE d.i=5", benchmarkState );
      bh.consume( result );
      result = parse( "SELECT m.id FROM m IN CLASS org.hibernate.test.Master, d IN elements(m.details) WHERE d.i=5", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testEjbqlExtensions(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "select object(a) from Animal a where a.mother member of a.offspring", benchmarkState );
      bh.consume( result );
      result = parse( "select object(a) from Animal a where a.mother member a.offspring", benchmarkState );
      bh.consume( result ); //no member of
      result = parse( "select object(a) from Animal a where a.offspring is empty", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testHB1042(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "select x from fmc_web.pool.Pool x left join x.containers c0 where (upper(x.name) = upper(':') and c0.id = 1)", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testKeywordInPath(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "from Customer c where c.order.status = 'argh'", benchmarkState );
      bh.consume( result );
      result = parse( "from Customer c where c.order.count > 3", benchmarkState );
      bh.consume( result );
      result = parse( "select c.where from Customer c where c.order.count > 3", benchmarkState );
      bh.consume( result );
      result = parse( "from Interval i where i.end <:end", benchmarkState );
      bh.consume( result );
      result = parse( "from Letter l where l.case = :case", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testPathologicalKeywordAsIdentifier(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "from Order order", benchmarkState );
      bh.consume( result );
      result = parse( "from X x order by x.group.by.from", benchmarkState );
      bh.consume( result );
      result = parse( "from Order x order by x.order.group.by.from", benchmarkState );
      bh.consume( result );
      result = parse( "select order.id from Order order", benchmarkState );
      bh.consume( result );
      result = parse( "select order from Order order", benchmarkState );
      bh.consume( result );
      result = parse( "from Order order where order.group.by.from is not null", benchmarkState );
      bh.consume( result );
      result = parse( "from Order order order by order.group.by.from", benchmarkState );
      bh.consume( result );
      result = parse( "from Group as group group by group.by.from", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testHHH354(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "from Foo f where f.full = 'yep'", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testWhereAsIdentifier(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "from where.Order", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testEjbqlKeywordsAsIdentifier(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "from org.hibernate.test.Bar bar where bar.object.id = ? and bar.object.class = ?", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testConstructorIn(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "from org.hibernate.test.Bar bar where (b.x, b.y, b.z) in (select foo, bar, baz from org.hibernate.test.Foo)", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testMultiByteCharacters(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "from User user where user.name like '%nn\u4e2dnn%'", benchmarkState );
      bh.consume( result );
      result = parse( "from User user where user.\u432d like '%\u4e2d%'", benchmarkState );
      bh.consume( result );
      result = parse( "from \u432d \u432d where \u432d.name like '%fred%'", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testHHH719(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "from Foo f order by com.fooco.SpecialFunction(f.id)", benchmarkState );
      bh.consume( result );
   }

   @Benchmark
   public void testHHH1107(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "from Animal where zoo.address.street = '123 Bogus St.'", benchmarkState );
      bh.consume( result );
   }


   @Benchmark
   public void testHHH1247(BenchmarkState benchmarkState, Blackhole bh) throws Exception {
      Object result;
      result = parse( "select distinct user.party from com.itf.iceclaims.domain.party.user.UserImpl user inner join user.party.$RelatedWorkgroups relatedWorkgroups where relatedWorkgroups.workgroup.id = :workgroup and relatedWorkgroups.effectiveTime.start <= :datesnow and relatedWorkgroups.effectiveTime.end > :dateenow ", benchmarkState );
      bh.consume( result );
   }


   private Object parse(String input, BenchmarkState benchmarkState) {

      return benchmarkState.hqlParser.parseHQL( input );

   }


}