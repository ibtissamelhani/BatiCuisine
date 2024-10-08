package model.entities;

import model.enums.ComponentType;

public class Material extends Component {

    private int id;
    private Double unitCost;
    private Double quantity;
    private Double transportCost;
    private Double qualityCoefficient;

    public Material() {
    }

    public Material(String name, ComponentType componentType, Double taxRate, Project project, Double unitCost, Double quantity, Double transportCost, Double qualityCoefficient) {
        super(name, componentType, taxRate,project);
        this.unitCost = unitCost;
        this.quantity = quantity;
        this.transportCost = transportCost;
        this.qualityCoefficient = qualityCoefficient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(Double unitCost) {
        this.unitCost = unitCost;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getTransportCost() {
        return transportCost;
    }

    public void setTransportCost(Double transportCost) {
        this.transportCost = transportCost;
    }

    public Double getQualityCoefficient() {
        return qualityCoefficient;
    }

    public void setQualityCoefficient(Double qualityCoefficient) {
        this.qualityCoefficient = qualityCoefficient;
    }

    public Double calculateTotalCost() {
        Double costBeforeTax = (unitCost * quantity * qualityCoefficient) + transportCost;
        Double costWithTax = costBeforeTax * (1 + (getTaxRate() / 100));
        return costWithTax;
    }
}
