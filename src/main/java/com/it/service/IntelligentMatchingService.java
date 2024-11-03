package com.it.service;

import com.it.entity.LostFound;
import com.it.entity.MissingNotices;
import org.springframework.scheduling.annotation.Async;

public interface IntelligentMatchingService {

    public void processSmartPush(LostFound lostFound);
    public void processSmartPush2(MissingNotices missingNotices);
}
