package com.it.controller;

import com.it.common.R;
import com.it.config.AppConfig;
import com.it.constants.Constants;
import com.it.entity.ClaimRequest;
import com.it.entity.Comments;
import com.it.entity.UserInfo;
import com.it.exception.BusinessException;
import com.it.query.EmailQuery;
import com.it.query.PasswordQuery;
import com.it.query.UserQuery;
import com.it.service.*;
import com.it.utils.CreateImageCode;
import com.it.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * ClassName: UserController
 * Description:
 * 用户账号相关控制层
 * @Author Joel
 * @Create 2024/9/19
 * @Version 1.0
 */

@RestController
@RequestMapping("/user")
@Validated
public class UserController extends CommonController{

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final String CONTENT_TYPE = "Content-Type";

    private static final String CONTENT_TYPE_VALUE = "application/json;charset=UTF-8";

    @Resource
    private EmailCodeService emailCodeService;

    @Resource
    private UserInfoService userInfoService;
    @Resource
    private LostFoundService lostFoundService;

    @Resource
    private AdminInfoService adminInfoService;
    @Resource
    private ClaimRequestService claimRequestService;
    @Resource
    private CommentsService commentsService;

    @Resource
    private AppConfig appConfig;

    /**
     * 生成验证码图像
     * 生成一个验证码图像，并将验证码字符串存储在会话中，以便后续验证用户输入的验证码是否正确。
     * 通过设置HTTP响应头，确保浏览器不会缓存这个图像，每次请求都会生成一个新的验证码图像。
     * @param response 用于设置HTTP响应头和将生成的图像写入响应流
     * @param session 用于在会话中存储生成的验证码
     * @throws IOException
     */
    @RequestMapping("/checkCode")
    public void checkCode(HttpServletResponse response, HttpSession session) throws IOException {
        CreateImageCode vCode = new CreateImageCode(130, 38, 5, 10);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        String code = vCode.getCode();
        session.setAttribute(Constants.CHECK_CODE_KEY, code);
        vCode.write(response.getOutputStream());
    }

    /**
     * 发送邮箱验证码
     * @param emailQuery
     * emailQuery中有type属性，0表示注册、1表示找回密码
     * @return
     */
    @PostMapping("/sendEmailCode")
    public R<String> sendEmailCode(@RequestBody @Validated EmailQuery emailQuery){
        String email = emailQuery.getEmail();
        Integer type = emailQuery.getType();
        // 发送邮箱验证码
        emailCodeService.sendEmailCode(email,type);
        return R.success(null);
    }

