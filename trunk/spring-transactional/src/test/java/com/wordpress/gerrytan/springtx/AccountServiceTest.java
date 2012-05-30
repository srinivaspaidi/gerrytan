package com.wordpress.gerrytan.springtx;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * Unit test for AccountService. @ContextConfiguration annotation below will
 * create a Spring application context using specified xml configuration for
 * this class. Spring also provides test-specific transaction configuration
 * which will automatically rollback transaction created for @Transactional
 * annotated method in the unit test class
 * </p>
 * 
 * <p>
 * This application uses hsql in-memory db populated with data from
 * src/test/resources/import.sql file. import.sql is a special file which if
 * exist on the root of classpath it will be recognized by hibernate and
 * executed after persistence context setup is ready.
 * </p>
 * 
 * <p>
 * Hibernate has been configured to output SQL and transaction related log
 * through log4j.properties, make sure you inspect the outcome of this unit test
 * to experience Spring @Transactional in action
 * </p>
 * 
 * @author Gerry Tan - 30 May 2012 - http://gerrytan.wordpress.com
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/META-INF/spring/applicationContext.xml" })
public class AccountServiceTest {
  @Autowired
  private AccountService accountService;
  private static Logger logger = LoggerFactory.getLogger(AccountServiceTest.class);

  @Test
  @Transactional
  public void testGetBalance() {
    logger.debug("Start test get balance");
    assertEquals(700, accountService.getBalance(1), 0);
    assertEquals(50, accountService.getBalance(2), 0);
    assertEquals(20, accountService.getBalance(3), 0);
  }

  /**
   * Test crediting an account with some amount. Notice that on the second
   * method call to accountService.getBalance(), hibernate did not issue a SQL
   * to database because the persistence context hasn't been synchronized (the
   * transaction has not yet finished)
   */
  @Test
  @Transactional
  public void testCredit() {
    logger.debug("============ Start test credit ======================");
    assertEquals(700, accountService.getBalance(1), 0);
    accountService.credit(1, 10);
    assertEquals(710, accountService.getBalance(1), 0);
  }

  @Test
  @Transactional
  public void testDebit() {
    logger.debug("============ Start test debit ======================");
    assertEquals(700, accountService.getBalance(1), 0);
    accountService.debit(1, 22);
    assertEquals(678, accountService.getBalance(1), 0);
  }

  /**
   * <p>
   * Note that this test method is not annotated with Spring @Transactional. As
   * an effect Spring will create a transaction for every call to AccountService
   * getBalance() and transfer() method, and the transaction will be comitted at
   * the end of method execution.
   * </p>
   * 
   * <p>
   * Observe that transfer() method invokes getBalance(), debit() and credit(),
   * however no new transaction is created for those 3 methods since Spring
   * detects there's already an active one open
   * </p>
   * 
   */
  @Test
  public void testTransferSuccess() {
    logger.debug("============ Start test transfer ======================");
    assertEquals(700, accountService.getBalance(1), 0);
    assertEquals(50, accountService.getBalance(2), 0);

    // Transfer 20 from account 1 to 2
    logger.debug("----- Transfer $20 from account 1 to 2 -----");
    accountService.transfer(1, 2, 20);
    logger.debug("----- Completed Transfer $20 from account 1 to 2 -----");
    assertEquals(680, accountService.getBalance(1), 0);
    assertEquals(70, accountService.getBalance(2), 0);

    // Transfer 20 back from account 2 to 1 to restore datbase back to original
    logger.debug("----- Transfer $20 from account 2 to 1 -----");
    accountService.transfer(2, 1, 20);
    logger.debug("----- Completed Transfer $20 from account 1 to 2 -----");
    assertEquals(700, accountService.getBalance(1), 0);
    assertEquals(50, accountService.getBalance(2), 0);
  }
}
