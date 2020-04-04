package Service;

import Domain.Entity;
import Domain.UndoableRedoableOperation;

import java.security.KeyException;
import java.util.Stack;

public class UndoRedoService {

    private Stack<UndoableRedoableOperation<? extends Entity>> undoStack = new Stack<>();
    private Stack<UndoableRedoableOperation<? extends Entity>> redoStack = new Stack<>();

    public UndoRedoService() {

    }

    public void addToUndo(UndoableRedoableOperation<? extends Entity> undoableRedoableOperation) {
        undoStack.push(undoableRedoableOperation);
        redoStack.clear();
    }

    public boolean undo() throws KeyException {
        if (!this.undoStack.isEmpty()) {
            UndoableRedoableOperation<? extends Entity> undoableRedoableOperation = this.undoStack.pop();
            undoableRedoableOperation.undo();
            redoStack.push(undoableRedoableOperation);
            return true;
        }
        return false;
    }

    public boolean redo() throws KeyException {
        if (!this.redoStack.isEmpty()) {
            UndoableRedoableOperation<? extends Entity> undoableRedoableOperation = this.redoStack.pop();
            undoableRedoableOperation.redo();
            undoStack.push(undoableRedoableOperation);
            return true;
        }
        return false;
    }

}
