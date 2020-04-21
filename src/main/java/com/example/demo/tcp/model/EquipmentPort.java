package com.example.demo.tcp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentPort {

    @Id
    private String id;

    private int port;
    private boolean run;
    private LocalDateTime runDateTime;
    private LocalDateTime stopDateTime;

    @JsonBackReference
    @ManyToOne
    private Equipment equipment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EquipmentPort that = (EquipmentPort) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
