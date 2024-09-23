package repository.interfaces;

import model.entities.Project;

import java.util.Optional;

public interface ProjectRepository {

    Project save(Project project);
    Optional<Project> findById(int id);
    Boolean update(Project project);
    Optional<Project> findByNameAndClient(String projectName, String clientName);
    Project findProjectWithDetails(int projectId);
}
