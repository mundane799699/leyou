package com.leyou.order.utils;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.order.config.PayConfig;
import com.leyou.order.enums.OrderStatusEnum;
import com.leyou.order.enums.PayState;
import com.leyou.order.mapper.OrderMapper;
import com.leyou.order.mapper.OrderStatusMapper;
import com.leyou.order.pojo.Order;
import com.leyou.order.pojo.OrderStatus;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.github.wxpay.sdk.WXPayConstants.FAIL;
import static com.github.wxpay.sdk.WXPayConstants.SUCCESS;

@Slf4j
@Component
public class PayHelper {
    @Autowired
    private WXPay wxPay;

    @Autowired
    private PayConfig payConfig;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderStatusMapper statusMapper;

    public String createPayUrl(Long orderId, Long totalFee, String body) {

        // 准备请求参数
        HashMap<String, String> data = new HashMap<>();
        data.put("body", body);
        data.put("out_trade_no", orderId.toString());
        data.put("total_fee", totalFee.toString());
        data.put("spbill_create_ip", payConfig.getSpbillCreateIp());
        String notifyUrl = payConfig.getNotifyUrl();
        log.info("notifyUrl = {}", notifyUrl);
        data.put("notify_url", notifyUrl);
        data.put("trade_type", payConfig.getTradeType());

        try {
            // 调用统一下单 API
            Map<String, String> result = wxPay.unifiedOrder(data);
            isSuccess(result);

            // 验证签名
            //isSignatureValid(result);

            String payUrl = result.get("code_url");
            return payUrl;
        } catch (Exception e) {
            log.error("【微信下单】下单失败，订单号:{}", orderId, e);
            throw new LyException(ExceptionEnum.WX_PAY_ORDER_FAIL);
        }
    }

    public void isSuccess(Map<String, String> result) {
        // 校验通信标示
        if (FAIL.equals(result.get("return_code"))) {
            log.error("【微信下单】下单通信失败, 原因：{}", result.get("return_msg"));
            throw new LyException(ExceptionEnum.WX_PAY_ORDER_FAIL);
        }

        // 校验业务标示
        if (FAIL.equals(result.get("result_code"))) {
            log.error("【微信下单】下单失败, 错误码：{}， 错误原因：{}", result.get("err_code"),
                    result.get("err_code_des"));
            throw new LyException(ExceptionEnum.WX_PAY_ORDER_FAIL);
        }
    }

    public void isValidSign(Map<String, String> data) {
        // 重新生成签名, 和传过来的签名进行比较
        try {
            String sign1 = WXPayUtil.generateSignature(data, payConfig.getKey(),
                    WXPayConstants.SignType.MD5);
            String sign2 = WXPayUtil.generateSignature(data, payConfig.getKey(),
                    WXPayConstants.SignType.HMACSHA256);
            String sign = data.get("sign");
            if (!StringUtils.equals(sign, sign1) && !StringUtils.equals(sign, sign2)) {
                // 签名有误
                throw new LyException(ExceptionEnum.INVALID_SIGN_ERROR);
            }
        } catch (Exception e) {
            log.error("【微信下单】签名验证出错 ", e);
            throw new LyException(ExceptionEnum.INVALID_SIGN_ERROR);
        }
    }

    public PayState queryPayState(Long orderId) {

        try {
            Map<String, String> data = new HashMap<>();
            data.put("out_trade_no", orderId.toString());
            Map<String, String> result = wxPay.orderQuery(data);
            // 校验状态
            isSuccess(result);
            // 校验签名
            isValidSign(result);
            // 校验金额
            String totalFeeStr = result.get("total_fee");
            String tradeNoStr = result.get("out_trade_no");
            if (StringUtils.isBlank(tradeNoStr) || StringUtils.isBlank(totalFeeStr)) {
                throw new LyException(ExceptionEnum.INVALID_ORDER_PARAM);
            }
            Long totalFee = Long.valueOf(totalFeeStr);
            Order order = orderMapper.selectByPrimaryKey(orderId);
            if (order == null) {
                throw new LyException(ExceptionEnum.INVALID_ORDER_PARAM);
            }
            // FIXME 这里应该是不等于实际金额
            if (totalFee != 1L) {
                // 金额不符
                throw new LyException(ExceptionEnum.INVALID_ORDER_PARAM);
            }

            String state = result.get("trade_state");
            if (SUCCESS.equals(state)) {
                // 修改订单状态
                OrderStatus status = new OrderStatus();
                status.setStatus(OrderStatusEnum.PAYED.getCode());
                status.setOrderId(orderId);
                status.setPaymentTime(new Date());
                int count = statusMapper.updateByPrimaryKeySelective(status);
                if (count != 1) {
                    throw new LyException(ExceptionEnum.UPDATE_ORDER_STATUS_ERROR);
                }
                return PayState.SUCCESS;
            } else if ("NOTPAY".equals(state) || "USERPAYING".equals(state)) {
                return PayState.NOT_PAY;
            }
            return PayState.FAIL;


        } catch (Exception e) {
            log.error("[微信支付], 调用微信接口查询支付状态失败", e);
            return PayState.NOT_PAY;
        }
    }
}
