# 项目说明
空间数据处理项目，包含数据解析、文件数据存储、点云处理、任务调度等模块。
# 模块说明
- hdmap-common：通用工具类模块
    - hdmap-common-core：核心模块
    - hdmap-common-file：文件存储模块
    - hdmap-common-geo：空间数据处理模块
    - hdmap-common-mybatis：Mybatis适配模块
    - hdmap-common-swagger：Swagger配置模块
- hdmap-data-parse：数据解析模块
    - hdmap-data-common：数据处理通用模块
    - hdmap-data-etl：数据ETL模块
    - hdmap-spatialite-parse：Spatialite数据解析模块
- hdmap-pointcloud：点云处理模块
- hdmap-pointcloud-api：点云处理模块接口
- hdmap-task：任务调度模块

# 注意
全局异常处理：@ControllerAdvice GlobalExceptionHandler
全局统一返回体：@RestControllerAdvice GlobalResponseBodyHandler，返回数据包装成统一格式