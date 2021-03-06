package com.doodl6.wechatrobot.handle;

import com.doodl6.wechatrobot.config.KeywordConfig;
import com.doodl6.wechatrobot.domain.WeChatMessage;
import com.doodl6.wechatrobot.response.BaseMessage;
import com.doodl6.wechatrobot.service.TulingService;
import com.doodl6.wechatrobot.util.LogUtil;
import com.doodl6.wechatrobot.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 文本类型消息处理类
 */
@Service
@Slf4j
public class TextMessageHandle implements WeChatMessageHandle {

    @Resource
    private TulingService tulingService;

    @Resource
    private KeywordConfig keywordConfig;

    @Override
    public String processMessage(WeChatMessage weChatMessage) {

        log.info(LogUtil.buildLog("收到用户文本信息", weChatMessage));

        String fromUserName = weChatMessage.getFromUserName();
        String toUserName = weChatMessage.getToUserName();
        String content = weChatMessage.getContent();

        BaseMessage message = keywordConfig.getMessageByKeyword(content);
        if (message == null) {
            message = tulingService.getTulingResponse(content, fromUserName);
        }

        if (message != null) {
            message.setFromUserName(toUserName);
            message.setToUserName(fromUserName);
            message.setCreateTime(System.currentTimeMillis());
        }
        return MessageUtil.toXml(message);
    }
}
