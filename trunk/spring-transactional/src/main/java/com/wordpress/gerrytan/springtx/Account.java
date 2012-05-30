package com.wordpress.gerrytan.springtx;

import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

/**
 * Account entity class. Additional "id" and "version" fields are 'weaved' by AspectJ
 * at compile time. @RooJpa.. annotations below are metadata to instruct Spring Roo
 * to generate additonal stuff for this class which is weaved at compile time
 * 
 * @author Gerry Tan - 30 May 2012 - http://gerrytan.wordpress.com
 *
 */
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Account {

  @NotNull private String name;

  @NotNull private double balance;
}
