package Domain;

import Repository.IRepositiory;

import java.security.KeyException;
import java.util.List;

public class DeleteOperationDrugAndTransactions<T extends Entity> extends UndoableRedoableOperation {


    private List<Transaction> transactions;
    private T addedEntity;
    private IRepositiory<Transaction> transactionIRepository;


    public DeleteOperationDrugAndTransactions(IRepositiory<T> repositiory, T addedEntity,
                                              IRepositiory<Transaction> transactionIRepositiory, List<Transaction> transactions) {
        super(repositiory);
        this.transactionIRepository = transactionIRepositiory;
        this.addedEntity = addedEntity;
        this.transactions = transactions;
    }

    @Override
    public void undo() {
        try {
            int i = 0;
            this.repository.create(addedEntity);
            while (!transactions.isEmpty() && i < transactions.size()) {
                this.transactionIRepository.create(transactions.get(i));
                i++;
            }
        } catch (KeyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void redo() {
        try {
            int i = 0;
            this.repository.remove(addedEntity.getId());
            while (!transactions.isEmpty() && i < transactions.size()) {
                this.transactionIRepository.remove(transactions.get(i).getId());
                i++;
            }
        } catch (KeyException e) {
            e.printStackTrace();
        }
    }
}
