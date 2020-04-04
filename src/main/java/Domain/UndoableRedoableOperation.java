package Domain;

import Repository.IRepositiory;


import java.security.KeyException;

public abstract class UndoableRedoableOperation<T extends Entity> {

    protected IRepositiory<T> repository;

    public UndoableRedoableOperation(IRepositiory<T> repository) {
        this.repository = repository;
    }

    public abstract void undo() throws KeyException;

    public abstract void redo() throws KeyException;
}
