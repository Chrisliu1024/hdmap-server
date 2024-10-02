package com.hdmap.pointcloud.service;

import com.hdmap.pointcloud.PointCloudApplication;
import com.hdmap.pointcloud.service.impl.LasGridClipHandler;
import com.hdmap.minio.service.impl.MinioSysFileServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.annotation.Resource;

@Slf4j
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootTest(classes = {GridClipInterface.class, RedissonClient.class, MinioSysFileServiceImpl.class, PointCloudApplication.class})
class PointCloudHandleTest {

    @SpyBean
    private LasGridClipHandler pointCloudHandle;
    @Resource
    private RedissonClient redissonClient;
    @SpyBean
    private MinioSysFileServiceImpl minioService;

    @Test
    public void testClip() throws Exception {
        // 多线程运行
        Thread thread = new Thread(() -> {
            try {
                pointCloudHandle.clip(55L, "/Users/admin/Downloads/276331500.las", 7);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
        Thread thread2 = new Thread(() -> {
            try {
                pointCloudHandle.clip(55L, "/Users/admin/Downloads/276406800.las", 7);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread2.start();
        thread.join();
        thread2.join();
        log.info("clip success");
    }

}