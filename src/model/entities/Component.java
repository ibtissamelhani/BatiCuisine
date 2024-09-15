package model.entities;

import model.enums.ComponentType;

public abstract class Component {

    private int id;
    private String name;
    private ComponentType componentType;
    private Double taxRate;

    public Component() {
    }

    public Component(String name, ComponentType componentType, Double taxRate) {
        this.name = name;
        this.componentType = componentType;
        this.taxRate = taxRate;
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

    public abstract Double calculateCost();
}
