# NAS Cloud Simulation System

A simulation system demonstrating hybrid storage management between NAS (Network Attached Storage) and Cloud storage using Spring Boot and CloudSim.

## Project Structure

### Backend Components

#### 1. Models
- **FileInfo.java**: Core data model representing file metadata
  - Tracks file ID, name, category, storage type, and archive status
  - Supports categorization (e.g., "course-form", "waec-result")
  - Manages storage location ("NAS" or "Cloud")

#### 2. Services
- **StorageSimulationService.java**: Handles file management operations
  - File upload with categorization
  - File retrieval and listing
  - Category-based file grouping
  - Archival management
  - File deletion

#### 3. Controllers
- **FileController.java**: REST API endpoints
  - `POST /api/files/upload`: Upload new files
  - `GET /api/files`: Retrieve all files
  - `GET /api/files/categories`: Get files grouped by category
  - `POST /api/files/{id}/archive`: Archive specific files
  - `DELETE /api/files/{id}`: Delete files
  - `GET /api/files/download/{id}`: Download files

## Features

- File upload with automatic categorization
- Hybrid storage simulation (NAS + Cloud)
- Category-based file organization
- Archival management system
- File download capabilities
- CORS support for frontend integration

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/files/upload` | Upload new file with category |
| GET | `/api/files` | List all files |
| GET | `/api/files/categories` | Get files grouped by category |
| POST | `/api/files/{id}/archive` | Archive a specific file |
| DELETE | `/api/files/{id}` | Delete a file |
| GET | `/api/files/download/{id}` | Download a file |

## Technical Details

- Built with Spring Boot
- Uses in-memory storage for demonstration
- Implements file system operations using java.nio
- Supports cross-origin requests from localhost:3000
- Includes error handling and response status codes

## Setup and Configuration

1. Ensure Java 11+ is installed
2. Clone the repository
3. Build the project:
```bash
./mvnw clean install
```
4. Run the application:
```bash
./mvnw spring-boot:run
```

## File Storage

- Files are stored in an `uploads/` directory
- File metadata is kept in memory using HashMap
- Archived files are marked and "moved" to Cloud storage

## Security Considerations

- Basic error handling implemented
- CORS configuration for local development
- File operation validation
- Safe file handling practices

## Future Improvements

- Implement persistent storage
- Add user authentication
- Enhance error handling
- Add file type validation
- Implement actual cloud storage integration

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

[Add your license here]
