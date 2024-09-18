package service;

import model.entities.Labor;
import model.entities.Material;
import repository.LaborRepositoryImpl;

public class LaborService {

    private LaborRepositoryImpl laborRepository;

    public LaborService(LaborRepositoryImpl laborRepository) {
        this.laborRepository = laborRepository;
    }

    public boolean addLabor(Labor labor) {
        try {
            laborRepository.save(labor);
            return true;
        } catch (Exception e) {
            System.out.println("Error adding labor: " + e.getMessage());
            return false;
        }
    }
}
