# 🔗 URL Shortener (No Framework)

Simple URL shortener using:
- Java `HttpServer` (no Spring)
- H2 Database (file mode) + JDBC
- HTML, CSS, JS (Vanilla only)
- SLF4J for logging
- JUnit 5 + Mockito for testing

---

##  Done

- Shorten URLs via `/shorten` POST API
- JSON API + basic validation
- H2 DB with JDBC for storage
- CORS headers added
- Minimal frontend UI
- Logging using SLF4J

---

##  Pending

- Signup/Login + JWT
- Custom URL for users
- Frontend URL history
- Docker setup
- GitHub Actions CI/CD

---

##  How to Run

### 1. Compile Java

```bash
javac -cp ".;lib/*" src/com/example/urlshortner/*.java -d out


### 2. Start Server
cd out
java com.example.urlshortner.UrlShortenerServer


### 3. FrontEnd
Open public/index.html in browser
OR
npx serve public

Author
Harshal Gangurde
GitHub: harshal6540
