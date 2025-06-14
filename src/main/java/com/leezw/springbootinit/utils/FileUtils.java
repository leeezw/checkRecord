package com.leezw.springbootinit.utils;
import com.leezw.springbootinit.common.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import com.leezw.springbootinit.exception.BusinessException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.io.*;
import java.net.InetAddress;
import java.util.UUID;

/**
 * @author lizhouwei
 */
@Component
@Slf4j
public class FileUtils {
    /**
     * 图片保存路径，自动从yml配置文件中获取数据
     */
    @Value("${file.path}")
    private String uploadPath;


    /**
     * 文件（图片）上传
     * @param file 图片文件
     */
    @SneakyThrows
    public String uploadFile(MultipartFile file, HttpServletRequest request) {

        //文件原名称
        String oldFilename = file.getOriginalFilename();
        //图片名后缀：.jpg、.png
        String suffix = oldFilename.substring(oldFilename.lastIndexOf("."));
        //uuid
        String uuid = UUID.randomUUID().toString();
        //文件新名称
        String newFileName = uuid + suffix;
        try {
            //创建保存上传文件的文件夹
            File folder = new File(uploadPath + newFileName);
            if (!folder.getParentFile().exists()) {
                folder.getParentFile().mkdirs();
            }
            //文件写入到该文件夹下
            file.transferTo(folder);
            // 返回可访问地址
        } catch (Exception e) {
            log.error("file upload error, filepath = " + newFileName, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        }
        //获得本机Ip（获取的是服务器的Ip）
        InetAddress inetAddress = InetAddress.getLocalHost();
        String ip = inetAddress.getHostAddress();
        //返回保存的url，根据url可以进行文件查看或者下载
        String fileDownloadUrl = request.getScheme() + "://" + ip + ":" + request.getServerPort() + "/api/file/" + newFileName;

        //返回保存的url
        return fileDownloadUrl;
    }


}




