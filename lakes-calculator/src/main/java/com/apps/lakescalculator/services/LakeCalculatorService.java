package com.apps.lakescalculator.services;

import com.apps.lakescalculator.core.Lake;
import com.apps.lakescalculator.core.LakeVisualizator;
import com.apps.lakescalculator.core.LakesCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LakeCalculatorService {

    private LakesCalculator calculator;
    private LakeVisualizator visualizator;

    private ConcurrentHashMap<String, Lake> persistanse = new ConcurrentHashMap<>();

    public List<Lake> calculate(int[] arr) {
        List<Lake> lakes = calculator.calculate(arr);
        for(Lake lake : lakes){
            persistanse.put(lake.getId(), lake);
        }
        return lakes;
    }

    public String visualize (String id){
        if(persistanse.containsKey(id)){
            return visualizator.visualize(persistanse.get(id));
        } else {
            return "nothing found for this id: " + id;
        }
    }

    @Autowired
    public void setCalculator(LakesCalculator calculator) {
        this.calculator = calculator;
    }

    @Autowired
    public void setVisualizator(LakeVisualizator visualizator) {
        this.visualizator = visualizator;
    }
}
