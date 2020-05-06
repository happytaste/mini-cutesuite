package com.cutesuite.security;

import com.cutesuite.security.model.Customer;
import com.cutesuite.security.repo.CustomerRepository;
import com.cutesuite.security.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Slf4j
@Component
public class AppRunner implements CommandLineRunner {


    private final BookingService bookingService;
    @Autowired
    CustomerRepository customerRepository;

    public AppRunner(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("transaction test ------------------------------");
//        transactionTest();
        log.info("jpa test ------------------------------");
//        jpaTest();
    }

    private void jpaTest() {

        // save a few customers
        customerRepository.save(new Customer("Jack", "Bauer"));
        customerRepository.save(new Customer("Chloe", "O'Brian"));
        customerRepository.save(new Customer("Kim", "Bauer"));
        customerRepository.save(new Customer("David", "Palmer"));
        customerRepository.save(new Customer("Michelle", "Dessler"));

        // fetch all customers
        log.info("Customers found with findAll():");
        log.info("-------------------------------");
        for (Customer customer : customerRepository.findAll()) {
            log.info(customer.toString());
        }
        log.info("");

        // fetch an individual customer by ID
        Customer customer = customerRepository.findById(1L);
        log.info("Customer found with findById(1L):");
        log.info("--------------------------------");
        log.info(customer.toString());
        log.info("");

        // fetch customers by last name
        log.info("Customer found with findByLastName('Bauer'):");
        log.info("--------------------------------------------");
        customerRepository.findByLastName("Bauer").forEach(bauer -> {
            log.info(bauer.toString());
        });
        // for (Customer bauer : customerRepository.findByLastName("Bauer")) {
        //  log.info(bauer.toString());
        // }
        log.info("");
        
    }

    private void transactionTest() {
        bookingService.book("Alice", "Bob", "Carol");
        Assert.isTrue(bookingService.findAllBookings().size() == 3,
                "First booking should work with no problem");
        log.info("Alice, Bob and Carol have been booked");
        try {
            bookingService.book("Chris", "Samuel");
        } catch (RuntimeException e) {
            log.info("v--- The following exception is expect because 'Samuel' is too " +
                    "big for the DB ---v");
            log.error(e.getMessage());
        }

        for (String person : bookingService.findAllBookings()) {
            log.info("So far, " + person + " is booked.");
        }
        log.info("You shouldn't see Chris or Samuel. Samuel violated DB constraints, " +
                "and Chris was rolled back in the same TX");
        Assert.isTrue(bookingService.findAllBookings().size() == 3,
                "'Samuel' should have triggered a rollback");

        try {
            bookingService.book("Buddy", null);
        } catch (RuntimeException e) {
            log.info("v--- The following exception is expect because null is not " +
                    "valid for the DB ---v");
            log.error(e.getMessage());
        }

        for (String person : bookingService.findAllBookings()) {
            log.info("So far, " + person + " is booked.");
        }
        log.info("You shouldn't see Buddy or null. null violated DB constraints, and " +
                "Buddy was rolled back in the same TX");
        Assert.isTrue(bookingService.findAllBookings().size() == 3,
                "'null' should have triggered a rollback");
    }
}
