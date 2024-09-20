package service;

import model.entities.Client;
import model.entities.Project;
import repository.ProjectRepositoryImpl;

import java.util.Optional;
import java.util.Scanner;

public class ProjectService {

    private ProjectRepositoryImpl projectRepository;
    private ClientService clientService ;
    private Scanner scanner = new Scanner(System.in);

    public ProjectService(ProjectRepositoryImpl projectRepository, ClientService clientService) {
        this.projectRepository = projectRepository;
        this.clientService = clientService;
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public void calculateCost(int id, Double profitMargin, Double meterialCost, Double laborCost) {
        double totalCost = 0.0;


        Optional<Project> OptionalProject = projectRepository.findById(id);

        if (!OptionalProject.isPresent()) {
            System.err.println("Project not found");
        }



        Project project = OptionalProject.get();
        totalCost = totalCost + meterialCost + laborCost;
        System.out.println("---->Total cost of the project: " + String.format("%.2f", totalCost) + " €");

        // Add the profit margin to the total cost
        totalCost = totalCost * (1 + (profitMargin/100));
        System.out.println("---->Total cost of the project after profit margin: " + String.format("%.2f", totalCost) + " €");

        System.out.print("Would you like to apply VAT to the project? (y/n): ");
        String applyVAT = scanner.nextLine();

        //apply TVA
        double TVAPercentage = 0.0;
        if (applyVAT.equalsIgnoreCase("y")) {
            System.out.print("Enter the VAT percentage (%): ");
            TVAPercentage = scanner.nextDouble();
            scanner.nextLine();

            totalCost =  totalCost * (1 + (TVAPercentage/100));
            System.out.println("---->Total cost of the project after applying VTA: " + String.format("%.2f", totalCost) + " €");
        }

        //if client is pro apply discount
        Optional<Client> client = clientService.getClientById(project.getClient().getId());
        double discount = 0.0;
        if (client.isPresent() && client.get().getProfessional()) {
            System.out.println("client : "+client.get().getName()+" is professional client");
            System.out.print("Would you like to give discount for this client? (y/n): ");
            String applyDiscount = scanner.nextLine();
            if (applyDiscount.equalsIgnoreCase("y")) {
                System.out.print("Enter the discount percentage (%): ");
                discount = scanner.nextDouble();
                scanner.nextLine();
            }
            // Add the discount  to the total cost
            totalCost = totalCost * (1 - (discount/100));
            System.out.println("---->Total cost of the project after discount: " + String.format("%.2f", totalCost) + " €");

        }

    }

}
