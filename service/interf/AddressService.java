package com.schoolProject.ecommerceApplication.service.interf;

import com.schoolProject.ecommerceApplication.dto.AddressDto;
import com.schoolProject.ecommerceApplication.dto.Response;

public interface AddressService {
    Response saveAndUpdateAddress(AddressDto addressDto);
}

