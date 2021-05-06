package com.example.datarest.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Slf4j
public class JavaStream1Test {

   @Data
   @AllArgsConstructor
   public static class Employee {
      private String employeeID;
      private String name;
      private int age;
      private BigDecimal salary;
      private String department;
   }

   private static Employee[] employeeData = {
      new Employee("001", "John Doe", 30, BigDecimal.valueOf(10000.0), "HR"),
      new Employee("003", "John Snow", 28, BigDecimal.valueOf(450000), "Night Watch"),
      new Employee("020", "Liu Kang", 33, BigDecimal.valueOf(70600), "Mortal Wombat"),
      new Employee("307", "Sub Zero", 44, BigDecimal.valueOf(80000), "Ice"),
      new Employee("333", "Night King", 2589, BigDecimal.valueOf(800000), "Ice"),
      new Employee("007", "Johny Cage", 28, BigDecimal.valueOf(370000), "Mortal Wombat"),
      new Employee("777", "Sponge Bob", 7, BigDecimal.valueOf(1000000), "Ocean")
   };

   /**
    * Retrieve the oldest Employee in a single line.
    */
   @Test
   void test_get_oldestEmployee_success() {
      Employee oldestEmployee = Arrays.stream(employeeData).max(Comparator.comparing(Employee::getAge)).
         orElseThrow(NoSuchElementException::new);

      assertEquals( 2589, oldestEmployee.getAge() );
      System.out.println( oldestEmployee );
   }

   /**
    * Retrieve the most overpaid employee in a single line.
    */
   @Test
   void test_get_highestPaidEmployee_success() {
      Employee overPaidEmployee = Arrays.stream(employeeData).max(Comparator.comparing(Employee::getSalary)).
         orElseThrow(NoSuchElementException::new);

      Employee expectedEmployee = employeeData[6];
      assertAll("Verify employee data",
         () -> assertEquals( expectedEmployee.getEmployeeID(), overPaidEmployee.getEmployeeID()),
         () -> assertEquals( expectedEmployee.getSalary(), overPaidEmployee.getSalary()),
         () -> assertEquals( expectedEmployee.getName(), overPaidEmployee.getName()) );
   }

   @Test
   void test_get_employeeWithNameContainingCage_success() {
      Employee johnyCage = Arrays.stream(employeeData).filter(employee -> employee.getName().contains("Cage")).
         findFirst().orElseThrow(NoSuchElementException::new);

      Employee expectedEmployee = employeeData[5];
      System.out.println("expectedEmployee: " + expectedEmployee.getName() + ", " + expectedEmployee.getEmployeeID());
      assertThat( expectedEmployee, allOf(
         hasProperty( "name", is( johnyCage.getName() )),
         hasProperty( "employeeID", is( johnyCage.getEmployeeID() ))
      ));
      System.out.println("Johny Cage: " + johnyCage);
   }

   /**
    * Get a map of Department(i.e. key) to Employee list(i.e. key) from employeeData
    */
   @Test
   void test_getDepartmentToEmployeeListMap() {
      Map<String, List< Employee>> groupByDepartment = Arrays.stream(employeeData).
         collect( Collectors.groupingBy(e -> e.getDepartment() ) );

       printMapData( groupByDepartment );
   }

   void printMapData(Map<String, List<Employee>> map) {
      map.entrySet().stream().forEach(e ->
         System.out.println(System.lineSeparator() + e.getKey() + ":" + System.lineSeparator() +
            e.getValue().stream().map(Object::toString).
               collect(Collectors.joining( System.lineSeparator()))));
      // See https://stackoverflow.com/questions/24882927/using-java-8-to-convert-a-list-of-objects-into-a-string-obtained-from-the-tostri
      // to convert a list of objects to string using toString()
   }

   String printEmployeeList(List<Employee> employeeList) {
      employeeList.stream().forEach( c -> System.out.println(c));
      return "";
   }

   // Sort Map by key using TreeMap
   @Test
   void test_sortMapByKey1() {
      Map<String, List<Employee>> departmentToEmployeeListMap = getDepartmentToEmployeeListMap();
      departmentToEmployeeListMap = new TreeMap<String, List<Employee>>( departmentToEmployeeListMap );
      printMapData( departmentToEmployeeListMap );
   }

