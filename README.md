# Purchase Order Management System  
*Built for Omega Wholesale Sdn Bhd (OWSB)*  

## Overview
This project is an **automated Purchase Order Management System** designed for Omega Wholesale Sdn Bhd (OWSB), a growing wholesale supplier based in Kuala Lumpur. The system replaces their manual procurement process with a structured, efficient, and error-resistant digital platform.

Developed using **Java (Object-Oriented Programming)** principles and **Swing GUI**, the system supports role-based access and document automation features essential for managing inventory, suppliers, requisitions, orders, and payments.

---

## Features
### 🔐 User Management
- Admin-controlled login & registration system
- Role-based access (Admin, Sales Manager, Purchase Manager, Inventory Manager, Finance Manager)

### 📦 Inventory & Supplier
- Add, update, delete, and search inventory items
- Supplier management with supplier-item relationships
- Low-stock alert & item validation

### 🛒 Procurement Workflow
- Create, approve, and manage **Purchase Requisitions (PR)**
- Generate & approve **Purchase Orders (PO)**
- Track PO statuses (Pending, Approved, Verified)

### 💰 Finance & Payments
- Supplier payment processing with validation
- Generate receipts with tax and total calculations

### 📊 Reports & Documents
- Generate **PDF reports** for:
  - Stock
  - Finance
  - Payment receipts
- Auto-generate and download receipts

---

## Object-Oriented Concepts Used
This project was built by fully applying **OOP principles**:
- ✅ **Encapsulation** — Private fields with public accessors (getters/setters)
- ✅ **Inheritance** — `User` superclass with subclasses for different roles
- ✅ **Polymorphism** — Overridden `accessMenu()` for role-specific UI
- ✅ **Abstraction** — Abstract classes/interfaces like `PdfGenerator`
- ✅ **Constructor Overloading**, **Composition**, **Association**, **Aggregation**, and **Exception Handling** implemented throughout

---

## User Roles & Access
| Role              | Key Functions |
|-------------------|----------------|
| **Admin**         | Manage users, view all modules |
| **Sales Manager** | Manage inventory, suppliers, daily sales, create PR |
| **Purchase Manager** | Approve PR, generate PO, manage suppliers |
| **Inventory Manager** | Track stock, generate stock reports |
| **Finance Manager** | Approve/verify PO, process payments, financial reports |

---

## Technologies Used
- Java (JDK 8+)
- Java Swing (GUI)
- File I/O for data storage (text-based)
- [iText PDF](https://itextpdf.com/en) (for PDF generation)

---

## How to Run
1. Open the project in **Apache NetBeans** or any Java IDE.
2. Run the `LoginGUI.java` or designated main class file.
3. Login using default/admin credentials or register new users (admin only).
4. Navigate features based on assigned roles.

> ⚠️ All data is stored in `.txt` files locally. Ensure file paths are correct if relocating the project.

---

## Screenshots
**Item Entry** <br>
<img width="717" height="542" alt="image" src="https://github.com/user-attachments/assets/4facbdb7-fbe8-4602-b55e-35d9640adc3d" />

**Supplier Entry**<br>
<img width="819" height="508" alt="image" src="https://github.com/user-attachments/assets/909c48fa-a321-46ef-977b-cbb789e314db" />

**Daily Sales Entry**<br>
<img width="683" height="445" alt="image" src="https://github.com/user-attachments/assets/6105937c-a279-418c-aee0-22a8ac958db5" />

**Purchase Requisition List**<br>
<img width="654" height="525" alt="image" src="https://github.com/user-attachments/assets/ded59c2e-679f-4ddf-9785-090f82df3da0" />

**Create Purchase Requisition**<br>
<img width="665" height="514" alt="image" src="https://github.com/user-attachments/assets/72f0413a-9350-41e2-abd6-11291706c60e" />

**View Purchase Order** <br>
<img width="817" height="470" alt="image" src="https://github.com/user-attachments/assets/b5649ee5-3774-4fc5-9743-7fa452421b55" />

**PR & PO Approval** <br>
<img width="828" height="488" alt="image" src="https://github.com/user-attachments/assets/a9aaa955-fb79-44cc-8598-aa04cca071ee" />
<img width="803" height="370" alt="image" src="https://github.com/user-attachments/assets/70aaa27a-b544-4a95-9ba9-d0b6c40f0f8b" />

**Manage Stock** <br>
<img width="827" height="445" alt="image" src="https://github.com/user-attachments/assets/07e2896f-5337-4b57-8ed2-91b8bb3f9f5d" />

**View Receipt** <br>
<img width="721" height="494" alt="image" src="https://github.com/user-attachments/assets/aa8ac4f0-e478-4936-8725-297268a670b1" />

---

## Conclusion
The Purchase Order Management System streamlines OWSB’s procurement workflow by:
- Reducing manual errors
- Improving internal coordination
- Supporting data-driven decisions via reports

While effective, its dependency on accurate data input and basic security measures should be noted for real-world deployment.
