package Domain;

public abstract class Entity {

    private int id = -1;

//    public Entity(int id) {
//        this.id = id;
//    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
