package repository.interfaces;

import model.entities.Project;

public interface ProjectRepository {

    boolean save(Project project);
}
