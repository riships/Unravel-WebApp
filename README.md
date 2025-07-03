## 🔧 Design Decisions

### 1. Session Management
- Used ConcurrentHashMap for intra-instance thread safety
- Recommended Redis for distributed session store
- Proper error handling with custom messages

### 2. Memory Management
- Avoided static HashMap holding large byte[] data
- Introduced Caffeine cache (or WeakHashMap as fallback)
- Monitored via JVM tools like VisualVM or jmap

### 3. Producer-Consumer with Priority
- Used PriorityBlockingQueue
- Task class (`LogTask`) implements Comparable
- Consumers use thread pool for scalability

### 4. Deadlock Resolution
- Applied consistent lock ordering (lock1 → lock2)
- Alternative ReentrantLock + tryLock for 3rd party lock safety

### 5. Database Pooling
- Used Spring Boot’s built-in HikariCP
- Custom monitoring with Timer + MXBean logging
- Alerts for underutilized or over-waiting threads
