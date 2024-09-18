package service;

import model.entities.Material;
import repository.MaterialRepositoryImpl;

public class MaterialService {

    private MaterialRepositoryImpl materialRepository;

    public MaterialService(MaterialRepositoryImpl materialRepository) {
        this.materialRepository = materialRepository;
    }

    public boolean addMaterialToProject(Material material) {
        try {
            materialRepository.save(material);
            return true;
        } catch (Exception e) {
            System.out.println("Error adding material: " + e.getMessage());
            return false;
        }
    }
}
