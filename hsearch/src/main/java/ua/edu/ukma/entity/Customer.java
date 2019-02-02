package ua.edu.ukma.entity;

import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;

@Entity
@Table(name = "customers")
public class Customer {
  private int id;
  private String firstName;
  private String lastName;
  private String countryCode;

  @Id
  @Field
  @Column(name = "id")
  public int getId() {
    return id;
  }

  public Customer setId(int id) {
    this.id = id;
    return this;
  }

  @Basic
  @Field
  @Column(name = "first_name")
  public String getFirstName() {
    return firstName;
  }

  public Customer setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  @Basic
  @Field
  @Column(name = "last_name")
  public String getLastName() {
    return lastName;
  }

  public Customer setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  @Basic
  @Field
  @Column(name = "country_code")
  public String getCountryCode() {
    return countryCode;
  }

  public Customer setCountryCode(String countryCode) {
    this.countryCode = countryCode;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Customer customer = (Customer) o;

    if (id != customer.id) return false;
    if (!Objects.equals(firstName, customer.firstName)) return false;
    if (!Objects.equals(lastName, customer.lastName)) return false;

    return Objects.equals(countryCode, customer.countryCode);
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    result = 31 * result + (countryCode != null ? countryCode.hashCode() : 0);
    return result;
  }
}
