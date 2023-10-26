package com.easycar.dto;

import com.easycar.embeded.ImageDTO;
import com.easycar.embeded.Rate;
import com.easycar.enums.AvailabilityType;
import com.easycar.enums.CarType;
import com.easycar.enums.FuelType;
import com.easycar.enums.TransmissionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarDTO {

    private String car_Id;

    private String name;
    private String brand;
    private CarType type;
    private ImageDTO image;
    private int number_Of_Passengers;
    private TransmissionType transmission_Type;
    private FuelType fuel_Type;
    private Rate rent_Duration_Price;
    private double price_Extra_KM;
    private String registration_Number;
    private double free_Mileage;
    private String color;
    private AvailabilityType vehicleAvailabilityType;
}
