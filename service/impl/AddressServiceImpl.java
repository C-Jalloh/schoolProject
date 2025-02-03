package com.schoolProject.ecommerceApplication.service.impl;

import com.schoolProject.ecommerceApplication.dto.AddressDto;
import com.schoolProject.ecommerceApplication.dto.Response;
import com.schoolProject.ecommerceApplication.entity.Address;
import com.schoolProject.ecommerceApplication.entity.User;
import com.schoolProject.ecommerceApplication.repository.AddressRepo;
import com.schoolProject.ecommerceApplication.service.interf.AddressService;
import com.schoolProject.ecommerceApplication.service.interf.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepo addressRepo;
    private final UserService userService;


    @Override
    public Response saveAndUpdateAddress(AddressDto addressDto) {
        User user = userService.getLoginUser();
        Address address = new Address();

        if (user != null){
            address = new Address();
            address.setUser(user);
        }

        if (addressDto.getStreet() != null) address.setStreet(addressDto.getStreet());
        if (addressDto.getState() != null) address.setState(addressDto.getState());
        if (addressDto.getCity() != null) address.setCity(addressDto.getCity());
        // change the c in zipcode to capital C
        if (addressDto.getZipCode() != null) address.setZipCode(addressDto.getZipCode());
        if (addressDto.getCountry() != null) address.setCountry(addressDto.getCountry());

        addressRepo.save(address);

        String message = (user.getAddress() == null) ? "Address successfully created": "Address successfully updated";
        return Response.builder()
                .status(200)
                .message(message)
                .build();
    }
}
