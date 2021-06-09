/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.cput.assignment3project.Solution;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import za.ac.cput.assignment3project.Customer;
import za.ac.cput.assignment3project.Supplier;

/**
 *
 * @author Zukisa Molisi 217089062
 */
public class Solution {

    ArrayList<Customer> customerList = new ArrayList<>();
    ArrayList<Supplier> supplierList = new ArrayList<>();

    //read file
    public List<Object> readFile(String file) {
        ObjectInputStream objectInputStream = null;
        List<Object> objects = new ArrayList();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            objectInputStream = new ObjectInputStream(bufferedInputStream);
            while (true) {

                objects.add(objectInputStream.readObject());
            }

        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex);
        } finally {
            try {
                objectInputStream.close();
            } catch (IOException ioException) {
                System.out.println(ioException);
            }
        }

        return objects;

    }

    //store values
    public void storeItems(List<Object> objectList) {
        for (Object object : objectList) {
            if (object instanceof Customer) {
                customerList.add((Customer) object);
            }
            if (object instanceof Supplier) {
                supplierList.add((Supplier) object);
            }

        }
    }

    //sort customer list
    public ArrayList<Customer> sortCustomerList() {
        customerList.sort(new SortCustomerById());
        return customerList;

    }

    //calculate customer age 
    public int calculateCustomerAge(String dob) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //convert String to LocalDate
        LocalDate birthDate = LocalDate.parse(dob, formatter);
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();

    }

    //format date
    public String formatDate(String date) {

        DateTimeFormatter stringDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //convert String to LocalDate
        LocalDate localDate = LocalDate.parse(date, stringDateFormatter);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        String formattedDateString = localDate.format(formatter);

        return formattedDateString;
    }

    //writeCustomerDetails
    public void writeCustomerDetailsToFile() {
        try {

            // Open the file in append mode.
            FileWriter fw = new FileWriter("customerOutFile.txt", false);
            PrintWriter out = new PrintWriter(fw);
            

            String formatStr = "%-10s %-15s %-15s %-15s %-15s";
            // Append the name of ocean to the file
            out.println("================================== CUSTOMERS ==============================");
            out.println(String.format(formatStr,"ID","Name", "Surname", "Date of birth", "Age"));
            out.println("===========================================================================");
            
            for (Customer c : customerList) {
                 out.println(String.format(formatStr,c.getStHolderId(),c.getFirstName(), c.getSurName(),formatDate(c.getDateOfBirth()),calculateCustomerAge(c.getDateOfBirth())));
                
            }
            out.println("\n");
           
            out.println("Number of customers who can rent:\t"+ countCustomersWhoCanRent() );
            out.println("Number of customers who cannot rent:\t"+ countCustomersWhoCannotRent() );
            // Close the file.
            out.close();
            System.out.println("The Object  was succesfully written to a file");

        } catch (IOException ex) {
            System.out.println(ex);
        }

    }

    //calculate number of customers who can rent 
    public int countCustomersWhoCanRent() {
        int count = 0;
        for (Customer customer : customerList) {
            if (customer.getCanRent()) {
                count++;
            }
        }
        return count;
    }

    //calculate customers who cannot rent
    public int countCustomersWhoCannotRent() {
        int count = 0;
        for (Customer customer : customerList) {
            if (!customer.getCanRent()) {
                count++;
            }
        }
        return count;
    }

    //sort supplier arrayList in ascending order 
    public ArrayList<Supplier> sortSupplierList() {
        supplierList.sort(new SortSupplierByName());
        return supplierList;

    }

    //write supplier Details
    public void writeSupplierDetailsToFile() {
            try {

            // Open the file in append mode.
            FileWriter fw = new FileWriter("supplierOutFile.txt", false);
            PrintWriter out = new PrintWriter(fw);

            String formatStr = "%-10s %-20s %-15s %-15s";
            // Append heading to the text file
            out.println("================================== SUPPLIERS ==============================");
             out.println(String.format(formatStr,"ID","Name", "Prod Type", "Description"));
            out.println("===========================================================================");
            
            for (Supplier s : supplierList) {
          
                out.println(String.format(formatStr,s.getStHolderId(),s.getName(), s.getProductType(), s.getProductDescription()));
            }
            out.println("\n");
           
            // Close the file.
            out.close();
            System.out.println("The Object  was succesfully written to a file");

        } catch (IOException ex) {
            System.out.println(ex);
        }

    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        List<Object> objects = new ArrayList<>();

        objects = solution.readFile("stakeholder.ser");
        solution.storeItems(objects);
        solution.sortCustomerList();
        solution.writeCustomerDetailsToFile();
        solution.writeSupplierDetailsToFile();

    }
}
