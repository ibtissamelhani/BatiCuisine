package service;

import model.entities.Material;
import repository.MaterialRepositoryImpl;
import repository.interfaces.MaterialRepository;

public class MaterialService {

    private MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public boolean addMaterial(Material material) {
        try {
            materialRepository.save(material);
            return true;
        } catch (Exception e) {
            System.out.println("Error adding material: " + e.getMessage());
            return false;
        }
    }
}
