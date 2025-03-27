# Reward_API
# Rewards Calculation API

This is a simple Spring Boot application that calculates customer rewards based on their transaction history. It provides endpoints to add transactions and fetch reward points for a given month or the last three months.

## Features 
- Add transactions dynamically
- Calculate reward points for a specific month
- Get rewards for the last three months
- Handles exceptions gracefully (invalid dates, missing customers, etc.)

## How It Works 
- Customers earn points based on their transaction amount:
  - $50 - $100: 1 point for every dollar over $50
  - Over $100: 2 points for every dollar over $100 + 1 point per dollar over $50
- Transactions older than 3 months are ignored

## Endpoints 

### Add Transaction
```http
POST /api/rewards/addTransaction/{customerId}/{amount}/{year}/{month}/{day}
```
_Adds a new transaction for a customer._

### Get Monthly Rewards
```http
GET /api/rewards/calculate/{customerId}/{month}/{year}
```
_Calculates reward points for a given month and year._

### Get Rewards for Last 3 Months
```http
GET /api/rewards/calculate/previous-three-months/{customerId}
```
_Calculates reward points for the previous three months._

## Tech Stack 🛠️
- Java 17
- Spring Boot
- Maven
- H2 Database (or configure your own)



