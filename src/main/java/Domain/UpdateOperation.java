package Domain;

import Repository.IRepositiory;

import java.security.KeyException;

public class UpdateOperation<T extends Entity> extends UndoableRedoableOperation {

    private T backUpEntity;
    private T updateEntity;

    public UpdateOperation(IRepositiory<T> repository, T backUpEntity, T updateEntity) {
        super(repository);
        this.backUpEntity = backUpEntity;
        this.updateEntity = updateEntity;
    }

    @Override
    public void undo() {
        try {
            this.repository.update(backUpEntity);
        } catch (KeyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void redo() {
        try {
            this.repository.update(updateEntity);
        } catch (KeyException e) {
            e.printStackTrace();
        }
    }
}
