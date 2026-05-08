# **Image Manager**

---

### **Setup Instructions**

**1. Backend (Spring Boot & Java 25)**
* **Database:** Ensure PostgreSQL is running and create a database named `task1_db`.
* **Configuration:** Update `src/main/resources/application.properties` with your PostgreSQL username and password.
* **Persistence:** The application uses Hibernate `ddl-auto: update` to automatically build the schema, including fields for **Dimensions**, **Format**, and **FileSize**.
* **Execution:** Run the application via your IDE or use `mvn spring-boot:run`. The API will start at `http://localhost:8080`. You can access the **Swagger UI** at `/swagger-ui/index.html` to test the endpoints.

**2. Frontend (Next.js)**
* **Dependencies:** Run `npm install` in the `frontend/` directory.
* **API Connection:** Verify that the API base URL in `services/api.ts` points to `http://localhost:8080/api/images`.
* **Execution:** Run `npm run dev`. The application will be accessible at `http://localhost:3000`.

---

### **Project Specification**
This project is a decoupled full-stack application centered around secure image management and automated processing.

**Backend Implementation (Spring Boot)**
The backend is built as a dedicated REST API using **Spring Boot 4.0.6**, focusing on a strictly layered architecture to maintain high cohesion and low coupling:
* **Controllers:** Expose RESTful endpoints and manage CORS for the Next.js frontend.
* **Services:** Contain the core business logic, utilizing **Java AWT Graphics2D** for advanced text-overlay processing with dynamic line wrapping and font scaling.
* **Repositories:** Manage data persistence using **Spring Data JPA** and PostgreSQL.
* **DTOs & Records:** Create immutable data contracts to ensure internal entities are never exposed.
* **Global Exception Handler:** Intercepts errors (like `IllegalArgumentException` for long text) and returns clean, structured JSON messages to the frontend.

---

### **Technical Highlights**
* **Security:** Files are renamed using **UUIDs** to prevent filename collisions and directory traversal attacks.
* **Processing:** Text is rendered with a black shadow offset to guarantee readability on any background, with automatic font-size adjustment to fit the image dimensions.
* **Robustness:** Includes file metadata extraction and physical file cleanup during deletion.

---

### **Frontend Implementation (Next.js)**
The frontend is a modern, dark-themed dashboard built with **Next.js 16** and the **App Router**.
* **UI/UX:** Styled with **Tailwind CSS** featuring a minimalist "Touch to Reveal" interaction model—action buttons stay hidden until the user hovers over a card.
* **Interactive Components:** Features a static sidebar with a **Live Image Preview** and a responsive 3-column gallery grid.
* **Version Control:** Supports a nested list of image versions (overlays) with custom horizontal scrolling and individual version management.

---

### **Demo**
A full demonstration of the application's functionality, including the upload process, metadata viewing, and the processed image results, can be found in this YouTube video:  
[https://www.youtube.com/watch?v=0GJmp2A90oE](https://www.youtube.com/watch?v=0GJmp2A90oE)

---
