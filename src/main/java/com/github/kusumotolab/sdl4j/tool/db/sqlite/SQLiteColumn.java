package com.github.kusumotolab.sdl4j.tool.db.sqlite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SQLiteColumn {

  String EMPTY_NAME = "";

  int type();

  String name() default EMPTY_NAME;

  boolean primaryKey() default false;

  boolean autoIncrement() default false;
}
