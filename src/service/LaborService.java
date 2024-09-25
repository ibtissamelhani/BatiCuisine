package service;

import model.entities.Labor;
import model.entities.Material;
import repository.LaborRepositoryImpl;
import repository.interfaces.LaborRepository;

public class LaborService {

    private LaborRepository laborRepository;

    public LaborService(LaborRepository laborRepository) {
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
