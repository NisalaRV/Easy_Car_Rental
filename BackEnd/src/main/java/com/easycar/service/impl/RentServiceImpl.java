package com.easycar.service.impl;

import com.easycar.dto.CustomDTO;
import com.easycar.dto.RentDTO;
import com.easycar.entity.Car;
import com.easycar.entity.Driver;
import com.easycar.entity.Rent;
import com.easycar.entity.RentDetails;
import com.easycar.enums.AvailabilityType;
import com.easycar.enums.RentRequest;
import com.easycar.enums.RequestType;
import com.easycar.repo.CarRepo;
import com.easycar.repo.DriverRepo;
import com.easycar.repo.RentRepo;
import com.easycar.service.RentService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.easycar.enums.AvailabilityType.AVAILABLE;
import static com.easycar.enums.AvailabilityType.UNAVAILABLE;

@Service
@Transactional
public class RentServiceImpl implements RentService {

    @Autowired
    private RentRepo rentRepo;


    @Autowired
    private CarRepo carRepo;

    @Autowired
    private DriverRepo driverRepo;
    @Autowired
    private ModelMapper mapper;



    @Override
    public CustomDTO rentIdGenerate() {
        return new CustomDTO(rentRepo.getLastIndex());
    }

    @Override
    public void bookingCars(RentDTO dto) {

        Rent rent = mapper.map(dto, Rent.class);

        if (rentRepo.existsById(dto.getRentID())) {
            throw new RuntimeException("Booking" + dto.getRentID() + " Already added.!");
        }
        if (dto.getRequestType().equals(RequestType.YES)) {
            List<Driver> drivers = driverRepo.availableDrivers();
            int x;

            for (RentDetails rentDetails : rent.getRentDetails()) {
                x = new Random().nextInt(drivers.size());
                rentDetails.setDriverID(drivers.get(x).getUser_Id());
                Car car = carRepo.findById(rentDetails.getCarID()).get();
                car.setVehicleAvailabilityType(UNAVAILABLE);
                carRepo.save(car);
                drivers.get(x).setDriverAvailability(UNAVAILABLE);
                driverRepo.save(drivers.get(x));

            }

            } else if (dto.getRequestType().equals(RequestType.NO)){
            for (RentDetails rentDetails : rent.getRentDetails()){
                Car car = carRepo.findById(rentDetails.getCarID()).get();
                car.setVehicleAvailabilityType(UNAVAILABLE);
                carRepo.save(car);
            }
        }

        rentRepo.save(rent);
    }

    @Override
    public CustomDTO getSumOfBooking() {
        return new CustomDTO(rentRepo.getSumOfBooking());
    }

    @Override
    public CustomDTO getSumOfBookingPending() {
        return new CustomDTO(rentRepo.getSumOfBookingPending());
    }

    @Override
    public CustomDTO getSumOfBookingActive() {
        return new CustomDTO(rentRepo.getSumOfBookingActive());
    }

    @Override
    public ArrayList<RentDTO> getAllRent() {
        return mapper.map(rentRepo.findAll(), new TypeToken<ArrayList<RentDTO>>() {
        }.getType());
    }

    @Override
    public void deleteRent(String rentID) {

        Rent rent = rentRepo.findById(rentID).get();
        if (rent.getRentDetails().get(0).getDriverID() != null) {
            Car car = carRepo.findById(rent.getRentDetails().get(0).getCarID()).get();
            car.setVehicleAvailabilityType(AvailabilityType.AVAILABLE);
            carRepo.save(car);

            Driver drivers = driverRepo.findById(rent.getRentDetails().get(0).getDriverID()).get();
            drivers.setDriverAvailability(AvailabilityType.AVAILABLE);
            driverRepo.save(drivers);

            rentRepo.deleteById(rentID);
        }
        if (rent.getRentDetails().get(0).getDriverID() == null) {
            Car car = carRepo.findById(rent.getRentDetails().get(0).getCarID()).get();
            car.setVehicleAvailabilityType(AvailabilityType.AVAILABLE);
            carRepo.save(car);

            rentRepo.deleteById(rentID);
        }
    }


    @Override
    public void bookingConform(String rentID, String driverId) {
        Rent rent = rentRepo.findById(rentID).get();

        if (rent.getRentDetails().get(0).getDriverID() != null) {

            Driver drivers = driverRepo.findById(rent.getRentDetails().get(0).getDriverID()).get();
            drivers.setDriverAvailability(AvailabilityType.AVAILABLE);
            driverRepo.save(drivers);

            rent.getRentDetails().get(0).setDriverID(driverId);
            Driver driver = driverRepo.findById(rent.getRentDetails().get(0).getDriverID()).get();
            driver.setDriverAvailability(UNAVAILABLE);
            rent.setRentType(RentRequest.CONFORM);
            rentRepo.save(rent);
        }
        if (rent.getRentDetails().get(0).getDriverID() == null) {
            rent.setRentType(RentRequest.CONFORM);
            rentRepo.save(rent);
        }
    }



    @Override
    public void bookingReject(String rentID, String driverId) {
        Rent rent = rentRepo.findById(rentID).get();
        if (rent.getRentDetails().get(0).getDriverID() != null) {

            Driver drivers = driverRepo.findById(rent.getRentDetails().get(0).getDriverID()).get();
            drivers.setDriverAvailability(AVAILABLE);
            driverRepo.save(drivers);

            Car car = carRepo.findById(rent.getRentDetails().get(0).getCarID()).get();
            car.setVehicleAvailabilityType(AVAILABLE);
            carRepo.save(car);

            rent.setRentType(RentRequest.REJECT);
            rentRepo.save(rent);

        } if (rent.getRentDetails().get(0).getDriverID() == null) {
            Car car = carRepo.findById(rent.getRentDetails().get(0).getCarID()).get();
            car.setVehicleAvailabilityType(AVAILABLE);
            carRepo.save(car);

            rent.setRentType(RentRequest.REJECT);
            rentRepo.save(rent);
        }
    }

    @Override
    public RentDTO searchId(String id) {
        if (!rentRepo.existsById(id)) {
            throw new RuntimeException("Wrong ID. Please enter Valid id..!");
        }
        return mapper.map(rentRepo.findById(id).get(),RentDTO.class);

    }
}