   // Sort Map by key using Map.Entry<K, V> and stream
   @Test
   void test_sortMapByKey2() {
      Map<String, List<Employee>> departmentToEmployeeListMap = getDepartmentToEmployeeListMap();

      departmentToEmployeeListMap = departmentToEmployeeListMap.entrySet().stream().
         sorted( Map.Entry.comparingByKey() ).collect(
            Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) ->
               oldValue, LinkedHashMap::new));
      printMapData( departmentToEmployeeListMap );
   }

   @Test
   void test_sortMapByKey3() {
      Map<String, List<Employee>> departmentToEmployeeListMap = getDepartmentToEmployeeListMap();
      LinkedHashMap<String, List<Employee>> sortedMap = new LinkedHashMap<>();

      departmentToEmployeeListMap.entrySet().stream().
         sorted( Map.Entry.comparingByKey() ).forEachOrdered(
            x -> sortedMap.put(x.getKey(), x.getValue())
      );
      printMapData( sortedMap );
   }

   @Test
   void test_getSortedEmployeeList_fromMap() {
      Map<String, List<Employee>> groupByDepartment = getDepartmentToEmployeeListMap();

      // Using flatMap to convert the Stream<List<Employee>> to Stream<Employee> then collect it in a List
      List< Employee> employeeList = groupByDepartment.entrySet().stream().map(e -> e.getValue()).
         flatMap( Collection::stream ).collect( Collectors.toList());

      employeeList.sort( Comparator.comparing( Employee::getEmployeeID ) );

      // Sort Employee list by employee name
      // employeeList.sort( Comparator.comparing( Employee::getName));

      // Reverse sort Employee name
      // employeeList.sort( Comparator.comparing(Employee::getName).reversed());
      employeeList.forEach(System.out::println);
   }

   @Test
   void test_getTotalSalary_ofAllEmployee() {
      List<Employee> employeeList = Arrays.asList( employeeData );
      BigDecimal totalSalary = employeeList.stream().map(x -> x.getSalary()).reduce(BigDecimal.ZERO, BigDecimal::add);
      System.out.println("Total Summary: " + totalSalary);

      assertEquals(  BigDecimal.valueOf(2780600.0), totalSalary );
   }

   // Get total expenditure of each department to create the map Map<{Department>, {TotalSalary}>
   @Test
   void test_getDepartmentToExpenditureMap() {
      Map<String, List<Employee>> groupByDepartment = getDepartmentToEmployeeListMap();
      Map<String, BigDecimal> departmentSalaryMap = groupByDepartment.entrySet().stream().collect( Collectors.toMap( Map.Entry::getKey, e ->
         getTotalExpenditure( e.getValue() )));

      departmentSalaryMap.entrySet().stream().forEach(e -> System.out.println(e.getKey() + ": " + e.getValue() ));
   }

   @Test
   void test_getOverPaidEmployeeList() {
      List<Employee> employeeList = Arrays.asList( employeeData );

      BigDecimal salaryLimit = BigDecimal.valueOf( 200000.00 );
      // List employee that's making too much $ over 120000
      List<Employee> overPaidEmployeeList = employeeList.stream().filter(employee ->
         isOverPaid( employee.getSalary(), salaryLimit ) ).collect(Collectors.toList());

      System.out.println("Over Paid Employee: " );
      overPaidEmployeeList.forEach(System.out::println);
   }

   public static BigDecimal getTotalExpenditure(List< Employee> employeeList) {
      return employeeList.stream().map(x -> x.getSalary()).reduce(BigDecimal.ZERO, BigDecimal::add);
   }

   public static boolean isOverPaid(BigDecimal salary, BigDecimal salaryLimit) {
      salary = Optional.ofNullable( salary ).orElseGet( () -> BigDecimal.ZERO );
      return (salary.compareTo( salaryLimit ) >= 0);
   }

   Map<String, List< Employee>>  getDepartmentToEmployeeListMap() {
      Map<String, List< Employee>> groupByDepartment = Arrays.stream(employeeData).
         collect( Collectors.groupingBy(e -> e.getDepartment() ) );
      return groupByDepartment;
   }
   
}
