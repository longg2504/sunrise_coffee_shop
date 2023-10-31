package com.cg.domain.dto.socket;

import com.cg.domain.dto.orderDetail.OrderDetailKitchenGroupDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    private NotificationType type;
    private Data data;
    private String sender;

    public enum NotificationType {
        KITCHEN, RECEPTION
    }
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data{
        private Long tableId;
        private String message;
        @Override
        public String toString() {
            return "Data{" +
                    "tableId=" + tableId +
                    ", message='" + message + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "type=" + type +
                ", data='" + data + '\'' +
                ", sender='" + sender + '\'' +
                '}';
    }
}