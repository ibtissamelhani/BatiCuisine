package service;

import model.entities.Project;
import repository.ProjectRepositoryImpl;

import java.util.Optional;

public class ProjectService {

    private ProjectRepositoryImpl projectRepository;

    public ProjectService(ProjectRepositoryImpl projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Boolean addCalculatedCostToProject(Project project, double totalCost, double profitMargin) {

        Optional<Project> opProject = projectRepository.findById(project.getId());

        if (!opProject.isPresent()) {
            System.err.println("Project not found");
            return false;
        }

        opProject.get().setProfitMargin(profitMargin);
        opProject.get().setTotalCost(totalCost);

        boolean success = projectRepository.update(opProject.get());
        if (success) {
            System.out.println("Project updated");
            return true;
        }else {
            System.out.println("Project not updated");
            return false;
        }

    }

    public Optional<Project> findByNameAndClientName(String projectName, String clientName){
        return projectRepository.findByNameAndClient(projectName, clientName);
    }

    public boolean update(Project project) {
        return projectRepository.update(project);
    }

    public Project findProjectWithDetails(int id) {
        return projectRepository.findProjectWithDetails(id);
    }
}
