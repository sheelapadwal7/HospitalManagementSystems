package com.hms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.hms.model.Inventory;
import com.hms.repository.InventoryRepository;
import com.hms.dto.InventoryDTO;
import com.hms.mapper.InventoryMapper;

@Service
public class InventoryService {

    @Autowired
    InventoryRepository inventoryRepository;
    @Autowired
    InventoryMapper inventoryMapper;

    
    public Page<InventoryDTO> getAllInventory(Pageable pageable) {
        Page<Inventory> inventoryPage = inventoryRepository.findAll(pageable);
        return inventoryPage.map(inventoryMapper::toDto);
    }
    
    public List<InventoryDTO> getAllInventory() {
        List<Inventory> inventoryList = inventoryRepository.findAll();
        return inventoryList.stream()
                .map(inventoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<InventoryDTO> getInventoryById(Integer id) {
        Optional<Inventory> optionalInventory = inventoryRepository.findById(id);
        return optionalInventory.map(inventoryMapper::toDto);
    }

    public InventoryDTO createInventory(InventoryDTO inventoryDTO) {
        Inventory inventory = inventoryMapper.toEntity(inventoryDTO);
        inventory = inventoryRepository.save(inventory);
        return inventoryMapper.toDto(inventory);
    }

    public InventoryDTO updateInventory(Integer id, InventoryDTO inventoryDTO) {
        Inventory existingInventory = inventoryRepository.findById(id).orElse(null);
        if (existingInventory == null) {
            return null; // or throw exception
        }
        Inventory updatedInventory = inventoryMapper.toEntity(inventoryDTO);
        updatedInventory.setId(id);
        updatedInventory = inventoryRepository.save(updatedInventory);
        return inventoryMapper.toDto(updatedInventory);
    }

    public void deleteInventory(Integer id) {
        inventoryRepository.deleteById(id);
    }

}
