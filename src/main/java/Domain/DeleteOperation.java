package Domain;

import Repository.IRepositiory;

import java.security.KeyException;

public class DeleteOperation<T extends Entity> extends UndoableRedoableOperation {

    private T addedEntity;


    @SuppressWarnings("unchecked")
    public DeleteOperation(IRepositiory<T> repository, T addedEntity) {
        super(repository);
        this.addedEntity = addedEntity;

    }

    @Override
    @SuppressWarnings("unchecked")
    public void undo() {
        try {
            this.repository.create(addedEntity);
        } catch (KeyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void redo() {
        try {
            this.repository.remove(addedEntity.getId());

        } catch (KeyException e) {
            e.printStackTrace();
        }
    }
}
