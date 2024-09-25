package repository;

import model.entities.*;
import model.enums.ComponentType;
import model.enums.ProjectStatus;
import repository.interfaces.ClientRepository;
import repository.interfaces.ProjectRepository;

import java.sql.*;
import java.util.*;

public class ProjectRepositoryImpl implements ProjectRepository {

    private ClientRepository clientRepository;
    Connection connection;
    public ProjectRepositoryImpl(ClientRepository clientRepository, Connection connection) {
        this.connection = connection;
        this.clientRepository = clientRepository;
    }

    @Override
    public Project save(Project project) {
        String sql = "INSERT INTO Projects (project_name, profit_margin, total_cost, project_status, surface_area, client_id) VALUES (?, ?, ?, ?::project_status, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, project.getProjectName());
            statement.setDouble(2, project.getProfitMargin());
            statement.setDouble(3, project.getTotalCost());
            statement.setObject(4, project.getProjectStatus().name());
            statement.setDouble(5, project.getSurfaceArea());
            statement.setInt(6, project.getClient().getId());
            statement.executeUpdate() ;

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                project.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return project;
    }

    @Override
    public Optional<Project> findById(int id) {

        Project project = new Project();

        String sql = "SELECT * FROM projects WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                project.setId(rs.getInt("id"));
                project.setProjectName(rs.getString("project_name"));
                project.setProfitMargin(rs.getDouble("profit_margin"));
                project.setTotalCost(rs.getDouble("total_cost"));
                project.setSurfaceArea(rs.getDouble("surface_area"));
                project.setProjectStatus(ProjectStatus.valueOf(rs.getString("project_status")));
                project.setClient(clientRepository.findById(rs.getInt("client_id")).get());
                return Optional.of(project);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Project> findByNameAndClient(String projectName, String clientName) {
        Optional<Client> client = clientRepository.findByName(clientName);
        if (!client.isPresent()) {
            return Optional.empty();
        }
        int client_id = client.get().getId();

        String query = "SELECT * FROM Projects WHERE project_name = ? AND client_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, projectName);
            ps.setInt(2, client_id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Project project = new Project();
                    project.setId(rs.getInt("id"));
                    project.setProjectName(rs.getString("project_name"));
                    project.setProfitMargin(rs.getDouble("profit_margin"));
                    project.setTotalCost(rs.getDouble("total_cost"));
                    project.setSurfaceArea(rs.getDouble("surface_area"));
                    project.setProjectStatus(ProjectStatus.valueOf(rs.getString("project_status")));
                    project.setClient(clientRepository.findById(rs.getInt("client_id")).get());
                    return Optional.of(project);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();

    }

    @Override
    public  Boolean update(Project project) {
        String query = "UPDATE Projects SET project_name = ?, profit_margin = ?, total_cost = ?, project_status = ?::project_status, surface_area = ?, client_id = ? WHERE id = ?";
        boolean isUpdated = false;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, project.getProjectName());
            stmt.setDouble(2, project.getProfitMargin());
            stmt.setDouble(3, project.getTotalCost());
            stmt.setObject(4, project.getProjectStatus().name());
            stmt.setDouble(5, project.getSurfaceArea());
            stmt.setInt(6, project.getClient().getId());
            stmt.setInt(7, project.getId());
            int rowsAffected = stmt.executeUpdate();
            isUpdated = rowsAffected > 0;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return isUpdated;
    }

    @Override
    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT p.id AS project_id, p.project_name, p.total_cost, p.profit_margin, p.surface_area, p.project_status, " +
                "c.id AS client_id, c.name AS client_name, c.address AS client_address, c.phone AS client_phone, c.is_professional, " +
                "comp.id AS component_id, comp.name AS component_name, comp.component_type, comp.tax_rate, " +
                "m.unit_cost, m.transport_cost, m.quality_coefficient, m.quantity, " +
                "l.hourly_rate, l.work_hours, l.worker_productivity " +
                "FROM Projects p " +
                "JOIN Clients c ON p.client_id = c.id " +
                "LEFT JOIN Components comp ON p.id = comp.project_id " +
                "LEFT JOIN Materials m ON comp.id = m.component_id " +
                "LEFT JOIN Labors l ON comp.id = l.component_id " +
                "ORDER BY p.id, comp.id";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            Map<Integer, Project> projectMap = new HashMap<>();

            while (rs.next()) {
                // Retrieve Client Information
                Client client = new Client();
                client.setId(rs.getInt("client_id"));
                client.setName(rs.getString("client_name"));
                client.setAddress(rs.getString("client_address"));
                client.setPhone(rs.getString("client_phone"));
                client.setProfessional(rs.getBoolean("is_professional"));

                // Retrieve Project Information
                int projectId = rs.getInt("project_id");
                Project project = projectMap.get(projectId);
                if (project == null) {
                    project = new Project();
                    project.setId(projectId);
                    project.setProjectName(rs.getString("project_name"));
                    project.setTotalCost(rs.getDouble("total_cost"));
                    project.setProfitMargin(rs.getDouble("profit_margin"));
                    project.setSurfaceArea(rs.getDouble("surface_area"));
                    project.setProjectStatus(ProjectStatus.valueOf(rs.getString("project_status")));
                    project.setClient(client);
                    projectMap.put(projectId, project);
                }

                // Retrieve Component Information
                String componentType = rs.getString("component_type");

                if ("MATERIAL".equals(componentType)) {
                    Material material = new Material();
                    material.setId(rs.getInt("component_id"));
                    material.setName(rs.getString("component_name"));
                    material.setQuantity(rs.getDouble("quantity"));
                    material.setTaxRate(rs.getDouble("tax_rate"));
                    material.setUnitCost(rs.getDouble("unit_cost"));
                    material.setTransportCost(rs.getDouble("transport_cost"));
                    material.setQualityCoefficient(rs.getDouble("quality_coefficient"));
                    project.getComponentList().add(material);  // Add material as a component
                } else if ("LABOR".equals(componentType)) {
                    Labor labor = new Labor();
                    labor.setId(rs.getInt("component_id"));
                    labor.setName(rs.getString("component_name"));
                    labor.setTaxRate(rs.getDouble("tax_rate"));
                    labor.setHourlyRate(rs.getDouble("hourly_rate"));
                    labor.setWorkHours(rs.getDouble("work_hours"));
                    labor.setWorkerProductivity(rs.getDouble("worker_productivity"));
                    project.getComponentList().add(labor);  // Add labor as a component
                }
            }

            projects.addAll(projectMap.values());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projects;
    }



}
