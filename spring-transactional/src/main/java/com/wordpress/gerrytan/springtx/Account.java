package com.wordpress.gerrytan.springtx;

import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

/**
 * Account entity class. @Roo.. annotations below are metadata to instruct
 * Spring Roo to generate additional stuffs for this class which will be weaved
 * at compile time, including additional "id" and "version" field (table column)
 * 
 * @author Gerry Tan - 30 May 2012 - http://gerrytan.wordpress.com
 * 
 */
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Account {

  @NotNull
  private String name;

  @NotNull
  private double balance;
}
