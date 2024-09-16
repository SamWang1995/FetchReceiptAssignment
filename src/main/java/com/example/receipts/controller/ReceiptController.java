package com.example.receipts.controller;


import com.example.receipts.ReceiptsApplication;
import com.example.receipts.model.Item;
import com.example.receipts.model.Receipt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {
    private final Map<String, Integer> receiptPointsMap = new HashMap<>();

    @PostMapping("/process")
    public Map<String, String> processReceipt(@RequestBody Receipt receipt) {
        // Generate a unique ID for the receipt
        String id = UUID.randomUUID().toString();
        // Calculate points
        int points = calculatePoints(receipt);
        // Store points in-memory
        receiptPointsMap.put(id, points);

        // Return the ID
        Map<String, String> response = new HashMap<>();
        response.put("id", id);
        return response;
    }

    @GetMapping("/{id}/points")
    public Map<String, Integer> getPoints(@PathVariable String id) {
        // Retrieve points for the given ID
        int points = receiptPointsMap.getOrDefault(id, 0);
        Map<String, Integer> response = new HashMap<>();
        response.put("points", points);
        return response;
    }

    private int calculatePoints(Receipt receipt) {
        int points = 0;

        // 1 point per alphanumeric character in retailer name
        points += receipt.getRetailer().replaceAll("[^a-zA-Z0-9]", "").length();
        System.out.println("points from name: " + points);
        // Check if the total amount is a round dollar amount or multiple of 0.25
        points += calculateMultiples(receipt);
        // 5 points for every two items
        points += calculateEveryTwo(receipt);
        // Points for item descriptions
        points += calculateItemDescription(receipt);
        // Points based on the purchase date and time
        points += calculatePurchaseDateTime(receipt);

        System.out.println("total points: " + points);
        return points;
    }

    private int calculateMultiples(Receipt receipt){
        int points = 0;
        double totalAmount = Double.parseDouble(receipt.getTotal());
        if (totalAmount % 1 == 0) {
            points += 50; // Total is a round dollar amount
        }
        if (totalAmount % 0.25 == 0) {
            points += 25; // Total is a multiple of 0.25
        }
        System.out.println("calculateMultiples: " + points);
        return points;
    }

    private int calculateEveryTwo(Receipt receipt){
        int points = 0;
        if (receipt.getItems() != null) {
            int itemCount = receipt.getItems().size();
            points += (itemCount / 2) * 5;
        }
        System.out.println("calculateEveryTwo: " + points);
        return points;
    }

    private int calculateItemDescription(Receipt receipt){
        int points = 0;
        if (receipt.getItems() != null) {
            for (Item item : receipt.getItems()) {
                String description = item.getShortDescription().trim();
                if (description.length() % 3 == 0) {
                    double price = Double.parseDouble(item.getPrice());
                    int itemPoints = (int) Math.ceil(price * 0.2);
                    points += itemPoints;
                }
            }
        }
        System.out.println("calculateItemDescription: " + points);
        return points;
    }

    private int calculatePurchaseDateTime(Receipt receipt){
        int points = 0;
        int day = Integer.parseInt(receipt.getPurchaseDate().split("-")[2]);
        if (day % 2 != 0) {
            points += 6; // Odd day
        }

        String[] timeParts = receipt.getPurchaseTime().split(":");
        int hour = Integer.parseInt(timeParts[0]);
        if (hour >= 14 && hour < 16) {
            points += 10; // Time is between 2 PM and 4 PM
        }
        System.out.println("calculatePurchaseDateTime: " + points);
        return points;
    }
}
