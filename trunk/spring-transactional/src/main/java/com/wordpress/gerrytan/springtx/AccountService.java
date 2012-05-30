package com.wordpress.gerrytan.springtx;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer class that provide business methods related to Account. Methods
 * annotated with @Transactional is recognized by spring transaction manager.
 * Everytime this method is invoked by a client (eg: a web controller, or
 * another service class), spring transaction manager checks if an active
 * transaction exist, if not it'll create a new one
 * 
 * @author Gerry Tan - 30 May 2012 - http://gerrytan.wordpress.com
 */
@Service
public class AccountService {
  @PersistenceContext
  private EntityManager em;
  
  private static Logger logger = LoggerFactory.getLogger(AccountService.class);

  @Transactional
  public double getBalance(long accountId) {
    Account account = em.find(Account.class, accountId);
    if (account != null) {
      return account.getBalance();
    }
    throw new IllegalStateException("Can't find account with id " + accountId);
  }

  @Transactional
  public void debit(long accountId, double amount) {
    Account account = em.find(Account.class, accountId);
    account.setBalance(account.getBalance() - amount);
  }

  @Transactional
  public void credit(long accountId, double amount) {
    Account account = em.find(Account.class, accountId);
    account.setBalance(account.getBalance() + amount);
  }

  @Transactional
  public void transfer(long fromAccountId, long toAccountId, double amount) {
    if (getBalance(fromAccountId) < amount) {
      throw new IllegalStateException(String.format(
        "Not enough balance to transfer %s from accountId %s to accountId %s", amount,
        fromAccountId, toAccountId));
    }

    debit(fromAccountId, amount);

    credit(toAccountId, amount);
  }
}
