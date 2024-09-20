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

    public void addCalculatedCostToProject(int id, Double totalCost, Double profitMargin) {

        Optional<Project> OptionalProject = projectRepository.findById(id);

        if (!OptionalProject.isPresent()) {
            System.err.println("Project not found");
        }

        Project project = OptionalProject.get();
        project.setTotalCost(totalCost);
        project.setProfitMargin(profitMargin);

    }

}
