package com.example.nascloudsimulation.controller;

import com.example.nascloudsimulation.service.SimulationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    /**
     * Run the CloudSim simulation and return results in JSON
     */
    @GetMapping("/simulate")
    public ResponseEntity<List<String>> runSimulation() {
        try {
            List<String> results = simulationService.runSimulation();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
