package com.it.controller;


import com.it.common.R;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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

    @Value("${reggie.path}")//读取配置文件里的值,写的是"${reggie.path}",它自动变成赋予的值了
    private String basepath;
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //file是一个临时文件，需要转存到指定位置，否则会自动删除
        String filename = file.getOriginalFilename();
        try {
            file.transferTo(new File(basepath+filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(filename);//先传一个文件过来，然后我后台开始下载，就是把它放到我自己的本地文件夹中，然后再把文件名传回去
        //让前端再发出一个请求，使用这个文件名，实现图片的回显，这步再下面的download中完成。
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        //输入流，通过输入流读取文件内容
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basepath+name));
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
}
