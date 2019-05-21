package com.at.techdayjava.service;

import java.util.List;
import java.util.Optional;

import com.at.techdayjava.model.Car;

public interface CarService {
	public List<Car> getAllCars();

	public Optional<Car> getCarById(Long id);

	public Car saveCar(Car car);

	public void deleteCar(Car car);
}
