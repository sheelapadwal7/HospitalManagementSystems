package com.hms.mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.hms.dto.InventoryDTO;
import com.hms.model.Inventory;

@Component
public class InventoryMapper {

    private final ModelMapper modelMapper;

    public InventoryMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public InventoryDTO toDto(Inventory inventory) {
        return modelMapper.map(inventory, InventoryDTO.class);
    }

    public Inventory toEntity(InventoryDTO inventoryDTO) {
        return modelMapper.map(inventoryDTO, Inventory.class);
    }
}
