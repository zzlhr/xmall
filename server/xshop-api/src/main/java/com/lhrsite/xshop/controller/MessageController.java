package com.lhrsite.xshop.controller;




import com.lhrsite.xshop.vo.MessageVO;
import com.lhrsite.xshop.vo.ResultVO;
import com.lhrsite.xshop.po.Message;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
@Slf4j
public class MessageController {


    private final MessageService messageService;


    private ResultVO resultVO;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
        resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("ok");
        resultVO.setData(null);
    }


    @PostMapping("/getMessageList")
    public ResultVO getMessageList(String token, Integer type, Integer page, Integer pageSize) throws XShopException {
        resultVO.setData(messageService.getMessage(token, type, page, pageSize));
        log.info("【获取消息列表】result={}", resultVO);
        return resultVO;
    }


    @PostMapping("/getNewOrderCount")
    public ResultVO getNewOrderCount(String token) throws XShopException {
        JSONObject result = new JSONObject();
        result.put("count", messageService.getMessageNoReadCount(token));
        resultVO.setData(result);
        log.info("【获取未读订单通知数量】result={}", resultVO);
        return resultVO;
    }

    @PostMapping("/readMessage")
    public ResultVO readMessage(String token, String msgId) throws XShopException {
        resultVO.setData(messageService.readMessage(msgId));
        log.info("【阅读消息】msgId={}", msgId);
        return resultVO;
    }

    @PostMapping("/delMessage")
    public ResultVO delMessage(String msgId) throws XShopException {
        messageService.delMessage(msgId);
        return resultVO;
    }

    @PostMapping("/sendMessage")
    public ResultVO sendMessage(Message message) {
        log.info("【发送首页通知】message={}", message);
        messageService.sendMessage(message);
        return resultVO;
    }

    @MessageMapping("/message")
    @SendTo("/topic/greetings")
    public MessageVO greetings() throws InterruptedException {
        Thread.sleep(1000); // simulated delay
        return new MessageVO();
    }

}
