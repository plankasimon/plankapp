package com.springTut;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sun.tools.javac.Main;

@SpringBootApplication
@RestController
@RequestMapping("api/v1/customers")
public class SpringBootExampleApplication {

	private final CustomerRepository customerRepository;

	public SpringBootExampleApplication(CustomerRepository customerRepository){
		this.customerRepository = customerRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootExampleApplication.class, args);
	}

	@GetMapping
	public List<Customer> getCustomers(){
		return customerRepository.findAll();
	}
	record NewCustomerRequest(
		String name,
		Integer age
	){

	}
	@PostMapping
	public void addCustomer(@RequestBody NewCustomerRequest request){
		Customer customer = new Customer();
		customer.setAge(request.age());
		customer.setName(request.name());
		customerRepository.save(customer);
	}

	@DeleteMapping("{customerId}")
	public void deleteCustomer(@PathVariable("customerId") Integer id){
		customerRepository.deleteById(id);
	}

	@PutMapping("{customerId}")
	public void updateCustomer(@PathVariable("customerId") Integer id, @RequestBody NewCustomerRequest request){
		Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("No such customer"));
		customer.setName(request.name());
		customer.setAge(request.age());
		customerRepository.save(customer);
	}
}
