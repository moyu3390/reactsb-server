package com.example.demo.tcp;

import com.example.demo.tcp.model.Equipment;
import com.example.demo.tcp.model.EquipmentPort;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/tcp")
public class TcpController {

    @Autowired
    EquipmentManager equipmentManager;

    @Autowired
    EquipmentRepository equipmentRepository;

    @Autowired
    EquipmentPortRepository equipmentPortRepository;

    @PostConstruct
    void init(){
        Equipment pl01 = Equipment.builder()
                .id("PL01")
                .name("PLC01")
                .build();

        pl01.addPorts(Arrays.asList(new EquipmentPort[]{
                EquipmentPort.builder().id("PL01-01").port(5000).build(),
                EquipmentPort.builder().id("PL01-02").port(5001).build(),
                EquipmentPort.builder().id("PL01-03").port(5002).build()
        }));

        Equipment bc01 = Equipment.builder()
                .id("BC01")
                .name("BCR01")
                .build();

        bc01.addPorts(Arrays.asList(new EquipmentPort[]{
                EquipmentPort.builder().id("BC01-01").port(5020).build(),
                EquipmentPort.builder().id("BC01-02").port(5021).build()
        }));

        equipmentRepository.saveAll(Arrays.asList(new Equipment[]{pl01, bc01}));

    }

    @GetMapping("/equipment")
    List<Equipment> getEquipments(){
        List<Equipment> equipments = equipmentRepository.findAll();
        return equipments;
    }


    @PostMapping("/equipment/{eq_id}/run/port/{port_id}")
    boolean runEquipmentPort(@PathVariable("eq_id") String equipmentId, @PathVariable("port_id") String portId) throws Exception {
        log.debug("runEquipmentPort, eq={}, port={}", equipmentId, portId);

        EquipmentPort port = equipmentPortRepository.findById(portId).orElseThrow(EntityNotFoundException::new);

        if(equipmentManager.run(port)){
            port.setRun(true);
            port.setRunDateTime(LocalDateTime.now());
            equipmentPortRepository.save(port);
            return true;
        }
        return false;
    }

    @PostMapping("/equipment/{eq_id}/stop/port/{port_id}")
    boolean stopEquipmentPort(@PathVariable("eq_id") String equipmentId, @PathVariable("port_id") String portId) throws Exception {
        log.debug("stopEquipmentPort, eq={}, port={}", equipmentId, portId);

        EquipmentPort port = equipmentPortRepository.findById(portId).orElseThrow(EntityNotFoundException::new);

        if(equipmentManager.stop(port)){
            port.setRun(true);
            port.setRunDateTime(LocalDateTime.now());
            equipmentPortRepository.save(port);
            return true;
        }
        return false;
    }

    @PostMapping("/equipment/{eq_id}/send")
    boolean sendEquipmentPort(@PathVariable("eq_id") String equipmentId, @RequestBody Map<String,String> message){
        log.debug("sendEquipmentPort, eq={}, message={}", equipmentId, message.get("message"));
        return equipmentManager.send(equipmentId, message.get("message"));
    }


}
