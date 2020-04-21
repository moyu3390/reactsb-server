package com.example.demo.tcp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Equipment {

    @Id
    private String id;
    private String name;

    @Enumerated(EnumType.STRING)
    private EquipmentType type;

    @JsonManagedReference
    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EquipmentPort> ports;

    public void addPorts(List<EquipmentPort> ports){
        ports.forEach(i -> addPort(i));
    }

    public void addPort(EquipmentPort port){

        if(ports == null)
            ports = new ArrayList<>();

        if(!ports.contains(port)){
            port.setEquipment(this);
            this.ports.add(port);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipment equipment = (Equipment) o;
        return id.equals(equipment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

enum  EquipmentType {
    PLC,
    BCR
}