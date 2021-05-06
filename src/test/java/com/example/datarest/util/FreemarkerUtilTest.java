package com.example.datarest.util;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertTrue;

// See https://mkyong.com/java/java-read-a-file-from-resources-folder/ if required
public class FreemarkerUtilTest {

   private static String resourceName = "templates";
   private static String templateName = "billing-template.txt";
   private static File templateFileDirectory = null;
   private static File templateFile = null;

   @BeforeAll
   public static void init(){
      ClassLoader classLoader = FreemarkerUtilTest.class.getClassLoader();
      templateFileDirectory = new File( classLoader.getResource(resourceName).getFile() );
      templateFile = new File( classLoader.getResource(resourceName + "/" + templateName).getFile() );
   }

   @Test
   void test_templateFileDirectoryLoaded_success() {
      String absolutePath = templateFileDirectory.getAbsolutePath();
      System.out.println(absolutePath);
      assertTrue( absolutePath.endsWith( resourceName ) );
   }

   @Test
   void test_parseTemplate_success() throws Exception {
      Map<String, String> map = new HashMap<>();
      map.put( "name", "Jason Bourne");
      map.put( "phone", "0800 838383");
      map.put( "address", "Bikini Bottom");

      String output = FreemarkerUtil.parseTemplate(templateName, templateFileDirectory, map );
      System.out.println(output);
   }

   @Test
   void test_findSubstitutionVars_success() throws Exception {
      String fileContent = FileUtils.readFileToString(templateFile, StandardCharsets.UTF_8);
      List<String> variableList = FreemarkerUtil.findSubstitutionVars( fileContent, FreemarkerUtil.DEFAULT_PATTERN );
      System.out.println( variableList );

      assertThat( variableList, containsInAnyOrder("name", "address", "phone"));
   }
}
