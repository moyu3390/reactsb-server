package com.example.demo.sorter.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;


public class Chute {

    @Entity(name = "MT_CHUTE")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Master {
        @Id
        private int no;

        private String name;

        @Enumerated(EnumType.STRING)
        private ChuteType type;
        @Enumerated(EnumType.STRING)
        private ChuteStatus status;

    }
}
