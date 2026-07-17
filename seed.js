// MongoDB database seeder script for Canteen Menu & Ratings System
// To run: docker exec -i canteen-mongo mongosh db < seed.js

print("--- Starting Database Seeding ---");

// Drop existing collections to ensure a clean state
print("Dropping existing collections...");
db.users.drop();
db.menuitems.drop();
db.orders.drop();

// Seed users
print("Seeding users...");
db.users.insertMany([
  {
    _id: "u1",
    email: "admin@canteen.com",
    password: "Password123",
    role: "ADMIN"
  },
  {
    _id: "u2",
    email: "staff@canteen.com",
    password: "Password123",
    role: "STAFF"
  },
  {
    _id: "u3",
    email: "customer@canteen.com",
    password: "Password123",
    role: "CUSTOMER"
  }
]);

// Helper variables for dynamic dates
var today = new Date().toISOString().slice(0, 10);
var yesterday = new Date(Date.now() - 86400000).toISOString().slice(0, 10);
var tomorrow = new Date(Date.now() + 86400000).toISOString().slice(0, 10);

// Seed menu items
print("Seeding menu items...");
db.menuitems.insertMany([
  {
    name: "Butter Chicken",
    description: "Tender grilled chicken pieces simmered in a rich, velvety, spiced tomato and butter sauce.",
    imageUrl: "https://images.unsplash.com/photo-1603894584373-5ac82b2ae398?auto=format&fit=crop&q=80&w=400",
    price: 220.0,
    date: today,
    ratings: [
      { stars: 5, userId: "u3", feedback: "Absolutely outstanding! Best Butter Chicken in town.", timestamp: new Date() },
      { stars: 4, userId: "u3", feedback: "Very creamy, just a bit too sweet for my personal taste.", timestamp: new Date() }
    ]
  },
  {
    name: "Paneer Butter Masala",
    description: "Soft Indian cottage cheese cubes cooked in a rich, creamy, and mildly sweet tomato butter gravy.",
    imageUrl: "https://images.unsplash.com/photo-1631452180519-c014fe946bc7?auto=format&fit=crop&q=80&w=400",
    price: 180.0,
    date: today,
    ratings: [
      { stars: 5, userId: "u3", feedback: "So rich and flavor-packed! Goes perfectly with butter naan.", timestamp: new Date() }
    ]
  },
  {
    name: "Crispy Masala Dosa",
    description: "Fermented thin rice crepe filled with spiced potato bhaji, served with fresh coconut chutney and hot sambar.",
    imageUrl: "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?auto=format&fit=crop&q=80&w=400",
    price: 90.0,
    date: today,
    ratings: [
      { stars: 5, userId: "u3", feedback: "Incredibly crispy and fresh. Chutney is delicious.", timestamp: new Date() }
    ]
  },
  {
    name: "Chicken Biryani",
    description: "Aromatic basmati rice layered with marinated chicken, fresh mint, coriander, caramelized onions, and saffron.",
    imageUrl: "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?auto=format&fit=crop&q=80&w=400",
    price: 250.0,
    date: today,
    ratings: [
      { stars: 4, userId: "u3", feedback: "Flavorful and portion size is huge. Chicken was perfectly tender.", timestamp: new Date() }
    ]
  },
  {
    name: "Veg Hakka Noodles",
    description: "Stir-fried noodles tossed with crunchy colorful vegetables, garlic, scallions, and savory soy sauces.",
    imageUrl: "https://images.unsplash.com/photo-1585032226651-759b368d7246?auto=format&fit=crop&q=80&w=400",
    price: 120.0,
    date: today,
    ratings: []
  },
  {
    name: "Classic Margherita Pizza",
    description: "Neapolitan-style pizza topped with simple san marzano tomato sauce, fresh mozzarella cheese, and basil leaves.",
    imageUrl: "https://images.unsplash.com/photo-1604382354936-07c5d9983bd3?auto=format&fit=crop&q=80&w=400",
    price: 190.0,
    date: tomorrow, // Available tomorrow
    ratings: []
  },
  {
    name: "Chocolate Lava Cake",
    description: "Decadent warm chocolate cake filled with a rich, oozing liquid chocolate center.",
    imageUrl: "https://images.unsplash.com/photo-1606313564200-e75d5e30476c?auto=format&fit=crop&q=80&w=400",
    price: 110.0,
    date: yesterday, // Served yesterday
    ratings: [
      { stars: 5, userId: "u3", feedback: "Incredibly delicious chocolate eruption!", timestamp: new Date() }
    ]
  }
]);

// Fetch menu items to link their actual IDs in orders
var paneer = db.menuitems.findOne({ name: "Paneer Butter Masala" });
var dosa = db.menuitems.findOne({ name: "Crispy Masala Dosa" });
var biryani = db.menuitems.findOne({ name: "Chicken Biryani" });

// Seed orders
print("Seeding orders...");
db.orders.insertMany([
  {
    userId: "u3",
    menuItemId: paneer._id.toString(),
    quantity: 2,
    status: "PENDING",
    orderedAt: new Date()
  },
  {
    userId: "u3",
    menuItemId: dosa._id.toString(),
    quantity: 1,
    status: "PREPARING",
    orderedAt: new Date(Date.now() - 1800000)
  },
  {
    userId: "u3",
    menuItemId: biryani._id.toString(),
    quantity: 1,
    status: "DELIVERED",
    orderedAt: new Date(Date.now() - 7200000)
  }
]);

print("--- Database Seeding Completed Successfully ---");
