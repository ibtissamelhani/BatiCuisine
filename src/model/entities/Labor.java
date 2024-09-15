package model.entities;

import model.enums.ComponentType;

public class Labor extends Component {

    private int id;
    private Double hourlyRate;
    private Double workHours;
    private Double workerProductivity;

    public Labor() {
    }

    public Labor(String name, ComponentType componentType, Double taxRate, Double hourlyRate, Double workHours, Double workerProductivity) {
        super(name, componentType, taxRate);
        this.hourlyRate = hourlyRate;
        this.workHours = workHours;
        this.workerProductivity = workerProductivity;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public Double getWorkHours() {
        return workHours;
    }

    public void setWorkHours(Double workHours) {
        this.workHours = workHours;
    }

    public Double getWorkerProductivity() {
        return workerProductivity;
    }

    public void setWorkerProductivity(Double workerProductivity) {
        this.workerProductivity = workerProductivity;
    }

    public Double calculateCost() {
        System.out.println("Labor calculateCost");
        return 0.0;
    }
}
