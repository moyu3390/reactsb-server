package com.example.demo.tcp;

import com.example.demo.tcp.model.Equipment;
import com.example.demo.tcp.model.EquipmentPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

@Component
@Slf4j
public class EquipmentManager {

    HashMap<String ,NettyTcpServer> servers = new HashMap<>();

    public void test(String msg){
        log.debug("test, msg={}", msg);
    }

    public boolean run(EquipmentPort port) throws Exception {
        if(!servers.containsKey(port.getId())){
            NettyTcpServer server = new NettyTcpServer(port.getPort());
            server.setId(port.getId());
            server.setEquipmentManager(this);
            server.run();
            servers.put(server.getId(), server);
            printCurrentServer();
            return true;
        }
        return false;
    }

    public boolean stop(EquipmentPort port) throws Exception {

        NettyTcpServer server = servers.getOrDefault(port.getId(), null);

        if(server != null){
            server.stop();
            servers.remove(port.getId());
            printCurrentServer();
            return true;
        }
        return false;
    }

    public boolean send(String equipmentPortId, String message){

        NettyTcpServer server = servers.getOrDefault(equipmentPortId, null);

        if(server!=null){
            server.send(message);
            return true;
        }

        return false;
    }


    private void printCurrentServer(){
        log.debug("current servers is ({})", servers.size());
        servers.entrySet().stream()
                .map(i -> i.getValue())
                .forEach(i -> log.debug("id={}, port={}", i.getId(), i.getPort()));
    }
}
