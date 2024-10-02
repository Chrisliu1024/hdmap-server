# Project Description
A spatial data processing project that includes modules for data parsing, file data storage, point cloud processing, and task scheduling.
# Module Description
- **hdmap-common**: Common utilities module
  - **hdmap-common-core**: Core module
  - **hdmap-common-file**: File storage module
  - **hdmap-common-geo**: Spatial data processing module
  - **hdmap-common-mybatis**: MyBatis adaptation module
  - **hdmap-common-swagger**: Swagger configuration module
- **hdmap-data-parse**: Data parsing module
  - **hdmap-data-common**: General data processing module
  - **hdmap-data-etl**: Data ETL (Extract, Transform, Load) module
  - **hdmap-spatialite-parse**: Spatialite data parsing module
- **hdmap-pointcloud**: Point cloud processing module
- **hdmap-pointcloud-api**: Point cloud processing module API
- **hdmap-task**: Task scheduling module

# Notes
Global exception handling: `@ControllerAdvice GlobalExceptionHandler`  
Global unified response body: `@RestControllerAdvice GlobalResponseBodyHandler`, returns data in a standardized format.