package reservoircomputer.reservoir;

public class DrivenCell implements Cell {
    private final int identity;
    private int currentState;

    public DrivenCell(int identity) {
        this.identity = identity;
        this.currentState = Math.random() < 0.5 ? 0 : 1;
    }
    @Override
    public int getCurrentState() {
        return currentState;
    }

    @Override
    public int getIdentity() {
        return identity;
    }

    public void updateInputNode(int newState) {
        currentState = newState;
    }
}
