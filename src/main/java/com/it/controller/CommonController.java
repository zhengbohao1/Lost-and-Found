package com.it.controller;


import com.it.common.R;
import com.it.utils.StringTools;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

/**
 * 用于实现图片上传和回显的controller
 * @author 郑博豪
 * @version 1.0
 * @date 2024/9/15
 * @description 首先，需要在application.yml中配置文件上传的位置，我这里配置在D://img里头，
 * 然后是具体逻辑，前端先传一个文件过来（使用/upload接口），然后我后台完事了再把文件名传回去，让前端
 * 再发一个请求（/download），从而实现图片的回显。
 * 除此之外，还有一个作用，就是在得到文件名后，令imgUrl字段的值为这个文件名，这样传递给后端。
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Value("${project.folder}")//读取配置文件里的值,写的是"${reggie.path}",它自动变成赋予的值了
    private String basepath;
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //file是一个临时文件，需要转存到指定位置，否则会自动删除
        String filename = file.getOriginalFilename();
        try {
            file.transferTo(new File(basepath+"lost_found_img/"+filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(filename);//先传一个文件过来，然后我后台开始下载，就是把它放到我自己的本地文件夹中，然后再把文件名传回去
        //让前端再发出一个请求，使用这个文件名，实现图片的回显，这步再下面的download中完成。
    }
    @PostMapping("/upload2")
    public R<String> upload2(MultipartFile file){
        //file是一个临时文件，需要转存到指定位置，否则会自动删除
        String filename = file.getOriginalFilename();
        try {
            file.transferTo(new File(basepath+"find_tip_img/"+filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(filename);//先传一个文件过来，然后我后台开始下载，就是把它放到我自己的本地文件夹中，然后再把文件名传回去
        //让前端再发出一个请求，使用这个文件名，实现图片的回显，这步再下面的download中完成。
    }
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        System.out.println(basepath+"lost_found_img/"+name);
        //输入流，通过输入流读取文件内容
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basepath+"lost_found_img/"+name));
            System.out.println(basepath);
            System.out.println(basepath+name);
            ServletOutputStream outputStream = response.getOutputStream();
            //设置响应回去的到底是什么文件
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //输出流，通过输出流将文件写回浏览器
    }
    //用于“我找到了你的东西”相关的图片上传验证。
    @GetMapping("/download2")
    public void download2(String name, HttpServletResponse response){
        System.out.println(basepath+"find_tip_img/"+name);
        //输入流，通过输入流读取文件内容
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basepath+"find_tip_img/"+name));
            System.out.println(basepath);
            System.out.println(basepath+name);
            ServletOutputStream outputStream = response.getOutputStream();
            //设置响应回去的到底是什么文件
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //输出流，通过输出流将文件写回浏览器
    }

    /**
     * 将指定文件的内容读取并写入到HTTP响应中
     * @param response
     * @param filePath
     */
    protected void readFile(HttpServletResponse response, String filePath) {
        if (!StringTools.pathIsOk(filePath)) {
            return;
        }
        OutputStream out = null;
        // 用于读取文件
        FileInputStream in = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return;
            }
            // TODO: 读取文件
            in = new FileInputStream(file);
            byte[] byteData = new byte[1024];
            // 获取 HttpServletResponse 的输出流 out，用于将文件内容写入响应
            out = response.getOutputStream();
            int len = 0;
            // while循环读取文件内容，每次读取 1024 字节
            while ((len = in.read(byteData)) != -1) {
                // 将读取的数据写入输出流 out
                out.write(byteData, 0, len);
            }
            // 通过 out.flush() 刷新输出流，确保数据被发送
            out.flush();
        } catch (Exception e) {
            logger.error("读取文件异常", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error("IO异常", e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("IO异常", e);
                }
            }
        }
    }

}
