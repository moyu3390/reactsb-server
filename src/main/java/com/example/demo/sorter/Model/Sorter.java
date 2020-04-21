package com.example.demo.sorter.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Sorter {
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderId implements Serializable {
        private int no;
        private String code;

        @Override
        public boolean equals(Object obj) {
            OrderId target= (OrderId) obj;
            return (this.no==target.no && this.code == target.code);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }

    @Entity(name = "OP_SORT_CHUTE")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Order {

        @EmbeddedId
        private Sorter.OrderId id;
        private int count;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderGroup {
        private int no;
        private int qty;

        public void addQty(int qty){
            this.qty += qty;
        }
    }

    @Entity(name = "OP_SORT_PROC")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Proc {
        // 년월일시분초+PID
        @Id
        private String id;
        private String inductionId;
        private String parcelId;
        private String barcode;
        private int chuteNo;
        @Enumerated(EnumType.STRING)
        private ChuteType chuteType;
        @Enumerated(EnumType.STRING)
        private SortReason sortReason;
        private int recirculationCount;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
    }

}
