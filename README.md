# Learning Resource Aggregator

## User Flow

### 1. Authentication

New users start by registering with their name, email, and password. Existing users can log in with their credentials. Both operations return a JWT token that must be included in subsequent requests.

### 2. Interest Selection

After authentication, users should select their interests from available specialties and subspecialties:

- Browse the list of specialties (e.g., Backend Development, Frontend Development)
- View subspecialties within each specialty (e.g., Java, Spring Boot)
- Select specialties and subspecialties that match their learning goals

### 3. Resource Discovery

Once interests are set, users can discover learning resources through:

- Personalized recommendations based on selected interests
- Filtering by specialty, subspecialty, or resource type
- Searching by keyword
- Browsing all available resources

### 4. Learning Journey

Users can update their interests at any time to receive different recommendations, allowing for a flexible learning journey as their career goals evolve.

## Extending the Application

### Additional Features to Consider

1. **User Progress Tracking**:
   - Mark resources as completed or in-progress
   - Track learning time spent on different topics

2. **Resource Ratings and Reviews**:
   - Allow users to rate and review resources
   - Use community ratings to improve recommendations

3. **Learning Paths**:
   - Create curated sequences of resources for specific career paths
   - Suggest next steps based on completed resources

4. **Admin Dashboard**:
   - Provide interfaces for managing resources, specialties, and users
   - Generate reports on user engagement and popular topics

5. **Notification System**:
   - Alert users about new resources in their areas of interest
   - Remind users about in-progress learning

### Integration with External APIs

While the current implementation uses seeded data, future versions could integrate directly with:

- Coursera API
- YouTube Data API
- Udemy API

This would enable real-time discovery of the latest learning resources.

## Security Considerations

- JWT tokens expire after 24 hours (configurable)
- Passwords are hashed using BCrypt
- Input validation is performed on all requests
- Error handling provides secure, informative messages

## Deployment

### Production Considerations

1. **Environment Configuration**:
   - Use environment variables for sensitive configuration
   - Set up different profiles for dev, test, and production

2. **Database**:
   - Use connection pooling for improved performance
   - Set up database backups and monitoring

3. **Security**:
   - Use HTTPS in production
   - Consider adding rate limiting for API endpoints
   - Implement IP-based restrictions if needed

### Deployment Options

- **Traditional Server**: Deploy as a JAR file on a VM
- **Docker**: Containerize the application for easier deployment
- **Cloud Services**: Deploy to AWS, Azure, or GCP

## Troubleshooting

### Common Issues

1. **Database Connection Errors**:
   - Verify database credentials in application.properties
   - Ensure PostgreSQL is running and accessible

2. **JWT Token Issues**:
   - Verify that the Authorization header is correctly formatted
   - Check if the token has expired

3. **Data Initialization**:
   - If sample data is not loading, check database logs
   - Try manually truncating tables and restarting the application

### Logging

The application uses SLF4J with Logback for logging. You can adjust log levels in `application.properties`:

```properties
logging.level.root=INFO
logging.level.com.caspercodes.aggregatorservice=DEBUG
logging.level.org.springframework.security=INFO
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
