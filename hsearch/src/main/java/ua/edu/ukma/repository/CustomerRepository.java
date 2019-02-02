package ua.edu.ukma.repository;

import java.util.List;

import ua.edu.ukma.entity.Customer;

public interface CustomerRepository {
  void createCustomer(Customer customer);

  List searchCustomer(String query);
}
