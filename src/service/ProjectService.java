package service;

import model.entities.Project;
import repository.ProjectRepositoryImpl;

public class ProjectService {

    private ProjectRepositoryImpl projectRepository;

    public ProjectService(ProjectRepositoryImpl projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }
}
