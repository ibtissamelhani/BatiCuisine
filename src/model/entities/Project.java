package model.entities;

import model.enums.ProjectStatus;

import java.util.List;

public class Project {

    private int id;
    private String projectName;
    private Double profitMargin;
    private Double totalCost;
    private ProjectStatus projectStatus;
    private Double surfaceArea ;
    private List<Component> componentList;
    private Client client;

    public Project() {
    }

    public Project(String projectName, Double profitMargin, Double totalCost, ProjectStatus projectStatus,Double surfaceArea, List<Component> componentList, Client client) {
        this.projectName = projectName;
        this.profitMargin = profitMargin;
        this.totalCost = totalCost;
        this.projectStatus = projectStatus;
        this.surfaceArea = surfaceArea;
        this.componentList = componentList;
        this.client = client;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Double getProfitMargin() {
        return profitMargin;
    }

    public void setProfitMargin(Double profitMargin) {
        this.profitMargin = profitMargin;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

    public List<Component> getComponentList() {
        return componentList;
    }

    public void setComponentList(List<Component> componentList) {
        this.componentList = componentList;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
