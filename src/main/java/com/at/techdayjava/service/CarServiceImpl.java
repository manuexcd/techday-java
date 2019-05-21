package com.at.techdayjava.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.at.techdayjava.model.Car;
import com.at.techdayjava.repository.CarRepository;

@Service
public class CarServiceImpl implements CarService {

	@Autowired
	private CarRepository repository;

	@Override
	public List<Car> getAllCars() {
		return repository.findAll();
	}

	@Override
	public Optional<Car> getCarById(Long id) {
		return repository.findById(id);
	}

	@Override
	public Car saveCar(Car car) {
		return repository.save(car);
	}

	@Override
	public void deleteCar(Car car) {
		repository.delete(car);
	}
}
