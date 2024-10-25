package com.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.dto.MissingNoticesDto;
import com.it.entity.*;
import com.it.enums.MessageType;
import com.it.exception.BusinessException;
import com.it.mapper.MissingNoticesMapper;
import com.it.service.*;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class IMissingNotciesServiceImpl extends ServiceImpl<MissingNoticesMapper, MissingNotices> implements IMissingNoticesService {
}
