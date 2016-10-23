package org.dynamicVMP;

import org.domain.PhysicalMachine;
import org.domain.VirtualMachine;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by Leonardo Benitez.
 */
public class Constraints {

    private Constraints() {
        // Default Constructor
    }

    /**
     * Check if any PM can host the VM.
     * <p>
     * For the Overbooking environment, the utilization must be involve. <br>
     * A Physical Machine can host a VM if and only if the resources requested by utilization
     * is less than the available resources of the PM.
     * </p>
     * @param pm               Physical Machine
     * @param deprecatedVM     Virtual Machine (VM to be updated with new Resources and Utilization)
     * @param vm               Virtual Machine
     * @param vms              Virtual Machinesld
     * @param isUpdate         <b>True</b>, if VM needs to be updated <br> <b>False</b>, otherwise
     * @return <b>True</b>, if exists a PM that can hold the VM.
     */
    public static Boolean checkResources(PhysicalMachine pm, VirtualMachine deprecatedVM, VirtualMachine vm,
            List<VirtualMachine> vms, Boolean isUpdate) {

        // If oldVM is not null, is an update

        VirtualMachine oldVm;
        if(deprecatedVM == null) {
            oldVm = new VirtualMachine(new ArrayList<>(), new ArrayList<>());
        } else {
            oldVm = deprecatedVM;
        }

        Boolean checkCPU = pm.getResourcesRequested().get(0)
                - (oldVm.getResources().get(0) * oldVm.getUtilization().get(0)/100 )
                + (vm.getResources().get(0) * vm.getUtilization().get(0)/100)
                + (vm.getResources().get(0) * (1- vm.getUtilization().get(0)/100)*DynamicVMP.PROTECTION_FACTOR)
                <= pm.getResources().get(0);

        Boolean checkRAM = pm.getResourcesRequested().get(1)
                - (oldVm.getResources().get(1) * oldVm.getUtilization().get(1)/100 )
                + (vm.getResources().get(1) * vm.getUtilization().get(1)/100)
                + (vm.getResources().get(1) * (1- vm.getUtilization().get(1)/100)*DynamicVMP.PROTECTION_FACTOR)
                <= pm.getResources().get(1);

        Boolean checkNET = pm.getResourcesRequested().get(2)
                - (oldVm.getResources().get(2) * oldVm.getUtilization().get(2)/100 )
                + (vm.getResources().get(2) * vm.getUtilization().get(2)/100)
                + (vm.getResources().get(2) * (1- vm.getUtilization().get(2)/100)*DynamicVMP.PROTECTION_FACTOR)
                <= pm.getResources().get(2);

        Boolean flag = checkCPU && checkRAM && checkNET;

        if (!isUpdate && flag && DynamicVMP.FAULT_TOLERANCE) {
            for (VirtualMachine vmTmp : vms) {

                if (vmTmp.getCloudService().equals(vm.getCloudService()) &&
                        vmTmp.getPhysicalMachine().equals(pm.getId())) {
                    return false;
                }
            }
        }

        return flag;
    }


    /**
     * Check if a PM is overloaded.
     * <p>
     * For the Overbooking environment, the utilization must be involve. <br>
     * A Physical Machine is overloaded if the resources utilization
     * is greater that the resources capacity of the PM.
     * </p>
     * @param pm                        Physical Machine
     * @param virtualMachinesAssoc      List of Virtual Machines associated to the Physical Machine
     * @param protectionFactor          Flag that indicates the degree of Overbooking
     * @return  <b>True</b>, if the PM is overloaded.
     */
    public static Boolean checkPMOverloaded(PhysicalMachine pm, List<VirtualMachine> virtualMachinesAssoc, Float protectionFactor ){

        float sumCpuResource = 0;
        float sumRamResource = 0;
        float sumNetResource = 0;

        for(VirtualMachine vm : virtualMachinesAssoc){

            sumCpuResource += (vm.getResources().get(0) * vm.getUtilization().get(0)/100)
                    + (vm.getResources().get(0) * (1- vm.getUtilization().get(0)/100)*protectionFactor);

            sumRamResource += (vm.getResources().get(1) * vm.getUtilization().get(1)/100)
                    + (vm.getResources().get(1) * (1- vm.getUtilization().get(1)/100)*protectionFactor);

            sumNetResource += (vm.getResources().get(2) * vm.getUtilization().get(2)/100)
                    + (vm.getResources().get(2) * (1- vm.getUtilization().get(2)/100)*protectionFactor);
        }

        return sumCpuResource > pm.getResources().get(0)
                || sumRamResource > pm.getResources().get(1)
                || sumNetResource > pm.getResources().get(2);
    }

    /**
     * @param virtualMachineList List of Virtual Machine
     * @param cloudServiceId     Cloud Service ID
     * @return <b>True</b>, is Fault to Tolerance is Active <br> <b>False</b>, otherwise
     */
    public static Boolean isFaultToleranceViolated(List<VirtualMachine> virtualMachineList, int cloudServiceId){
        Predicate<VirtualMachine> vmFilter = vm -> vm.getCloudService() == cloudServiceId;
        return virtualMachineList.stream().filter(vmFilter).count() > 1L;
    }

}
