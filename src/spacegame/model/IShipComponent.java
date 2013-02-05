package spacegame.model;


public interface IShipComponent {

	ISpaceShip getShip();

	void setShip(ISpaceShip ship);

	void update();

}