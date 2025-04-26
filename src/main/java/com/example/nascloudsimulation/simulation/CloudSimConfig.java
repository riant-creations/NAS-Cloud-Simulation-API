package com.example.nascloudsimulation.simulation;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A helper class that configures CloudSim with two datacenters:
 *  - "NAS_Datacenter"
 *  - "Cloud_Datacenter"
 */
@Configuration
public class CloudSimConfig {

    private CloudSim cloudSim;
    private Datacenter nasDatacenter;
    private Datacenter cloudDatacenter;

    public CloudSimConfig() {
        // Initialize CloudSim
        int numUsers = 1; // number of cloud users
        Calendar calendar = Calendar.getInstance();
        boolean traceFlag = false; // no debug trace

        // Create the CloudSim instance
        CloudSim.init(numUsers, calendar, traceFlag);

        // Create two datacenters to represent "NAS" and "Cloud"
        nasDatacenter = createDatacenter("NAS_Datacenter");
        cloudDatacenter = createDatacenter("Cloud_Datacenter");
    }

    private Datacenter createDatacenter(String name) {
        // 1) Create a list of hosts
        List<Host> hostList = new ArrayList<>();

        // Basic example: single host with 1 CPU core
        int ram = 8192;    // host memory (MB)
        long storage = 1_000_000; // host storage (MB)
        int bw = 10000;    // bandwidth
        int mips = 1000;   // MIPS per CPU
        int cores = 1;     // number of CPU cores

        List<Pe> peList = new ArrayList<>();
        for (int i = 0; i < cores; i++) {
            peList.add(new Pe(i, new PeProvisionerSimple(mips))); // MIPS Rating
        }

        Host host = new Host(
                hostList.size(),
                new RamProvisionerSimple(ram),
                new BwProvisionerSimple(bw),
                storage,
                peList,
                new VmSchedulerTimeShared(peList)
        );

        hostList.add(host);

        // 2) Create the DatacenterCharacteristics object
        String arch = "x86";      // system architecture
        String os = "Linux";      // operating system
        String vmm = "Xen";       // virtual machine monitor
        double timeZone = 10.0;   // time zone
        double costPerSec = 3.0;  // arbitrary cost per second
        double costPerMem = 0.05; // cost per MB
        double costPerStorage = 0.001; // cost per MB of storage
        double costPerBw = 0.0;   // cost per MB of bandwidth

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList,
                timeZone, costPerSec, costPerMem, costPerStorage, costPerBw
        );

        // 3) Finally, create the Datacenter
        Datacenter datacenter = null;
        try {
            datacenter = new Datacenter(name, characteristics,
                    new VmAllocationPolicySimple(hostList), new ArrayList<Storage>(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;
    }
@Bean
    public CloudSim getCloudSim() {
        return cloudSim;
    }

    public Datacenter getNasDatacenter() {
        return nasDatacenter;
    }

    public Datacenter getCloudDatacenter() {
        return cloudDatacenter;
    }
}
