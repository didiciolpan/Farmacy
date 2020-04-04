package Domain;

import Repository.IRepositiory;

import java.security.KeyException;

public class AddOperation<T extends Entity> extends UndoableRedoableOperation {

    private T addedEntity;


    public AddOperation(IRepositiory<T> repository, T addedEntity) {
        super(repository);
        this.addedEntity = addedEntity;
    }

    @Override
    public void undo() throws KeyException {
        try {
            this.repository.remove(addedEntity.getId());
        } catch (KeyException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override

    public void redo() {
        try {
            this.repository.create(addedEntity);
        } catch (KeyException e) {
            e.printStackTrace();
        }
    }
}