    /**
     * 用户注册
     * @param session
     * @param userQuery
     * @return
     */
    @PostMapping ("/register")
    public R<String> register(HttpSession session, @RequestBody @Validated UserQuery userQuery){
        String email = userQuery.getEmail();
        String nickName = userQuery.getNickName();
        String password = userQuery.getPassword();
        String emailCode = userQuery.getEmailCode();
        String checkCode = userQuery.getCheckCode();
        try {
            // 校验图片验证码
            if(!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY))){
                throw new BusinessException("图片验证码错误");
            }
            // TODO: 进行注册
            userInfoService.register(email, nickName, password, emailCode);
            return R.success(null);
        } finally {
            // 清除会话中存储的验证码
            session.removeAttribute(Constants.CHECK_CODE_KEY);
        }
    }

    /**
     * 普通用户登录
     * @param session
     * @param userQuery
     * @return
     */
    @PostMapping("/login")
    public R<String> login(HttpSession session,@RequestBody @Validated UserQuery userQuery){
        String email = userQuery.getEmail();
        String password = userQuery.getPassword();
        String checkCode = userQuery.getCheckCode();
        try {
            // 校验图片验证码
            if(!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY))){
                throw new BusinessException("图片验证码错误");
            }
            // TODO: 进行登录
            return userInfoService.login(email, password);
        } finally {
            // 清除会话中存储的图片验证码
            session.removeAttribute(Constants.CHECK_CODE_KEY);
        }
    }

    /**
     * 管理员登录
     * @param session
     * @param userQuery
     * @return
     */
    @PostMapping("/admin/login")
    public R<String> adminLogin(HttpSession session,@RequestBody @Validated UserQuery userQuery){
        String email = userQuery.getEmail();
        String password = userQuery.getPassword();
        String checkCode = userQuery.getCheckCode();
        try {
            // 校验图片验证码
            if(!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY))){
                throw new BusinessException("图片验证码错误");
            }
            // TODO: 进行登录
            return adminInfoService.adminLogin(email, password);
        } finally {
            // 清除会话中存储的图片验证码
            session.removeAttribute(Constants.CHECK_CODE_KEY);
        }
    }

    /**
     * 获取用户详细信息
     * @return
     */
    @GetMapping("/userInfo")
    public R<UserInfo> getUserInfo(){
        Map<String,Object> map = ThreadLocalUtil.get();
        String nickName = (String) map.get("nickName");
        return userInfoService.getUserInfo(nickName);
    }

    /**
     * 获取用户头像
     * @param response
     */
    @GetMapping("/getAvatar")
    public void getAvatar(HttpServletResponse response) {
        // 从ThreadLocal中获取用户id
        Map<String,Object> claims = ThreadLocalUtil.get();
        String userId = (String) claims.get("userId");
        String avatarFolderName = Constants.FILE_FOLDER_AVATAR_NAME;
        // 判断存储头像的文件夹是否存在，不存在则创建
        File avatarFolder = new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_AVATAR_NAME);
        if (!avatarFolder.exists()) {
            avatarFolder.mkdirs();
        }
        // 判断用户头像是否存在，如果不存在就使用默认头像
        String avatarPath = appConfig.getProjectFolder() + avatarFolderName + userId + Constants.AVATAR_SUFFIX;
        while (!new File(avatarPath).exists()) {
            if (!new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_AVATAR_NAME + Constants.AVATAR_DEFUALT).exists()) {
                printNoDefaultAvatar(response);
                return;
            }
            avatarPath = appConfig.getProjectFolder() + Constants.FILE_FOLDER_AVATAR_NAME + Constants.AVATAR_DEFUALT;
        }
        response.setContentType("image/jpeg");
        readFile(response, avatarPath);
    }

    /**
     * 根据用户id获取用户头像
     * @param response
     * @param userId
     */
    @GetMapping("/getAvatarById")
    public void getAvatarByUserId(HttpServletResponse response, @RequestParam String userId){
        String avatarFolderName = Constants.FILE_FOLDER_AVATAR_NAME;
        // 判断存储头像的文件夹是否存在，不存在则创建
        File avatarFolder = new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_AVATAR_NAME);
        if (!avatarFolder.exists()) {
            avatarFolder.mkdirs();
        }
        // 判断用户头像是否存在，如果不存在就使用默认头像
        String avatarPath = appConfig.getProjectFolder() + avatarFolderName + userId + Constants.AVATAR_SUFFIX;
        while (!new File(avatarPath).exists()) {
            if (!new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_AVATAR_NAME + Constants.AVATAR_DEFUALT).exists()) {
                printNoDefaultAvatar(response);
                return;
            }
            avatarPath = appConfig.getProjectFolder() + Constants.FILE_FOLDER_AVATAR_NAME + Constants.AVATAR_DEFUALT;
        }
        response.setContentType("image/jpeg");
        readFile(response, avatarPath);
    }

    /**
     * 打印没有默认头像的信息
     * @param response
     */
    private void printNoDefaultAvatar(HttpServletResponse response) {
        response.setHeader(CONTENT_TYPE,CONTENT_TYPE_VALUE);
        response.setStatus(HttpStatus.OK.value());
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.print("请在头像目录下放置默认头像default_avatar.jpg");
            writer.close();
        } catch (IOException e) {
            logger.error("输出无默认图失败", e);
        }finally {
            writer.close();
        }
    }

    /**
     * 上传用户头像
     * @param avatar
     * @return
     */
    @PostMapping("/updateAvatar")
    public R<String> updateUserAvatar(@RequestParam("avatar") MultipartFile avatar){
        // 从ThreadLocal中获取用户id
        Map<String,Object> claims = ThreadLocalUtil.get();
        String userId = (String) claims.get("userId");
        // baseFolder是到项目的file层级目录的路径
        File avatarsFolder = new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_AVATAR_NAME);
        // 判断头像文件夹是否存在，不存在则创建
        if(!avatarsFolder.exists()){
            avatarsFolder.mkdirs();
        }
        File targetFile = new File(avatarsFolder.getPath() + userId + Constants.AVATAR_SUFFIX);
        try {
            avatar.transferTo(targetFile);
        } catch (IOException e) {
            logger.error("头像上传失败", e);
        }
        return R.success(null);
    }

    /**
     * 更新密码，用于用户登录之后的修改密码操作
     * @param passwordQuery
     * @return
     */
    @PostMapping("/updatePwd")
    public R<String> updatePwd(@RequestBody @Validated PasswordQuery passwordQuery){
        String oldPwd = passwordQuery.getOldPwd();
        String newPwd = passwordQuery.getNewPwd();
        String rePwd = passwordQuery.getRePwd();
        userInfoService.updatePwd(oldPwd, newPwd, rePwd);
        return R.success(null);
    }

    /**
     * 重置密码，用于登陆界面中忘记密码的操作
     * @param session
     * @param userQuery
     * @return
     */
    @PostMapping("/resetPwd")
    public R<String> resetPwd(HttpSession session,@RequestBody @Validated UserQuery userQuery){
        String email = userQuery.getEmail();
        String emailCode = userQuery.getEmailCode();
        String password = userQuery.getPassword();
        // TODO: 进行密码重置
        userInfoService.resetPwd(email, password, emailCode);
        return R.success(null);
    }
    /**
     * 发送认领请求
     * @param claimRequest
     */
    @PostMapping("/sendClaimInfo")
    public R<String> sendEmailCode(@RequestBody ClaimRequest claimRequest){
        try {
            claimRequestService.save(claimRequest);
            return R.success("认领信息发送成功~");
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 拾主确认认领结案
     */
    @PutMapping("/confirmClaim")
    public R<String> confirmClaim(@RequestParam(value ="user_id")int user_id,@RequestParam(value ="post_id")int post_id){
       String msg=lostFoundService.confirmClaim(post_id,user_id);
       if(msg.equals("认领信息已确认")){
           return R.success(msg);
       }
        return R.error("信息有误，请再核对");
    }

    /**
     * 发送父评论
     * @param comments
     * @return
     */
    @PostMapping("/sendLostParentComments")
    public R<String> sendLostComments(@RequestBody Comments comments){
        try {
            commentsService.sendLostParentComments(comments);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return R.success("评论成功");
    }
    @PostMapping("/sendMissingParentComments")
    public R<String> sendMissingComments(@RequestBody Comments comments){
        try {
            commentsService.sendMissingParentComments(comments);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return R.success("评论成功");
    }

    /**
     * 发送子评论
     * @param comments
     * @return
     */
    @PostMapping("/sendLostChildComments")
    public R<String> sendLostChildComments(@RequestBody Comments comments){
        try {
            commentsService.sendLostChildComments(comments);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return R.success("评论成功");
    }
    @PostMapping("/sendMissingChildComments")
    public R<String> sendMissingChildComments(@RequestBody Comments comments){
        try {
            commentsService.sendMissingChildComments(comments);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return R.success("评论成功");
    }

    /**
     * 获得lost的父评论
     * @return
     */
    @GetMapping("/getLostParentComments")
    public R<List<Comments>> getParentComments(int postId){
        List<Comments> parentComments = null;
        try {
            parentComments = commentsService.getLostParentCommentsByid(postId);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return R.success(parentComments);
    }
    /**
     * 获得missing的父评论
     */
    @GetMapping("/getMissingParentComments")
    public R<List<Comments>> getMissingParentComments(int postId){
        List<Comments> parentComments = null;
        try {
            parentComments = commentsService.getMissingParentCommentsByid(postId);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return R.success(parentComments);
    }
    /**
     * 获得lost的子评论
     * @param parentId
     * @return
     */
    @GetMapping("/getLostChildComments")
    public R<List<Comments>> getLostChildComments(@RequestParam("parentId")int parentId){
        List<Comments> childComments = null;
        try {
            childComments = commentsService.getLostChildComments(parentId);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return R.success(childComments);
    }
    /**
     * 获得missing的子评论
     */
    @GetMapping("/getMissingChildComments")
    public R<List<Comments>> getMissingChildComments(@RequestParam("parentId")int parentId){
        List<Comments> childComments = null;
        try {
            childComments = commentsService.getMissingChildComments(parentId);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return R.success(childComments);
    }
}
