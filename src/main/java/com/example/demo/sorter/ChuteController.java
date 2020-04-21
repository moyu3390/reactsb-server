package com.example.demo.sorter;

import com.example.demo.sorter.Model.Chute;
import com.example.demo.sorter.Model.ChuteStatus;
import com.example.demo.sorter.Model.ChuteType;
import com.example.demo.sorter.Model.Sorter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@RestController
@RequestMapping("/sorter")
@Slf4j
public class ChuteController {

    @Autowired
    ChuteMasterRepository chuteMasterRepository;

    @Autowired
    ChuteOrderRepository chuteOrderRepository;

    @PostConstruct
    private void init(){
        int limit = 10;
        IntStream.range(1, limit).forEach(i -> {
            chuteMasterRepository.save(
                    Chute.Master.builder()
                            .no(i)
                            .name(i + "번 슈트")
                            .type(i==(limit-2)? ChuteType.REJECT:i==(limit-1)? ChuteType.OVERFLOW:ChuteType.NORMAL)
                            .status(ChuteStatus.NORMAL)
                    .build()
            );
        });

        List<Chute.Master> masters = chuteMasterRepository.findAll();

        masters.stream().filter(i -> i.getType() == ChuteType.NORMAL)
                .collect(Collectors.toList())
                .forEach(chute -> {
                    IntStream.range(1, ran.nextInt(10)+2).forEach(i -> {

                        String code = "BARCODE" + String.format("%04d",chute.getNo()) + String.format("%04d",i);

                        chuteOrderRepository.save(
                                Sorter.Order.builder()
                                        .id(new Sorter.OrderId(){{
                                            setNo(chute.getNo());
                                            setCode(code);
                                        }})
                                        .count(ran.nextInt(10) + 1)
                                        .build()
                        );
                    });
        });
    }

    Random ran = new Random();

    @GetMapping("/master/chute")
    private List<Chute.Master> getMasterChute(){
        List<Chute.Master> chuteMaster = chuteMasterRepository.findAll();
        return chuteMaster;
    }

    @PatchMapping("/master/chute/{no}")
    private void patchMasterChute(@PathVariable("no") int chuteNo, @RequestBody Chute.Master chute){
        Chute.Master c = chuteMasterRepository.findById(chuteNo).orElseThrow(EntityNotFoundException::new);
        chuteMasterRepository.save(chute);
    }

    @GetMapping("/order/chute")
    private List<Sorter.OrderGroup> getOrderChute(){
        List<Sorter.Order> orders = chuteOrderRepository.findAll();
        List<Sorter.OrderGroup> orderGroups = new ArrayList<>();

        orders.forEach(order -> {
            Sorter.OrderGroup orderGroup = orderGroups.stream().filter(group -> group.getNo() == order.getId().getNo()).findFirst().orElse(null);
            if(orderGroup == null){
                orderGroup = new Sorter.OrderGroup();
                orderGroup.setNo(order.getId().getNo());
                orderGroup.setQty(order.getCount());
                orderGroups.add(orderGroup);
            }else{
//                orderGroup.setQty(orderGroup.getQty() + order.getCount());
                orderGroup.addQty(order.getCount());
            }
        });

        orderGroups.stream().sorted((o1, o2) -> o1.getNo() > o2.getNo()? 0:1);
        return orderGroups;
    }

    @GetMapping("/order/chute/{no}")
    private List<Sorter.Order> GetOrderByChute(@PathVariable("no") int no){
        List<Sorter.Order> orders = chuteOrderRepository.findByIdNo(no);
        return orders;
    }

}
