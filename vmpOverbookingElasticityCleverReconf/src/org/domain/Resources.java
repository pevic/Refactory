/*
 * Virtual Machine Placement: Overbooking and Elasticity
 * Author: Rodrigo Ferreira (rodrigofepy@gmail.com)
 * Author: Saúl Zalimben (szalimben93@gmail.com)
 * Author: Leonardo Benitez  (benitez.leonardo.py@gmail.com)
 * Author: Augusto Amarilla (agu.amarilla@gmail.com)
 * 22/8/2016.
 */

package org.domain;

/**
 * @author Saul Zalimben.
 * @since 8/20/16.
 */
public class Resources {

    private Float cpu;
    private Float ram;
    private Float net;

    /**
     * Default Constructor
     * @param cpu CPU
     * @param ram RAM
     * @param net NET
     */
    public Resources(final Float cpu, final Float ram, final Float net) {

        this.cpu = cpu;
        this.ram = ram;
        this.net = net;
    }

    public Resources() {
        this.cpu = 0F;
        this.ram = 0F;
        this.net = 0F;
    }

    public Float getCpu() {

        return cpu;
    }

    public void setCpu(final Float cpu) {

        this.cpu = cpu;
    }

    public Float getRam() {

        return ram;
    }

    public void setRam(final Float ram) {

        this.ram = ram;
    }

    public Float getNet() {

        return net;
    }

    public void setNet(final Float net) {

        this.net = net;
    }

    /**
     * To String
     * @return String
     */
    @Override
    public String toString() {

        return  this.getCpu().toString() + "\t"
                + this.getRam().toString() + "\t"
                + this.getNet().toString() + "\t";


    }

}
