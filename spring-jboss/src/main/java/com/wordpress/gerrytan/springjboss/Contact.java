package com.wordpress.gerrytan.springjboss;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Contact {

    @NotNull
    @Size(min = 1, message = "First Name Is Required")
    private String firstName;

    @NotNull
    @Size(min = 1, message = "Last Name Is Required")
    private String lastName;

    @NotNull
    @Size(min = 1, message = "Phone Number is Required")
    private String phoneNumber;
}
