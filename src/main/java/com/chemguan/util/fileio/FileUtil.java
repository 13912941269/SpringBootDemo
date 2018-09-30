package com.chemguan.util.fileio;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;

import static com.chemguan.util.Tools.productCode;

/**
 * Created by ShiWei on 2018-07-13.
 */
public class FileUtil {
    /**
     * 下载文件
     * @param filePath  文件地址
     * @param response
     * @param isOnLine  是否在线打开
     * @throws Exception
     */
    public void downLoad(String filePath, HttpServletResponse response, boolean isOnLine) throws Exception {
        File f = new File(filePath);
        if (!f.exists()) {
            response.sendError(404, "File not found!");
            return;
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len = 0;
        response.reset(); // 非常重要
        if (isOnLine) { // 在线打开方式
            URL u = new URL("file:///" + filePath);
            response.setContentType(u.openConnection().getContentType());
            response.setHeader("Content-Disposition", "inline; filename=" + f.getName());
            // 文件名应该编码成UTF-8
        } else { // 纯下载方式
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename=" + f.getName());
        }
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0)
            out.write(buf, 0, len);
        br.close();
        out.close();
    }






    /**
     * 上传图片
     */
    public String saveImage(HttpServletRequest request, HttpServletResponse response,String imgRoot,String str) throws Exception{
        String imageUrl = null;
        MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)request;
        MultipartFile file = mreq.getFile(str);
        if(StringUtils.isNotEmpty(file.getOriginalFilename())){
            String filename = productCode()+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
            file.transferTo(new File(imgRoot+ "upload/"+filename));
            imageUrl  = "upload/"+filename;
        }
        return imageUrl ;
    }


    /**
     * 上传图片(带压缩)
     */
    public String saveImageYS(HttpServletRequest request, String imgRoot,HttpServletResponse response, String str){
        String imageUrl = null;
        MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)request;
        MultipartFile file = mreq.getFile(str);
        if(StringUtils.isNotEmpty(file.getOriginalFilename())){
            String filename = productCode()+".jpg";
            try {
                //file.transferTo(new File(request.getSession().getServletContext().getRealPath("/")+ "upload/"+filename));
                file.transferTo(new File(imgRoot+ "upload/"+filename));
                imageUrl  = "upload/"+filename;
                //压缩图片
                Thumbnails.of(imgRoot + imageUrl)
                        .scale(1)
                        .outputQuality(0.5)
                        .toFile(imgRoot+ imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imageUrl ;
    }




    //上传图片
    public String saveImageMany(HttpServletRequest request, HttpServletResponse response,String imgRoot, CommonsMultipartFile files[]) throws Exception{
        String imageUrl = "";
        String path = imgRoot+ "upload/";
        for (int i = 0; i < files.length; i++) {
            // 获得原始文件名
            if(StringUtils.isNotEmpty(files[i].getOriginalFilename())){
                String filename = productCode()+files[i].getOriginalFilename().substring(files[i].getOriginalFilename().lastIndexOf('.'));
                try {
                    files[i].transferTo(new File(imgRoot+ "upload/"+filename));
                    if(StringUtils.isEmpty(imageUrl)){
                        imageUrl  += "upload/"+filename;
                    }else{
                        imageUrl  += ",upload/"+filename;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return imageUrl;

    }
}
