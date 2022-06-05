package PrimitiveObjects;

public interface LogicGate {
    String getName();
    void setInputs(boolean[] inputs);
    boolean execute();
}
