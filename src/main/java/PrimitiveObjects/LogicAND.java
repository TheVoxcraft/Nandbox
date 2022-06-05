package PrimitiveObjects;

public class LogicAND implements LogicGate {
    public boolean[] inputs = new boolean[2];
    public boolean output;
    public boolean hasBeenUpdated = false;

    @Override
    public String getName() {
        return "AND";
    }

    @Override
    public void setInputs(boolean[] inputs) {
        this.inputs = inputs;
        hasBeenUpdated = true;
    }

    @Override
    public boolean execute() {
        if(!hasBeenUpdated) {
            throw new IllegalStateException("LogicOR inputs have never been set");
        }
        output = inputs[0] && inputs[1];
        return output;
    }
}
