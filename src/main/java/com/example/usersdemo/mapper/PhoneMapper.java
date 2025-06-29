package com.example.usersdemo.mapper;

import com.example.usersdemo.models.entity.Phone;
import com.example.usersdemo.dto.phone.PhoneRequestDTO;
import com.example.usersdemo.dto.phone.PhoneResponseDTO;

public class PhoneMapper {

    private PhoneMapper() {
    }

    /**
     * Converts a PhoneRequestDTO to a Phone entity.
     * 
     * @param dto
     * @return
     */
    public static Phone toEntity(PhoneRequestDTO dto) {
        if (dto == null)
            return null;
        Phone phone = new Phone();
        phone.setNumber(dto.getNumber());
        phone.setCityCode(dto.getCityCode());
        phone.setCountryCode(dto.getCountryCode());
        return phone;
    }

    /**
     * Converts a Phone entity to a PhoneResponseDTO.
     * 
     * @param phone
     * @return
     */
    public static PhoneResponseDTO toResponseDTO(Phone phone) {
        if (phone == null)
            return null;
        PhoneResponseDTO dto = new PhoneResponseDTO();
        dto.setNumber(phone.getNumber());
        dto.setCityCode(phone.getCityCode());
        dto.setCountryCode(phone.getCountryCode());
        return dto;
    }
}
