package main.java.Objetcs;

import java.util.Objects;

public class OpenedMachines {

    private String machineNuber;
    private boolean existanceInNomenclatuur;


    public OpenedMachines(String machineNuber, boolean existanceInNomenclatuur) {
        this.machineNuber = machineNuber;
        this.existanceInNomenclatuur = existanceInNomenclatuur;
    }

    public String getMachineNuber() {
        return machineNuber;
    }

    public void setMachineNuber(String machineNuber) {
        this.machineNuber = machineNuber;
    }

    public boolean isExistanceInNomenclatuur() {
        return existanceInNomenclatuur;
    }

    public void setExistanceInNomenclatuur(boolean existanceInNomenclatuur) {
        this.existanceInNomenclatuur = existanceInNomenclatuur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenedMachines that = (OpenedMachines) o;
        return existanceInNomenclatuur == that.existanceInNomenclatuur &&
                Objects.equals(machineNuber, that.machineNuber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(machineNuber, existanceInNomenclatuur);
    }

    @Override
    public String toString() {
        return "OpenedMachines{" +
                "machineNuber='" + machineNuber + '\'' +
                ", existanceInNomenclatuur=" + existanceInNomenclatuur +
                '}';
    }
}
