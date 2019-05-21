package com.at.techdayjava.controller;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.at.techdayjava.model.Car;
import com.at.techdayjava.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/msjava")
public class CarController {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${microservices.mskotlin.url}")
	private String URL;

	@Value("${microservices.msjavaconsumer.queue-name}")
	private String carQueue;

	@Autowired
	private RabbitTemplate template;

	@Autowired
	private CarService carService;

	@GetMapping
	public ResponseEntity<List<Car>> getAllCars() {
		return new ResponseEntity<List<Car>>(carService.getAllCars(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Car> getCarById(@PathVariable Long id) {
		return carService.getCarById(id).map(car -> new ResponseEntity<>(car, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@PostMapping
	public ResponseEntity<Car> saveCar(@RequestBody Car car) {
		try {
			restTemplate.postForEntity(URL, car, Car.class);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		try {
			ObjectMapper mapper = new ObjectMapper();
			template.convertAndSend(carQueue, mapper.writeValueAsString(car));
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return new ResponseEntity<Car>(carService.saveCar(car), HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
		carService.getCarById(id).map(car -> {
			carService.deleteCar(car);
			return new ResponseEntity<>(HttpStatus.OK);
		}).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
