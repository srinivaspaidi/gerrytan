package com.wordpress.gerrytan.springjboss;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/contact")
public class ContactController {
  private static Logger logger = LoggerFactory.getLogger(ContactController.class);
  
  /**
   * This method will be used when a get request to /contact path is made. A new contact
   * object is created everytime and passed to the web browser for user to fill. The 
   * getContactList() method will also be invoked because it is annotated by @ModelAttribute
   * annotation. The result of getContactList() will be passed to the view with the attribute
   * name "contacts"
   * @param contact a new contact to be manipulated by user in web browser
   */
  @RequestMapping(method = RequestMethod.GET)
  public String get(@ModelAttribute("newContact") Contact contact) {
    logger.debug("invoking get() method");
    return "contact/index";
  }
  
  /**
   * Delete specified contact. Doesn't check validity of deleteId
   * @param deleteId
   * @return
   */
  @RequestMapping(method = RequestMethod.POST, params="deleteId")
  @Transactional
  public String delete(@RequestParam long deleteId) {
    logger.debug("Deleting contact " + deleteId);
    Contact contact = Contact.findContact(deleteId);
    contact.remove();
    return "redirect:/contact";
  }

  /**
   * Save the new contact object, render back to the list page, listing the new 
   * @param contact a new contact that has been manipulated by user in a web browser.
   * @param bindingResult the result of binding user input into a Contact object. If any
   * mandatory field is missing the hasError() method of this object will return false
   * @return once saving is finish, issue redirect into /contact path so GET method
   * handler is invoked again
   */
  @RequestMapping(method = RequestMethod.POST)
  public String add(@ModelAttribute("newContact") @Valid Contact contact, BindingResult bindingResult) {
    if (!bindingResult.hasErrors()) {
      logger.debug("saving contact " + contact);
      contact.persist();
      return "redirect:/contact";
    }
    return "contact/index";
  }
  
  /**
   * Controller method annotated with @ModelAttribute will be invoked before any handler
   * method is invoked. The return result will be included into the Model object passed
   * down to View. The model attribute name is the annotation value
   * @return list of contacts
   */
  @ModelAttribute("contacts")
  public List<Contact> getContactList() {
    logger.debug("getting contact lists");
    List<Contact> result = Contact.findAllContacts();
    return result;
  }
}
