package com.easycar.service.impl;

import com.easycar.dto.CustomDTO;
import com.easycar.dto.PaymentDTO;
import com.easycar.entity.Car;
import com.easycar.entity.Driver;
import com.easycar.entity.Payment;
import com.easycar.entity.Rent;
import com.easycar.enums.AvailabilityType;
import com.easycar.repo.CarRepo;
import com.easycar.repo.DriverRepo;
import com.easycar.repo.PaymentRepo;
import com.easycar.repo.RentRepo;
import com.easycar.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static com.easycar.enums.RentRequest.PAY;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private RentRepo rentRepo;

    @Autowired
    private CarRepo carRepo;

    @Autowired
    private DriverRepo driverRepo;

    @Autowired
    private ModelMapper mapper;

    @Override
    public CustomDTO paymentIdGenerate() {
        return new CustomDTO(paymentRepo.getLastIndex());
    }

    @Override
    public void savePayment(PaymentDTO dto, String rentID) {
        Payment payment = mapper.map(dto, Payment.class);
        Rent rent = rentRepo.findById(rentID).get();
        if (rent.getRentDetails().get(0).getDriverID() != null) {

            Driver drivers = driverRepo.findById(rent.getRentDetails().get(0).getDriverID()).get();
            drivers.setDriverAvailability(AvailabilityType.AVAILABLE);
            driverRepo.save(drivers);

            Car car = carRepo.findById(rent.getRentDetails().get(0).getCarID()).get();
            car.setVehicleAvailabilityType(AvailabilityType.UNDER_MAINTAIN);
            carRepo.save(car);

            rent.setRentType(PAY);
            rentRepo.save(rent);

        }
        if (rent.getRentDetails().get(0).getDriverID() == null) {
            Car car = carRepo.findById(rent.getRentDetails().get(0).getCarID()).get();
            car.setVehicleAvailabilityType(AvailabilityType.UNDER_MAINTAIN);
            carRepo.save(car);
        }
        paymentRepo.save(payment);
    }

    @Override
    public ArrayList<PaymentDTO> getAllPayment() {
      return mapper.map(paymentRepo.findAll(),new TypeToken<ArrayList<PaymentDTO>>(){
      }.getType());
    }
}
