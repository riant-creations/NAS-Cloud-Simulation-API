package com.example.nascloudsimulation.service;

import com.example.nascloudsimulation.simulation.CloudSimConfig;
import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Runs a minimal CloudSim simulation with 2 datacenters (NAS vs. Cloud).
 * Returns results (Cloudlet execution) as a list of strings or objects.
 */
@Service
public class SimulationService {

    public List<String> runSimulation() throws Exception {
        // 1) Initialize CloudSim environment (2 datacenters)
        CloudSimConfig config = new CloudSimConfig();
        CloudSim CloudSim = config.getCloudSim();

        // 2) Create a DatacenterBroker to submit VMs and Cloudlets
        DatacenterBroker broker = createBroker();
        int brokerId = broker.getId();

        // 3) Create VMs
        List<Vm> vmList = new ArrayList<>();
        // Let's create 2 VMs, one for "NAS" DC, one for "Cloud" DC
        vmList.add(createVm(brokerId, 0, 500));  // VM #0
        vmList.add(createVm(brokerId, 1, 1000)); // VM #1

        // 4) Create Cloudlets (representing file operations or tasks)
        List<Cloudlet> cloudletList = new ArrayList<>();
        // We'll create 2 Cloudlets, each with some arbitrary length
        cloudletList.add(createCloudlet(brokerId, 0, 2000));
        cloudletList.add(createCloudlet(brokerId, 1, 4000));

        // 5) Submit VMs and Cloudlets to the broker
        broker.submitVmList(vmList);
        broker.submitCloudletList(cloudletList);

        // 6) Start the simulation
        CloudSim.startSimulation();

        // 7) Retrieve results
        List<Cloudlet> newList = broker.getCloudletReceivedList();
        CloudSim.stopSimulation();

        // 8) Build a result summary
        List<String> results = new ArrayList<>();
        for (Cloudlet cloudlet : newList) {
            results.add(cloudletInfo(cloudlet));
        }

        return results;
    }

    private DatacenterBroker createBroker() throws Exception {
        return new DatacenterBroker("Broker");
    }

      /**
     * Create a simple VM.
     * @param userId the broker ID
     * @param vmId the ID for this VM
     * @param mips MIPS rating
     */
    private Vm createVm(int userId, int vmId, int mips) {
        int ram = 512;       // VM memory (MB)
        long bw = 1000;      // bandwidth
        long size = 10000;   // image size (MB)
        int pesNumber = 1;   // number of CPU cores
        String vmm = "Xen";  // VMM name

        return new Vm(vmId, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
    }

    /**
     * Create a simple Cloudlet (task) that runs on a VM.
     * @param userId the broker ID
     * @param cloudletId the ID for this Cloudlet
     * @param length the number of instructions
     */
    private Cloudlet createCloudlet(int userId, int cloudletId, long length) {
        int pesNumber = 1;       // CPU cores needed
        long fileSize = 300;     // input file size
        long outputSize = 300;   // output file size
        UtilizationModel utilizationModel = new UtilizationModelFull();

        Cloudlet cloudlet = new Cloudlet(
                cloudletId, length, pesNumber,
                fileSize, outputSize,
                utilizationModel, utilizationModel, utilizationModel
        );
        cloudlet.setUserId(userId);
        return cloudlet;
    }

    private String cloudletInfo(Cloudlet cloudlet) {
        return String.format("Cloudlet %d finished in Datacenter %d; Status: %s; Actual CPU Time: %.2f",
                cloudlet.getCloudletId(),
                cloudlet.getResourceId(),
                cloudlet.getCloudletStatusString(),
                cloudlet.getActualCPUTime()
        );
    }
}
