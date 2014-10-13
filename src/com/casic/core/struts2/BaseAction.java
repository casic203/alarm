 package com.casic.core.struts2;
 
 import com.opensymphony.xwork2.Action;
 import com.opensymphony.xwork2.Validateable;
 import com.opensymphony.xwork2.ValidationAware;
 import com.opensymphony.xwork2.ValidationAwareSupport;
 import java.util.Collection;
 import java.util.List;
 import java.util.Map;
 
 public class BaseAction
   implements Action, Validateable, ValidationAware
 {
   private ValidationAwareSupport validationAware = new ValidationAwareSupport();
 
   public void setActionErrors(Collection<String> errorMessages) {
     this.validationAware.setActionErrors(errorMessages);
   }
 
   public Collection<String> getActionErrors() {
     return this.validationAware.getActionErrors();
   }
 
   public void setActionMessages(Collection<String> messages) {
     this.validationAware.setActionMessages(messages);
   }
 
   public Collection<String> getActionMessages() {
     return this.validationAware.getActionMessages();
   }
 
   public void setFieldErrors(Map<String, List<String>> errorMap) {
     this.validationAware.setFieldErrors(errorMap);
   }
 
   public Map<String, List<String>> getFieldErrors() {
     return this.validationAware.getFieldErrors();
   }
 
   public void addActionError(String anErrorMessage) {
     this.validationAware.addActionError(anErrorMessage);
   }
 
   public void addActionMessage(String aMessage) {
     this.validationAware.addActionMessage(aMessage);
   }
 
   public void addFieldError(String fieldName, String errorMessage) {
     this.validationAware.addFieldError(fieldName, errorMessage);
   }
 
   public String input() throws Exception {
     return "input";
   }
 
   public String doDefault() throws Exception {
     return "success";
   }
 
   public String execute() throws Exception {
	   System.out.println("this is the generic action class");
     return "success";
   }
 
   public boolean hasActionErrors() {
     return this.validationAware.hasActionErrors();
   }
 
   public boolean hasActionMessages() {
     return this.validationAware.hasActionMessages();
   }
 
   public boolean hasErrors() {
     return this.validationAware.hasErrors();
   }
 
   public boolean hasFieldErrors() {
     return this.validationAware.hasFieldErrors();
   }
 
   public void clearFieldErrors() {
     this.validationAware.clearFieldErrors();
   }
 
   public void clearActionErrors() {
     this.validationAware.clearActionErrors();
   }
 
   public void clearMessages() {
     this.validationAware.clearMessages();
   }
 
   public void clearErrors() {
     this.validationAware.clearErrors();
   }
 
   public void clearErrorsAndMessages() {
     this.validationAware.clearErrorsAndMessages();
   }
 
   public void validate()
   {
   }
 
   public void pause(String result)
   {
   }
 }

