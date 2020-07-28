package com.bluebook;

import com.bluebook.domain.CustomUser;
import com.bluebook.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class app implements CommandLineRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	UserRepository useRepo;
	public static void main(String[] args) {
		SpringApplication.run(app.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		CustomUser u = new CustomUser();
		u.setUsername("joe147258");
		u.setEmail("joe@joe.com");
		u.setId(0);
		u.setFirstName("Joe");
		u.setLastName("Phillips");
		u.setRole("TEACHER");
		u.setPassword(passwordEncoder.encode("123"));
		useRepo.save(u);

		CustomUser u1 = new CustomUser();
		u1.setUsername("VergeOfEden");
		u1.setEmail("seb@seb.com");
		u1.setId(1);
		u1.setFirstName("Seb");
		u1.setLastName("Adamo");
		u1.setRole("STUDENT");
		u1.setPassword(passwordEncoder.encode("123"));
		useRepo.save(u1);

		CustomUser u2 = new CustomUser();
		u2.setUsername("Simmyb1798");
		u2.setEmail("sim@sim.com");
		u2.setId(2);
		u2.setFirstName("Simrit");
		u2.setLastName("Bains");
		u2.setRole("STUDENT");
		u2.setPassword(passwordEncoder.encode("123"));
		useRepo.save(u2);

		CustomUser u3 = new CustomUser();
		u3.setUsername("KitchenPorter560");
		u3.setEmail("tom@tom.com");
		u3.setId(3);
		u3.setFirstName("Thomas");
		u3.setLastName("Pardo");
		u3.setRole("STUDENT");
		u3.setPassword(passwordEncoder.encode("123"));
		useRepo.save(u3);


	}

}
