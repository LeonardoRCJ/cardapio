package com.leo.cardapio.model.order.dtos;

import com.google.gson.annotations.SerializedName;

record WebhookData(String id){}

public record WebhookNotificationDTO(
        String action,
        @SerializedName("data") WebhookData data
) { }
