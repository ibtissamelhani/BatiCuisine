package model.entities;

import java.util.ArrayList;
import java.util.List;

public class Client {

    private int id;
    private String name;
    private String address;
    private String phone;
    private Boolean isProfessional;
    private List<Project> projects;

    public Client() {
    }

    public Client(String name, String address, String phone, Boolean isProfessional) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.isProfessional = isProfessional;
        this.projects = new ArrayList<>();
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getProfessional() {
        return isProfessional;
    }

    public void setProfessional(Boolean professional) {
        isProfessional = professional;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", isProfessional=" + isProfessional +
                ", projects=" + projects +
                '}';
    }
}
