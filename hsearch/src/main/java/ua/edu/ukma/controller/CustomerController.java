package ua.edu.ukma.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ua.edu.ukma.entity.Customer;
import ua.edu.ukma.repository.CustomerRepository;

@Controller
public class CustomerController {

  private CustomerRepository repository;

  @PostMapping
  public String createCustomer(@RequestBody MultiValueMap<String, String> form) {
    var customer = new Customer()
            .setCountryCode(form.get("countryCode").get(0))
            .setFirstName(form.get("firstName").get(0))
            .setLastName(form.get("lastName").get(0));

    repository.createCustomer(customer);
    return "/";
  }

  @GetMapping
  public String searchCustomer(Model model, @RequestBody MultiValueMap<String, String> form) {
    var result = repository.searchCustomer(form.get("query").get(0));
    model.addAttribute("result", result);
    return "/";
  }

  @Required
  public void setRepository(CustomerRepository repository) {
    this.repository = repository;
  }
}
