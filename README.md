# BatiCuisine

### Kitchen Construction Cost Estimation Application

---

## Description

**BatiCuisine** is a Java application designed for professionals in kitchen construction and renovation. It allows users to manage projects by automatically calculating the costs of materials, labor, taxes, and profit margins. The application simplifies the cost estimation process and quotation generation for kitchen projects, while also ensuring effective management of clients and projects.

---

## Features

- **Client Management**: Add, edit, and manage client information (individuals or businesses).
- **Project Creation and Management**: Create a new project for a client, manage its components (materials, labor), and estimate costs.
- **Material Management**: Add materials with unit cost, quantity, and transportation cost.
- **Labor Management**: Track labor costs based on hourly rate and hours worked.
- **Quotation Generation**: Create detailed quotations with estimated costs for materials, labor, and taxes, with the option to add profit margins and discounts.
- **Cost Calculation**: Automatically calculate the total project cost, considering materials, labor, VAT, and discounts.
- **Discount Management**: Apply discounts for professional clients.

---

## Prerequisites

- **Java 8** or later
- **PostgreSQL** for the database
- **JDBC** for database connection management

---

## Installation

### 1. Clone the GitHub repository:

```bash
git clone https://github.com/your-username/baticuisine.git

## Configure the database:
Create a PostgreSQL database named BatiCuisine.
Run the SQL scripts located in the sql folder to create the necessary tables.

## Project Structure
The project follows a layered architecture to ensure a clear separation of concerns:
UI Layer: User interface (interactions with the user).
Service Layer: Business logic, handling quotes, clients, and projects.
Repository Layer: Data access via JDBC, managing transactions with the database.
Model Layer: Defines entities (Client, Project, Quote, Material, Labor).
