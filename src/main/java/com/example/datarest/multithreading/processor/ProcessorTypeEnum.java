package com.example.datarest.multithreading.processor;

public enum ProcessorTypeEnum {

   APPLE_PROCESSOR( AppleTaskProcessor.class ),
   M2X_PROCESSOR( M2xProcessor.class ),
   STIR_FRY_MIX_PROCESSOR( StirFryMixProcessor.class );

   private Class classValue;

   ProcessorTypeEnum(Class clazz) {
      this.classValue = clazz;
   }

   public static ProcessorTypeEnum fromClass(Class aClass) {
      for (ProcessorTypeEnum e : values()) {
         if( e.classValue == aClass ) {
            return e;
         }
      }
      throw new RuntimeException("Unknown class:" + aClass);
   }

   public Class getClassValue() {
      return classValue;
   }
}
