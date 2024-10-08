package model.entities;

import model.enums.ComponentType;

public abstract class Component {

    private int id;
    private String name;
    private ComponentType componentType;
    private Double taxRate;
    private Project project;

    public Component() {
    }

    public Component(String name, ComponentType componentType, Double taxRate, Project project) {
        this.name = name;
        this.componentType = componentType;
        this.taxRate = taxRate;
        this.project = project;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public abstract Double calculateTotalCost();
}
