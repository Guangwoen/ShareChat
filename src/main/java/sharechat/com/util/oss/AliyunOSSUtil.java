package sharechat.com.util.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class AliyunOSSUtil {

    // private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AliyunOSSUtil.class);

    public static String upload(File file, String type) {
        // logger.info("===>OSS文件上传开始: " + file.getName());
        System.out.println("===>OSS文件上传开始: " + file.getName());
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;
        String fileHost = ConstantPropertiesUtils.FILE_HOST;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(new Date());

        if(null == file) {
            return null;
        }

        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            String typeUrl;
            // 假设容器已经存在
            if(Objects.equals(type, "image")) {
                typeUrl = "/image";
            }
            else if(Objects.equals(type, "video")) {
                typeUrl = "/video";
            }
            else if(Objects.equals(type, "avatar")) {
                typeUrl = "/avatar";
            }
            else if(Objects.equals(type, "file")) {
                typeUrl = "/file";
            }
            else return null;
            // 创建文件路径
            String fileUrl = fileHost+typeUrl+
                    ("/"+ UUID.randomUUID().toString().replace("-","")+"-"+file.getName());
            // 上传文件
            PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, file));
            // 设置权限
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            if(null != result) {
                // logger.info("===>OSS文件上传成功, OSS地址: "+ fileUrl);
                System.out.println("===>OSS文件上传成功, OSS地址: "+ fileUrl);
                return fileUrl;
            }
        }
        catch (OSSException oe) {
            System.out.println(oe.getMessage());
        }
        catch (ClientException ce) {
            System.out.println(ce.getMessage());
        }
        finally {
            // 关闭
            ossClient.shutdown();
        }
        return null;
    }
}
